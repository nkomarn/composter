package xyz.nkomarn.composter.entity;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
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
    private final LongSet loadedChunks;
    private boolean crouching;

    private static final double DEFAULT_STANCE = 67.240000009536743;
    //TODO crouching support

    public Player(Connection connection, String username) {
        super(Composter.SPAWN.getWorld());
        this.connection = connection;
        this.username = username;
        this.tracker = new EntityTracker(this);
        this.loadedChunks = new LongOpenHashSet();
    }

    public Connection connection() {
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
                false
                // isTouchingGround()
        ));
    }

    // @Override - override once entities are implemented (should players be ticked differently?)
    public void tick() {
        tracker.tick();
        syncChunks(false);
        // this.session.sendPacket(new KeepAliveBiPacket()); // don't send every tick but for now im lazy so keep this

        // TODO tracker.tick();
    }

    public void syncChunks(boolean sync) {
        // calculate which chunks need to be loaded.
        // make sure all of those chunks are loaded (check the loaded set for keys)
        // if any extra chunk is loaded, send unload packet for them!

        var currentChunkX = location.getBlockX() >> 4;
        var currentChunkZ = location.getBlockZ() >> 4;
        var viewDistance = 10;

        var currentlyLoaded = new LongOpenHashSet(loadedChunks);

        for (var x = (currentChunkX - viewDistance); x < (currentChunkX + viewDistance); x++) {
            for (var z = (currentChunkZ - viewDistance); z < (currentChunkZ + viewDistance); z++) {
                var chunkKey = Chunk.key(x, z);

                if (currentlyLoaded.contains(chunkKey)) {
                    currentlyLoaded.remove(chunkKey);
                    continue;
                }

                connection.sendPacket(new PreChunkS2CPacket(x, z, true));
                connection.sendPacket(new MapChunkS2CPacket(world.getChunk(x, z)));
                loadedChunks.add(chunkKey);
            }
        }

        /* FIXME occasionally causes chunk errors
        currentlyLoaded.removeIf(key -> {
            var chunk = world.getLoadedChunk(key);

            if (chunk != null) {
                connection.sendPacket(new PreChunkS2CPacket(chunk.getX(), chunk.getZ(), false));
            }

            return true;
        });
         */
    }
}
