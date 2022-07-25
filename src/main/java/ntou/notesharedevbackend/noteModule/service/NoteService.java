package ntou.notesharedevbackend.noteModule.service;

import ntou.notesharedevbackend.coinModule.entity.Coin;
import ntou.notesharedevbackend.coinModule.service.CoinService;
import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.commentModule.service.*;
import ntou.notesharedevbackend.exception.BadRequestException;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderReturn;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.notificationModule.entity.MessageReturn;
import ntou.notesharedevbackend.notificationModule.service.NotificationService;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.searchModule.entity.NoteBasicReturn;
import ntou.notesharedevbackend.searchModule.entity.Pages;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.entity.UserObj;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    @Lazy(value = true)
    private CoinService coinService;
    @Autowired
    private PostService postService;
    @Autowired
    @Lazy
    private FolderService folderService;
    @Autowired
    @Lazy(value = true)
    private CommentService commentService;
    @Autowired
    @Lazy
    private NotificationService notificationService;

    protected final SimpMessagingTemplate messagingTemplate;

    @Autowired
    protected NoteService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public Note getNote(String id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find note."));
    }

    public VersionContent getNoteVersion(String id, int version) {
        Note note = getNote(id);
        return note.getVersion().get(version);
    }

    public Note updateNoteVersion(String id, int version, VersionContent newVersionContent) {
        Note note = getNote(id);
        ArrayList<VersionContent> oldVersionContent = note.getVersion();
        if (oldVersionContent.size() > version) {
            oldVersionContent.set(version, newVersionContent);
        } else {
            oldVersionContent.add(newVersionContent);
        }

        note.setVersion(oldVersionContent);

        return replaceNote(note, note.getId());

//        return noteRepository.save(note);
    }

    public ArrayList<String> getNoteTags(String id) {
        Note note = getNote(id);
        return note.getTag();
    }

    public Note createNote(Note request, String email) {
        Note note = new Note();
        AppUser user = appUserService.getUserByEmail(email);
        ArrayList<String> authorEmail = new ArrayList<String>();
        ArrayList<String> authorName = new ArrayList<String>();
        authorEmail.add(user.getEmail());
        authorName.add(user.getName());

        note.setType(request.getType());
        note.setDepartment(request.getDepartment());
        note.setSubject(request.getSubject());
        note.setTitle(request.getTitle());
        note.setHeaderEmail(user.getEmail());
        note.setHeaderName(user.getName());
        note.setAuthorEmail(authorEmail);
        note.setAuthorName(authorName);
        note.setManagerEmail(request.getManagerEmail());
        note.setProfessor(request.getProfessor());
        note.setSchool(request.getSchool());
        note.setLiker(new ArrayList<String>());
        note.setBuyer(new ArrayList<String>());
        note.setFavoriter(new ArrayList<String>());
        note.setLikeCount(0);
        note.setFavoriteCount(0);
        note.setUnlockCount(0);
        note.setDownloadable(request.getDownloadable());
        note.setCommentCount(0);
        note.setComments(new ArrayList<Comment>());
        note.setPrice(request.getPrice());
        note.setPublic(request.getPublic());
        note.setSubmit(request.getSubmit());
        note.setQuotable(request.getQuotable());
        note.setTag(new ArrayList<String>());
        note.setHiddenTag(new ArrayList<String>());
        if (request.getVersion() == null || request.getVersion().isEmpty()) {
            note.setVersion(new ArrayList<VersionContent>());
        } else {
            note.setVersion(request.getVersion());
        }
        note.setContributors(new ArrayList<String>());
        note.setPostID(request.getPostID());
        note.setReference(request.getReference());
        note.setBest(request.getBest());
        if (request.getDescription() != null) {
            note.setDescription(request.getDescription());
        } else {
            note.setDescription("");
        }
        note.setClickDate(new ArrayList<>());
        note.setClickNum(0);
        return noteRepository.insert(note);
    }

    public void setManager(String noteID, String email) {
        Note note = getNote(noteID);
        note.setManagerEmail(email);
        replaceNote(note, note.getId());
//        noteRepository.save(note);
    }

    public void kickUserFromCollaboration(String noteId, String email) {
        Note note = getNote(noteId);
        ArrayList<String> currentEmails = note.getAuthorEmail();
        ArrayList<String> currentNames = note.getAuthorName();
        int userIndex = currentEmails.indexOf(email);

        currentEmails.remove(userIndex);
        currentNames.remove(userIndex);
        note.setAuthorEmail(currentEmails);
        note.setAuthorName(currentNames);
        if (note.getManagerEmail() != null && note.getManagerEmail().equals(email)) {//檢查踢除人是否為管理員
            note.setManagerEmail(null);
        }
        replaceNote(note, note.getId());
        postService.kickUserFromCollaboration(note.getPostID(), email);
//        noteRepository.save(note);
    }

    public void triggerKickUserFromCollaboration(String noteId, String email) {
        Note note = getNote(noteId);
        ArrayList<String> currentEmails = note.getAuthorEmail();
        ArrayList<String> currentNames = note.getAuthorName();
        int userIndex = currentEmails.indexOf(email);

        currentEmails.remove(userIndex);
        currentNames.remove(userIndex);
        note.setAuthorEmail(currentEmails);
        note.setAuthorName(currentNames);
        if (note.getManagerEmail() != null && note.getManagerEmail().equals(email)) {//檢查踢除人是否為管理員
            note.setManagerEmail(null);
        }
        replaceNote(note, note.getId());
//        noteRepository.save(note);
    }

    public Note publishNote(String noteID) {
        Note note = getNote(noteID);
        if (note.getType().equals("normal")) {
            note.setPublic(true);
        } else {//collaboration
            note.setPublic(!note.getPublic());

            //傳送通知給所有共筆作者、開啟bell使用者
            if (note.getPublic()) {
                MessageReturn messageReturn = new MessageReturn();
                messageReturn.setMessage("共筆筆記已發布");
                UserObj userObj = new UserObj();
                userObj.setUserObjEmail("noteshare@gmail.com");
                userObj.setUserObjName("NoteShare System");
                userObj.setUserObjAvatar("https://i.imgur.com/5V1waq3.png");
                messageReturn.setUserObj(userObj);
                messageReturn.setType("collaboration");
                messageReturn.setId(noteID);
                messageReturn.setDate(new Date());
                for (String author : note.getAuthorEmail()) {
                    messagingTemplate.convertAndSendToUser(author, "/topic/private-messages", messageReturn);
                    notificationService.saveNotificationPrivate(author, messageReturn);
                }
            }
        }

        if (note.getPublic()) {
            for (String author : note.getAuthorEmail()) {
                MessageReturn messageReturnForBell = notificationService.getMessageReturn(author, "發布了筆記", "note", noteID);
                messagingTemplate.convertAndSend("/topic/bell-messages/" + author, messageReturnForBell);
                notificationService.saveNotificationBell(author, messageReturnForBell);
            }
        }

        // update publish date
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
        note.setPublishDate(calendar.getTime());
        // update note
        return replaceNote(note, note.getId());
//        noteRepository.save(note);
    }

    public Note submitRewardNote(String noteID) {
        Note note = getNote(noteID);
        note.setSubmit(true);
        note.setPublishDate(new Date());
        Note newNote = replaceNote(note, noteID);
        //remove from folder
//        AppUser appUser = appUserService.getUserByEmail(note.getHeaderEmail());
//        Folder tempRewardNoteFolder = folderService.getTempRewardNoteFolder(appUser.getEmail());
//        tempRewardNoteFolder.getNotes().remove(noteID);
//        folderService.replaceFolder(tempRewardNoteFolder);
        Post post = postService.getPostById(note.getPostID());
        post.getAnswers().add(noteID);
        postService.replacePost(post.getId(), post);
        //通知懸賞人有人投稿筆記
//        UserObj userObj = appUserService.getUserInfo(note.getHeaderEmail());
//        MessageReturn messageReturn = new MessageReturn();
//        messageReturn.setDate(new Date());
//        messageReturn.setUserObj(userObj);
//        messageReturn.setMessage(userObj.getUserObjName() + "對你投稿了懸賞筆記");
//        messageReturn.setType("reward");
//        messageReturn.setId(post.getId());
        MessageReturn messageReturn = notificationService.getMessageReturn(note.getHeaderEmail(), "對你投稿了懸賞筆記", "reward", post.getId());
        messagingTemplate.convertAndSendToUser(post.getAuthor(), "/topic/private-messages", messageReturn);
        notificationService.saveNotificationPrivate(post.getAuthor(), messageReturn);
        return newNote;
    }

    public Note withdrawRewardNote(String noteID) {
        Note note = getNote(noteID);
        note.setSubmit(false);
        Note newNote = replaceNote(note, noteID);
        //remove from folder
//        AppUser appUser = appUserService.getUserByEmail(note.getHeaderEmail());
//        Folder tempRewardNoteFolder = folderService.getTempRewardNoteFolder(appUser.getEmail());
//        tempRewardNoteFolder.getNotes().remove(noteID);
//        folderService.replaceFolder(tempRewardNoteFolder);
        Post post = postService.getPostById(note.getPostID());
        post.getAnswers().remove(noteID);
        postService.replacePost(post.getId(), post);
        return newNote;
    }

    public void rewardNoteBestAnswer(String noteID, String email, String bestPrice) {
        //email為懸賞人email
        Note note = getNote(noteID);
        note.setBest(true);
        Coin bestAnswerCoin = new Coin();
        bestAnswerCoin.setCoin('+' + bestPrice);
        coinService.changeCoin(note.getAuthorEmail().get(0), bestAnswerCoin);
        String contributor = note.getAuthorEmail().get(0);//投稿人email
        note.getContributors().add(contributor);//將投稿人放入contributor
        note = replaceNote(note, note.getId());
//        note.getAuthorEmail().add(email);
//        String userName = appUserService.getUserByEmail(email).getName();
//        note.getAuthorName().add(userName);
        Folder buyFolder = folderService.getBuyFolderByUserEmail(email);
        copyNoteToFolder(note.getId(), buyFolder.getId());//筆記放入懸賞人的buy folder;
        Folder tempRewardNote = folderService.getTempRewardNoteFolder(contributor);
        tempRewardNote.getNotes().remove(noteID);
        folderService.replaceFolder(tempRewardNote);
//        noteRepository.save(note);
    }

    public void rewardNoteReferenceAnswer(String noteID, String email, String referencePrice) {
        //email為懸賞人email
        Note note = getNote(noteID);
        note.setReference(true);
        Coin referenceAnswerCoin = new Coin();
        referenceAnswerCoin.setCoin('+' + referencePrice);
        coinService.changeCoin(note.getAuthorEmail().get(0), referenceAnswerCoin);//購買者增加點
//        note.getAuthorEmail().add(email);
//        String userName = appUserService.getUserByEmail(email).getName();
//        note.getAuthorName().add(userName);
        replaceNote(note, note.getId());
        //筆寄放入懸賞人buyFolder
        Folder buyFolder = folderService.getBuyFolderByUserEmail(email);
        copyNoteToFolder(noteID, buyFolder.getId());
        Folder tempRewardNote = folderService.getTempRewardNoteFolder(note.getAuthorEmail().get(0));
        tempRewardNote.getNotes().remove(noteID);
        folderService.replaceFolder(tempRewardNote);
//        noteRepository.save(note);
    }

    public void collaborationNoteSetPostID(String noteID, String postID) {
        Note note = getNote(noteID);
        note.setPostID(postID);
        replaceNote(note, note.getId());
//        noteRepository.save(note);
    }

    public Note replaceNote(Note request, String id) {
        Note oldNote = getNote(id);
        Note note = new Note();

        note.setId(oldNote.getId());
        note.setType(request.getType());
        note.setDepartment(request.getDepartment());
        note.setSubject(request.getSubject());
        note.setTitle(request.getTitle());
        note.setHeaderEmail(request.getHeaderEmail());
        note.setHeaderName(request.getHeaderName());
        note.setAuthorEmail(request.getAuthorEmail());
        note.setAuthorName(request.getAuthorName());
        note.setManagerEmail(request.getManagerEmail());
        note.setProfessor(request.getProfessor());
        note.setSchool(request.getSchool());
        note.setLiker(request.getLiker());
        note.setBuyer(request.getBuyer());
        note.setFavoriter(request.getFavoriter());
        note.setFavoriteCount(note.getFavoriter().size());
        note.setLikeCount(note.getLiker().size());
        note.setUnlockCount(note.getBuyer().size());
        note.setDownloadable(request.getDownloadable());
        note.setComments(request.getComments());
        note.setCommentCount(request.getComments().size());
        note.setPrice(request.getPrice());
        note.setPublic(request.getPublic());
        note.setSubmit(request.getSubmit());
        note.setQuotable(request.getQuotable());
        note.setTag(request.getTag());
        note.setHiddenTag(request.getHiddenTag());
        note.setVersion(request.getVersion());
        note.setContributors(request.getContributors());
        note.setPostID(request.getPostID());
        note.setReference(request.getReference());
        note.setBest(request.getBest());
        note.setPublishDate(request.getPublishDate());
        note.setDescription(request.getDescription());
        note.setClickDate(oldNote.getClickDate());
        note.setClickNum(note.getClickDate().size());
        return noteRepository.save(note);
    }

    public Folder copyNoteToFolder(String noteID, String folderID) {
        Folder folder = folderService.getFolderByID(folderID);
        ArrayList<String> notes = folder.getNotes();

        notes.add(noteID);
        folder.setNotes(notes);
        folderService.replaceFolder(folder);

        return folder;
    }

    public Folder deleteNoteFromFolder(String noteID, String folderID) {
        Folder folder = folderService.getFolderByID(folderID);
        ArrayList<String> notes = folder.getNotes();

        if (notes.contains(noteID)) {
            notes.remove(noteID);
        } else {
            return null;
        }

        folder.setNotes(notes);
        folderService.replaceFolder(folder);

        return folder;
    }

    public Note changeDescription(String noteID, Note request) {
        Note note = getNote(noteID);
        if (request.getDescription() == null) {
            throw new BadRequestException("Description should not be null!");
        }
        note.setDescription(request.getDescription());
        replaceNote(note, note.getId());
        return note;
    }

    public ArrayList<NoteFolderReturn> getUserAllNote(String email) {
        List<Note> tmp = noteRepository.findAllByHeaderEmail(email);
        ArrayList<NoteFolderReturn> res = new ArrayList<>();
        for (Note note : tmp) {
            NoteFolderReturn noteFolderReturn = new NoteFolderReturn(note);
            noteFolderReturn.setHeaderUserObj(appUserService.getUserInfo(note.getHeaderEmail()));
            if (note.getManagerEmail() != null) {
                noteFolderReturn.setManagerEmail(note.getManagerEmail());
                noteFolderReturn.setManagerUserObj(appUserService.getUserInfo(note.getManagerEmail()));
            }
            ArrayList<UserObj> authorUserObj = new ArrayList<>();
            for (String authorEmail : note.getAuthorEmail()) {
                UserObj userObj = appUserService.getUserInfo(authorEmail);
                authorUserObj.add(userObj);
            }
            noteFolderReturn.setAuthorUserObj(authorUserObj);
            res.add(noteFolderReturn);
        }
        return res;
    }

    public boolean rewardNoteHaveAnswer(ArrayList<String> answers) {
        for (String noteID : answers) {
            if (getNote(noteID).getBest() != null && getNote(noteID).getBest()) {
                return true;
            }
        }
        return false;
    }

    public void returnRewardNoteToAuthor(String postID, ArrayList<String> answers) {
        Post post = postService.getPostById(postID);
        ArrayList<String> answersNotes = new ArrayList<>();
        answersNotes.addAll(post.getAnswers());
        for (String noteID : answers) {
            Note note = getNote(noteID);
            //非最佳解且非參考解 歸還原作者
            if ((note.getBest() == null || note.getBest() == false) && (note.getReference() == null || note.getReference() == false)) {
                String authorEmail = note.getHeaderEmail();
                //移出tempRewardNote Folder
                Folder tempRewardNote = folderService.getTempRewardNoteFolder(authorEmail);
                tempRewardNote.getNotes().remove(noteID);
                folderService.replaceFolder(tempRewardNote);
                //移入Folder Folder
                Folder defaultFolder = folderService.getFolderFolderByEmail(authorEmail);
                copyNoteToFolder(noteID, defaultFolder.getId());
                note.setPostID(null);
                note.setType("normal");
                replaceNote(note, noteID);
                answersNotes.remove(noteID);
            }
        }
        post.setAnswers(answersNotes);
        postService.replacePost(postID, post);
    }

    public NoteReturn getUserinfo(Note note) {
        NoteReturn noteReturn = new NoteReturn();
        noteReturn.setId(note.getId());
        noteReturn.setType(note.getType());
        noteReturn.setDepartment(note.getDepartment());
        noteReturn.setSubject(note.getSubject());
        noteReturn.setTitle(note.getTitle());
        noteReturn.setProfessor(note.getProfessor());
        noteReturn.setSchool(note.getSchool());
        noteReturn.setLikeCount(note.getLikeCount());
        noteReturn.setFavoriteCount(note.getFavoriteCount());
        noteReturn.setUnlockCount(note.getUnlockCount());
        noteReturn.setDownloadable(note.getDownloadable());
        noteReturn.setCommentCount(note.getCommentCount());
        noteReturn.setComments(note.getComments());
        ArrayList<CommentReturn> commentReturnArrayList = new ArrayList<>();
        for (Comment comment : note.getComments()) {
            CommentReturn commentReturn = commentService.getUserInfo(comment);
            commentReturnArrayList.add(commentReturn);
        }
        noteReturn.setCommentsUserObj(commentReturnArrayList);

        noteReturn.setPrice(note.getPrice());
        noteReturn.setPublic(note.getPublic());
        noteReturn.setSubmit(note.getSubmit());
        noteReturn.setQuotable(note.getQuotable());
        noteReturn.setTag(note.getTag());
        noteReturn.setHiddenTag(note.getHiddenTag());
        noteReturn.setVersion(note.getVersion());
        noteReturn.setPostID(note.getPostID());
        noteReturn.setReference(note.getReference());
        noteReturn.setBest(note.getBest());
        noteReturn.setPublishDate(note.getPublishDate());
        noteReturn.setDescription(note.getDescription());
        noteReturn.setHeaderName(note.getHeaderName());
        noteReturn.setHeaderEmail(note.getHeaderEmail());
        noteReturn.setHeaderUserObj(appUserService.getUserInfo(note.getHeaderEmail()));
        if (note.getManagerEmail() != null) {
            noteReturn.setManagerEmail(note.getManagerEmail());
            noteReturn.setManagerUserObj(appUserService.getUserInfo(note.getManagerEmail()));
        }
        noteReturn.setAuthorName(note.getAuthorName());
        noteReturn.setAuthorEmail(note.getAuthorEmail());
        ArrayList<UserObj> authorUserObj = new ArrayList<>();
        for (String authorEmail : note.getAuthorEmail()) {
            UserObj userObj = appUserService.getUserInfo(authorEmail);
            authorUserObj.add(userObj);
        }
        noteReturn.setAuthorUserObj(authorUserObj);
        noteReturn.setLiker(note.getLiker());
        ArrayList<UserObj> likerUserObj = new ArrayList<>();
        for (String likerEmail : note.getLiker()) {
            UserObj userObj = appUserService.getUserInfo(likerEmail);
            likerUserObj.add(userObj);
        }
        noteReturn.setLikerUserObj(likerUserObj);
        noteReturn.setBuyer(note.getBuyer());
        ArrayList<UserObj> buyerUserObj = new ArrayList<>();
        for (String buyerEmail : note.getBuyer()) {
            UserObj userObj = appUserService.getUserInfo(buyerEmail);
            buyerUserObj.add(userObj);
        }
        noteReturn.setBuyerUserObj(buyerUserObj);
        noteReturn.setFavoriter(note.getFavoriter());
        ArrayList<UserObj> favoriterUserObj = new ArrayList<>();
        for (String favoriterEmail : note.getFavoriter()) {
            UserObj userObj = appUserService.getUserInfo(favoriterEmail);
            favoriterUserObj.add(userObj);
        }
        noteReturn.setFavoriterUserObj(favoriterUserObj);
        noteReturn.setContributors(note.getContributors());
        ArrayList<UserObj> contributorUserObj = new ArrayList<>();
        for (String contributorEmail : note.getContributors()) {
            UserObj userObj = appUserService.getUserInfo(contributorEmail);
            contributorUserObj.add(userObj);
        }
        noteReturn.setContributorUserObj(contributorUserObj);
        return noteReturn;
    }

    public VersionContent modifyVersionStatus(String noteID, Integer version) {
        Note note = getNote(noteID);
        ArrayList<VersionContent> oldVersionContent = note.getVersion();
        VersionContent versionContent = oldVersionContent.get(version);
        versionContent.setTemp(!versionContent.getTemp());//修改狀態
        oldVersionContent.set(version, versionContent);
        note.setVersion(oldVersionContent);
        return replaceNote(note, note.getId()).getVersion().get(version);
    }

    public Note createRewardNote(String postID, String email, Note request) {
        request.setType("reward");
        request.setPostID(postID);
        Note note = createNote(request, email);
        AppUser appUser = appUserService.getUserByEmail(email);
        Folder folder = folderService.getTempRewardNoteFolder(appUser.getEmail());
        copyNoteToFolder(note.getId(), folder.getId());
        return note;
    }

    public FolderReturn folderGetUserInfo(String folderID) {
        return folderService.getAllContentUnderFolderID(folderID);
    }

    public Folder removeNoteFromFolder(String noteID, String folderID) {
        //判斷是否為buyFolder，裡面筆記不可刪除
        Folder folder = folderService.getFolderByID(folderID);
        if (folder.getFolderName().equals("Buy")) {
            return folder;
        }
        //判斷是否為購買的筆記
//        Note note = getNote(noteID);
//        AppUser appUser = appUserService.getUserByName(folder.getCreatorName());
//        if (note.getHeaderName().equals(appUser.getName())) {//自己的筆記
//            //判斷是否為最後一份
//            ArrayList<Folder> folderArrayList = folderService.getAllFoldersFromUser(appUser.getEmail());
//            for (Folder f : folderArrayList) {
//                if (f.getId().equals(folderID)) continue;
//                ;
//                if (f.getNotes().contains(noteID)) {//其餘folder內也有
//                    folder.getNotes().remove(noteID);
//                    return folderService.replaceFolder(folder);
//                }
//            }
//        } else {//購買筆記可直接移出
//            folder.getNotes().remove(noteID);
//            return folderService.replaceFolder(folder);
//        }
        folder.getNotes().remove(noteID);
        return folderService.replaceFolder(folder);
//        return folder;//不可移出
    }

    public FolderReturn turnFolderToFolderReturn(Folder folder) {
        return folderService.turnFolderToFolderReturn(folder);
    }

    public void updateClick(String noteID) {
        Note note = getNote(noteID);
        Date date = new Date();
        Long gap = TimeUnit.MILLISECONDS.convert(3, TimeUnit.DAYS);
        //刪除三天前的點擊
        for (Long clickDate : note.getClickDate()) {
            if (clickDate < date.getTime() - gap) {
                note.getClickDate().remove(clickDate);
            }
        }
        note.getClickDate().add(date.getTime());
        note.setClickNum(note.getClickDate().size());
        noteRepository.save(note);
    }

    public long getTotalPage(int pageSize) {
        long totalNotesNum = noteRepository.countAllByIsPublicTrueAndTypeNot("reward");
        if ((totalNotesNum % pageSize) == 0) {
            return totalNotesNum / pageSize - 1;
        } else {
            return totalNotesNum / pageSize ;
        }
    }


    public Pages getHotNotes(int offset, int pageSize) {
        Page<Note> listPage = noteRepository.findAllByIsPublicTrueAndTypeNot(PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "clickNum")),"reward");
        ArrayList<NoteBasicReturn> noteBasicReturns = new ArrayList<>();
        listPage.forEach(note -> {
            NoteBasicReturn noteBasicReturn = new NoteBasicReturn(note);
            UserObj headerUserObj = appUserService.getUserInfo(note.getHeaderEmail());
            noteBasicReturn.setHeaderEmailUserObj(headerUserObj);
            ArrayList<UserObj> authorsUserObj = new ArrayList<>();
            for (String email : note.getAuthorEmail()) {
                UserObj authorUserObj = appUserService.getUserInfo(email);
                authorsUserObj.add(authorUserObj);
            }
            noteBasicReturn.setAuthorEmailUserObj(authorsUserObj);
            noteBasicReturns.add(noteBasicReturn);
        });
        Page page =new PageImpl<>(noteBasicReturns);
        return new Pages(page.getContent(),(int)getTotalPage(pageSize));
    }
}
