package ntou.notesharedevbackend.searchModule.entity;

public class SearchPost {
    private String subject;
    private String department;
    private String professor;
    private String author;
    private Integer bestPrice;
    private Boolean haveQA;
    private Boolean haveCollaboration;
    private Boolean haveReward;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(Integer bestPrice) {
        this.bestPrice = bestPrice;
    }

    public Boolean getHaveQA() {
        return haveQA;
    }

    public void setHaveQA(Boolean haveQA) {
        this.haveQA = haveQA;
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
