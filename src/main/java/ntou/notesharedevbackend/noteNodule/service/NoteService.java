package ntou.notesharedevbackend.noteNodule.service;

import ntou.notesharedevbackend.coinModule.entity.Coin;
import ntou.notesharedevbackend.coinModule.service.CoinService;
import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.commentModule.service.*;
import ntou.notesharedevbackend.exception.BadRequestException;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.entity.UserObj;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

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


    public Note getNote(String id){
        return noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find note."));
    }

    public VersionContent getNoteVersion(String id, int version) {
        Note note = getNote(id);
        return note.getVersion().get(version);
    }

    public Note updateNoteVersion(String id, int version,VersionContent newVersionContent){
        Note note = getNote(id);
        ArrayList<VersionContent> oldVersionContent = note.getVersion();
        if(oldVersionContent.size() > version) {
            oldVersionContent.set(version, newVersionContent);
        }else{
            oldVersionContent.add(newVersionContent);
        }

        note.setVersion(oldVersionContent);

        return replaceNote(note,note.getId());

//        return noteRepository.save(note);
    }

    public ArrayList<String> getNoteTags(String id) {
        Note note = getNote(id);
        return note.getTag();
    }

    public Note createNote(Note request,String email){
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
        if(request.getVersion() == null || request.getVersion().isEmpty()){
            note.setVersion(new ArrayList<VersionContent>());
        }else{
            note.setVersion(request.getVersion());
        }
        note.setContributors(new ArrayList<String>());
        note.setPostID(request.getPostID());
        note.setReference(request.getReference());
        note.setBest(request.getBest());
        note.setDescription("");

        return noteRepository.insert(note);
    }

    public void setManager(String noteID, String email) {
        Note note = getNote(noteID);
        note.setManagerEmail(email);
        replaceNote(note,note.getId());
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
        if(note.getManagerEmail()!= null && note.getManagerEmail().equals(email)){//檢查踢除人是否為管理員
            note.setManagerEmail(null);
        }
        replaceNote(note,note.getId());
        postService.kickUserFromCollaboration(note.getPostID(),email);
//        noteRepository.save(note);
    }

    public void publishOrSubmit(String noteID){
        Note note = getNote(noteID);
        if(note.getType().equals("reward")){
            if(!note.getSubmit()) {
                note.setSubmit(true);
            }
        }else{
            note.setPublic(!note.getPublic());
        }
        // update publish date
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
        note.setPublishDate(calendar.getTime());
        // update note
        replaceNote(note,note.getId());
//        noteRepository.save(note);
    }

    public void rewardNoteBestAnswer(String noteID,String email, String bestPrice){
        Note note = getNote(noteID);
        note.setBest(true);
        Coin bestAnswerCoin = new Coin();
        bestAnswerCoin.setCoin('+'+bestPrice);
        coinService.changeCoin(note.getAuthorEmail().get(0),bestAnswerCoin);
        String contributor = note.getAuthorEmail().get(0);//投稿人email
        note.getContributors().add(contributor);//將投稿人放入contributor
        //TODO 移除投稿人擁有權（投稿人可以看嗎？
        note.getAuthorEmail().add(email);
        String userName = appUserService.getUserByEmail(email).getName();
        note.getAuthorName().add(userName);
        note = replaceNote(note,note.getId());
        copyNoteToFolder(note.getId(), appUserService.getUserByEmail(email).getFolders().get(0));//筆記放入懸賞人的buy folder;
//        noteRepository.save(note);
    }

    public void rewardNoteReferenceAnswer(String noteID,String email, String referencePrice){
        Note note = getNote(noteID);
        note.setReference(true);
        Coin referenceAnswerCoin = new Coin();
        referenceAnswerCoin.setCoin('+'+referencePrice);
        coinService.changeCoin(note.getAuthorEmail().get(0),referenceAnswerCoin);//購買者增加點
        note.getAuthorEmail().add(email);
        String userName = appUserService.getUserByEmail(email).getName();
        note.getAuthorName().add(userName);
        replaceNote(note,note.getId());
//        noteRepository.save(note);
    }

    public void collaborationNoteSetPostID(String noteID, String postID){
        Note note = getNote(noteID);
        note.setPostID(postID);
        replaceNote(note,note.getId());
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

        return noteRepository.save(note);
    }

    public Folder copyNoteToFolder(String noteID, String folderID) {
        Folder folder =  folderService.getFolderByID(folderID);
        ArrayList<String> notes = folder.getNotes();

        notes.add(noteID);
        folder.setNotes(notes);
        folderService.replaceFolder(folder);

        return folder;
    }

    public Folder deleteNoteFromFolder(String noteID, String folderID) {
        Folder folder =  folderService.getFolderByID(folderID);
        ArrayList<String> notes = folder.getNotes();

        if(notes.contains(noteID)){
            notes.remove(noteID);
        }else{
            return null;
        }

        folder.setNotes(notes);
        folderService.replaceFolder(folder);

        return folder;
    }

    public Note changeDescription(String noteID, Note request) {
        Note note = getNote(noteID);
        if(request.getDescription() == null){
            throw new BadRequestException("Description should not be null!");
        }
        note.setDescription(request.getDescription());
        replaceNote(note,note.getId());
        return note;
    }

    public ArrayList<NoteFolderReturn> getUserAllNote(String email) {
        List<Note> tmp = noteRepository.findAllByHeaderEmail(email);
        ArrayList<NoteFolderReturn> res = new ArrayList<>();
        for(Note note:tmp){
            res.add(new NoteFolderReturn(note));
        }
        return res;
    }

    public boolean rewardNoteHaveAnswer (ArrayList<String> answers){
        for(String noteID : answers){
            if(getNote(noteID).getBest()!=null && getNote(noteID).getBest()){
                    return true;
            }
        }
        return false;
    }

    public NoteReturn getUserinfo(Note note){
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

        ArrayList<CommentReturn> commentReturnArrayList = new ArrayList<>();
        for(Comment comment : note.getComments()){
            CommentReturn commentReturn = commentService.getUserInfo(comment);
            commentReturnArrayList.add(commentReturn);
        }
        noteReturn.setComments(commentReturnArrayList);

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
        noteReturn.setHeaderUserObj(appUserService.getUserInfo(note.getHeaderEmail()));
        if(note.getManagerEmail()!=null){
            noteReturn.setManagerUserObj(appUserService.getUserInfo(note.getManagerEmail()));
        }
        ArrayList<UserObj> authorUserObj = new ArrayList<>();
        for(String authorEmail : note.getAuthorEmail()){
            UserObj userObj = appUserService.getUserInfo(authorEmail);
            authorUserObj.add(userObj);
        }
        noteReturn.setAuthorUserObj(authorUserObj);
        ArrayList<UserObj> likerUserObj = new ArrayList<>();
        for(String likerEmail : note.getLiker()){
            UserObj userObj = appUserService.getUserInfo(likerEmail);
            likerUserObj.add(userObj);
        }
        noteReturn.setLikerUserObj(likerUserObj);
        ArrayList<UserObj> buyerUserObj = new ArrayList<>();
        for(String buyerEmail : note.getBuyer()){
            UserObj userObj = appUserService.getUserInfo(buyerEmail);
            buyerUserObj.add(userObj);
        }
        noteReturn.setBuyerUserObj(buyerUserObj);
        ArrayList<UserObj> favoriterUserObj = new ArrayList<>();
        for(String favoriterEmail : note.getFavoriter()){
            UserObj userObj = appUserService.getUserInfo(favoriterEmail);
            favoriterUserObj.add(userObj);
        }
        noteReturn.setFavoriterUserObj(favoriterUserObj);
        ArrayList<UserObj> contributorUserObj = new ArrayList<>();
        for(String contributorEmail : note.getContributors()){
            UserObj userObj = appUserService.getUserInfo(contributorEmail);
            contributorUserObj.add(userObj);
        }
        noteReturn.setContributorUserObj(contributorUserObj);
        return noteReturn;
    }
}
