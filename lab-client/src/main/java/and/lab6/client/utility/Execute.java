package and.lab6.client.utility;

import and.lab6.client.commands.ExecuteScript;
import and.lab6.client.commands.PrintFieldAscendingStatus;
import and.lab6.client.managers.CollectionManager;
import and.lab6.client.managers.CommandManager;
import and.lab6.client.managers.UDPManager;
import models.Worker;
import util.ProgramStatus;
import util.Request;
import util.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Execute {
    private final Console console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;
    private boolean serverAvailable = false;
    private final long RECEIVE_TIMEOUT = 100; // 100ms
    private final CollectionManager collectionManager = new CollectionManager();
    private int packetCount = 0;
    private List<Request> requests = new ArrayList<>();


    public Execute(CommandManager commandManager, Console console, UDPManager udpManager) {
        this.commandManager = commandManager;
        this.console = console;
        this.udpManager = udpManager;
    }

    private void connect() {
        udpManager.send(ProgramStatus.CLIENT_CONNECTS);
        console.println("Пробуем подключиться к серверу");
        var currentDate = System.currentTimeMillis();
        int i = 1;
        while (!serverAvailable) {
            if (System.currentTimeMillis() - currentDate > 5000) {
                if (i == 1) {
                    console.println("Похоже, сервер сильно загружен или недоступен. ");
                    console.println("Попробуйте подключиться позже или ожидайте подтверждение подключения от сервера");
                }
                i = 0;
                currentDate = System.currentTimeMillis();
                udpManager.send(ProgramStatus.CLIENT_CONNECTS);
            }
            Object response = udpManager.receive(RECEIVE_TIMEOUT);
            if (response instanceof ProgramStatus) {
                answerIsProgrammStatus((ProgramStatus) response);
                break;
            }
        }
    }

    public void execute() {
        connect();
        console.println("Успешно подключились к серверу");
        while (true) {
            recieve();
            if (!requests.isEmpty() && serverAvailable) {
                while (!requests.isEmpty()) {
                    if (requests.get(0) != null) {
                        send(requests.get(0));
                    }
                    requests.remove(0);
                }
            }
            if (!collectionManager.getCollection().isEmpty() && packetCount == 0) {
                console.println("Введите yes если хотите увидеть все элементы коллекции");
            }
            console.print(">");
            console.selectConsoleScanner();
            var line = console.readln().trim();
            if (line.isEmpty()) continue;
            String[] tokens = line.split(" ", 2);
            var command = commandManager.getCommands().get(tokens[0]);
            if (line.equals("yes") && !collectionManager.getCollection().isEmpty() && packetCount == 0) {
                for (Worker w : collectionManager.getCollection()) {
                    console.println(w);
                }
                collectionManager.removeAll();
            } else {
                collectionManager.removeAll();

                if (command == null) {
                    console.printError("Команда не распознана");
                    continue;
                }
                Request request = executeCommand(tokens);
                if (serverAvailable) {
                    if (request != null) {
                        send(request);
                    }
                } else {
                    console.println("Сервер сейчас недоступен.");
                    console.println("Ваша команда выполнится когда серевер станет доступным");
                    requests.add(request);
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
        if (packetCount == 0) packetCount++;
        if (packetCount > 0) {
            int i = 2;
            do {
                Object response = udpManager.receive(RECEIVE_TIMEOUT);
                // System.out.println("f" + String.valueOf(i));
                if (response != null) {
                    i = 3;
                    packetCount--;
                    if (response instanceof Response) {
                        answerIsResponse((Response) response);
                    } else if (response instanceof ProgramStatus) {
                        answerIsProgrammStatus((ProgramStatus) response);
                    } else {
                        console.println("Получен неизвестный объект: " +
                                response.toString());
                    }
                } else {
                    i--;
                }
                if (packetCount < 0) {
                    packetCount = 0;
                }
            } while (packetCount > 0 && i > 0);


        }


    }

    private void send(Request request) {
        do {
            if (serverAvailable) {
                //console.println(request);
                udpManager.send(request);
                break;
            }
            recieve();
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
            else {
                return null;
            }
        } else if (val instanceof String) {
            return (val.toString());
        } else {
            var list = new ArrayList<Worker>();
            list.add((Worker) val);
            return new Request(tokens[0], tokens.length > 1 ?
                    Collections.singletonList(tokens[1]) : null, list);
        }
    }

    public void answerIsProgrammStatus(ProgramStatus programStatus) {
        if (programStatus == ProgramStatus.SERVER_DISCONNECTS) {
            console.println("сервер недоступен");
            serverAvailable = false;
        }
        if (programStatus == ProgramStatus.SERVER_CONNECTS) {
            console.println("сервер доступен");
            serverAvailable = true;
        }
    }

    public void answerIsResponse(Response response) {
        //System.out.println(((Response) response).workers());
        //System.out.println(response.returnCode());
        if (response.returnCode() < 0) {
            packetCount = -response.returnCode();
        }
        if (response.returnCode() == 1001) {
            assert response.workers() != null;
            ((PrintFieldAscendingStatus) commandManager.
                    getCommands().get("print_field_ascending_status")).
                    execute(response.workers());
            return;
        }
        if (response.returnCode() != 200 && response.returnCode() > 0)
            console.printError(response.message());
        else if (response.returnCode() != 0)
            console.println(response.message());

        if (response.workers() != null && response.returnCode() == 0) {
            if (collectionManager.getCollection().isEmpty()) {
                console.println("Вот первые 50 элементов коллекции:");
                for (var w : response.workers()) {
                    console.println(w);
                }
                console.println("Продолжаем принимать сообщения с содержимым коллекции от сервера");
            }
            for (var w : response.workers()) {
                collectionManager.add(w);
            }
        } else if (response.workers() != null && response.returnCode() != 0) {
            for (var w : response.workers()) {
                console.println(w);
            }
        }
    }
}