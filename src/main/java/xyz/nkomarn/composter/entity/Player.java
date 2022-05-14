package xyz.nkomarn.composter.entity;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.command.CommandSource;
import xyz.nkomarn.composter.entity.tracker.EntityTracker;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundChatPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.MapChunkS2CPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.PlayerPosLookS2CPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.PreChunkS2CPacket;
import xyz.nkomarn.composter.type.Chunk;
import xyz.nkomarn.composter.type.Location;
import xyz.nkomarn.composter.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public final class Player extends Entity implements CommandSource {

    private final Connection connection;
    private final String username;
    private final EntityTracker tracker;
    private final Set<Chunk.Key> loadedChunks = new HashSet<>();
    private boolean crouching;

    private static final double DEFAULT_STANCE = 67.240000009536743;
    //TODO crouching support

    public Player(@NotNull Connection connection, @NotNull String username) {
        super(Composter.SPAWN.getWorld());
        this.connection = connection;
        this.username = username;
        this.tracker = new EntityTracker(this);
    }

    public Connection getSession() {
        return connection;
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
    public String getName() {
        return getUsername();
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        connection.sendPacket(new ClientboundChatPacket(message));
    }

    public void teleport(@NotNull Location location) {
        this.location = location;
        syncChunks(true);
        // updateLocation();
    }

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public void updateLocation() {
        connection.sendPacket(new PlayerPosLookS2CPacket(
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
        tracker.tick();
        syncChunks(false);
        // this.session.sendPacket(new KeepAliveBiPacket()); // don't send every tick but for now im lazy so keep this

        // TODO tracker.tick();
    }

    public synchronized void syncChunks(boolean sync) {
        final Set<Chunk.Key> previousChunks = new HashSet<>(loadedChunks);
        final int centralX = ((int) this.location.getX()) / 16;
        final int centralZ = ((int) this.location.getZ()) / 16;
        final int viewDistance = 3; // customizable in config

        for (int x = (centralX - viewDistance); x <= (centralX + viewDistance); x++) {
            for (int z = (centralZ - viewDistance); z <= (centralZ + viewDistance); z++) {
                Chunk.Key key = new Chunk.Key(x, z);

                if (!loadedChunks.contains(key)) {
                    loadedChunks.add(key);

                    if (sync) { // TODO hacky af but itll do for now
                        try {
                            Chunk chunk = world.getChunk(x, z).get();
                            this.connection.sendPacket(new PreChunkS2CPacket(x, z, true));
                            this.connection.sendPacket(new MapChunkS2CPacket(x * 16, (short) 0, z * 16, chunk.serializeTileData()));
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        final int finalX = x;
                        final int finalZ = z;

                        world.getChunk(x, z).thenAccept(chunk -> {
                            this.connection.sendPacket(new PreChunkS2CPacket(finalX, finalZ, true));
                            this.connection.sendPacket(new MapChunkS2CPacket(finalX * 16, (short) 0, finalZ * 16, chunk.serializeTileData()));
                        });
                    }
                }

                previousChunks.remove(key);
            }
        }

        for (Chunk.Key key : previousChunks) {
            this.connection.sendPacket(new PreChunkS2CPacket(key.getX(), key.getZ(), false));
            loadedChunks.remove(key);
        }

        previousChunks.clear();
    }
}
