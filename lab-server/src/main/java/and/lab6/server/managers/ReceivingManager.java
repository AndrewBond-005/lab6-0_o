package and.lab6.server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class ReceivingManager {
    InetSocketAddress lastReceivedAddress = null;
    private static final Logger logger = LogManager.getLogger(ReceivingManager.class);

    public Object receive(int port, DatagramSocket socket) {
        try {
            while (true) {
                // Получаем датаграмму
                byte[] buffer = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    logger.info("Открываем порт, где ждём данные от клиента");
                    socket.receive(packet); // Блокирует до получения данных
                    logger.info("получили запрос от клиента с " + packet.getAddress() + packet.getPort());
                } catch (Exception ignored) {
                }
                lastReceivedAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
                // Десериализация
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object object = ois.readObject();
                return object;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка при получении запроса от клиента");
        }

        return null;
    }

}
