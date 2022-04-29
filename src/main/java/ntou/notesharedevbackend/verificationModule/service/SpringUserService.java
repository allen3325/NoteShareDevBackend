package ntou.notesharedevbackend.verificationModule.service;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
// 因為 SecurityConfig @Autowired UserDetailsService，Spring 會自動找到有實作 UserDetailsService 的類別
public class SpringUserService implements UserDetailsService {
    @Autowired
    private AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO 以後改成ByName
        try{
            AppUser user =  appUserService.getUserByEmail(email);

            //該 User 類別是由 org.springframework.security.core.userdetails 的套件提供，本身已經實作 UserDetails，回傳該物件是較簡易的做法。至於建構子的第三個參數是 authorities，是用來定義使用者擁有的權限。
            return new User(user.getEmail(),user.getPassword(), Collections.emptyList());
        }catch (NotFoundException e){
            throw new UsernameNotFoundException("This is is wrong.");
        }
    }
}
