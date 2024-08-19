
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public Runnable getRunnable() {
        return () -> {
            int port = 8010;
            try {
                InetAddress address = InetAddress.getByName("localhost");
                Socket socket = new Socket(address, port);
                socket.setSoTimeout(5000); // Set timeout to 5 seconds

                try (
                    PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    toSocket.println("Hello from Client " + socket.getLocalSocketAddress());
                    String line = fromSocket.readLine();
                    System.out.println("Response from Server: " + line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Socket will close automatically after the try-with-resources block
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public static void main(String[] args) {
        Client client = new Client();
        ExecutorService executor = Executors.newFixedThreadPool(10); // Create a thread pool of 10 threads

        for (int i = 0; i < 100; i++) {
            executor.execute(client.getRunnable());
        }

        executor.shutdown(); // Initiates an orderly shutdown
    }
}
