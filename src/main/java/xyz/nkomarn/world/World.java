package xyz.nkomarn.world;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.ByteBufUtil;
import xyz.nkomarn.world.generator.WorldGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class World {

    public final Location spawn = new Location(0, 15, 0); // TODO implement for player spawning at some point

    private ArrayList<Player> players = new ArrayList<>();
    private final HashMap<Chunk.Key, Chunk> chunks = new HashMap<>();

    // TODO entities list

    private long time = 0;
    private WorldGenerator generator;
    private ChunkIO io = new ChunkIO();

    public World(WorldGenerator generator) {
        this.generator = generator;
    }

    public Chunk getChunkAt(final int x, final int z) {
        Chunk.Key key = new Chunk.Key(x, z);
        Chunk chunk = chunks.get(key);
        if (chunk == null) {
            try {
                chunk = io.read(x, z);
            } catch (IOException e) {
                chunk = null;
            }

            if (chunk == null) {
                chunk = generator.generate(x, z);
            }
            chunks.put(key, chunk);
        }
        return chunk;
    }

    public void addPlayer(final Player player) {
        this.players.add(player);
    }

    public void removePlayer(final Player player) {
        this.players.remove(player);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void broadcastMessage(final String message) {
        ByteBuf chatMessage = Unpooled.buffer();
        chatMessage.writeInt(0x03);
        ByteBufUtil.writeString(chatMessage, message);
        players.forEach(player -> player.getSession().write(chatMessage));
    }

    // TODO save chunks, etc
}
