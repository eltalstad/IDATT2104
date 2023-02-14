package Oppg1;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        byte[] buffer = new byte[1024];
        DatagramPacket packet = null;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Calculator Client");
            System.out.println("Enter the first number: ");
            String number1 = scanner.nextLine();
            System.out.println("Enter the second number: ");
            String number2 = scanner.nextLine();
            System.out.println("Enter the operator: ");
            String operator = scanner.nextLine();

            String message = number1 + " " + operator + " " + number2;
            buffer = message.getBytes();
            packet = new DatagramPacket(buffer, buffer.length, address, 1234);
            socket.send(packet);
            System.out.println("Message sent: " + message);

            String result = "";
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            result = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Result: " + result);
        }
    }

}
