import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Desktop;
import java.net.URI;

// Java localhost server browser ko camera page deta hai. Video server par nahi aata.
public class CameraMonitor {
    public static void main(String[] args) throws Exception {
        final byte[] page = Files.readAllBytes(Paths.get("camera.html"));
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        server.createContext("/", exchange -> {
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, page.length);
            exchange.getResponseBody().write(page);
            exchange.close();
        });
        server.start();
        System.out.println("Open http://localhost:8080 then click Start Camera. Ctrl+C stops server.");
        // Browser auto-open fail ho toh printed address manually open kar sakte hain.
        try {
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI("http://localhost:8080"));
        } catch (Exception e) {
            System.out.println("Please open the address manually in your browser.");
        }
    }
}
