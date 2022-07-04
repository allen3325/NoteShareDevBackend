package ntou.notesharedevbackend.schedulerModule.entity;

import ntou.notesharedevbackend.userModule.entity.UserObj;

import java.util.ArrayList;

public class VoteReturn {
    private String id;
    private Task task;
    private String kickTarget;//user's email
    private String result;

    private ArrayList<UserObj> agreeUserObj;//user's email
    private ArrayList<UserObj> disagreeUserObj;//user's email

    public VoteReturn(){}

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

    public String getKickTarget() {
        return kickTarget;
    }

    public void setKickTarget(String kickTarget) {
        this.kickTarget = kickTarget;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}