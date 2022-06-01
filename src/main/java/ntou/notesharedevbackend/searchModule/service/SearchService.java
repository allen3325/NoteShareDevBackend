package ntou.notesharedevbackend.searchModule.service;

import com.fasterxml.jackson.databind.*;
import ntou.notesharedevbackend.folderModule.entity.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.searchModule.entity.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import javax.swing.text.*;
import java.util.*;
import java.util.stream.*;

@Service
public class SearchService {
    private final int PAGE = 0;
    private final int SIZE = 20;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private FolderRepository folderRepository;

    public AppUser[] getSearchedUser(String userName) {
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("name").descending());
        Page<AppUser> appUserLikePage = userRepository.findByNameRegex(userName, pageRequest);
        List<AppUser> appUserList = appUserLikePage.getContent();
        return appUserList.toArray(new AppUser[0]);
    }

    public Note[] getSearchedNoteByKeyword(String keyword, Search search) {
        // initial search
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("name").descending());
        Page<Note> notesLikePage = noteRepository.findNoteByNameRegex(keyword, pageRequest);
        List<Note> noteList = notesLikePage.getContent();
        if (noteList.isEmpty())
            return noteList.toArray(new Note[0]);

        // additional search condition
        String school = search.getSchool();
        String subject = search.getSubject();
        String department = search.getDepartment();
        String professor = search.getProfessor();
        String headerName = search.getHeaderName();
        Boolean haveNormal = search.getHaveNormal();
        Boolean haveCollaboration = search.getHaveCollaboration();
        Boolean haveReward = search.getHaveReward();
        Boolean downloadable = search.getDownloadable();
        ArrayList<String> tag = search.getTag();
        Integer unlockCount = search.getUnlockCount();
        Integer favoriteCount = search.getFavoriteCount();
        Integer price = search.getPrice();
        Boolean quotable = search.getQuotable();

        if (school != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getSchool().contains(school))
                    .collect(Collectors.toList());
        if (subject != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getSubject().contains(subject))
                    .collect(Collectors.toList());
        if (department != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getDepartment().contains(department))
                    .collect(Collectors.toList());
        if (professor != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getProfessor().contains(professor))
                    .collect(Collectors.toList());
        if (headerName != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getHeaderName().contains(headerName))
                    .collect(Collectors.toList());
        if (downloadable != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getDownloadable().equals(downloadable))
                    .collect(Collectors.toList());
        if (tag != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getTag().containsAll(tag))
                    .collect(Collectors.toList());
        if (unlockCount != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getUnlockCount() >= unlockCount)
                    .collect(Collectors.toList());
        if (favoriteCount != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getFavoriteCount() >= favoriteCount)
                    .collect(Collectors.toList());
        if (price != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getPrice() >= price)
                    .collect(Collectors.toList());
        if (quotable != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getQuotable().equals(quotable))
                    .collect(Collectors.toList());

        //determine which type of note should be displayed
        if (haveNormal != null) {
            if (!haveNormal)
                noteList.removeIf((Note n) -> (n.getType().equals("normal")));
        }
        if (haveCollaboration != null) {
            if (!haveCollaboration)
                noteList.removeIf((Note n) -> (n.getType().equals("collaboration")));
        }
        if (haveReward != null) {
            if (!haveReward)
                noteList.removeIf((Note n) -> (n.getType().equals("reward")));
        }

        return noteList.toArray(new Note[0]);
    }

    public Folder[] getSearchedFolderByKeyword(String keyword) {
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("title").descending());
        Page<Folder> foldersLikePage = folderRepository.findByFolderNameRegex(keyword, pageRequest);
        List<Folder> folderList = foldersLikePage.getContent();
        return folderList.toArray(new Folder[0]);
    }
}
