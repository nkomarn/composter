package xyz.nkomarn.type;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.packet.bi.ChatBiPacket;
import xyz.nkomarn.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.protocol.packet.s2c.MapChunkS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.PlayerPosLookS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.PreChunkS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.SpawnPositionS2CPacket;
import xyz.nkomarn.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public final class Player extends Entity {
    private final String username;
    private final Session session;
    private final Set<Chunk.Key> loadedChunks = new HashSet<>();

    private static final double DEFAULT_STANCE = 67.240000009536743;
    //TODO crouching support

    public Player(@NotNull Session session, @NotNull String username) {
        super(Composter.SPAWN.getWorld());
        this.session = session;
        this.username = username;
        this.location = world.spawn;

        updateChunks(true);

        // Send spawn position
        final Location spawn = world.spawn;
        this.session.sendPacket(new SpawnPositionS2CPacket((int) spawn.getX(), (int) spawn.getY(), (int) spawn.getZ()));

        // Send player location to enter the world
        this.session.sendPacket(new PlayerPosLookS2CPacket(0, 100, 0, 0, 0, 100.240000009536743D, isGrounded()));

        this.sendMessage("§6Welcome to Composter :)");
        this.sendMessage("§cComposter is still in early development.");
        this.sendMessage("§cMany features are incomplete!");
    }

    public Session getSession() {
        return this.session;
    }

    public World getWorld() {
        return this.world;
    }

    public String getUsername() {
        return this.username;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public void sendMessage(final String message) {
        this.session.sendPacket(new ChatBiPacket(message));
    }

    public boolean isGrounded() { // TODO lol this is retarded
        try {
            return world.getChunk(
                    location.getChunk().getX(),
                    location.getChunk().getZ()
            ).get().getType(
                    location.getBlockX(),
                    location.getBlockY() - 1,
                    location.getBlockZ()
            ) != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void teleport(@NotNull Location location) {
        this.location = location;
        session.sendPacket(new PlayerPosLookS2CPacket(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), DEFAULT_STANCE, isGrounded())); // TODO figure out grounded detection
    }

    public void tick() {
        if (session.getState() != Session.State.PLAY) return;
        updateChunks(false);

        this.session.sendPacket(new KeepAliveBiPacket());
        System.out.println(String.format("%s, %s, %s", location.getX(), location.getY(), location.getZ()));
        System.out.println("Grounded?: " + isGrounded());
        //System.out.println(String.format("Current chunk- X: %s, Z: %s", location.getChunk().getX(), location.getChunk().getZ()));
    }

    private void updateChunks(boolean sync) {
        final Set<Chunk.Key> previousChunks = new HashSet<>(loadedChunks);
        final int centralX = ((int) this.location.getX()) / 16;
        final int centralZ = ((int) this.location.getZ()) / 16;
        final int renderDistance = 10; // customizable in config

        for (int x = (centralX - renderDistance); x <= (centralX + renderDistance); x++) {
            for (int z = (centralZ - renderDistance); z <= (centralZ + renderDistance); z++) {
                Chunk.Key key = new Chunk.Key(x, z);
                if (!loadedChunks.contains(key)) {
                    loadedChunks.add(key);

                    if (sync) { // TODO hacky af but itll do for now
                        try {
                            Chunk chunk = world.getChunk(x, z).get();
                            this.session.sendPacket(new PreChunkS2CPacket(x, z, true));
                            this.session.sendPacket(new MapChunkS2CPacket(x * 16, (short) 0, z * 16, chunk.serializeTileData()));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        final int finalX = x;
                        final int finalZ = z;

                        world.getChunk(x, z).thenAccept(chunk -> {
                            this.session.sendPacket(new PreChunkS2CPacket(finalX, finalZ, true));
                            this.session.sendPacket(new MapChunkS2CPacket(finalX * 16, (short) 0, finalZ * 16, chunk.serializeTileData()));
                        });
                    }
                }

                previousChunks.remove(key);
            }
        }

        for (Chunk.Key key : previousChunks) {
            this.session.sendPacket(new PreChunkS2CPacket(key.getX(), key.getZ(), false));
            loadedChunks.remove(key);
        }

        previousChunks.clear();
    }
}
