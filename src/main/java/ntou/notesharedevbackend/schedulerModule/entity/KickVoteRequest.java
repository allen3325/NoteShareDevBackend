package ntou.notesharedevbackend.schedulerModule.entity;

public class KickVoteRequest {
    private String kickTargetEmail ;
    private int year;
    private int month;
    private int day;

    public KickVoteRequest(){}

    public String getKickTargetEmail() {
        return kickTargetEmail;
    }

    public void setKickTargetEmail(String kickTargetEmail) {
        this.kickTargetEmail = kickTargetEmail;
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
}
