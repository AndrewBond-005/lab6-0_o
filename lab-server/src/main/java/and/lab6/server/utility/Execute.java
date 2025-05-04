package and.lab6.server.utility;

import and.lab6.server.managers.CommandManager;
import and.lab6.server.managers.UDPManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ProgramStatus;
import util.Request;

public class Execute {
    private final Console console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;
    private static final Logger logger = LogManager.getLogger(Execute.class);


    public Execute(CommandManager commandManager, Console console, UDPManager udpManager) {
        this.commandManager = commandManager;
        this.console = console;
        this.udpManager = udpManager;
    }

    public void execute() {
        while (true) {
            logger.info("ожидаем получения");
            Object object = udpManager.receive();
            if (object instanceof Request request) {
                var commandName = request.command();
                var command = commandManager.getCommands().get(commandName);
                logger.info("пришла команда" + command.getName());
                var response = command.execute(request);
                logger.info("команда выполнена, сформирован ответ");
                udpManager.send(response);
                // System.out.println(response.toString());
            } else if (object instanceof ProgramStatus) {
                udpManager.somethingWithClient((ProgramStatus) object, commandManager);
            }
        }
    }
}
