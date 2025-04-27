package and.lab6.client.managers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ReceivingManager {
    public Object receive(DatagramChannel channel, int clientPort) {
        try {
            // Получаем датаграмму
            ByteBuffer buffer = ByteBuffer.allocate(65536);
            buffer.clear();
           // System.out.println("Открываем порт, где ждём данные от сервера" + clientPort);
            channel.receive(buffer);
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                return ois.readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Ошибка при получении данных: " + e.getMessage());
        }
        return null;
    }
}


//package and.lab6.server.managers;
//
//import util.Request;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.DatagramChannel;
//
//public class ReceivingManager {
//    InetSocketAddress lastReceivedAddress = null;
//
//    public Object receive(int port,DatagramChannel channel) {
//        try {
//            while (true) {
//                // Получаем датаграмму
//                ByteBuffer buffer = ByteBuffer.allocate(65536);
//                buffer.clear();
//                InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
//                lastReceivedAddress = clientAddress;
//                System.out.println("получены данные с "+lastReceivedAddress+ " на " + port);
//                buffer.flip();
//                byte[] data = new byte[buffer.limit()];
//                buffer.get(data);
//                // Десериализация объекта
//                ByteArrayInputStream bais = new ByteArrayInputStream(data);
//                ObjectInputStream ois = new ObjectInputStream(bais);
//                Object object =  ois.readObject();
//                return object;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        return null;
//    }
//
//}