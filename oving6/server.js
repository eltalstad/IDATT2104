const net = require('net');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', () => {
        let content = `<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
  </head>
  <body>
    <h1>WebSocket Test</h1>
    <div id="chat">
        <div id="messages"></div>
        <input id="message" type="text" />
        <button id="send" onclick="send()">Send</button>
    </div>
   
    <script>
    function send() {
        console.log('Sending message');
        let message = document.getElementById('message').value;
        console.log('Message: ' + message);
        ws.send(message);
    }
      let ws = new WebSocket('ws://localhost:3001');
      ws.onmessage = event => alert('Message from server: ' + event.data);
      ws.onopen = () => {
          console.log('WebSocket connection established');
      }
      ws.onerror = error => console.log('WebSocket error: ' + error);
      ws.onclose = event => {
            console.log('WebSocket connection closed');
            console.log('Code: ' + event.code);
            console.log('Reason: ' + event.reason);
      }
    </script>
  </body>
</html>
`;
        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});
httpServer.listen(3000, () => {
    console.log('HTTP server listening on port 3000');
});