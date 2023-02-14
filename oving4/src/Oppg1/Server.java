package Oppg1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(1234);
        byte[] buffer = new byte[256];

        String number1 = "";
        String number2 = "";
        String operator = "";
        String result = "";

        while(true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Message received: " + message);

            number1 = message.substring(0, message.indexOf(" "));
            number2 = message.substring(message.indexOf(" ", message.indexOf(" ") + 1) + 1);
            operator = message.substring(message.indexOf(" ") + 1, message.indexOf(" ", message.indexOf(" ") + 1));

            switch(operator) {
                case "+":
                    result = String.valueOf(Integer.parseInt(number1) + Integer.parseInt(number2));
                    break;
                case "-":
                    result = String.valueOf(Integer.parseInt(number1) - Integer.parseInt(number2));
                    break;
                case "*":
                    result = String.valueOf(Integer.parseInt(number1) * Integer.parseInt(number2));
                    break;
                case "/":
                    result = String.valueOf(Integer.parseInt(number1) / Integer.parseInt(number2));
                    break;
                default:
                    result = "Invalid operator";
                    break;
            }

            buffer = result.getBytes();
            packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);
            System.out.println("Result sent: " + result);
        }

    }
}
