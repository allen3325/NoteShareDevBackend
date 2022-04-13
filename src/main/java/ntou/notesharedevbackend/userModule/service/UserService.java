package ntou.notesharedevbackend.userModule.service;

import ntou.notesharedevbackend.userModule.entity.User;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUser(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public User createUser(User request) {
        User user = new User();
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
        return userRepository.insert(user);
    }

}
