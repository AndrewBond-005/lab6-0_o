package and.lab6.client.managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class UDPManager {
    ReceivingManager receivingManager;
    SendingManager sendingManager = new SendingManager();
    private int serverPort = 46525;
    private int clientPort = 4652;
    private DatagramChannel channel;
    private Selector selector;
    private int maxWorkerCount =100;

    public int getMaxWorkerCount() {
        return maxWorkerCount;
    }

    public UDPManager(ReceivingManager receivingManager, SendingManager sendingManager, int serverPort, int clientPort) {
        this.receivingManager = receivingManager;
        this.sendingManager = sendingManager;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        try {
            channel = DatagramChannel.open();
            // Привязываем DatagramChannel к указанному порту
            channel.bind(new InetSocketAddress(clientPort));
            channel.configureBlocking(false);

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("Не удалось создать сетевой канал");
        }


    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void send(Object object) {
        sendingManager.send(object, serverPort, clientPort, channel);

    }

    public Object receive(long timeoutMillis) {
        try {
            // Проверяем, есть ли данные без блокировки
            if(!selector.isOpen()){
                System.exit(0);
            }
            if (selector.select(timeoutMillis) > 0) {
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isReadable()) {
                        return receivingManager.receive(channel, clientPort);
                    }
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("Error receiving data"+ e);
        }
        return null;
    }
    public void close() {
        try {
            if (selector != null) {
                selector.close();
            }
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
//package and.lab6.server.managers;
//
//import util.ProgramStatus;
//import util.Response;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.channels.DatagramChannel;
//import java.util.HashSet;
//
//public class UDPManager {
//    private int port = 46525;
//    private ReceivingManager receivingManager = new ReceivingManager();
//    private SendingManager sendingManager = new SendingManager();
//    private HashSet<InetSocketAddress> sessions = new HashSet<>();
//    private DatagramChannel channel;
//
//
//    /**
//     * добавлять в sessions
//     * если сервер сдох отправлять всем
//     * если клиент сдох убирать из sessions
//     */
//    public int getPort() {
//        return port;
//    }
//
//    public UDPManager(int port, SendingManager sendingManager, ReceivingManager receivingManager) throws IOException {
//        this.port = port;
//        this.sendingManager = sendingManager;
//        this.receivingManager = receivingManager;
//        channel = DatagramChannel.open();
//        // Привязываем DatagramChannel к указанному порту
//        channel.bind(new InetSocketAddress(port));
//        // channel.configureBlocking(false);
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    public void send(Object object) {
//        sendingManager.send(port, this.receivingManager.lastReceivedAddress, object, channel);
//    }
//
//    public void sendAll(Object object) {
//        for (InetSocketAddress address : sessions)
//            sendingManager.send(port, address, object, channel);
//    }
//
//
//    public Object receive() {
//        Object obj = receivingManager.receive(port, channel);
//        return obj;
//    }
//
//    public void addAddress(InetSocketAddress address) {
//        sessions.add(address);
//    }
//
//    public void deleteAddress(InetSocketAddress address) {
//        sessions.remove(address);
//    }
//
//    public void somethingWithClient(ProgramStatus programStatus, CommandManager commandManager) {
//        if (programStatus == ProgramStatus.CLIENT_CONNECTS) {
//            addAddress(receivingManager.lastReceivedAddress);
//            System.out.println("подключился новый клиент с " + receivingManager.lastReceivedAddress);
//            send(new Response(null, null, null, 1));
//        }
//        if (programStatus == ProgramStatus.CLIENT_DISCONNECTS) {
//            deleteAddress(receivingManager.lastReceivedAddress);
//            System.out.println("клиент с адреса " + receivingManager.lastReceivedAddress + " отключился от сервера");
//        }
//    }
//}