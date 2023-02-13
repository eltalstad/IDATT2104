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
                if (!operator.equals("+") && !operator.equals("-")) {
                    System.out.println("Invalid operator");
                    break;
                }
                out.writeUTF(operator);

                int result = in.readInt();
                System.out.println("Result: " + result);

                System.out.println("Continue? y/n");
                String answer = scanner.next();
                if (answer.equals("n")) {
                    break;
                }
           }

           out.close();
           in.close();
           socket.close();

       } catch (Exception e) {
           e.printStackTrace();
       }
    }

}
