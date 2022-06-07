//筆記不同版本內容 VersionContentSchema
//    日期date* date  default
//    內容content string
//    圖片picURL [string]
//    檔案filesURL [string]
//    markdown [string]
package ntou.notesharedevbackend.noteNodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class VersionContent {

    // attributes
    private String id;
    private String name;
    private String slug;
    @JsonFormat(timezone = "GMT+08:00")
    private Date date;
    private ArrayList<Content> content;
    private ArrayList<String> picURL;
    private ArrayList<String> fileURL;
    private Boolean isTemp; // 看是暫存區還是版本號


    // constructors
    public VersionContent() {
        this.id = new ObjectId().toString();
        this.date = new Date();
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

    public void setDate() {
        this.date = new Date();
    }

    public ArrayList<Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Content> content) {
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

    public Boolean getTemp() {
        return isTemp;
    }

    public void setTemp(Boolean temp) {
        isTemp = temp;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
