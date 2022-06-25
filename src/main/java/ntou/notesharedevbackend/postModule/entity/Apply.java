package ntou.notesharedevbackend.postModule.entity;

import java.util.ArrayList;

public class Apply {
    private String wantEnterUsersEmail;
    private String commentFromApplicant;

    public Apply() {
        this.wantEnterUsersEmail = "";
        this.commentFromApplicant = "";
    }

    public Apply(String wantEnterUsersEmail, String commentFromApplicant) {
        this.wantEnterUsersEmail = wantEnterUsersEmail;
        this.commentFromApplicant = commentFromApplicant;
    }

    public String getWantEnterUsersEmail() {
        return wantEnterUsersEmail;
    }

    public void setWantEnterUsersEmail(String wantEnterUsersEmail) {
        this.wantEnterUsersEmail = wantEnterUsersEmail;
    }

    public String getCommentFromApplicant() {
        return commentFromApplicant;
    }

    public void setCommentFromApplicant(String commentFromApplicant) {
        this.commentFromApplicant = commentFromApplicant;
    }
}
