package xyz.nkomarn.entity;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.command.CommandSource;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.packet.bi.ChatBiPacket;
import xyz.nkomarn.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.protocol.packet.s2c.MapChunkS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.PlayerPosLookS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.PreChunkS2CPacket;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public final class Player extends Entity implements CommandSource {

    private final Session session;
    private final String username;
    private final Set<Chunk.Key> loadedChunks = new HashSet<>();

    private static final double DEFAULT_STANCE = 67.240000009536743;
    //TODO crouching support

    public Player(@NotNull Session session, @NotNull String username) {
        super(Composter.SPAWN.getWorld());
        this.session = session;
        this.username = username;
    }

    public Session getSession() {
        return session;
    }

    public String getUsername() {
        return username;
    }

    public World getWorld() {
        return world;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        session.sendPacket(new ChatBiPacket(message));
    }

    public void teleport(@NotNull Location location) {
        this.location = location;
        syncChunks(true);
        updateLocation();
    }

    public void updateLocation() {
        session.sendPacket(new PlayerPosLookS2CPacket(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                DEFAULT_STANCE,
                isTouchingGround()
        ));
    }

    // @Override - override once entities are implemented (should players be ticked differently?)
    public void tick() {
        syncChunks(false);
        this.session.sendPacket(new KeepAliveBiPacket()); // don't send every tick but for now im lazy so keep this
    }

    public synchronized void syncChunks(boolean sync) {
        final Set<Chunk.Key> previousChunks = new HashSet<>(loadedChunks);
        final int centralX = ((int) this.location.getX()) / 16;
        final int centralZ = ((int) this.location.getZ()) / 16;
        final int viewDistance = 8; // customizable in config

        for (int x = (centralX - viewDistance); x <= (centralX + viewDistance); x++) {
            for (int z = (centralZ - viewDistance); z <= (centralZ + viewDistance); z++) {
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
