package and.lab6.server;

import and.lab6.server.commands.*;
import and.lab6.server.managers.*;
import and.lab6.server.utility.Execute;
import and.lab6.server.utility.StandardConsole;
import and.lab6.server.utility.Terminate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ProgramStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
        StandardConsole console = new StandardConsole();
        String fileName = "collection.csv";
        File file = new File(fileName);
        if (!file.exists()) {
            logger.warn("Файл collection.csv не существует");
        }
        var fileManager = new FileManager(fileName, console);
        UDPManager udpManager = null;
        int i = 1200;
        while (i <= 65000)
            try {
                udpManager = new UDPManager(i, new SendingManager(), new ReceivingManager());
                logger.info("Сервер открыт на порту: " + i);
                break;
            } catch (IOException e) {
                logger.info("Порт " + i+ " занят, пробуем следующий");
                i++;
            }

        Scanner scanner = new Scanner(System.in);
        Map<String, Command> com = new HashMap<>();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager(fileManager);
        com.put("add", new Add(collectionManager));
        com.put("add_if_max", new AddIfMax(collectionManager));
        com.put("clear", new Clear(collectionManager));
        com.put("filter_by_status", new FilterByStatus(collectionManager));
        com.put("generate", new Generate(collectionManager));
        com.put("help", new Help(commandManager, udpManager));
        com.put("info", new Info(console, collectionManager));
        com.put("exit", new Exit(console));
        com.put("remove_by_id", new RemoveById(console, collectionManager));
        com.put("show", new Show(console, collectionManager));
        com.put("print_field_ascending_status", new PrintFieldAscendingStatus(console, collectionManager));
        com.put("min_by_position", new MinByPosition(collectionManager));
        com.put("update_id", new UpdateID(console, collectionManager));
        com.put("remove_lower", new RemoveLower(console, collectionManager));
        com.put("remove_greater", new RemoveGreater(console, collectionManager));
        com.put("es", new ExecuteScript(console, collectionManager, commandManager, udpManager));
        collectionManager.loadCollection();
        commandManager.setCommands(com);
        ///BackUp.read((ExecuteScript) commandManager.getCommands().get("es"), console);
        if (udpManager != null) {
            udpManager.setSessions(fileManager.loadClients());
            logger.info("Отправляем всем известным клиентам, что сервер доступен");
            udpManager.sendAll(ProgramStatus.SERVER_CONNECTS);
            udpManager.setSessions(new HashSet<>());
        }
        Runtime.getRuntime().addShutdownHook(new Terminate(udpManager, new Save(collectionManager), fileManager));
        new Execute(commandManager, console, udpManager).execute();
    }
}

// Обязанности серверного приложения:
//
// Работа с файлом, хранящим коллекцию.
// Управление коллекцией объектов.
// Назначение автоматически генерируемых полей объектов в коллекции.
// Ожидание подключений и запросов от клиента.
// Обработка полученных запросов (команд).
// Сохранение коллекции в файл при завершении работы приложения.
// Сохранение коллекции в файл при исполнении специальной команды, доступной только серверу (клиент такую команду отправить не может).

// Серверное приложение должно состоять из следующих модулей (реализованных в виде одного или нескольких классов):
// Модуль приёма подключений.
// Модуль чтения запроса.
// Модуль обработки полученных команд.
// Модуль отправки ответов клиенту.

// Необходимо выполнить следующие требования:
//
// Операции обработки объектов коллекции должны быть реализованы с помощью Stream API с использованием лямбда-выражений.
// Объекты между клиентом и сервером должны передаваться в сериализованном виде.
// Объекты в коллекции, передаваемой клиенту, должны быть отсортированы по имени.
// Клиент должен корректно обрабатывать временную недоступность сервера.
// Обмен данными между клиентом и сервером должен осуществляться по протоколу UDP.
// Для обмена данными на сервере необходимо использовать датаграммы.
// Для обмена данными на клиенте необходимо использовать сетевой канал.
// Сетевые каналы должны использоваться в неблокирующем режиме.