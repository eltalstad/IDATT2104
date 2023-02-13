import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {
        final int port = 80;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

        writer.println("HTTP/1.0 200 OK");
        writer.println("Content-Type: text/html; charset=utf-8");
        writer.println("");
        writer.println("<html><body>");
        writer.println("<h1> Connected to web-server </h1>");
        writer.println("Header from client: ");
        writer.println("<ul>");
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            writer.println("<li>" + line + "</li>");
        }
        writer.println("</ul>");
        writer.println("</body></html>");

        writer.close();
        reader.close();
        socket.close();
        serverSocket.close();
    }
}
