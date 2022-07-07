package ntou.notesharedevbackend.userModule.entity;

import ntou.notesharedevbackend.notificationModule.entity.*;

import java.util.*;

public class AppUserReturn {

    // attributes
    private String id;
    private String email;
    private String name;
    private String password;
    private String verifyCode;
    private boolean isAdmin;
    private boolean isActivate;
    private String profile; //自我介紹
    private ArrayList<String> strength; //擅長科目
    //    private ArrayList<Folder> folders;
    private ArrayList<String> folders; // folder's ID
    private Integer coin;
    private String headshotPhoto;
    private Integer unreadMessageCount;

    private ArrayList<UserObj> notificationUserObj;
    private ArrayList<UserObj> subscribeUserObj;
    private ArrayList<UserObj> bellUserObj;
    private ArrayList<UserObj> belledByUserObj;
    private ArrayList<UserObj> fansUserObj;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public ArrayList<String> getStrength() {
        return strength;
    }

    public void setStrength(ArrayList<String> strength) {
        this.strength = strength;
    }

    public ArrayList<String> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<String> folders) {
        this.folders = folders;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public String getHeadshotPhoto() {
        return headshotPhoto;
    }

    public void setHeadshotPhoto(String headshotPhoto) {
        this.headshotPhoto = headshotPhoto;
    }

    public ArrayList<UserObj> getNotificationUserObj() {
        return notificationUserObj;
    }

    public void setNotificationUserObj(ArrayList<UserObj> notificationUserObj) {
        this.notificationUserObj = notificationUserObj;
    }

    public Integer getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Integer unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public ArrayList<UserObj> getSubscribeUserObj() {
        return subscribeUserObj;
    }

    public void setSubscribeUserObj(ArrayList<UserObj> subscribeUserObj) {
        this.subscribeUserObj = subscribeUserObj;
    }

    public ArrayList<UserObj> getBellUserObj() {
        return bellUserObj;
    }

    public void setBellUserObj(ArrayList<UserObj> bellUserObj) {
        this.bellUserObj = bellUserObj;
    }

    public ArrayList<UserObj> getBelledByUserObj() {
        return belledByUserObj;
    }

    public void setBelledByUserObj(ArrayList<UserObj> belledByUserObj) {
        this.belledByUserObj = belledByUserObj;
    }

    public ArrayList<UserObj> getFansUserObj() {
        return fansUserObj;
    }

    public void setFansUserObj(ArrayList<UserObj> fansUserObj) {
        this.fansUserObj = fansUserObj;
    }
}
