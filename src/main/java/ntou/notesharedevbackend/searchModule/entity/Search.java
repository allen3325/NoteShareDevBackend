package ntou.notesharedevbackend.searchModule.entity;

import java.util.*;

public class Search {
    private String school;
    private String subject;
    private String department;
    private String professor;
    private String headerName;
    private Boolean haveNormal;
    private Boolean haveCollaboration;
    private Boolean haveReward;
    private Boolean downloadable;
    private ArrayList<String> tag;

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public Boolean getHaveNormal() {
        return haveNormal;
    }

    public void setHaveNormal(Boolean haveNormal) {
        this.haveNormal = haveNormal;
    }

    public Boolean getHaveCollaboration() {
        return haveCollaboration;
    }

    public void setHaveCollaboration(Boolean haveCollaboration) {
        this.haveCollaboration = haveCollaboration;
    }

    public Boolean getHaveReward() {
        return haveReward;
    }

    public void setHaveReward(Boolean haveReward) {
        this.haveReward = haveReward;
    }
}
