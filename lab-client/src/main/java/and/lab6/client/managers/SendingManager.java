package and.lab6.client.managers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class SendingManager {
    public void send(Object object, int serverPort, int clientPort, DatagramChannel channel) {
        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            // Сериализация
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            byte[] data = baos.toByteArray();

            // Отправка по UDP
            ByteBuffer buffer = ByteBuffer.wrap(data);
            System.out.println("отправляем на  " + serverAddress + " с " + serverPort);
            channel.send(buffer,new InetSocketAddress(serverAddress,serverPort));
            oos.close();
        } catch (IOException e) {
            System.out.println("произошла ошибка при отправки запроса на сервер");
        }
    }
}
//package and.lab6.server.managers;
//
//import util.Request;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.DatagramChannel;
//
//public class SendingManager {
//
//    public void send(int serverPort, InetSocketAddress clientAddress, Object object, DatagramChannel channel) {
//        try  {
//
//            // Сериализация
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(object);
//            oos.flush();
//            byte[] data = baos.toByteArray();
//
//            // Отправка по UDP
//            ByteBuffer buffer = ByteBuffer.wrap(data);
//            System.out.println("отправляем на  "+ clientAddress + " с "+serverPort);
//            channel.send(buffer, clientAddress);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//}