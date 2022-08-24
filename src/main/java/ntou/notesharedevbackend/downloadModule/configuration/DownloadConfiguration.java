package ntou.notesharedevbackend.downloadModule.configuration;

import org.springframework.boot.*;
import org.springframework.context.annotation.*;

import java.io.*;
import java.nio.file.*;

@Configuration
public class DownloadConfiguration {
    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            String homePath = System.getProperty("user.home");
            String downloadPath = homePath + "/NoteShareDownload";
            String htmlPath = downloadPath + "/note.html";
            String cssPath  = downloadPath + "/note.css";
            if (!Files.exists(Paths.get(downloadPath))) {
                Files.createDirectories(Paths.get(downloadPath));
                Files.createFile(Paths.get(htmlPath));
                Files.createFile(Paths.get(cssPath));
            }
        };
    }
}
