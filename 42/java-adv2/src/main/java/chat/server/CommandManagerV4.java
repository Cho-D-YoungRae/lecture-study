package chat.server;

import chat.server.command.ChangeCommand;
import chat.server.command.Command;
import chat.server.command.DefaultCommand;
import chat.server.command.ExitCommand;
import chat.server.command.JoinCommand;
import chat.server.command.MessageCommand;
import chat.server.command.UsersCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandManagerV4 implements CommandManager {

    private static final String DELIMETER = "\\|";
    private final Map<String, Command> commands = new HashMap<>();
    private final Command defaultCommand = new DefaultCommand();

    public CommandManagerV4(SessionManager sessionManager) {
        commands.put("/join", new JoinCommand(sessionManager));
        commands.put("/message", new MessageCommand(sessionManager));
        commands.put("/change", new ChangeCommand(sessionManager));
        commands.put("/users", new UsersCommand(sessionManager));
        commands.put("/exit", new ExitCommand());
    }

    @Override
    public void execute(String totalMessage, Session session) throws IOException {
        String[] args = totalMessage.split(DELIMETER);
        String key = args[0];

        Command command = commands.getOrDefault(key, defaultCommand);
          command.execute(args, session);
    }
}
