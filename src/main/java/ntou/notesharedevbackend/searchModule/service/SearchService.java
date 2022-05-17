package ntou.notesharedevbackend.searchModule.service;

import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class SearchService {
    @Autowired
    private UserRepository userRepository;

    public AppUser[] getSearchedUser(String userName) {
        List<AppUser> appUserLike = userRepository.findByNameLike(userName);
        return appUserLike.toArray(new AppUser[0]);
    }
}
