package ntou.notesharedevbackend.userModule.service;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.verificationModule.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AppUserService {
    //TODO 以後要做 getByName
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy(value = true)
    private MailService mailService = new MailService();
    private BCryptPasswordEncoder passwordEncoder;

    public AppUserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean hasExitUserByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public AppUser getUserById(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public AppUser getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public AppUser[] getAllUsers(){
        List<AppUser> tmp = userRepository.findAll();
        return tmp.toArray(new AppUser[0]);
    }

    public AppUser createUser(AppUser request) {
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setName(request.getName());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setVerifyCode(randomCode());
        appUser.setAdmin(false);
        appUser.setActivate(false);
//        appUser.setProfile(request.getProfile());
//        appUser.setStrength(request.getStrength());
        // TODO: mongoDB 預設值
//        appUser.setFolders();
//        appUser.setSubscribe(request.getSubscribe());
//        appUser.setBell(request.getBell());
//        appUser.setFans(request.getFans());
        appUser.setCoin(300);
        mailService.sendEmailToUser(request.getEmail(),"Your Verification Code",appUser.getVerifyCode());
        return userRepository.insert(appUser);
    }

    public AppUser replaceUser(AppUser request){
        AppUser user = new AppUser();
        user.setActivate(request.isActivate());
        user.setAdmin(request.isAdmin());
        user.setBell(request.getBell());
        user.setCoin(request.getCoin());
        user.setEmail(request.getEmail());
        user.setFolders(request.getFolders());
        user.setFans(request.getFans());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setProfile(request.getProfile());
        user.setStrength(request.getStrength());
        user.setSubscribe(request.getSubscribe());
        user.setVerifyCode(request.getVerifyCode());
        user.setId(request.getId());
        return userRepository.save(user);
    }

    public void modifyStrength(String email, ArrayList<String> strength) {
        AppUser user = getUserByEmail(email);
        user.setStrength(strength);
        userRepository.save(user);
    }

    public void modifyProfile(String email, String profile) {
        AppUser user = getUserByEmail(email);
        user.setProfile(profile);
        userRepository.save(user);
    }

    public void replacePassword(String email, String genRandomPassword) {
        AppUser user = getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(genRandomPassword));
        userRepository.save(user);
    }

    public static String randomCode(){
        char[] chars = new char[4];
        Random rnd = new Random();
        for(int i = 0; i < 4; i++){
            chars[i]=(char)('0'+rnd.nextInt(10));
        }
        return new String(chars);
    }
}
