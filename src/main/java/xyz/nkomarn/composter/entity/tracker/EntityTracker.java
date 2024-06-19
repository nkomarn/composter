package xyz.nkomarn.composter.entity.tracker;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import kyta.composter.Tickable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundAddEntityPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundAddPlayerPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundRemoveEntityPacket;

public class EntityTracker implements Tickable {
    private static final Logger LOGGER = LoggerFactory.getLogger("Entity Tracker");
    private final Player player;
    private final Int2ObjectMap<TrackedEntity> trackedEntities;

    public EntityTracker(@NotNull Player player) {
        this.player = player;
        this.trackedEntities = new Int2ObjectOpenHashMap<>();
    }

    @Override
    public void tick(long currentTick) {
        /* track any newly added entities */
        for (var entity : player.getWorld().getEntities()) {
            if (entity.getId() != player.getId() && !trackedEntities.containsKey(entity.getId())) {
                trackNewEntity(entity);
            }
        }

        /* un-track old entities and tick all tracked entities */
        var iterator = trackedEntities.values().iterator();

        while (iterator.hasNext()) {
            var trackedEntity = iterator.next();

            if (trackedEntity.getEntity().isRemoved()) {
                untrackEntity(trackedEntity.getEntity());
                iterator.remove();
                continue;
            }

            trackedEntity.tick(currentTick);
        }
    }

    private void trackNewEntity(Entity entity) {
        if (entity instanceof Player otherPlayer) {
            player.connection().sendPacket(new ClientboundAddPlayerPacket(otherPlayer));
        } else {
            player.connection().sendPacket(new ClientboundAddEntityPacket(entity));
        }

        var trackedEntity = new TrackedEntity(entity, packet -> {
//             player.getWorld().server.getLogger().info("sending movement packet: {}", packet.getClass().getSimpleName());
            player.connection().sendPacket(packet);
            return null;
        });

        trackedEntities.put(entity.getId(), trackedEntity);
        LOGGER.info("{} began tracking {} (entity #{}).", player.getUsername(), entity.getClass().getSimpleName(), entity.getId());
    }

    private void untrackEntity(Entity entity) {
        player.connection().sendPacket(new ClientboundRemoveEntityPacket(entity));
        LOGGER.info("{} untracked {} (entity #{}).", player.getUsername(), entity.getClass().getSimpleName(), entity.getId());
    }
}
