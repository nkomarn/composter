package xyz.nkomarn.type;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;

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

        // Send spawn position to player
        /*ByteBuf spawnPosition = Unpooled.buffer();
        spawnPosition.writeInt(0x06);
        spawnPosition.writeInt((int) spawn.getX());
        spawnPosition.writeInt((int) spawn.getY());
        spawnPosition.writeInt((int) spawn.getZ());
        player.getSession().write(spawnPosition);
        player.setLocation(spawn);*/

        // Player look + pos packet
        ByteBuf look = Unpooled.buffer();
        look.writeByte(0x0D);
        look.writeDouble(this.location.getX());
        look.writeDouble(this.location.getY());
        look.writeDouble(this.location.getY() + 1.62D);
        look.writeDouble(this.location.getZ());
        look.writeFloat(this.location.getYaw());
        look.writeFloat(this.location.getPitch());
        look.writeBoolean(false);
        session.write(look);

        Composter.getWorld().broadcastMessage(String.format("§e%s joined the server.", username));
        session.sendMessage("§6Welcome to Composter :)");
    }

    public Session getSession() {
        return this.session;
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

    public void tick() {
        if (session.getState() != State.PLAY) return;
        updateChunks();
    }

    private void updateChunks() {
        Composter.chunkPool.submit(() -> {
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

                        // Send a 0x32
                        ByteBuf loadChunk = Unpooled.buffer();
                        loadChunk.writeInt(0x32);
                        loadChunk.writeInt(x);
                        loadChunk.writeInt(z);
                        loadChunk.writeBoolean(true);
                        session.write(loadChunk);

                        // Send compressed chunk (0x33)
                        session.write(world.getChunkAt(x, z).getAsBuffer());
                    }
                    previousChunks.remove(key);
                }
            }
            for (Chunk.Key key : previousChunks) {
                Composter.getLogger().debug(String.format("Unloading chunk (%s, %s).",
                    key.getX(), key.getZ()));

                // Send a 0x32 to unload the chunk
                ByteBuf loadChunk = Unpooled.buffer();
                loadChunk.writeInt(0x32);
                loadChunk.writeInt(key.getX());
                loadChunk.writeInt(key.getZ());
                loadChunk.writeBoolean(false);
                session.write(loadChunk);
                loadedChunks.remove(key);
            }
            previousChunks.clear();
        });
    }

}
