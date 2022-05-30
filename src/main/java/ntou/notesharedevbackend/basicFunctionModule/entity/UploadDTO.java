package ntou.notesharedevbackend.basicFunctionModule.entity;

import org.springframework.web.multipart.*;

public class UploadDTO {
    private String noteID;
    private String version;
    private MultipartFile[] files;

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
