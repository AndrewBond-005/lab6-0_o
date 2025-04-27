package and.lab6.server.managers;

import and.lab6.server.utility.Console;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import models.Worker;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import static models.Worker.fromArray;
import static models.Worker.toArray;


public class FileManager {
    private final String fileName;
    private final Console console;

    public FileManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    public void write(Collection<Worker> collection) {
        CSVWriter writer = null;
        File file = new File(fileName);
        if (!file.exists()) {
            console.printError("Файл не существует");
            console.print("Вы хотите создать новый файл с именем " + fileName + " ?" +
                    " yes - если да");
            var line = console.readln().trim().toLowerCase();
            if (line.equals("yes")) {
                try {
                    if (file.createNewFile())
                        console.println("Файл " + fileName + " успешно создан");
                } catch (IOException e) {
                    console.printError("ошибка при создании файла!");
                }
            }
        }
        if (!file.canRead()) {
            console.printError("Файл " + fileName + " не досутпен для записи");
            return;
        }
        try {
            var w = new BufferedOutputStream(new FileOutputStream(fileName));//40960
            StringWriter s = new StringWriter();
            writer = new CSVWriter(s);
            int i = 0;
            for (Worker worker : collection) {
                i++;
                writer.writeNext(toArray(worker));
                if (i >= 5000) {
                    //console.print(i);
                    i -= 5000;
                    w.write(s.toString().getBytes());
                    w.flush();
                    s.close();
                    writer.close();
                    s = new StringWriter();
                    writer = new CSVWriter(s);
                    w.write(s.toString().getBytes());
                }
            }
            w.write(s.toString().getBytes());
            w.flush();
            s.close();
            w.close();
        } catch (IOException e) {
            console.printError(("Ошибка сериализации:" + e.getCause().getMessage()));
            //return null;
        } catch (NullPointerException e) {
            console.printError("Файл не найден");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                console.printError("Ошибка закрытия файла");
            }
        }
    }

    public int read(Collection<Worker> collection) {
        CSVReader reader = null;
        int id = 0;
        File file = new File(fileName);
        if (!file.exists()) {
            console.printError("Файл не существует");
            console.print("Вы хотите создать новый файл с именем " + fileName + " ?" +
                    " yes - если да");
            var line = console.readln().trim().toLowerCase();
            if (line.equals("yes")) {
                try {
                    if (file.createNewFile())
                        console.println("Файл " + fileName + " успешно создан");
                } catch (IOException e) {
                    console.printError("ошибка при создании файла!");
                }
            }
        }
        if (!file.canRead()) {
            console.printError("Файл " + fileName + " не доступен для чтения");
            return -1;
        }
        if (file.length() == 0) {
            console.printError("Файл " + fileName + " пуст!");
            return 0;
        }
        try (Scanner scanner = new Scanner(new FileInputStream(fileName));) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append('\n');
            }
            StringReader stringReader = new StringReader(stringBuilder.toString());
            reader = new CSVReader(stringReader);
            var res = reader.readNext();
            collection.clear();
            HashMap<Integer, Integer> col = new HashMap<>();
            while (res != null) {
                String str;
                Worker worker;
                try {
                    worker = fromArray(res);
                    str = worker.validate();
                    if (str.isEmpty()) {
                        if (!col.containsKey(worker.getId())) {
                            collection.add(worker);
                            id = Math.max(id, worker.getId());
                            col.put(worker.getId(), worker.getId());
                        } else {
                            console.printError("Worker c id = " + worker.getId() + " уже содержится в коллекции");
                        }
                    } else {
                        console.printError("Элемент не корректен: " + worker + '\n' +
                                (worker.validate()) + (worker.getPerson().validate()));
                    }
                } catch (NullPointerException e) {
                    console.printError("Элемент не корректен: id или salary некорректны");
                }
                res = reader.readNext();
            }
            if (!collection.isEmpty()) {
                console.println("Коллекция успешна загружена!");
                return id;
            }
            stringReader.close();
            reader.close();
            scanner.close();
        } catch (IOException e) {
            console.printError("Ошибка сериализации:");
            //return null;
            return -1;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                console.printError("Ошибка закрытия файла");
            }
        }
        return 0;
    }
}
