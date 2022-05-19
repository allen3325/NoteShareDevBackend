package ntou.notesharedevbackend.searchModule.service;

import com.fasterxml.jackson.databind.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class SearchService {
    private final int PAGE = 0;
    private final int SIZE = 20;

    @Autowired
    private UserRepository userRepository;

    public AppUser[] getSearchedUser(String userName) {
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("name").descending());
        Page<AppUser> appUserLikePage = userRepository.findAppUserByNameLike(userName, pageRequest);
        ObjectMapper objectMapper = new ObjectMapper();

//        List<AppUser> appUserList = userRepository.findAppUserByNameLike(userName);
        List<AppUser> appUserList = appUserLikePage.getContent();
        return appUserList.toArray(new AppUser[0]);
    }

//    public String getSearchedByKeyword() {
//
//    }
}
