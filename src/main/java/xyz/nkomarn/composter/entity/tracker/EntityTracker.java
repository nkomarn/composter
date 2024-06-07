package xyz.nkomarn.composter.entity.tracker;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundAddPlayerPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundRemoveEntityPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundTeleportEntityPacket;

public class EntityTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger("Entity Tracker");
    private final Player player;
    private final IntSet trackedEntities;

    public EntityTracker(@NotNull Player player) {
        this.player = player;
        this.trackedEntities = new IntOpenHashSet();
    }

    public void tick() {
        for (var entity : player.getWorld().getEntities()) {
            if (entity.getId() == player.getId()) {
                continue;
            }

            if (entity.isRemoved()) {
                untrackEntity(entity);
                continue;
            }

            if (!trackedEntities.contains(entity.getId())) {
                trackNewEntity(entity);
            }

            updateLocation(entity);
        }
    }

    private void trackNewEntity(Entity entity) {
        if (entity instanceof Player otherPlayer) {
            player.connection().sendPacket(new ClientboundAddPlayerPacket(otherPlayer));
        }

        trackedEntities.add(entity.getId());
        LOGGER.info("{} is now tracking entity with id {}.", player.getUsername(), entity.getId());
    }

    private void untrackEntity(Entity entity) {
        trackedEntities.remove(entity.getId());
        player.connection().sendPacket(new ClientboundRemoveEntityPacket(entity));
        LOGGER.info("{} untracked entity with id {}.", player.getUsername(), entity.getId());
    }

    public void updateLocation(Entity entity) {
        player.connection().sendPacket(new ClientboundTeleportEntityPacket(entity));
    }
}
