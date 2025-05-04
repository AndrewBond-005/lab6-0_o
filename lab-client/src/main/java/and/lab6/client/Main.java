package and.lab6.client;

import and.lab6.client.commands.*;
import and.lab6.client.managers.CommandManager;
import and.lab6.client.managers.ReceivingManager;
import and.lab6.client.managers.SendingManager;
import and.lab6.client.managers.UDPManager;
import and.lab6.client.utility.Execute;
import and.lab6.client.utility.StandardConsole;
import and.lab6.client.utility.Terminate;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        var console = new StandardConsole();
        int clientPort = 10000 + (int) (Math.random() * 40001);
//
        int port=1200;
//
        var udpManager = new UDPManager(new ReceivingManager(), new SendingManager(), port, clientPort);
        Runtime.getRuntime().addShutdownHook(new Terminate(udpManager));
        CommandManager commandManager = new CommandManager();
        Map<String, Command> com = new HashMap<>();
        com.put("add", new Add(console));
        com.put("add_if_max", new AddIfMax(console));
        com.put("clear", new Clear(console));
        com.put("filter_by_status", new FilterByStatus(console));
        com.put("generate", new Generate(console));
        com.put("help", new Help(console));
        com.put("info", new Info(console));
        com.put("exit", new Exit(console));
        com.put("remove_by_id", new RemoveById(console));
        com.put("show", new Show(console));
        com.put("print_field_ascending_status", new PrintFieldAscendingStatus(console));
        com.put("min_by_position", new MinByPosition(console));
        com.put("update_id", new UpdateID(console));
        com.put("remove_lower", new RemoveLower(console));
        com.put("remove_greater", new RemoveGreater(console));
        com.put("es", new ExecuteScript(console, commandManager));
        commandManager.setCommands(com);
        new Execute(commandManager, console, udpManager).execute();

    }
}
//Обязанности клиентского приложения:
//
//Чтение команд из консоли.
//Валидация вводимых данных.
//Сериализация введённой команды и её аргументов.
//Отправка полученной команды и её аргументов на сервер.
//Обработка ответа от сервера (вывод результата исполнения команды в консоль).
//Команду save из клиентского приложения необходимо убрать.
//Команда exit завершает работу клиентского приложения.

////Необходимо выполнить следующие требования:
////
////Операции обработки объектов коллекции должны быть реализованы с помощью Stream API с использованием лямбда-выражений.
////Объекты между клиентом и сервером должны передаваться в сериализованном виде.
////Объекты в коллекции, передаваемой клиенту, должны быть отсортированы по имени
////Клиент должен корректно обрабатывать временную недоступность сервера.
////Обмен данными между клиентом и сервером должен осуществляться по протоколу UDP
////Для обмена данными на сервере необходимо использовать датаграммы
////Для обмена данными на клиенте необходимо использовать сетевой канал
////Сетевые каналы должны использоваться в неблокирующем режиме.