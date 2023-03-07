import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.*;

public class ClientHandler implements Runnable {

  private final Socket socket;
  private final WebSocket webSocket;

  public ClientHandler(Socket socket, WebSocket webSocket) {
    this.socket = socket;
    this.webSocket = webSocket;
  }

  @Override
  public void run() {
    try {
      System.out.println("New client connected: " + socket.getInetAddress() + ":" + socket.getPort());
      InputStream inputStream = socket.getInputStream();
      OutputStream outputStream = socket.getOutputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

      // Parse and verify HTTP request
        String request = bufferedReader.readLine();
        System.out.println("Request: " + request);
        if (!request.startsWith("GET")) {
            System.out.println("Invalid request");
            return;
        }

      // Parse HTTP headers
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = bufferedReader.readLine()).equals("")) {
            String[] parts = line.split(": ");
            headers.put(parts[0], parts[1]);
        }

        // Verify HTTP headers
        if (!headers.containsKey("Sec-WebSocket-Key")) {
            System.out.println("Invalid headers");
            return;
        }

        // Send HTTP response
        String key = headers.get("Sec-WebSocket-Key");
        String response =
                "HTTP/1.1 101 Switching Protocols\r\n" +
                        "Upgrade: websocket\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Sec-WebSocket-Accept: " +
                        Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1")
                        .digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes())) +
                        "\r\n\r\n";
        System.out.println("Responding to client:: " + socket.getInetAddress() + ":" + socket.getPort());
        System.out.println(response);
        outputStream.write(response.getBytes());

        // Handle WebSocket connection
      if(isWebSocketRequest(request, headers)) {
        handleWebSocketConnection(inputStream, outputStream);
      }

      // Close connection
      webSocket.clients.remove(this);
      socket.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleWebSocketConnection(InputStream inputStream, OutputStream outputStream) throws IOException {
    while (true) {
      int b = inputStream.read();
      if (b == -1) {
        break;
      }
      int opcode = b & 0b00001111;
      if (opcode == 8) {
        break;
      }
      if (opcode == 1) {
        int length = inputStream.read() & 0b01111111;
        byte[] mask = new byte[4];
        inputStream.read(mask);
        byte[] message = new byte[length];
        for (int i = 0; i < length; i++) {
          message[i] = (byte) (inputStream.read() ^ mask[i % 4]);
        }
        String text = new String(message);
        System.out.println("Received message: " + text);
        webSocket.broadcastMessage(text, this);
      }
    }
  }

public void sendMessage(String message) throws IOException {
    byte[] bytes = message.getBytes();
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write(0b10000001);
    outputStream.write(bytes.length);
    outputStream.write(bytes);
  }

  private boolean isWebSocketRequest(String request, Map<String, String> headers) {
    return (
            request.startsWith("GET") &&
                    headers.containsKey("Upgrade") &&
                    headers.get("Upgrade").equals("websocket")
    );
  }
}
