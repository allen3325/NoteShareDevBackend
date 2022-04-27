package ntou.notesharedevbackend.noteNodule.service;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Note getNote(String id){
        return noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find note."));
    }

    public Note createNote(Note request,String email){
        //TODO 建立完要更新 User Schema
        Note note = new Note();
        System.out.println("email is "+email);
        note.setType(request.getType());
        note.setDepartment(request.getDepartment());
        note.setSubject(request.getSubject());
        note.setTitle(request.getTitle());
        //TODO HeaderEmail 應從URL拿？
        note.setHeaderEmail(request.getHeaderEmail());
        note.setHeaderName(request.getHeaderName());
        note.setAuthorEmail(request.getAuthorEmail());
        note.setAuthorName(request.getAuthorName());
        note.setManagerEmail(request.getManagerEmail());
        note.setProfessor(request.getProfessor());
        note.setSchool(request.getSchool());
        // TODO exitFolder 有必要？
//        note.setExitFolders(request.getExitFolders());
        note.setLikeCount(request.getLikeCount());
        note.setFavoriteCount(request.getFavoriteCount());
        note.setUnlockCount(request.getUnlockCount());
        note.setDownloadable(request.getDownloadable());
        note.setCommentCount(request.getCommentCount());
        note.setComments(request.getComments());
        note.setPrice(request.getPrice());
        note.setPublic(request.getPublic());
        note.setSubmit(request.getSubmit());
        note.setQuotable(request.getQuotable());
        note.setTag(request.getTag());
        note.setHiddenTag(request.getHiddenTag());
        note.setVersionToSave(request.getVersionToSave());
        note.setVersion(request.getVersion());
        note.setContributors(request.getContributors());
        note.setPostID(request.getPostID());
        note.setBest(request.getBest());
        note.setFavorite(request.getFavorite());

        return noteRepository.insert(note);
    }

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
}
