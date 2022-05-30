package ntou.notesharedevbackend.schedulerModule.entity;


import org.bson.types.ObjectId;

public class Task {
    private String id;
    private String type;//publish or vote
    private int year;
    private int month;
    private int day;
//    private int hour = 17;
//    private int minute = 0;
//    private int second = 0;
    private String noteIDOrVoteID ;
    private String postID;
    public Task(){
        this.id = new ObjectId().toString();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

//    public int getHour() {
//        return hour;
//    }
//
//    public void setHour(int hour) {
//        this.hour = hour;
//    }
//
//    public int getMinute() {
//        return minute;
//    }
//
//    public void setMinute(int minute) {
//        this.minute = minute;
//    }
//
//    public int getSecond() {
//        return second;
//    }
//
//    public void setSecond(int second) {
//        this.second = second;
//    }

    public String getNoteIDOrVoteID() {
        return noteIDOrVoteID;
    }

    public void setNoteIDOrVoteID(String noteIDOrVoteID) {
        this.noteIDOrVoteID = noteIDOrVoteID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
