package ntou.notesharedevbackend.userModule.service;

import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import ntou.notesharedevbackend.notificationModule.entity.Message;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.verificationModule.entity.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AppUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy(value = true)
    private MailService mailService = new MailService();
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    @Lazy(value = true)
    private FolderService folderService;

    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean hasExitUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public AppUser getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public AppUser[] getAllUsers() {
        List<AppUser> tmp = userRepository.findAll();
        return tmp.toArray(new AppUser[0]);
    }

    public AppUser createUser(AppUser request) {
        // check email has existed
        if (userRepository.existsByEmail(request.getEmail())) {
            return null;
        }
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setName(request.getName());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setVerifyCode(randomCode());
        appUser.setAdmin(false);
        appUser.setActivate(false);
        appUser.setProfile("");
        appUser.setStrength(new ArrayList<String>());
        appUser.setFolders(new ArrayList<String>());
        appUser.setSubscribe(new ArrayList<String>());
        appUser.setBell(new ArrayList<String>());
        appUser.setFans(new ArrayList<String>());
        appUser.setBelledBy(new ArrayList<String>());
        appUser.setCoin(300);
        appUser.setHeadshotPhoto(request.getHeadshotPhoto());
        appUser.setNotification(new ArrayList<Message>());
        appUser.setUnreadMessageCount(0);
        userRepository.insert(appUser);
        // create Buy and Favorite and default folder
        createFolderAtRoot(appUser.getEmail(), "Buy");
        createFolderAtRoot(appUser.getEmail(), "Favorite");
        createFolderAtRoot(appUser.getEmail(), "Folder");
        createFolderAtRoot(appUser.getEmail(), "Temp Reward Note");//放未提交的reward note
        appUser = getUserById(appUser.getId());
        return appUser;
    }

    public void createFolderAtRoot(String email, String name) {
        FolderRequest folder = new FolderRequest();
        folder.setParent(null);
        folder.setFolderName(name);
        folder.setPublic(false);
        folder.setPath("/" + name);
        folderService.createFolder(email, folder);
    }

    public AppUser replaceUser(AppUser request) {
        AppUser user = new AppUser();

        user.setId(request.getId());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setVerifyCode(request.getVerifyCode());
        user.setAdmin(request.isAdmin());
        user.setActivate(request.isActivate());
        user.setProfile(request.getProfile());
        user.setStrength(request.getStrength());
        user.setFolders(request.getFolders());
        user.setSubscribe(request.getSubscribe());
        user.setBell(request.getBell());
        user.setFans(request.getFans());
        user.setCoin(request.getCoin());
        user.setHeadshotPhoto(request.getHeadshotPhoto());
        user.setNotification(request.getNotification());
        user.setUnreadMessageCount(request.getUnreadMessageCount());
        user.setBelledBy(request.getBelledBy());
        return userRepository.save(user);
    }

    public void modifyStrength(String email, ArrayList<String> strength) {
        AppUser user = getUserByEmail(email);
        user.setStrength(strength);
        replaceUser(user);
    }

    public void modifyProfile(String email, String profile) {
        AppUser user = getUserByEmail(email);
        user.setProfile(profile);
        replaceUser(user);
    }

    public void replacePassword(String email, String genRandomPassword) {
        AppUser user = getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(genRandomPassword));
        replaceUser(user);
    }

    public static String randomCode() {
        char[] chars = new char[4];
        Random rnd = new Random();
        for (int i = 0; i < 4; i++) {
            chars[i] = (char) ('0' + rnd.nextInt(10));
        }
        return new String(chars);
    }

    public String getUserHeadshotPhoto(String email) {
        return getUserByEmail(email).getHeadshotPhoto();
    }

    public AppUser updateUserHeadshotPhoto(String email, String headshotPhoto) {
        AppUser appUser = getUserByEmail(email);
        appUser.setHeadshotPhoto(headshotPhoto);

        return replaceUser(appUser);
    }

    public String getUserProfile(String email) {
        return getUserByEmail(email).getProfile();
    }

    public ArrayList<String> getUserStrength(String email) {
        return getUserByEmail(email).getStrength();
    }

    public String getUserName(String email) {
        return getUserByEmail(email).getName();
    }

    public String updateUserName(String email, String newName) {
        AppUser appUser = getUserByEmail(email);
        appUser.setName(newName);
        replaceUser(appUser);
        return appUser.getName();
    }

    public UserObj getUserInfo(String email) {
        AppUser appUser = getUserByEmail(email);
        UserObj userObj = new UserObj();
        userObj.setUserObjEmail(email);
        userObj.setUserObjName(appUser.getName());
        userObj.setUserObjAvatar(appUser.getHeadshotPhoto());

        return userObj;
    }

    public AppUserReturn getAppUserInfo(AppUser appUser) {
        AppUserReturn appUserReturn = new AppUserReturn();
        appUserReturn.setId(appUser.getId());
        appUserReturn.setEmail(appUser.getEmail());
        appUserReturn.setName(appUser.getName());
        appUserReturn.setPassword(appUser.getPassword());
        appUserReturn.setVerifyCode(appUser.getVerifyCode());
        appUserReturn.setAdmin(appUser.isAdmin());
        appUserReturn.setActivate(appUser.isActivate());
        appUserReturn.setProfile(appUser.getProfile());
        appUserReturn.setStrength(appUser.getStrength());
        appUserReturn.setFolders(appUser.getFolders());
        appUserReturn.setCoin(appUser.getCoin());
        appUserReturn.setHeadshotPhoto(appUser.getHeadshotPhoto());
        appUserReturn.setUnreadMessageCount(appUser.getUnreadMessageCount());

        ArrayList<UserObj> notificationUserObj = new ArrayList<>();
        for (Message message : appUser.getNotification()) {
            UserObj userObj = getUserInfo(message.getUser());
            notificationUserObj.add(userObj);
        }
        appUserReturn.setNotificationUserObj(notificationUserObj);
        ArrayList<UserObj> subscribe = new ArrayList<>();
        for (String subscribeEmail : appUser.getSubscribe()) {
            UserObj userObj = getUserInfo(subscribeEmail);
            subscribe.add(userObj);
        }
        appUserReturn.setSubscribeUserObj(subscribe);
        ArrayList<UserObj> bell = new ArrayList<>();
        for (String bellEmail : appUser.getBell()) {
            UserObj userObj = getUserInfo(bellEmail);
            bell.add(userObj);
        }
        appUserReturn.setBellUserObj(bell);
        ArrayList<UserObj> belledBy = new ArrayList<>();
        for (String belledByEmail : appUser.getBelledBy()) {
            UserObj userObj = getUserInfo(belledByEmail);
            belledBy.add(userObj);
        }
        appUserReturn.setBelledByUserObj(belledBy);
        ArrayList<UserObj> fans = new ArrayList<>();
        for (String fansEmail : appUser.getFans()) {
            UserObj userObj = getUserInfo(fansEmail);
            fans.add(userObj);
        }
        appUserReturn.setFansUserObj(fans);

        return appUserReturn;
    }
}
