package ntou.notesharedevbackend.searchModule.service;

import com.fasterxml.jackson.databind.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.repository.*;
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

    public AppUser[] getSearchedUser(String userName) {
//        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("name").descending());
//        Page<AppUser> appUserLikePage = userRepository.findAllByNameLike(userName, pageRequest);
//        List<AppUser> appUserList = appUserLikePage.getContent();
        List<AppUser> appUserList = userRepository.findAllByNameLike(userName);
        return appUserList.toArray(new AppUser[0]);
    }

    public Note[] getSearchedByKeyword(String keyword, Note note) {
        // initial search
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("title").descending());
        Page<Note> notesLikePage = noteRepository.findNoteByTitleLike(keyword, pageRequest);
        List<Note> noteList = notesLikePage.getContent();
        if (noteList.isEmpty())
            return noteList.toArray(new Note[0]);

        // additional search condition
        String school = note.getSchool();
        String subject = note.getSubject();
        String department = note.getDepartment();
        String professor = note.getProfessor();
        String headerName = note.getHeaderName();
        Boolean isBest = note.getBest();
        String type = note.getType();
        Boolean downloadable = note.getDownloadable();
        ArrayList<String> tag = note.getTag();

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
        if (isBest != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getBest().equals(isBest))
                    .collect(Collectors.toList());
        if (type != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getType().contains(type))
                    .collect(Collectors.toList());
        if (downloadable != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getDownloadable().equals(downloadable))
                    .collect(Collectors.toList());
        if (tag != null)
            noteList = noteList.stream()
                    .filter((Note n) -> n.getTag().containsAll(tag))
                    .collect(Collectors.toList());

        return noteList.toArray(new Note[0]);
    }
}
