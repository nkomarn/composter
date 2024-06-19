package xyz.nkomarn.composter.entity;

import kyta.composter.world.ChunkPos;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.command.CommandSource;
import xyz.nkomarn.composter.entity.tracker.EntityTracker;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.packet.play.ClientboundChunkDataPacket;
import xyz.nkomarn.composter.network.protocol.packet.play.ClientboundChunkOperationPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundChatPacket;
import xyz.nkomarn.composter.world.World;

import java.util.HashSet;
import java.util.Set;

public final class Player extends Entity implements CommandSource {
    private static final int VIEW_DISTANCE = 16;
    public static final double DEFAULT_STANCE = 67.240000009536743;

    private final Connection connection;
    private final String username;
    private final EntityTracker tracker;
    private final Set<ChunkPos> visibleChunks;
    private boolean crouching;
    private boolean onGround;
    private long lastDigStartTime;

    //TODO crouching support

    public Player(World world, Connection connection, String username) {
        super(world);
        this.connection = connection;
        this.username = username;
        this.tracker = new EntityTracker(this);
        this.visibleChunks = new HashSet<>();
    }

    public Connection connection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        connection.sendPacket(new ClientboundChatPacket(message));
    }

    /*
    public void teleport(@NotNull Location location) {
        this.location = location;
        updateVisibleChunks();
        // updateLocation();
    }
     */

    public boolean isCrouching() {
        return crouching;
    }

    public void setCrouching(boolean crouching) {
        this.crouching = crouching;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public double getStance() {
        return DEFAULT_STANCE;
    }

    public long getLastDigStartTime() {
        return lastDigStartTime;
    }

    public void setLastDigStartTime(long lastDigStartTime) {
        this.lastDigStartTime = lastDigStartTime;
    }

    @Override
    public void tick(long currentTick) {
        super.tick(currentTick);
        tracker.tick(currentTick);

        /*
         * if this player has moved, update their visible chunks.
         */
        // if (tracker.hasChangedBlock()) {
        updateVisibleChunks();
        // }

        // this.session.sendPacket(new KeepAliveBiPacket());
    }

    public void updateVisibleChunks() {
        var currentBlock = getBlockPos();
        var currentChunk = new ChunkPos(currentBlock);
        int currentChunkX = currentChunk.getX();
        int currentChunkZ = currentChunk.getZ();

        /*
         * send chunk packets for chunks that the player should
         * be able to see from their current position.
         */
        var pendingUnload = new HashSet<>(visibleChunks);

        for (var x = (currentChunkX - VIEW_DISTANCE); x < (currentChunkX + VIEW_DISTANCE); x++) {
            for (var z = (currentChunkZ - VIEW_DISTANCE); z < (currentChunkZ + VIEW_DISTANCE); z++) {
                var pos = new ChunkPos(x, z);

                /*
                 * if the chunk is currently visible, don't
                 * send another chunk data packet to the client.
                 */
                if (visibleChunks.contains(pos)) {
                    pendingUnload.remove(pos);
                    continue;
                }

                /*
                 * if the chunk hasn't been loaded on the server
                 * side, don't send it to the client yet.
                 */
                var chunk = getWorld().getLoadedChunk(pos);
                if (chunk == null) continue;

                connection.sendPacket(new ClientboundChunkOperationPacket(x, z, true));
                connection.sendPacket(new ClientboundChunkDataPacket(chunk));
                visibleChunks.add(pos);
            }
        }

        /*
         * unload chunks that are no longer visible.
         */
        pendingUnload.forEach(pos -> {
            visibleChunks.remove(pos);
            connection.sendPacket(new ClientboundChunkOperationPacket(pos.getX(), pos.getZ(), false));
        });
    }
}
