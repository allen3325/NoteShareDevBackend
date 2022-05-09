package ntou.notesharedevbackend.basicFunctionModule.service;

import ntou.notesharedevbackend.basicFunctionModule.config.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.noteNodule.service.*;
import ntou.notesharedevbackend.repository.*;
import org.apache.tomcat.util.http.fileupload.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

@Service
public class UploadService {
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;

    public String[] uploadImage(MultipartFile[] request, String noteID, String version) {
        //save files to temporary directory
        Upload.saveFile(request);

        String uploadedFileName = Arrays.stream(request).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(","));
        List<String> responseURL = new ArrayList<>();
        String[] response = new String[0];
        try {
            for (String image : uploadedFileName.split(",")) {
                String imagesURL = Upload.uploadImage(image);
                responseURL.add(imagesURL);

                savePicURL(noteID, version, imagesURL);
            }
            response = responseURL.toArray(new String[0]);

            //delete temporary files
            FileUtils.cleanDirectory(new File("files"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String[] uploadFile(MultipartFile[] request, String noteID, String version) {
        //save files to temporary directory
        Upload.saveFile(request);

        String uploadedFileName = Arrays.stream(request).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(","));
        List<String> responseURL = new ArrayList<>();
        String[] response = new String[0];
        try {
            for (String file : uploadedFileName.split(",")) {
                String filesURL = Upload.uploadFile(file);
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
    public void savePicURL(String noteID, String version, String picURL) {
        Note note = noteService.getNote(noteID);
        ArrayList<VersionContent> versionContents = note.getVersion();
        VersionContent currentVersion = versionContents.get(Integer.parseInt(version));
        ArrayList<String> newPicsUrl = currentVersion.getPicURL();

        if (newPicsUrl == null)
            newPicsUrl = new ArrayList<>();
        newPicsUrl.add(picURL);

        currentVersion.setPicURL(newPicsUrl);
        versionContents.set(Integer.parseInt(version), currentVersion);
        note.setVersion(versionContents);
        noteRepository.save(note);
    }

    public void saveFileURL(String noteID, String version, String fileURL) {
        Note note = noteService.getNote(noteID);
        ArrayList<VersionContent> versionContents = note.getVersion();
        VersionContent currentVersion = versionContents.get(Integer.parseInt(version));
        ArrayList<String> newFilesUrl = currentVersion.getFileURL();

        if (newFilesUrl == null)
            newFilesUrl = new ArrayList<>();
        newFilesUrl.add(fileURL);

        currentVersion.setFileURL(newFilesUrl);
        versionContents.set(Integer.parseInt(version), currentVersion);
        note.setVersion(versionContents);
        noteRepository.save(note);
    }
}
