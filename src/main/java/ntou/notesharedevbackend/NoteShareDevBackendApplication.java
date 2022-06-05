package ntou.notesharedevbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.*;

@SpringBootApplication
public class NoteShareDevBackendApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(NoteShareDevBackendApplication.class);
        builder.headless(false);
        builder.run(args);
    }

}
