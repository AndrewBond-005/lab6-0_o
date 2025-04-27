package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import util.Request;
import util.Response;


public class RemoveGreater extends Command {
    private final CollectionManager collectionManager;
    private final Console console;

    public RemoveGreater(Console console, CollectionManager collectionManager) {
        super("remove_greater {element}", "удалить из коллекции все элементы, большие, чем заданный");
        this.collectionManager = collectionManager;
        this.console = console;
    }


    public Response execute(Request request) {
        if (request.args() != null) {
            return new Response("Введен лишний аргумент", null, 450);
        }
        collectionManager.getCollection().removeIf(item -> item.compareTo(request.workers().get(0)) < 0);
        return new Response("Элементы успешно удалёны", null, 200);
    }
}
