package ntou.notesharedevbackend.noteNodule.service;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private AppUserService appUserService;

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
        if(oldVersionContent.size() > version+1) {
            oldVersionContent.set(version, newVersionContent);
        }else{
            oldVersionContent.add(newVersionContent);
        }

        return noteRepository.save(note);
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

        note.setName(request.getName());
        note.setType(request.getType());
        note.setSlug(request.getSlug());
        note.setCreatedAt(request.getCreatedAt());
        note.setUpdatedAt(request.getUpdatedAt());
        note.set__v(request.get__v());
        note.setDepartment(request.getDepartment());
        note.setSubject(request.getSubject());
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
        note.setVersion(new ArrayList<VersionContent>());
        note.setContributors(new ArrayList<String>());
        note.setPostID(request.getPostID());
        note.setBest(request.getBest());

        return noteRepository.insert(note);
    }

    // TODO: 存到對應的post裡的email（為管理員以及發起者，具有權限管理貼文）裡
    public void setManager(String noteID, String email) {
        Note note = getNote(noteID);
        note.setManagerEmail(email);
        noteRepository.save(note);
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

        noteRepository.save(note);
    }

    public void publishOrSubmit(String noteID){
        Note note = getNote(noteID);
        if(note.getType().equals("reward")){
            if(!note.getSubmit()) {
                note.setSubmit(true);
            }
        }else{
            if(!note.getPublic()){
                note.setPublic(true);
            }else{
                note.setPublic(false);
            }
        }
        noteRepository.save(note);
    }

    public void rewardNoteBestAnswer(String noteID,String email){
        Note note = getNote(noteID);
        note.setBest(true);
        //TODO 新增點數
        //TODO 位置，筆記移轉給懸賞人 移除投稿人擁有權
        note.getAuthorEmail().add(email);
        String userName = appUserService.getUserByEmail(email).getName();
        note.getAuthorName().add(userName);
        noteRepository.save(note);
    }

    public void collaborationNoteSetPostID(String noteID, String postID){
        Note note = getNote(noteID);
        note.setPostID(postID);
        noteRepository.save(note);
    }
}
