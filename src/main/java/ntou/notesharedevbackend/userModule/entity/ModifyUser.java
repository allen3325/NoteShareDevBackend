package ntou.notesharedevbackend.userModule.entity;

import ntou.notesharedevbackend.folderModule.entity.Folder;

import java.util.ArrayList;

public class ModifyUser {
    private boolean isAdmin;
    private boolean isActivate;
    private String profile; //自我介紹
    private ArrayList<String> strength; //擅長科目
    private ArrayList<Folder> folders;
    private ArrayList<String> subscribe;
    private ArrayList<String> bell;
    private ArrayList<String> fans;
    private Integer coin;

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

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    public ArrayList<String> getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(ArrayList<String> subscribe) {
        this.subscribe = subscribe;
    }

    public ArrayList<String> getBell() {
        return bell;
    }

    public void setBell(ArrayList<String> bell) {
        this.bell = bell;
    }

    public ArrayList<String> getFans() {
        return fans;
    }

    public void setFans(ArrayList<String> fans) {
        this.fans = fans;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }
}
