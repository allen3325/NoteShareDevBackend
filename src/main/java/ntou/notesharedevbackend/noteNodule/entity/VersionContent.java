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
    private String mycustom_html;
    private String mycustom_components;
    private String mycustom_assets;
    private String mycustom_css;
    private String mycustom_styles;
    private ArrayList<String> picURL;
    private ArrayList<String> fileURL;


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

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

    public String getMycustom_html() {
        return mycustom_html;
    }

    public void setMycustom_html(String mycustom_html) {
        this.mycustom_html = mycustom_html;
    }

    public String getMycustom_components() {
        return mycustom_components;
    }

    public void setMycustom_components(String mycustom_components) {
        this.mycustom_components = mycustom_components;
    }

    public String getMycustom_assets() {
        return mycustom_assets;
    }

    public void setMycustom_assets(String mycustom_assets) {
        this.mycustom_assets = mycustom_assets;
    }

    public String getMycustom_css() {
        return mycustom_css;
    }

    public void setMycustom_css(String mycustom_css) {
        this.mycustom_css = mycustom_css;
    }

    public String getMycustom_styles() {
        return mycustom_styles;
    }

    public void setMycustom_styles(String mycustom_styles) {
        this.mycustom_styles = mycustom_styles;
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
//
//    public ArrayList<String> getMarkdown() {
//        return markdown;
//    }
//
//    public void setMarkdown(ArrayList<String> markdown) {
//        this.markdown = markdown;
//    }
}
