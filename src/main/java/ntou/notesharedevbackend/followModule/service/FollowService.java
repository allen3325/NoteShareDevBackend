package ntou.notesharedevbackend.followModule.service;

import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class FollowService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppUserService appUserService;

    public String[] getFollowers(String email) {
        AppUser appUser = userRepository.findByEmail(email);
        ArrayList<String> fans = appUser.getFans();
        return fans.toArray(new String[0]);
    }

    public String[] getFollowing(String email) {
        AppUser appUser = userRepository.findByEmail(email);
        ArrayList<String> subscribes = appUser.getSubscribe();
        return subscribes.toArray(new String[0]);
    }

    public void follow(String userEmail, String followEmail) {
        AppUser user = userRepository.findByEmail(userEmail);
        AppUser followingUser = userRepository.findByEmail(followEmail);
        ArrayList<String> subscribeList = user.getSubscribe();
        ArrayList<String> fansList = followingUser.getFans();
        if (!subscribeList.contains(followEmail)) {
            subscribeList.add(followEmail);
            fansList.add(userEmail);
            user.setSubscribe(subscribeList);
            followingUser.setFans(fansList);
            userRepository.save(user);
            userRepository.save(followingUser);
        }
    }

    public void unfollow(String userEmail, String unfollowEmail) {
        AppUser user = userRepository.findByEmail(userEmail);
        AppUser followingUser = userRepository.findByEmail(unfollowEmail);
        ArrayList<String> subscribeList = user.getSubscribe();
        ArrayList<String> fansList = followingUser.getFans();
        if (subscribeList.contains(unfollowEmail)) {
            subscribeList.remove(unfollowEmail);
            fansList.remove(userEmail);
            user.setSubscribe(subscribeList);
            followingUser.setFans(fansList);
            userRepository.save(user);
            userRepository.save(followingUser);
        }
    }

    public UserObj getUserInfo(String email) {
        return appUserService.getUserInfo(email);
    }

    public void bell(String userEmail, String bellEmail) {
        AppUser appUser = appUserService.getUserByEmail(userEmail);
        if(!appUser.getBell().contains(bellEmail)) {
            appUser.getBell().add(bellEmail);
            appUserService.replaceUser(appUser);
            AppUser beBellUser = appUserService.getUserByEmail(bellEmail);
            beBellUser.getBelledBy().add(appUser.getEmail());
            appUserService.replaceUser(beBellUser);
        }
    }

    public void cancelBell(String userEmail, String cancelBellEmail) {
        AppUser appUser = appUserService.getUserByEmail(userEmail);
        appUser.getBell().remove(cancelBellEmail);
        appUserService.replaceUser(appUser);
        AppUser cancelBellUser = appUserService.getUserByEmail(cancelBellEmail);
        cancelBellUser.getBelledBy().remove(appUser.getEmail());
        appUserService.replaceUser(cancelBellUser);
    }

    public ArrayList<String> getBell(String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        if (appUser.getBell() != null) {
            return appUser.getBell();
        } else {
            return new ArrayList<String>();
        }
    }

    public ArrayList<String> getBellBy(String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        if (appUser.getBelledBy() != null) {
            return appUser.getBelledBy();
        } else {
            return new ArrayList<String>();
        }
    }
}
