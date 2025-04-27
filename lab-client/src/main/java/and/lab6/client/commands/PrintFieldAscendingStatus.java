package and.lab6.client.commands;

import and.lab6.client.utility.Console;
import models.Position;
import models.Worker;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class PrintFieldAscendingStatus extends Command {
    private final Console console;

    public PrintFieldAscendingStatus(Console console) {
        super("print_field_ascending_status", "вывести значения поля status всех элементов в порядке возрастания");
        this.console = console;
    }

    @Override
    public Object validate(String arguments, boolean scriprtMode) {
        return true;
    }
}