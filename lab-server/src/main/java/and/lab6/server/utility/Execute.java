package and.lab6.server.utility;

import and.lab6.server.managers.CommandManager;
import and.lab6.server.managers.UDPManager;
import util.ProgramStatus;
import util.Request;

public class Execute {
    private final Console console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;

    public Execute(CommandManager commandManager, Console console, UDPManager udpManager) {
        this.commandManager = commandManager;
        this.console = console;
        this.udpManager = udpManager;
    }

    public void execute() {
        while (true) {
            console.println("ожидаем получения");
            Object object = udpManager.receive();
            console.println("получили:" + object.toString());
            if (object instanceof Request request) {
                var commandName = request.command();
                var command = commandManager.getCommands().get(commandName);
                var response = command.execute(request);
                udpManager.send(response);
               // console.println(response.toString());
            } else if (object instanceof ProgramStatus) {
                udpManager.somethingWithClient((ProgramStatus) object, commandManager);
            }
        }
    }
}
//
//while (true) {
//            console.println("ожидаем получения");
//            Object object = udpManager.receive();
//            console.println("получили:");
//            if (object instanceof Request) {
//                console.println(((Request) object).command());
//                mustSend = 1;
//            } else if (object instanceof ProgramStatus) {
//                console.println(((ProgramStatus) object).toString());
//                mustSend = 0;
//            } else {
//                console.println("что-то непонятное");
//                mustSend = 0;
//            }
//            if (mustSend == 1){
//                console.println("что отправить");
//                udpManager.send(new Request(scanner.nextLine(), null, null));
//