package xyz.nkomarn.net;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static HashMap<Channel, Session> sessions = new HashMap<>();

    public static Session getSession(final Channel channel) {
        return sessions.get(channel);
    }

    public static void openSession(final Session session) {
        sessions.put(session.getChannel(), session);
    }

    public static void closeSession(final Channel channel) {
        sessions.remove(channel);
    }

    public static int sessionCount() {
        return sessions.size();
    }

    public static void tick() {
        for (Session session : sessions.values()) {
            //System.out.println("Ticking session.");
            session.tick();
        }
    }
}
