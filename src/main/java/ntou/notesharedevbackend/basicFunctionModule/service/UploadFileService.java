package ntou.notesharedevbackend.basicFunctionModule.service;

import ntou.notesharedevbackend.basicFunctionModule.config.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.noteNodule.service.*;
import ntou.notesharedevbackend.repository.*;
import org.apache.tomcat.util.http.fileupload.*;
import org.checkerframework.checker.units.qual.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

@Service
public class UploadFileService {
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;

    public String[] uploadImage(MultipartFile[] request, String noteID, String version) {
        //save files to temporary directory
        UploadConfig.saveFile(request);

        String uploadedFileName = Arrays.stream(request).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(","));
        List<String> responseURL = new ArrayList<>();
        String[] response = new String[0];
        try {
            for (String file : uploadedFileName.split(",")) {
                String filesURL = "https://www.googleapis.com/drive/v3/files/" + UploadConfig.uploadFile(file);
                responseURL.add(filesURL);

                saveFileURL(noteID, version, filesURL);
            }
            response = responseURL.toArray(new String[0]);

            //delete temporary files
            FileUtils.cleanDirectory(new File("files"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    //save the Google Drive files url to current version
    public void saveFileURL(String noteID, String version, String filesURL) {
        Note note = noteService.getNote(noteID);
        ArrayList<VersionContent> versionContents = note.getVersion();
        VersionContent currentVersion = versionContents.get(Integer.parseInt(version));
        ArrayList<String> newFilesUrl = currentVersion.getFileURL();

        newFilesUrl.add(filesURL);

        currentVersion.setFileURL(newFilesUrl);
        versionContents.set(Integer.parseInt(version), currentVersion);
        note.setVersion(versionContents);
        noteRepository.save(note);
    }
}
