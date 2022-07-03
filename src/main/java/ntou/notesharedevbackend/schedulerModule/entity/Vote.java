package ntou.notesharedevbackend.schedulerModule.entity;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Vote {
    private String id;
//    private String type;//kick, publishTime
    private Task task;
    private ArrayList<String> agree;//user's email
    private ArrayList<String> disagree;//user's email
    private String kickTarget;//user's email
    private String result;

    public Vote(){
        this.id = new ObjectId().toString();
        this.agree = new ArrayList<String>();
        this.disagree = new ArrayList<String>();
        this.result = "yet";
    }

    //getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
