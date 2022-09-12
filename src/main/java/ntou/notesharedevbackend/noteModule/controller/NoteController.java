package ntou.notesharedevbackend.noteModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderReturn;
import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.noteModule.service.NoteService;
import ntou.notesharedevbackend.searchModule.entity.Pages;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.util.*;

@RestController
@RequestMapping(value = "/note", produces = MediaType.APPLICATION_JSON_VALUE)
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Operation(summary = "get a whole note by noteID")
    @GetMapping("/{noteID}")
    public ResponseEntity<Object> getNoteById(@PathVariable("noteID") String id) {
        Note note = noteService.getNote(id);
        noteService.updateClick(id);//更新點擊次數
        NoteReturn noteReturn = noteService.getUserinfo(note);
        Map<String, Object> res = new HashMap<>();

        res.put("res", noteReturn);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get a note content by note version", description = "version為0~5")
    @GetMapping("/{noteID}/{version}")
    public ResponseEntity<Object> getNoteVersion(@PathVariable("noteID") String id, @PathVariable("version") int version) {

        VersionContent versionContent = noteService.getNoteVersion(id, version);

        Map<String, Object> res = new HashMap<>();
        res.put("res", versionContent);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get note's tags by noteID")
    @GetMapping("/tags/{noteID}")
    public ResponseEntity<Object> getNoteTags(@PathVariable("noteID") String id) {
        ArrayList<String> tags = noteService.getNoteTags(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("tags", tags);
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "get user's all notes by email.")
    @GetMapping("/all/{email}")
    public ResponseEntity<Object> getUserAllNotes(@PathVariable("email") String email) {
        ArrayList<NoteFolderReturn> notes = noteService.getUserAllNote(email);
        HashMap<String, Object> res = new HashMap<>();

        res.put("res", notes);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "create a note under the user's folder")
    @PostMapping("/{email}/{folderID}")
    public ResponseEntity<Object> createNote(@PathVariable("email") String email,
                                             @PathVariable("folderID") String folderID,
                                             @RequestBody Note request) {

        Map<String, Object> res = new HashMap<>();
        Note note = noteService.createNote(request, email);
        Folder folder = noteService.copyNoteToFolder(note.getId(), folderID);
        NoteReturn noteReturn = noteService.getUserinfo(note);

        res.put("res", noteReturn);

        return ResponseEntity.status(201).body(res);
    }


    @Operation(summary = "create note doesn't has folder.")
    @PostMapping("/{email}")
    public ResponseEntity<Object> createNoteWithoutFolder(@PathVariable("email") String email,
                                                          @RequestBody Note request) {

        Map<String, Object> res = new HashMap<>();
        Note note = noteService.createNote(request, email);
        NoteReturn noteReturn = noteService.getUserinfo(note);

        res.put("res", noteReturn);

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "update whole note.", description = "body should be complete full and correct.")
    @PutMapping("/{id}")
    public ResponseEntity<Object> saveNote(@PathVariable("id") String id, @RequestBody Note request) {
        Map<String, Object> res = new HashMap<>();
        Note note = noteService.replaceNote(request, id);
        NoteReturn noteReturn = noteService.getUserinfo(note);

        res.put("res", noteReturn);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "save note content by version 0~5")
    @PutMapping("/{noteID}/{version}")
    public ResponseEntity<Object> updateNoteContent(@RequestBody VersionContent versionContent, @PathVariable("noteID") String id,
                                                    @PathVariable("version") int version) {
        Map<String, Object> res = new HashMap<>();

        if (version > 5) {
            res.put("res", "over the size.");
            return ResponseEntity.status(400).body(res);
        }

        Note note = noteService.updateNoteVersion(id, version, versionContent);

        res.put("res", note.getVersion().get(version));

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "modify the version's name.")
    @PutMapping("/{noteID}/{version}/{name}")
    public ResponseEntity<Object> updateNoteContentName(@PathVariable("noteID") String id,
                                                        @PathVariable("version") int version,
                                                        @PathVariable("name") String name) {
        Map<String, Object> res = new HashMap<>();

        Note note = noteService.updateNoteContentName(id, version, name);

        res.put("res", note.getVersion().get(version));

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "set manager in the collaboration note.", description = "email為要加為管理員的人")
    @PutMapping("/admin/{noteID}/{email}")
    public ResponseEntity<Object> setManager(@PathVariable("noteID") String noteID, @PathVariable("email") String email) {
        noteService.setManager(noteID, email);
        Map<String, String> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "kick someone from collaboration note.", description = "email為想要踢掉的人")
    @PutMapping("/kick/{noteID}/{email}")
    public ResponseEntity<Object> kickUserFromCollaboration(@PathVariable("noteID") String noteID, @PathVariable("email") String email) {
        noteService.kickUserFromCollaboration(noteID, email);
        Map<String, String> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "copy note to folder.", description = "第一個ID為noteID，第二個ID為folderID")
    @PutMapping("/save/{noteID}/{folderID}")
    public ResponseEntity<Object> copyNoteToFolder(@PathVariable("noteID") String noteID,
                                                   @PathVariable("folderID") String folderID) {
        Map<String, Object> res = new HashMap<>();

        Folder folder = noteService.copyNoteToFolder(noteID, folderID);
        FolderReturn folderReturn = noteService.folderGetUserInfo(folderID);
        res.put("res", folderReturn);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "modify the description.", description = "body傳入description")
    @PutMapping("/description/{noteID}")
    public ResponseEntity<Object> changeDescription(@PathVariable("noteID") String noteID, @RequestBody Note request) {
        Map<String, Object> res = new HashMap<>();
        Note note = noteService.changeDescription(noteID, request);
        NoteReturn noteReturn = noteService.getUserinfo(note);

        res.put("res", noteReturn);
        return ResponseEntity.ok(res);
    }

//    @Operation(summary = "delete note from folder.", description = "第一個ID為noteID，第二個ID為folderID")
//    @PutMapping("/delete/{noteID}/{folderID}")
//    public ResponseEntity<Object> deleteNoteToFolder(@PathVariable("noteID") String noteID,
//                                                     @PathVariable("folderID") String folderID) {
//        Map<String, Object> res = new HashMap<>();
//        Folder folder = noteService.deleteNoteFromFolder(noteID, folderID);
//
//        if (folder == null) {
//            res.put("msg", "This folder hasn't contains the note.");
//            return ResponseEntity.status(204).body(res);
//        }
//        FolderReturn folderReturn = noteService.folderGetUserInfo(folder.getId());
//        res.put("res", folderReturn);
//        return ResponseEntity.ok(res);
//    }

    //更改version isTemp 需要noteID versionID
    @Operation(summary = "modify version publish status", description = "第一個ID為noteID，第二個是version編號0-5")
    @PutMapping("/publish/{noteID}/{version}")
    public ResponseEntity<Object> modifyVersionStatus(@PathVariable("noteID") String noteID,
                                                      @PathVariable("version") Integer version) {
        VersionContent versionContent = noteService.modifyVersionStatus(noteID, version);

        Map<String, Object> res = new HashMap<>();
        res.put("res", versionContent);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "publish note for normal and collaboration")
    @PutMapping("/publish/{noteID}")
    public ResponseEntity<Object> publishNote(@PathVariable("noteID") String noteID) {
        noteService.publishNote(noteID);
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "submit reward note ", description = "提交懸賞筆記")
    @PutMapping("/submit/{noteID}")
    public ResponseEntity<Object> submitRewardNote(@PathVariable("noteID") String noteID) {
        noteService.submitRewardNote(noteID);
        Map<String, String> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "withdraw reward note ", description = "收回懸賞筆記")
    @PutMapping("/withdraw/{noteID}")
    public ResponseEntity<Object> withdrawRewardNote(@PathVariable("noteID") String noteID) {
        noteService.withdrawRewardNote(noteID);
        Map<String, String> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "delete note from folder.", description = "第一個ID為noteID，第二個ID為folderID")
    @PutMapping("/delete/{noteID}/{folderID}")
    public ResponseEntity<Object> removeNoteFromFolder(@PathVariable("noteID") String noteID, @PathVariable("folderID") String folderID) {
        Folder folder = noteService.removeNoteFromFolder(noteID, folderID);
        Map<String, Object> res = new HashMap<>();
        if (folder.getFolderName().equals("Owned")) {
            res.put("res", "Can't delete note which in Buy Folder");
            return ResponseEntity.status(406).body(res);
        } else if (folder.getNotes().contains(noteID)) {
            res.put("res", "Can't delete last note");
            return ResponseEntity.status(409).body(res);
        } else {//可移除
            res.put("res", noteService.folderGetUserInfo(folder.getId()));
            return ResponseEntity.ok(res);
        }
    }

    @Operation(summary = "check the note's plagiarism point and save to db.", description = "前端使用直接不用等回傳，好了會用socket" +
            "通知使用者")
    @GetMapping("/plagiarism/{noteID}")
    public void checkNotePlagiarismAndSave(@PathVariable("noteID") String noteID) {
        noteService.checkNotePlagiarismAndSave(noteID);
    }

    @Operation(summary = "get hot notes", description = "offset頁數，pageSize一頁顯示筆記的數量")
    @GetMapping("/hotNotes/{offset}/{pageSize}")
    public ResponseEntity<Object> getHotNote(@PathVariable("offset") int offset,
                                             @PathVariable("pageSize") int pageSize) {
        Pages noteBasicReturns = noteService.getHotNotes(offset, pageSize);
        Map<String, Object> res = new HashMap<>();
        res.put("res", noteBasicReturns);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "save collaboration note", description = "collaboration note's id and json request body -> { } 。 400 -> wrong type of request body")
    @PutMapping("/saveTempCollaborationNote/{noteID}")
    public ResponseEntity<Object> saveTempCollaborationNote(@PathVariable("noteID") String noteID,
                                                            @RequestBody String content) {
        Map<String, Object> res = new HashMap<>();
        try {
            new JSONObject(content);
        } catch (JSONException e) {
            res.put("res", "Wrong type of Request Body");
            return ResponseEntity.badRequest().body(res);
        }
        noteService.saveTempCollaborationNote(noteID, content);
        res.put("res", "success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "load collaboration note", description = "collaboration note's id")
    @GetMapping("/loadTempCollaborationNote/{noteID}")
    public ResponseEntity<Object> loadTempCollaborationNote(@PathVariable("noteID") String noteID) {
        String content = noteService.loadTempCollaborationNote(noteID);
        JSONObject jsonObject = new JSONObject(content);

        Map<String, Object> res = new HashMap<>();
        res.put("res", jsonObject.toMap());
        return ResponseEntity.ok(res);
    }
}
