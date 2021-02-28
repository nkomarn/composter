package xyz.nkomarn.entity.tracker;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.entity.Entity;
import xyz.nkomarn.entity.Player;


public class EntityTracker {

    private final Player player;
    private Int2ObjectMap<Entity> trackedEntities;

    public EntityTracker(@NotNull Player player) {
        this.player = player;
        this.trackedEntities = Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>());
    }

    public void startTracking(Entity entity) {
        trackedEntities.put(entity.getId(), entity);

        if (entity instanceof Player) {
            /*Player visiblePlayer = (Player) entity;
            player.getSession().sendPacket(new NamedEntitySpawnS2CPacket(
                    entity.getId(),
                    visiblePlayer.getUsername(),
                    visiblePlayer.getLocation().getBlockX(),
                    visiblePlayer.getLocation().getBlockY(),
                    visiblePlayer.getLocation().getBlockZ(),
                    (byte) visiblePlayer.getLocation().getYaw(), // this is labelled as rotation, not sure about this
                    (byte) visiblePlayer.getLocation().getPitch(),
                    (short) 1 // TODO yes this is placeholder code
            ));*/
        }
    }
    
    private int distanceSquared(Entity entity) {
        int x = entity.getLocation().getBlockX();
        int y = entity.getLocation().getBlockY();
        int z = entity.getLocation().getBlockZ();
        return x * x + y * y + z * z;
    }

    public void tick() {
        for (Entity entity : player.getWorld().getEntities().values()) {
            /*if (entity == player) {
                continue;
            }*/

            if (trackedEntities.containsKey(entity.getId())) {
                continue;
            }

            int distance = distanceSquared(entity);
            System.out.println(distance + " blocks");
            if (distance > 9000) {
                continue;
            }

            System.out.println("Tracking entity with ID " + entity.getId());
            startTracking(entity);
        }

        /*player.getWorld().getEntities().values().stream()
                .filter(entity -> !trackedEntities.containsKey(entity.getId()))
                .filter(entity -> distanceSquared(entity) <= 30) // configurable in config for tracking range
                .forEach(entity -> {
*/

                    /*if (!trackedEntities.contains(entity)) {
                        if (entity instanceof Player) {
                            Player visiblePlayer = (Player) entity;

                            // Initialize the entity
                            player.getSession().sendPacket(new EntityS2CPacket(entity.getId()));


                            trackedEntities.add(visiblePlayer);

                            System.out.println("Started tracking entity with ID " + entity.getId());
                        }
                    }*/

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
                    // player.getSession().sendPacket(new EntityS2CPacket(entity.getId()));
               // });
    }

}
