import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        final int port = 80;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);


        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
        }

    }
}
