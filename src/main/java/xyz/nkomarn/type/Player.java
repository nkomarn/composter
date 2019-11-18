package xyz.nkomarn.type;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;

import java.util.HashSet;
import java.util.Set;

public final class Player extends Entity {

    private final String name;
    private final Session session;

    private Set<Chunk.Key> loadedChunks = new HashSet<Chunk.Key>();

    //TODO crouching support

    public Player(final Session session, final String name) {
        super(Composter.getWorld()); // TODO move getServer() to session and then getServer().getworld();
        this.name = name;
        this.session = session;
        this.sendChunks();
    }

    // Tick
    public void tick() {
        // Keepalive
        session.send(Unpooled.EMPTY_BUFFER);
        if (session.getState() != State.PLAY) return;
        sendChunks();
    }

    // Send chunks
    private void sendChunks() {
        Composter.chunkPool.submit(() -> {
            final Set<Chunk.Key> previousChunks = new HashSet<Chunk.Key>(loadedChunks); // Take a "snapshot"

            final int centralX = ((int) location.getX()) / 16;
            final int centralZ = ((int) location.getZ()) / 16;

            final int renderDistance = 1; // TODO configurable in composter.yml

            for (int x = (centralX - renderDistance); x <= (centralX + renderDistance); x++) {
                for (int z = (centralZ - renderDistance); z <= (centralZ + renderDistance); z++) {
                    Chunk.Key key = new Chunk.Key(x, z);
                    if (!loadedChunks.contains(key)) {
                        loadedChunks.add(key);

                        System.out.println(String.format("Allocating space for chunk (%s, %s).", x, z));

                        // Send a 0x32
                        ByteBuf loadChunk = Unpooled.buffer();
                        loadChunk.writeInt(0x32);
                        loadChunk.writeInt(x);
                        loadChunk.writeInt(z);
                        loadChunk.writeBoolean(true);
                        session.send(loadChunk);

                        // Send compressed chunk (0x33)
                        session.send(world.getChunkAt(x, z).getAsBuffer());
                    }
                    previousChunks.remove(key);
                }
            }

            for (Chunk.Key key : previousChunks) {
                // Send a 0x32 to unload the chunk
                ByteBuf loadChunk = Unpooled.buffer();
                loadChunk.writeInt(0x32);
                loadChunk.writeInt(key.getX());
                loadChunk.writeInt(key.getZ());
                loadChunk.writeBoolean(false);
                session.send(loadChunk);
                loadedChunks.remove(key);
            }
            previousChunks.clear();
        });
    }

}
