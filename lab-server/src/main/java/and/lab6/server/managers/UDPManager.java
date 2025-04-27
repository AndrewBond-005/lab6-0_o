package and.lab6.server.managers;

import util.ProgramStatus;
import util.Response;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.HashSet;

public class UDPManager {
    private int port = 46525;
    private ReceivingManager receivingManager = new ReceivingManager();
    private SendingManager sendingManager = new SendingManager();
    private HashSet<InetSocketAddress> sessions = new HashSet<>();
    private DatagramSocket socket=null;


    /**
     * добавлять в sessions
     * если сервер сдох отправлять всем
     * если клиент сдох убирать из sessions
     */
    public int getPort() {
        return port;
    }

    public UDPManager(int port, SendingManager sendingManager, ReceivingManager receivingManager) throws IOException {
        this.port = port;
        this.sendingManager = sendingManager;
        this.receivingManager = receivingManager;
        try{
            this.socket=new DatagramSocket(port);
        }catch (SocketException e) {
            System.out.println("Ошибка при подключении порта");
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void send(Object object) {
        sendingManager.send(port, this.receivingManager.lastReceivedAddress, object,socket);
    }

    public void sendAll(Object object) {
        for (InetSocketAddress address : sessions)
            sendingManager.send(port, address, object,socket);
    }


    public Object receive() {
        Object obj = receivingManager.receive(port, socket);
        return obj;
    }

    public void addAddress(InetSocketAddress address) {
        sessions.add(address);
    }

    public void deleteAddress(InetSocketAddress address) {
        sessions.remove(address);
    }

    public void somethingWithClient(ProgramStatus programStatus, CommandManager commandManager) {
        if (programStatus == ProgramStatus.CLIENT_CONNECTS) {
            addAddress(receivingManager.lastReceivedAddress);
            System.out.println("подключился новый клиент с " + receivingManager.lastReceivedAddress);
            send(ProgramStatus.SERVER_CONNECTS);
        }
        if (programStatus == ProgramStatus.CLIENT_DISCONNECTS) {
            deleteAddress(receivingManager.lastReceivedAddress);
            System.out.println("клиент с адреса " + receivingManager.lastReceivedAddress + " отключился от сервера");
        }
    }
}
//package and.lab6.client.managers;
//
//import java.net.DatagramSocket;
//import java.net.SocketException;
//
//public class UDPManager {
//    ReceivingManager receivingManager;
//    SendingManager sendingManager = new SendingManager();
//    private int serverPort = 465259;
//    private int clientPort = 4652;
//    private DatagramSocket socket;
//
//    public UDPManager(ReceivingManager receivingManager, SendingManager sendingManager, int serverPort, int clientPort) {
//        this.receivingManager = receivingManager;
//        this.sendingManager = sendingManager;
//        this.serverPort = serverPort;
//        this.clientPort = clientPort;
//        try {
//            this.socket = new DatagramSocket();
//        } catch (SocketException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public void setServerPort(int serverPort) {
//        this.serverPort = serverPort;
//    }
//
//    public int getServerPort() {
//        return serverPort;
//    }
//
//    public void send(Object object) {
//        sendingManager.send(object, serverPort, clientPort, socket);
//
//    }
//
//    public Object receive() {
//        return receivingManager.receive(socket,clientPort);
//    }
//}