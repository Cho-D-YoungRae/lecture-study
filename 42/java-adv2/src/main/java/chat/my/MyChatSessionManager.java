package chat.my;

import network.tcp.v6.SessionV6;

import java.util.ArrayList;
import java.util.List;

public class MyChatSessionManager {

    private final List<SessionV6> sessions = new ArrayList<>();

    public synchronized void add(SessionV6 session) {
        sessions.add(session);
    }

    public synchronized void remove(SessionV6 session) {
        sessions.remove(session);
    }

    public synchronized void closeAll() {
        for (SessionV6 session : sessions) {
            session.close();
        }
        sessions.clear();
    }
}
