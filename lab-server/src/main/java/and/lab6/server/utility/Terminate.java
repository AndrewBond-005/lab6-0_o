package and.lab6.server.utility;

import and.lab6.server.commands.Save;
import and.lab6.server.managers.FileManager;
import and.lab6.server.managers.UDPManager;
import util.ProgramStatus;
import util.Request;

public class Terminate extends Thread {
    private UDPManager udpManager;
    private Save save;
    private FileManager fileManager;

    public Terminate(UDPManager udpManager, Save save, FileManager fileManager) {
        this.udpManager = udpManager;
        this.save = save;
        this.fileManager = fileManager;
    }

    public void run() {
        System.out.println("Завершение программы и сохранение коллекции в файл");
        save.execute(new Request(null, null, null));
        fileManager.saveClients(udpManager.getSessions());
        udpManager.sendAll(ProgramStatus.SERVER_DISCONNECTS);

    }
}
