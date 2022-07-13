package ntou.notesharedevbackend.schedulerModule.entity;

import ntou.notesharedevbackend.userModule.entity.UserObj;

import java.util.ArrayList;

public class VoteReturn {
    private String id;
    private Task task;
    private String kickTarget;
    private UserObj kickTargetUserObj;//user's email
    private String result;

    private ArrayList<String> agree;
    private ArrayList<String> disagree;
    private ArrayList<UserObj> agreeUserObj;//user's email
    private ArrayList<UserObj> disagreeUserObj;//user's email

    public VoteReturn() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ArrayList<UserObj> getAgreeUserObj() {
        return agreeUserObj;
    }

    public void setAgreeUserObj(ArrayList<UserObj> agreeUserObj) {
        this.agreeUserObj = agreeUserObj;
    }

    public ArrayList<UserObj> getDisagreeUserObj() {
        return disagreeUserObj;
    }

    public void setDisagreeUserObj(ArrayList<UserObj> disagreeUserObj) {
        this.disagreeUserObj = disagreeUserObj;
    }

    public UserObj getKickTargetUserObj() {
        return kickTargetUserObj;
    }

    public void setKickTargetUserObj(UserObj kickTargetUserObj) {
        this.kickTargetUserObj = kickTargetUserObj;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getKickTarget() {
        return kickTarget;
    }

    public void setKickTarget(String kickTarget) {
        this.kickTarget = kickTarget;
    }

    public ArrayList<String> getAgree() {
        return agree;
    }

    public void setAgree(ArrayList<String> agree) {
        this.agree = agree;
    }

    public ArrayList<String> getDisagree() {
        return disagree;
    }

    public void setDisagree(ArrayList<String> disagree) {
        this.disagree = disagree;
    }
}
