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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FolderRepository folderRepository;

    public Pages getSearchedUser(String userName, int offset, int pageSize) {
        PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by("name").descending());
        Page<AppUser> appUserLikePage = userRepository.findByNameRegex(userName, pageRequest);
        return new Pages(appUserLikePage.getContent(), appUserLikePage.getTotalPages());
    }

//    Note -> Sort By
//      - likeCount
//      - Date = createdAt or updatedAt
//      - price
//      - unlockCount
//      - favoriteCount
    public Pages getSearchedNoteByKeyword(String keyword, int offset, int pageSize, SearchNote searchNote, String sortBy) {
        //determine sort method
        if (sortBy.equals(""))
            sortBy = "title";
        List<Note> noteList = noteRepository.findNoteByTitleRegex(keyword, Sort.by(sortBy).descending());
        if (noteList.isEmpty())
            return new Pages(null, 0);

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
        List<Note> copyOfNoteList = new ArrayList<>(noteList);

        if (haveNormal != null) {
            if (!haveNormal)
                copyOfNoteList.removeIf((Note n) -> (n.getType().equals("normal")));
        }
        if (haveCollaboration != null) {
            if (!haveCollaboration)
                copyOfNoteList.removeIf((Note n) -> (n.getType().equals("collaboration")));
        }
        if (haveReward != null) {
            if (!haveReward)
                copyOfNoteList.removeIf((Note n) -> (n.getType().equals("reward")));
        }

        Pageable paging = PageRequest.of(offset, pageSize, Sort.by(sortBy).descending());
        int start = Math.min((int)paging.getOffset(), copyOfNoteList.size());
        int end = Math.min((start + paging.getPageSize()), copyOfNoteList.size());
        Page<Note> page = new PageImpl<>(copyOfNoteList.subList(start, end), paging, copyOfNoteList.size());

        return new Pages(page.getContent(), page.getTotalPages());
    }

//    Post -> Sort By
//      - commentCount
//      - date
//      - price
    public Pages getSearchedPostByKeyword(String keyword, int offset, int pageSize, SearchPost searchPost, String sortBy) {
        //determine sort method
        if (sortBy.equals(""))
            sortBy = "title";
        List<Post> postList = postRepository.findPostByTitleRegex(keyword, Sort.by(sortBy).descending());

        if (postList.isEmpty())
            return new Pages(null, 0);

        // additional search condition
        String subject = searchPost.getSubject();
        String department = searchPost.getDepartment();
        String author = searchPost.getAuthor();
        Integer bestPrice = searchPost.getBestPrice();
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
        if (bestPrice != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getBestPrice() >= bestPrice)
                    .collect(Collectors.toList());

        //determine which type of note should be displayed
        List<Post> copyOfPostList = new ArrayList<>(postList);

        if (haveQA != null) {
            if (!haveQA)
                copyOfPostList.removeIf((Post p) -> (p.getType().equals("QA")));
        }
        if (haveCollaboration != null) {
            if (!haveCollaboration)
                copyOfPostList.removeIf((Post p) -> (p.getType().equals("collaboration")));
        }
        if (haveReward != null) {
            if (!haveReward)
                copyOfPostList.removeIf((Post p) -> (p.getType().equals("reward")));
        }

        Pageable paging = PageRequest.of(offset, pageSize, Sort.by(sortBy).descending());
        int start = Math.min((int)paging.getOffset(), copyOfPostList.size());
        int end = Math.min((start + paging.getPageSize()), copyOfPostList.size());
        Page<Post> page = new PageImpl<>(copyOfPostList.subList(start, end), paging, copyOfPostList.size());

        return new Pages(page.getContent(), page.getTotalPages());
    }

    public Pages getSearchedFolderByKeyword(String keyword, int offset, int pageSize) {
        PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by("title").descending());
        Page<Folder> foldersLikePage = folderRepository.findByFolderNameRegex(keyword, pageRequest);
        return new Pages(foldersLikePage.getContent(), foldersLikePage.getTotalPages());
    }
}
