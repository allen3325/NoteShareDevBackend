package ntou.notesharedevbackend.userModule.service;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
    //TODO 以後要做 getByName
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AppUserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public AppUser getUserById(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public AppUser createUser(AppUser request) {
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setName(request.getName());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setVerifyCode(request.getVerifyCode());
        appUser.setAdmin(request.isAdmin());
        appUser.setActivate(request.isActivate());
        appUser.setProfile(request.getProfile());
        appUser.setStrength(request.getStrength());
        appUser.setFolders(request.getFolders());
        appUser.setSubscribe(request.getSubscribe());
        appUser.setBell(request.getBell());
        appUser.setFans(request.getFans());
        appUser.setCoin(request.getCoin());
        return userRepository.insert(appUser);
    }
}
