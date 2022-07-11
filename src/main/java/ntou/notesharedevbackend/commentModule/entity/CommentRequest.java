package ntou.notesharedevbackend.commentModule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;

public class CommentRequest {
    private String email;
    private String content;
    //    private ArrayList<String> referenceNotesURL; // 參考筆記的URL
    private ArrayList<String> picURL; //截圖後引用筆記

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getPicURL() {
        return picURL;
    }

    public void setPicURL(ArrayList<String> picURL) {
        this.picURL = picURL;
    }
}
