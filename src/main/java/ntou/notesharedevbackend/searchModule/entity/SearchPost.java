package ntou.notesharedevbackend.searchModule.entity;

public class SearchPost {
    private String subject;
    private String department;
    private String author;
    private Integer price;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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
