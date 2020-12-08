package pubMed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pubMed.net.NettyServer;
import pubMed.user.Interact;


@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        NettyServer ns = new NettyServer();
        ns.start();
        Interact u = new Interact();
        u.interact();
    }
}
