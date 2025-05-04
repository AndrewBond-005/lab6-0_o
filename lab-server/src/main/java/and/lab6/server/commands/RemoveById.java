package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import models.Worker;
import util.Request;
import util.Response;

public class RemoveById extends Command {
    private final CollectionManager collectionManager;
    private final Console console;


    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id {element}", "удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("Не введён аргумент", null, 450);
        }
        collectionManager.remove(collectionManager.getById(Integer.parseInt(request.args().get(0))));
        return new Response("Элемент успешно удалён",null,200);
    }
}
