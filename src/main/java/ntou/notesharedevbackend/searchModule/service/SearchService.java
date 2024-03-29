package ntou.notesharedevbackend.searchModule.service;

import ntou.notesharedevbackend.folderModule.entity.*;
import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.noteModule.service.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.postModule.service.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.searchModule.entity.*;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.*;
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
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PostService postService;
    @Autowired
    private NoteService noteService;

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

        noteList = noteList.stream()
                .filter((Note n) -> n.getPublic().equals(true))
                .collect(Collectors.toList());
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
        else
            copyOfNoteList.removeIf((Note n) -> (n.getType().equals("normal")));
        if (haveCollaboration != null) {
            if (!haveCollaboration)
                copyOfNoteList.removeIf((Note n) -> (n.getType().equals("collaboration")));
        }
        else
            copyOfNoteList.removeIf((Note n) -> (n.getType().equals("collaboration")));
        if (haveReward != null) {
            if (!haveReward)
                copyOfNoteList.removeIf((Note n) -> (n.getType().equals("reward")));
        }
        else
            copyOfNoteList.removeIf((Note n) -> (n.getType().equals("reward")));

        List<NoteBasicReturn> noteBasicReturns = new ArrayList<>();
        for (Note note : copyOfNoteList) {
            NoteBasicReturn noteBasicReturn = new NoteBasicReturn(note);
            UserObj userObj = appUserService.getUserInfo(note.getHeaderEmail());
            noteBasicReturn.setHeaderEmailUserObj(userObj);

            ArrayList<UserObj> userObjs = new ArrayList<>();
            for(String email : note.getAuthorEmail()){
                UserObj userInfo = appUserService.getUserInfo(email);
                userObjs.add(userInfo);
            }
            noteBasicReturn.setAuthorEmailUserObj(userObjs);

            noteBasicReturns.add(noteBasicReturn);
        }

        Pageable paging = PageRequest.of(offset, pageSize, Sort.by(sortBy).descending());
        int start = Math.min((int)paging.getOffset(), noteBasicReturns.size());
        int end = Math.min((start + paging.getPageSize()), noteBasicReturns.size());
        Page<NoteBasicReturn> page = new PageImpl<>(noteBasicReturns.subList(start, end), paging, noteBasicReturns.size());

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
        String authorName = searchPost.getAuthorName();
        String professor = searchPost.getProfessor();
        Integer bestPrice = searchPost.getBestPrice();
        Boolean haveQA = searchPost.getHaveQA();
        Boolean haveCollaboration = searchPost.getHaveCollaboration();
        Boolean haveReward = searchPost.getHaveReward();

        postList = postList.stream()
                .filter((Post n) -> n.getPublic().equals(true))
                .collect(Collectors.toList());

        if (subject != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getSubject().contains(subject))
                    .collect(Collectors.toList());
        if (department != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getDepartment().contains(department))
                    .collect(Collectors.toList());
        if (authorName != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getAuthorName().contains(authorName))
                    .collect(Collectors.toList());
        if (professor != null)
            postList = postList.stream()
                    .filter((Post p) -> p.getProfessor().contains(professor))
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
        else
            copyOfPostList.removeIf((Post p) -> (p.getType().equals("QA")));
        if (haveCollaboration != null) {
            if (!haveCollaboration)
                copyOfPostList.removeIf((Post p) -> (p.getType().equals("collaboration")));
        }
        else
            copyOfPostList.removeIf((Post p) -> (p.getType().equals("collaboration")));
        if (haveReward != null) {
            if (!haveReward)
                copyOfPostList.removeIf((Post p) -> (p.getType().equals("reward")));
        }
        else
            copyOfPostList.removeIf((Post p) -> (p.getType().equals("reward")));

        List<PostReturn> postReturns = new ArrayList<>();
        for (Post post : copyOfPostList) {
            PostReturn postReturn = postService.getUserInfo(post);
            postReturns.add(postReturn);
        }

        Pageable paging = PageRequest.of(offset, pageSize, Sort.by(sortBy).descending());
        int start = Math.min((int)paging.getOffset(), postReturns.size());
        int end = Math.min((start + paging.getPageSize()), postReturns.size());
        Page<PostReturn> page = new PageImpl<>(postReturns.subList(start, end), paging, postReturns.size());

        return new Pages(page.getContent(), page.getTotalPages());
    }

    public Pages getSearchedFolderByKeyword(String keyword, int offset, int pageSize, String creator) {
        List<Folder> foldersLikePage = folderRepository.findByFolderNameRegex(keyword);
        foldersLikePage = foldersLikePage.stream()
                .filter((Folder n) -> n.getPublic().equals(true))
                .collect(Collectors.toList());
        if (creator != null || !creator.equals(""))
            foldersLikePage = foldersLikePage.stream()
                    .filter((Folder p) -> p.getCreatorName().contains(creator))
                    .collect(Collectors.toList());

        List<Folder> copyFolderList = new ArrayList<>(foldersLikePage);
        copyFolderList.removeIf((Folder p) -> (p.getFolderName().equals("Owned")));
        copyFolderList.removeIf((Folder p) -> (p.getFolderName().equals("Favorites")));
        copyFolderList.removeIf((Folder p) -> (p.getFolderName().equals("Folders")));

        List<FolderBasicReturn> folderBasicReturnList = new ArrayList<>();
        for (Folder folder : copyFolderList) {
            String creatorName = folder.getCreatorName();
            AppUser appUser = userRepository.findByName(creatorName);
            FolderBasicReturn folderBasicReturn = new FolderBasicReturn(folder, appUser);
            folderBasicReturnList.add(folderBasicReturn);
        }

        Pageable paging = PageRequest.of(offset, pageSize, Sort.by("folderName").descending());
        int start = Math.min((int)paging.getOffset(), folderBasicReturnList.size());
        int end = Math.min((start + paging.getPageSize()), folderBasicReturnList.size());
        Page<FolderBasicReturn> page = new PageImpl<>(folderBasicReturnList.subList(start, end), paging, folderBasicReturnList.size());

        return new Pages(page.getContent(), page.getTotalPages());
    }

    public Pages getNotesByTags(int offset, int pageSize, String[] tags) {
        List<Note> notes = new ArrayList<>();
        Set<String> noteIdHashSet = new HashSet<>();
        for (String tag : tags) {
            Set<String> findNotesId = noteRepository.findAllByTagRegex(tag);
            noteIdHashSet.addAll(findNotesId);
        }

        ArrayList<String> realIDArray = new ArrayList<>();
        for (String tmpID : noteIdHashSet) {
            String realID = tmpID.substring(18, tmpID.length() - 3);
            realIDArray.add(realID);
        }
        for (String id : realIDArray)
            notes.add(noteService.getNote(id));

        notes = notes.stream()
                .filter((Note n) -> n.getPublic().equals(true))
                .collect(Collectors.toList());

        List<NoteBasicReturn> noteBasicReturns = new ArrayList<>();
        for (Note note : notes) {
            NoteBasicReturn noteBasicReturn = new NoteBasicReturn(note);
            UserObj userObj = appUserService.getUserInfo(note.getHeaderEmail());
            noteBasicReturn.setHeaderEmailUserObj(userObj);

            ArrayList<UserObj> userObjs = new ArrayList<>();
            for (String email : note.getAuthorEmail()) {
                UserObj userInfo = appUserService.getUserInfo(email);
                userObjs.add(userInfo);
            }
            noteBasicReturn.setAuthorEmailUserObj(userObjs);

            noteBasicReturns.add(noteBasicReturn);
        }

        Pageable paging = PageRequest.of(offset, pageSize, Sort.by("title").descending());
        int start = Math.min((int)paging.getOffset(), noteBasicReturns.size());
        int end = Math.min((start + paging.getPageSize()), noteBasicReturns.size());
        Page<NoteBasicReturn> page = new PageImpl<>(noteBasicReturns.subList(start, end), paging, noteBasicReturns.size());

        return new Pages(page.getContent(), page.getTotalPages());
    }
}
