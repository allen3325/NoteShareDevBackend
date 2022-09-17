package ntou.notesharedevbackend.downloadModule.service;

import com.itextpdf.html2pdf.*;
import com.lowagie.text.pdf.*;
import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.noteModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.xhtmlrenderer.pdf.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

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
//        try {
//            HtmlConverter.convertToPdf(new File(htmlPath), new File(homePath + "/NoteShare.pdf"));
//            Path pdfPath = Paths.get(homePath + "/NoteShare.pdf");
//            pdf = Files.readAllBytes(pdfPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            String url = new File(htmlPath).toURI().toURL().toString();
            OutputStream os = new FileOutputStream(homePath + "/NoteShare.pdf");
            ITextRenderer renderer = new ITextRenderer();

            renderer.getFontResolver().addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            renderer.setDocument(url);
            renderer.layout();
            renderer.createPDF(os);
            os.close();
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
        css = removeFontFamily(css);

        htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <link  href=\"note.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                "</head>\n" +
                "<body style=\"font-family:SimSun\" >\n" +
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

    public String removeFontFamily (String cssString) {
        int indexOfFontFamily = cssString.indexOf("font-family", 0);

        // found "font-family"
        while (indexOfFontFamily >= 0) {
            // get index of "font-family" & ";"
            int i = indexOfFontFamily;
            int indexOfSemicolon = cssString.indexOf(";", i) + 1;
            // remove substring
            String toBeRemoved = cssString.substring(indexOfFontFamily, indexOfSemicolon);
            cssString = cssString.replace(toBeRemoved, "");

            indexOfFontFamily = cssString.indexOf("font-family", 0);
        }

        return cssString;
    }
}
