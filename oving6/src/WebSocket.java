import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class WebSocket {

  private final int port;
  final ArrayList<ClientHandler> clients = new ArrayList<>();

  public WebSocket(int port) {
    this.port = port;
  }

  public void start() throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Server started on port " + port);
    while (true) {
      ClientHandler client = new ClientHandler(serverSocket.accept(), this);
      clients.add(client);
      new Thread(client).start();
    }
  }

  public void broadcastMessage(String text, ClientHandler clientHandler) throws IOException {
    for (ClientHandler client : clients) {
      if (client != clientHandler) {
        client.sendMessage(text);
      }
    }
  }

  public static void main(String[] args) throws IOException {
    new WebSocket(3001).start();
  }
}
