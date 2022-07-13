package ntou.notesharedevbackend.postModule.entity;

import ntou.notesharedevbackend.userModule.entity.UserObj;

public class ApplyReturn {
    private UserObj userObj;
    private String commentFromApplicant;

    public ApplyReturn() {
    }

    public UserObj getUserObj() {
        return userObj;
    }

    public void setUserObj(UserObj userObj) {
        this.userObj = userObj;
    }

    public String getCommentFromApplicant() {
        return commentFromApplicant;
    }

    public void setCommentFromApplicant(String commentFromApplicant) {
        this.commentFromApplicant = commentFromApplicant;
    }
}
