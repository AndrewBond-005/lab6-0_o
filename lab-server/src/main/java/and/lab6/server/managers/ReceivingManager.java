package and.lab6.server.managers;

import util.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ReceivingManager {
    InetSocketAddress lastReceivedAddress = null;

    public Object receive(int port,DatagramSocket socket) {
        try {
            while (true) {
                // Получаем датаграмму
                byte[] buffer = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    System.out.println("Открываем порт, где ждём данные от клиента" + socket.getInetAddress());
                    socket.receive(packet); // Блокирует до получения данных
                }catch (Exception e){
                    System.out.println("fool");
                }
                lastReceivedAddress = new InetSocketAddress(packet.getAddress(),packet.getPort());
                System.out.println("получены данные с "+lastReceivedAddress+ " на " + port);
                // Десериализация
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object object = ois.readObject();
                return object;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка при получении ответа от сервера");
        }

        return null;
    }

}
//package and.lab6.client.managers;
//
//import org.w3c.dom.ls.LSOutput;
//import util.Request;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//
//public class ReceivingManager {
//    public Object receive(DatagramSocket socket, int clientPort) {
//        try {
//            byte[] buffer = new byte[65535];
//            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//            System.out.println("Открываем порт, где ждём данные от сервера" +clientPort+socket.getInetAddress());
//
//            socket.receive(packet); // Блокирует до получения данных
//            System.out.println("получены данные с "+packet.getAddress() + packet.getPort());
//            // Десериализация
//            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            Object object = ois.readObject();
//            return object;
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}