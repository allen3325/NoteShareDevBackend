//筆記不同版本內容 VersionContentSchema
//    日期date* date  default
//    內容content string
//    圖片picURL [string]
//    檔案filesURL [string]
//    markdown [string]
package ntou.notesharedevbackend.entity;

import java.util.ArrayList;
import java.util.Date;

public class VersionContent {

    // attributes
    private Date date;
    private String content;
    private ArrayList<String> picURL;
    private ArrayList<String> fileURL;
    private ArrayList<String> markdown;

    // constructors
    public VersionContent() {
    }
    public VersionContent(Date date, String content, ArrayList<String> picURL, ArrayList<String> fileURL, ArrayList<String> markdown) {
        this.date = date;
        this.content = content;
        this.picURL = picURL;
        this.fileURL = fileURL;
        this.markdown = markdown;
    }

    // getter and setter
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
