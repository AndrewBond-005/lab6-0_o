package and.lab6.server.managers;

import models.Worker;
import util.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;
import java.util.stream.Collectors;

public class SendingManager {
    private int maxWorkerCount = 490;

    public void send(int serverPort, InetSocketAddress clientAddress, Object object,DatagramSocket socket) {
        try  {
            if (object instanceof Response) {
                if (((Response) object).workers() != null) {
                    System.out.println(((Response) object).message() + " " + ((Response) object).workers().size());
                    if (((Response) object).workers().size() > maxWorkerCount) {
                        sendMany(serverPort, clientAddress, (Response) object, socket, maxWorkerCount);
                    }
                } else
                    System.out.println(((Response) object).message());

            }
            // Сериализация
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            byte[] data = baos.toByteArray();
            // Отправка по UDP
            DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress);
            try  {
                socket.send(packet);
            } catch (SocketException e) {
                System.out.println("debil");
                //sendMany(serverPort, clientAddress, (Response) object, new DatagramSocket(1201), maxWorkerCount / 2);
            }
            System.out.println("отправляем на " + clientAddress + " " + "   c " + serverPort);
            oos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMany(int serverPort, InetSocketAddress clientAddress,
                          Response object, DatagramSocket socket, int maxWorkerCount) {
        int count = ((object).workers().size() - 1) / maxWorkerCount + 1;
        var message = object.message();
        var returnCode = object.returnCode();
        var workers = object.workers();
        send(serverPort, clientAddress,
                new Response(String.valueOf(count), null, (-1) * count),socket);
        for (int i = 1; i <= count; i++) {
            List<Worker> w = workers.stream()
                    .skip((long) (i - 1) * maxWorkerCount)
                    .limit(maxWorkerCount)
                    .collect(Collectors.toList());
            System.out.println(i+" "+w.size());
            send(serverPort, clientAddress
                    , new Response(message, w, 0),socket);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


        }


    }

}
//package and.lab6.client.managers;
//
//import util.Request;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.*;
//
//public class SendingManager {
//    public void send(Object object, int serverPort, int clientPort,DatagramSocket socket)  {
//        try{
//        InetAddress serverAddress = InetAddress.getByName("localhost");
//        // Сериализация
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(object);
//        oos.flush();
//        byte[] data = baos.toByteArray();
//
//        // Отправка по UDP
//        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
//        socket.send(packet);
//        System.out.println("отправляем на "+ serverAddress+ " "+serverPort+ "   c " +clientPort);
//
//        oos.close();
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}