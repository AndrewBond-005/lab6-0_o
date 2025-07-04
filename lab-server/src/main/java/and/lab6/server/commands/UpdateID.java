package and.lab6.server.commands;

import and.lab6.server.managers.CollectionManager;
import and.lab6.server.utility.Console;
import models.Worker;
import util.Request;
import util.Response;

public class UpdateID extends Command {
    private final CollectionManager collectionManager;
    private final Console console;
    public static Worker worker = null;

    public UpdateID(Console console, CollectionManager collectionManager) {
        super("update_id {id}", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public Response execute(Request request) {
        if (request.args() == null) {
            return new Response("Не введён аругемнт", null,  449);
        }
        worker = request.workers().get(0);
        int id = Integer.parseInt(request.args().get(0));
        worker.setId(id);
        collectionManager.update(worker);
        return new Response("Worker успешно обновлён", null,  200);
    }
}