package chat.my;

import java.util.ArrayList;
import java.util.List;

public class MyChatSessionManager {

    private final List<MyChatSession> sessions = new ArrayList<>();

    public synchronized void add(MyChatSession session) {
        sessions.add(session);
    }

    public synchronized void remove(MyChatSession session) {
        sessions.remove(session);
    }

    public synchronized void closeAll() {
        for (MyChatSession session : sessions) {
            session.close();
        }
        sessions.clear();
    }

    public List<String> getAllNames() {
        return sessions.stream().map(MyChatSession::getName).toList();
    }

    public void broadcast(String message) {
        for (MyChatSession session : sessions) {
            session.sendMessage(message);
        }
    }
}
