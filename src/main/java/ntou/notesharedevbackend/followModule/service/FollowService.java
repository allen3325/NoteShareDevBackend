package ntou.notesharedevbackend.followModule.service;

import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class FollowService {
    @Autowired
    private UserRepository userRepository;

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
        subscribeList.add(followEmail);
        fansList.add(userEmail);
        user.setSubscribe(subscribeList);
        followingUser.setFans(fansList);
        userRepository.save(user);
        userRepository.save(followingUser);
    }

    public void unfollow(String userEmail, String unfollowEmail) {
        AppUser user = userRepository.findByEmail(userEmail);
        AppUser followingUser = userRepository.findByEmail(unfollowEmail);
        ArrayList<String> subscribeList = user.getSubscribe();
        ArrayList<String> fansList = followingUser.getFans();
        subscribeList.remove(unfollowEmail);
        fansList.remove(userEmail);
        user.setSubscribe(subscribeList);
        followingUser.setFans(fansList);
        userRepository.save(user);
        userRepository.save(followingUser);
    }
}
