import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 80);
        System.out.println("Connected to server");

       try {
           DataOutputStream out = new DataOutputStream(socket.getOutputStream());
           DataInputStream in = new DataInputStream(socket.getInputStream());

           Scanner scanner = new Scanner(System.in);

           while (true) {
               System.out.println("Number 1: ");
                int num1 = scanner.nextInt();
                out.writeInt(num1);
                System.out.println("Number 2: ");
                int num2 = scanner.nextInt();
                out.writeInt(num2);
                System.out.println("Operator: + or -");
                String operator = scanner.next();
                out.writeUTF(operator);

                int result = in.readInt();
                System.out.println("Result: " + result);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

}
