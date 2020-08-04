package xyz.nkomarn.entity.tracker;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.entity.Entity;
import xyz.nkomarn.entity.Player;
import xyz.nkomarn.protocol.packet.s2c.EntityS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.EntityTeleportS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.NamedEntitySpawnS2CPacket;

import java.util.HashSet;
import java.util.Set;

public class EntityTracker {

    private final Player player;
    private final Set<Entity> trackedEntities;

    public EntityTracker(@NotNull Player player) {
        this.player = player;
        this.trackedEntities = new HashSet<>();
    }

    public void tick() {
        player.getWorld().getEntities().values().stream()
                .filter(entity -> Math.abs(entity.getLocation().getX() - player.getLocation().getX()) <= 10)
                .filter(entity -> Math.abs(entity.getLocation().getZ() - player.getLocation().getZ()) <= 10)
                .forEach(entity -> {
                    if (player.getUUID().equals(entity.getUUID())) {
                        return;
                    }

                    // System.out.println("Tracking entity with ID " + entity.getId());

                    if (!trackedEntities.contains(entity)) {
                        if (entity instanceof Player) {
                            Player visiblePlayer = (Player) entity;

                            // Initialize the entity
                            player.getSession().sendPacket(new EntityS2CPacket(entity.getId()));

                            player.getSession().sendPacket(new NamedEntitySpawnS2CPacket(
                                    entity.getId(),
                                    visiblePlayer.getUsername(),
                                    visiblePlayer.getLocation().getBlockX(),
                                    visiblePlayer.getLocation().getBlockY(),
                                    visiblePlayer.getLocation().getBlockZ(),
                                    (byte) visiblePlayer.getLocation().getYaw(), // this is labelled as rotation, not sure about this
                                    (byte) visiblePlayer.getLocation().getPitch(),
                                    (short) 1 // TODO yes this is placeholder code
                            ));
                            trackedEntities.add(visiblePlayer);

                            System.out.println("Started tracking entity with ID " + entity.getId());
                        }
                    }

                    // TODO yes i should use relative move for less than 4 blocks, but this will do for testing purposes
                    /*player.getSession().sendPacket(new EntityTeleportS2CPacket(
                            entity.getId(),
                            entity.getLocation().getBlockX(),
                            entity.getLocation().getBlockY(),
                            entity.getLocation().getBlockZ(),
                            (byte) entity.getLocation().getYaw(),
                            (byte) entity.getLocation().getPitch()
                    ));*/

                    // Entity didn't move but still exists
                    player.getSession().sendPacket(new EntityS2CPacket(entity.getId()));
                });
    }
}
