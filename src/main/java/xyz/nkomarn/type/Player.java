package xyz.nkomarn.type;

import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.packets.*;
import xyz.nkomarn.world.World;

import java.util.HashSet;
import java.util.Set;

public final class Player extends Entity {

    private final String username;
    private final Session session;
    private static Set<Chunk.Key> loadedChunks = new HashSet<>();

    //TODO crouching support

    public Player(final Session session, final String username) {
        super(Composter.getWorld());
        this.session = session;
        this.username = username;
        this.location = world.spawn;

        this.updateChunks();

        // Send spawn position
        final Location spawn = world.spawn;
        this.session.sendPacket(new PacketSpawnPosition((int) spawn.getX(),
            (int) spawn.getY(), (int) spawn.getZ()));

        // Send player location to enter the world
        this.session.sendPacket(new PacketPlayerPositionAndLook(
            5, 15, 67.240000009536743D, 10, 0, 0, false));

        this.world.broadcastMessage(String.format("§e%s joined the server.", username));
        this.sendMessage("§6Welcome to Composter :)");
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
        this.session.sendPacket(new PacketChat(message));
    }

    public void tick() {
        if (session.getState() != State.PLAY) return;
        //updateChunks();
    }

    private void updateChunks() {
            final Set<Chunk.Key> previousChunks = new HashSet<>(loadedChunks);
            final int centralX = ((int) this.location.getX()) / 16;
            final int centralZ = ((int) this.location.getZ()) / 16;
            final int renderDistance = 1;

            for (int x = (centralX - renderDistance); x <= (centralX + renderDistance); x++) {
                for (int z = (centralZ - renderDistance); z <= (centralZ + renderDistance); z++) {
                    Chunk.Key key = new Chunk.Key(x, z);
                    if (!loadedChunks.contains(key)) {
                        loadedChunks.add(key);
                        Composter.getLogger().debug(String.format(
                            "Allocating space for chunk (%s, %s).", x, z));

                        this.session.sendPacket(new PacketPreChunk(x * 16, z * 16, true));
                        this.session.sendPacket(new PacketMapChunk(x, (short) 0, z,
                            world.getChunkAt(x, z).serializeTileData()));
                    }
                    previousChunks.remove(key);
                }
            }
            for (Chunk.Key key : previousChunks) {
                Composter.getLogger().debug(String.format("Unloading chunk (%s, %s).",
                    key.getX(), key.getZ()));

                this.session.sendPacket(new PacketPreChunk(key.getX(), key.getZ(), false));
                loadedChunks.remove(key);
            }
            previousChunks.clear();
    }

}
