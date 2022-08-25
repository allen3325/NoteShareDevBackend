package ntou.notesharedevbackend.downloadModule.service;

import com.itextpdf.html2pdf.*;
import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.noteModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

@Service
public class DownloadService {
    @Autowired
    private NoteService noteService;
    static final private String homePath = System.getProperty("user.home");
    static final private String downloadPath = homePath + "/NoteShareDownload";
    static final private String htmlPath = downloadPath + "/note.html";
    static final private String cssPath  = downloadPath + "/note.css";

    // generate PDF from HTML -> convert into byte[]
    public byte[] convertHtmlToPdf() {
        byte[] pdf = null;
        try {
            HtmlConverter.convertToPdf(new File(htmlPath), new File(homePath + "/NoteShare.pdf"));
            Path pdfPath = Paths.get(homePath + "/NoteShare.pdf");
            pdf = Files.readAllBytes(pdfPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pdf;
    }

    // delete PDF file after response
    public void deletePDF() {
        try {
            Files.delete(Paths.get(homePath + "/NoteShare.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // generate HTML & CSS files
    public void writeHTML(String noteID, int version, int content) {
        Note note = noteService.getNote(noteID);
        VersionContent versionContent = note.getVersion().get(version);
        Content getContent = versionContent.getContent().get(content);
        String htmlContent = getContent.getMycustom_html();
        String css = getContent.getMycustom_css();
        htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <link  href=\"note.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                "</head>\n" +
                "<body>\n" +
                htmlContent + "\n" +
                "</body>\n" +
                "</html>" ;

        try {
            new PrintWriter(htmlPath).close();
            new PrintWriter(cssPath).close();
            Files.write(Paths.get(htmlPath), htmlContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            Files.write(Paths.get(cssPath), css.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
