//筆記不同版本內容 VersionContentSchema
//    日期date* date  default
//    內容content string
//    圖片picURL [string]
//    檔案filesURL [string]
//    markdown [string]
package ntou.notesharedevbackend.noteNodule.entity;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class VersionContent {

    // attributes
    private String id;
    private Date date;
    private String content;
    private ArrayList<String> picURL;
    private ArrayList<String> fileURL;
    private ArrayList<String> markdown;

    // constructors
    public VersionContent() {
        this.id = new ObjectId().toString();
    }

    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public ArrayList<String> getFileURL() {
        return fileURL;
    }

    public void setFileURL(ArrayList<String> fileURL) {
        this.fileURL = fileURL;
    }

    public ArrayList<String> getMarkdown() {
        return markdown;
    }

    public void setMarkdown(ArrayList<String> markdown) {
        this.markdown = markdown;
    }
}
