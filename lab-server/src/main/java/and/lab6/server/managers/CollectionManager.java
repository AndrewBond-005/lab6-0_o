package and.lab6.server.managers;

import models.Worker;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CollectionManager {
    private Set<Worker> tree = new TreeSet<>();
    private int currentId = 1;
    private final LocalDateTime lastInitTime = null;
    private LocalDateTime lastSaveTime = null;
    private final Map<Integer, Worker> workers = new HashMap<>();
    private FileManager fileManager;

    public CollectionManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Worker getById(int id) {
        return workers.get(id);
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public boolean isContain(Worker e) {
        if (e == null) return true;
        return workers.values().stream()
                .anyMatch(w -> w.getId() == e.getId());
    }

    public void update(Worker w) {
        if (w == null) return;
        remove(getById(w.getId()));
        add(w);
    }

    public int getCurrentId() {
        return currentId;
    }

    public int getFreeId() {
        while (workers.containsKey(currentId)) {
            currentId++;
            if (currentId < 0) currentId = 1;
        }
        return currentId;
    }

    public boolean add(Worker a) {
        workers.put(a.getId(), a);
        return tree.add(a);
    }

    public void remove(Worker a) {
        if (a == null) return;
        workers.remove(a.getId());
        tree.remove(a);
    }

    public void removeAll() {
        tree.clear();
        workers.clear();
    }

    public Set<Worker> getCollection() {
        return tree;
    }

    public boolean loadCollection() {
        int id = fileManager.read(tree);
        workers.clear();
        workers.putAll(tree.stream()
                .collect(Collectors.toMap(Worker::getId, w -> w)));
        currentId = ++id;
        return id > 0;
    }

    public void saveCollection() {
        fileManager.write(tree);
        lastSaveTime = LocalDateTime.now();
    }
}
