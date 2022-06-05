package ntou.notesharedevbackend.searchModule.service;

import ntou.notesharedevbackend.folderModule.entity.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.searchModule.entity.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

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
    private PostRepository postRepository;
    @Autowired
    private FolderRepository folderRepository;

    public AppUser[] getSearchedUser(String userName) {
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("name").descending());
        Page<AppUser> appUserLikePage = userRepository.findByNameRegex(userName, pageRequest);
        List<AppUser> appUserList = appUserLikePage.getContent();
        return appUserList.toArray(new AppUser[0]);
    }

//    Note -> Sort By
//      - likeCount
//      - Date = createdAt or updatedAt
//      - price
//      - unlockCount
//      - favoriteCount
    public Note[] getSearchedNoteByKeyword(String keyword, SearchNote searchNote, String sortBy) {
        // initial search, determine sort method
        if (sortBy.equals(""))
            sortBy = "name";
        else if (sortBy.equals("Date"))
            sortBy = "createAt";
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by(sortBy).descending());
        Page<Note> notesLikePage = noteRepository.findNoteByNameRegex(keyword, pageRequest);
        List<Note> noteList = notesLikePage.getContent();
        if (noteList.isEmpty())
            return noteList.toArray(new Note[0]);

        // additional search condition
        String school = searchNote.getSchool();
        String subject = searchNote.getSubject();
        String department = searchNote.getDepartment();
        String professor = searchNote.getProfessor();
        String headerName = searchNote.getHeaderName();
        Boolean haveNormal = searchNote.getHaveNormal();
        Boolean haveCollaboration = searchNote.getHaveCollaboration();
        Boolean haveReward = searchNote.getHaveReward();
        Boolean downloadable = searchNote.getDownloadable();
        ArrayList<String> tag = searchNote.getTag();
        Integer unlockCount = searchNote.getUnlockCount();
        Integer favoriteCount = searchNote.getFavoriteCount();
        Integer price = searchNote.getPrice();
        Boolean quotable = searchNote.getQuotable();

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
        if (tag != null) {
            List<Note> tempNoteList = new ArrayList<>();
            for (Note note : noteList) {
                if (note.getTag().size() == 0)
                    continue;
                ArrayList<String> noteTag = note.getTag();
                boolean find = false;
                for (String value : noteTag) {
                    for (String s : tag) {
                        if (value.contains(s)) {
                            find = true;
                            break;
                        }
                    }
                }
                if (find)
                    tempNoteList.add(note);
            }

            noteList = tempNoteList;
        }
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

//    Post -> Sort By
//      - commentCount
//      - date
//      - price
    public Post[] getSearchedPostByKeyword(String keyword, SearchPost searchPost, String sortBy) {
        //initial search, determine sort method
        if (sortBy.equals(""))
            sortBy = "title";
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by(sortBy).descending());
        Page<Post> postsLikePage = postRepository.findPostByTitleRegex(keyword, pageRequest);
        List<Post> postList = postsLikePage.getContent();
        if (postList.isEmpty())
            return postList.toArray(new Post[0]);

        // additional search condition
        String subject = searchPost.getSubject();
        String department = searchPost.getDepartment();
        String author = searchPost.getAuthor();
        Integer price = searchPost.getPrice();
        Boolean haveQA = searchPost.getHaveQA();
        Boolean haveCollaboration = searchPost.getHaveCollaboration();
        Boolean haveReward = searchPost.getHaveReward();

        if (subject != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getSubject().contains(subject))
                    .collect(Collectors.toList());
        if (department != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getDepartment().contains(department))
                    .collect(Collectors.toList());
        if (author != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getAuthor().contains(author))
                    .collect(Collectors.toList());
        if (price != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getPrice() >= price)
                    .collect(Collectors.toList());

        //determine which type of note should be displayed
        if (haveQA != null) {
            if (!haveQA)
                postList.removeIf((Post p) -> (p.getType().equals("QA")));
        }
        if (haveCollaboration != null) {
            if (!haveCollaboration)
                postList.removeIf((Post p) -> (p.getType().equals("collaboration")));
        }
        if (haveReward != null) {
            if (!haveReward)
                postList.removeIf((Post p) -> (p.getType().equals("reward")));
        }

        return postList.toArray(new Post[0]);
    }

    public Folder[] getSearchedFolderByKeyword(String keyword) {
        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by("title").descending());
        Page<Folder> foldersLikePage = folderRepository.findByFolderNameRegex(keyword, pageRequest);
        List<Folder> folderList = foldersLikePage.getContent();
        return folderList.toArray(new Folder[0]);
    }
}
