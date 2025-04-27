package and.lab6.client.utility;

import and.lab6.client.commands.ExecuteScript;
import and.lab6.client.managers.CollectionManager;
import and.lab6.client.managers.CommandManager;
import and.lab6.client.managers.UDPManager;
import util.ProgramStatus;
import util.Request;
import util.Response;

import java.util.Collections;


public class Execute {
    private final Console console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;
    private boolean serverAvailable = false;
    private final long RECEIVE_TIMEOUT = 50; // 100ms
    private final CollectionManager collectionManager = new CollectionManager();
    private int packetCount=0;


    public Execute(CommandManager commandManager, Console console, UDPManager udpManager) {
        this.commandManager = commandManager;
        this.console = console;
        this.udpManager = udpManager;
    }

    public void execute() {
        udpManager.send(ProgramStatus.CLIENT_CONNECTS);
        while (!serverAvailable) {
            Object response = udpManager.receive(RECEIVE_TIMEOUT);
            if (response instanceof ProgramStatus) {
                answerIsProgrammStatus((ProgramStatus) response);
            }
        }

        console.println("Успешно подключились к серверу");
        while (true) {
            recieve();
            if(packetCount>0){
                packetCount--;;
            }
            else if (console.hasInput()) {
                var line = console.readln().trim();
                if (line.isEmpty()) continue;
//                if (line.equals("yes")) {
//                    console.println(collectionManager.getCollection().size());
//                    var start = collectionManager.getLastWorker();
//                    var end = collectionManager.getLastWorker() + udpManager.getMaxWorkerCount();
//                    collectionManager.getCollection().stream()
//                            .skip(start)
//                            .limit(end - start)
//                            .forEach(console::println);
//                    collectionManager.setLastWorker(
//                            collectionManager.getLastWorker() + udpManager.getMaxWorkerCount());
//                    continue;
//                } else {
//                    collectionManager.removeAll();
//                }
                String[] tokens = line.split(" ", 2);
                var command = commandManager.getCommands().get(tokens[0]);

                if (command == null) {
                    console.printError("Команда не распознана");
                    continue;
                }

                Request request = executeCommand(tokens);
                if (request != null) {
                    send(request);
                }
            }
        }
    }


    private Request executeCommand(String[] tokens) {
        Request request = null;
        if (tokens[0].equals("es")) {
            request = (Request) executeScipt(tokens);
        } else if (tokens[0].equals("exit")) {
            System.exit(0);
        } else {
            var res = notExecuteScript(tokens);
            if (res instanceof Request) {
                request = (Request) res;
            } else if (res instanceof String) {
                console.println(res);
            }
        }
        return request;
    }

    private void recieve() {
        Object response = udpManager.receive(RECEIVE_TIMEOUT);
        if (response != null) {
            if (response instanceof Response) {
                answerIsResponse((Response) response);
            } else if (response instanceof ProgramStatus) {
                answerIsProgrammStatus((ProgramStatus) response);
            } else {
                console.println("Получен неизвестный объект: " +
                        (response == null ? "null" : response.toString()));
            }
        }
    }

    private void send(Request request) {
        do {
            if (serverAvailable) {
                console.println(request);
                udpManager.send(request);
                break;
            }
        } while (true);
    }

    public Object executeScipt(String[] tokens) {
        int res = ((ExecuteScript) commandManager.getCommands().get("es")).
                execute(tokens.length > 1 ? tokens[1] : null, true);
        if (res == -1) {
            console.printError("");
            return null;
        } else {
            var es = ((ExecuteScript) commandManager.getCommands().get("es"));
            var request = new Request("es", es.getArgs(), es.getWorkers());
            return request;
        }
    }

    public Object notExecuteScript(String[] tokens) {
        var command = commandManager.getCommands().get(tokens[0]);
        var val = command.validate(tokens.length > 1 ? tokens[1] : null, false);
        if (val instanceof Boolean) {
            if ((Boolean) val)
                return new Request(tokens[0], tokens.length > 1 ?
                        Collections.singletonList((tokens[1])) : null, null);
            else return null;
        } else {
            return (val.toString());
        }
    }

    public void answerIsProgrammStatus(ProgramStatus programStatus) {
        if (programStatus == ProgramStatus.SERVER_DISCONNECTS) {
            console.println("сервер не доступен");
            serverAvailable = false;
        }
        if (programStatus == ProgramStatus.SERVER_CONNECTS) {
            console.println("сервер доступен");
            serverAvailable = true;
        }
    }

    public void answerIsResponse(Response response) {
        if (response.returnCode() < 0) {
            packetCount =-response.returnCode();
        }
        if (response.returnCode() != 200 && response.returnCode() != 0)
            console.printError(response.message());
        else if (response.returnCode() != 0)
            console.println(response.message());
        if (response.workers() != null && response.returnCode() != 0) {
            for (var w : response.workers()) {
                console.println(w);
            }
        }
        if (response.returnCode() == 0) {
            if (response.workers() != null) {
                var k = response.workers().size();
                for (int j = 1; j <= k; j++) {
                    //collectionManager.add(response.workers().get(j - 1));
                    console.println(response.workers().get(j - 1).toString());
                }
            }
            ///console.println(collectionManager.getCollection().size());
        }
    }
}


//
// udpManager.send(ProgramStatus.CLIENT_CONNECTS);
//        console.println("Подключились к серверу");
//        boolean serverAvailable = true;
//        while (true) {
//            if (serverAvailable) {
//                console.println("что отправить");
//                udpManager.send(new Request(scanner.nextLine(), null, null));
//                console.println("отправили");
//                console.println("ждём получения");
//            }
//            Object obj = udpManager.receive();
//            console.println("получили:");
//            if (obj instanceof Request)
//                console.println(((Request) obj).command());
//            else if (obj instanceof ProgramStatus) {
//                console.println(((ProgramStatus) obj).toString());
//                ProgramStatus programStatus = (ProgramStatus) obj;
//                if (programStatus == ProgramStatus.SERVER_DISCONNECTS) {
//                    console.println("сервер не доступен");
//                    serverAvailable = false;
//                }
//                if (programStatus == ProgramStatus.SERVER_CONNECTS) {
//                    console.println("сервер доступен");
//                    serverAvailable = true;
//                }
//            } else console.println("что-то непонятное");
//        }


//
//        commandManager.setCommands((Map<String, Command>) obj);
//        Map<String, Command> commands = commandManager.getCommands();
//        console.println((Response)obj);
//        String[] commands = ((Response) obj).message().split(" ");


//        for (Map.Entry<String, Command> entry : commands.entrySet()) {
//            if(!entry.getKey().equals("es")){
//                entry.setValue(new DefaultCommand("дефолтная команда", "описание"));
//
//            }else{
//                entry.setValue(new ExecuteScript(console,commandManager));
//            }
//        }
//
//        commandManager.setCommands(commands);
//        console.println(commandManager.getCommands());