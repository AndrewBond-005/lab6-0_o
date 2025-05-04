package and.lab6.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.ProgramStatus;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashSet;

public class UDPManager {
    private int port = 46525;
    private ReceivingManager receivingManager = new ReceivingManager();
    private SendingManager sendingManager = new SendingManager();
    private HashSet<InetSocketAddress> sessions = new HashSet<>();
    private DatagramSocket socket = null;
    private static final Logger logger = LogManager.getLogger(UDPManager.class);


    /**
     * добавлять в sessions
     * если сервер сдох отправлять всем
     * если клиент сдох убирать из sessions
     */
    public int getPort() {
        return port;
    }

    public HashSet<InetSocketAddress> getSessions() {
        return sessions;
    }

    public void setSessions(HashSet<InetSocketAddress> sessions) {
        this.sessions = sessions;
    }

    public UDPManager(int port, SendingManager sendingManager, ReceivingManager receivingManager) throws IOException {
        this.port = port;
        this.sendingManager = sendingManager;
        this.receivingManager = receivingManager;
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            logger.error("Ошибка при подключении порта");
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void send(Object object) {
        try {
            sendingManager.send(port, this.receivingManager.lastReceivedAddress, object, socket);
        } catch (Exception ignored) {
        }
    }

    public void sendAll(Object object) {
        if (sessions != null)
            for (InetSocketAddress address : sessions) {
                try {
                    sendingManager.send(port, address, object, socket);
                } catch (Exception ignored) {
                }
            }
    }


    public Object receive() {
        Object obj = receivingManager.receive(port, socket);
        return obj;
    }

    public void addAddress(InetSocketAddress address) {
        if (sessions == null) {
            sessions = new HashSet<>();
        }
        sessions.add(address);
    }

    public void deleteAddress(InetSocketAddress address) {
        if (sessions != null)
            sessions.remove(address);
    }

    public void somethingWithClient(ProgramStatus programStatus, CommandManager commandManager) {
        if (programStatus == ProgramStatus.CLIENT_CONNECTS) {
            addAddress(receivingManager.lastReceivedAddress);
            logger.info("подключился новый клиент с {}", receivingManager.lastReceivedAddress);
            send(ProgramStatus.SERVER_CONNECTS);
        }
        if (programStatus == ProgramStatus.CLIENT_DISCONNECTS) {
            deleteAddress(receivingManager.lastReceivedAddress);
            logger.info("клиент с адреса {} отключился от сервера", receivingManager.lastReceivedAddress);
        }
    }

}
