package ntou.notesharedevbackend.downloadModule.controller;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.downloadModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
public class DownloadControlller {
    @Autowired
    private DownloadService downloadService;

    @Operation(summary = "get pdf file")
    @GetMapping("/{noteID}/{version}/{content}")
    public ResponseEntity<?> getPDF(@PathVariable("noteID") String noteID,
                                    @PathVariable("version") int version,
                                    @PathVariable("content") int content) {
        downloadService.writeHTML(noteID, version, content);
//        downloadService.copyHTMLAndCSS();
        byte[] bytes = downloadService.convertHtmlToPdf();
        downloadService.deletePDF();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=NoteShare.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }

}
