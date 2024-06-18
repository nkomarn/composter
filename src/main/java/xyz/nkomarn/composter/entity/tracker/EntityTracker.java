package xyz.nkomarn.composter.entity.tracker;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundAddEntityPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundAddPlayerPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundRemoveEntityPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundTeleportEntityPacket;

public class EntityTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger("Entity Tracker");
    private final Player player;
    private final Int2ObjectMap<Entity> trackedEntities;

    public EntityTracker(@NotNull Player player) {
        this.player = player;
        this.trackedEntities = new Int2ObjectOpenHashMap<>();
    }

    public void tick() {
        trackedEntities.values().removeIf(entity -> {
           if (entity.isRemoved()) {
               untrackEntity(entity);
               return true;
           }

           return false;
        });

        for (var entity : player.getWorld().getEntities()) {
            if (entity.getId() == player.getId()) {
                continue;
            }

            if (!trackedEntities.containsKey(entity.getId())) {
                trackNewEntity(entity);
            }

            updateLocation(entity);
        }
    }

    private void trackNewEntity(Entity entity) {
        if (entity instanceof Player otherPlayer) {
            player.connection().sendPacket(new ClientboundAddPlayerPacket(otherPlayer));
        } else {
            player.connection().sendPacket(new ClientboundAddEntityPacket(entity));
        }

        trackedEntities.put(entity.getId(), entity);
        LOGGER.info("{} began tracking {} (entity #{}).", player.getUsername(), entity.getClass().getSimpleName(), entity.getId());
    }

    private void untrackEntity(Entity entity) {
        trackedEntities.remove(entity.getId());
        player.connection().sendPacket(new ClientboundRemoveEntityPacket(entity));
        LOGGER.info("{} untracked {}} (entity #{}).", player.getUsername(), entity.getClass().getSimpleName(), entity.getId());
    }

    public void updateLocation(Entity entity) {
        player.connection().sendPacket(new ClientboundTeleportEntityPacket(entity));
    }
}
