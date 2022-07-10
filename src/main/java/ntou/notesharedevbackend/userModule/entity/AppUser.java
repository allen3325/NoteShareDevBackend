//        email* string unique
//        name*	string
//        password* string
//        verifyCode string
//        isAdmin* boolean default false
//        isActivated* boolean default false
//        profile string//自我介紹
//        strength [string]//擅長科目
//        folder* [FolderSchema]
//              default [
//                        { folderName: ‘Uncategorized’,
//                        folder:[ ],
//                        note:[ ]
//                        },
//                        { folderName:’Favorite’,//收藏
//                        folder:[ ],
//                        note:[ ]
//                        },
//                        { folderName: ‘Likes’,//愛心
//                        folder:[ ],
//                        note:[ ]
//                        },
//                        { folderName: ‘Purchased’,//已購買
//                        folder:[ ],
//                        note:[ ]
//                        },
//                      ]
//        subscribe [user's email] 追蹤的人
//        follow [user's email] 開啟小鈴鐺
//        fans string//發文通知粉絲 存email或是userName
//        coin * int default 初始點數
package ntou.notesharedevbackend.userModule.entity;
import ntou.notesharedevbackend.notificationModule.entity.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "user")
public class AppUser {

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
    private ArrayList<String> subscribe;
    private ArrayList<String> bell; // 開啟誰的小鈴鐺
    private ArrayList<String> belledBy; // 誰開啟我的小鈴鐺
    private ArrayList<String> fans;
    private Integer coin;
    private String headshotPhoto;
    private ArrayList<MessageReturn> notification;
    private Integer unreadMessageCount;

    // constructor
    public AppUser() {}

    // getter and setter

    public String getHeadshotPhoto() {
        return headshotPhoto;
    }

    public void setHeadshotPhoto(String headshotPhoto) {
        this.headshotPhoto = headshotPhoto;
    }

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

    public ArrayList<String> getBelledBy() {
        return belledBy;
    }

    public void setBelledBy(ArrayList<String> belledBy) {
        this.belledBy = belledBy;
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

    public ArrayList<MessageReturn> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<MessageReturn> notification) {
        this.notification = notification;
    }

    public Integer getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Integer unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }
}
