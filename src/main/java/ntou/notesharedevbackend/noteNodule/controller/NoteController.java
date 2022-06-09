package ntou.notesharedevbackend.noteNodule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/note",produces = MediaType.APPLICATION_JSON_VALUE)
//TODO: 缺少like筆記api -> update Liker
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Operation(summary = "get a whole note by noteID")
    @GetMapping("/{noteID}")
    public ResponseEntity<Object> getNoteById(@PathVariable("noteID") String id){
        Note note = noteService.getNote(id);
        Map<String,Object> res = new HashMap<>();

        res.put("res",note);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get a note content by note version")
    @GetMapping("/{noteID}/{version}")
    public ResponseEntity<Object> getNoteVersion(@PathVariable("noteID") String id, @PathVariable("version") int version) {

        VersionContent versionContent = noteService.getNoteVersion(id, version);

        Map<String,Object> res = new HashMap<>();
        res.put("res",versionContent);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get note's tags by noteID")
    @GetMapping("/tags/{noteID}")
    public ResponseEntity<Object> getNoteTags(@PathVariable("noteID") String id) {
        ArrayList<String> tags = noteService.getNoteTags(id);
        HashMap<String,Object> map = new HashMap<>();
        map.put("tags",tags);
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "create a note under the user's folder")
    @PostMapping("/{email}/{folderID}")
    public ResponseEntity<Object> createNote(@PathVariable("email") String email,
                                           @PathVariable("folderID") String folderID,
                                           @RequestBody Note request) {

        Map<String,Object> res = new HashMap<>();
        Note note = noteService.createNote(request,email);
        Folder folder = noteService.copyNoteToFolder(note.getId(), folderID);

        res.put("res",note);

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "update whole note.",description = "body should be complete full and correct.")
    @PutMapping("/{id}")
    public ResponseEntity<Object> saveNote(@PathVariable("id") String id,@RequestBody Note request){
        Map<String,Object> res = new HashMap<>();
        Note note = noteService.replaceNote(request,id);

        res.put("res",note);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "save note content by version 0~5")
    @PutMapping("/{noteID}/{version}")
    public ResponseEntity<Object> updateNoteContent(@RequestBody VersionContent versionContent,@PathVariable("noteID")String id,
                                            @PathVariable(
            "version")int version){
        Map<String,Object> res = new HashMap<>();

        Note note = noteService.updateNoteVersion(id,version,versionContent);

        res.put("res",note.getVersion().get(version));

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "set manager in the collaboration note.")
    @PutMapping("/admin/{noteID}/{email}")
    public ResponseEntity<Object> setManager(@PathVariable("noteID") String noteID, @PathVariable("email") String email) {
        noteService.setManager(noteID, email);
        Map<String,String> res = new HashMap<>();
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "kick someone from collaboration note.")
    @PutMapping("/kick/{noteID}/{email}")
    public ResponseEntity<Object> kickUserFromCollaboration(@PathVariable("noteID") String noteID, @PathVariable("email") String email) {
        noteService.kickUserFromCollaboration(noteID, email);
        Map<String,String> res = new HashMap<>();
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "copy note to folder.")
    @PutMapping("/save/{noteID}/{folderID}")
    public ResponseEntity<Object> copyNoteToFolder(@PathVariable("noteID") String noteID,
                                                   @PathVariable("folderID") String folderID){
        Map<String,Object> res = new HashMap<>();

        Folder folder = noteService.copyNoteToFolder(noteID,folderID);
        res.put("res",folder);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "delete note from folder.")
    @PutMapping("/delete/{noteID}/{folderID}")
    public ResponseEntity<Object> deleteNoteToFolder(@PathVariable("noteID") String noteID,
                                                   @PathVariable("folderID") String folderID){
        Map<String,Object> res = new HashMap<>();
        Folder folder = noteService.deleteNoteFromFolder(noteID,folderID);

        if(folder == null){
            res.put("msg","This folder hasn't contains the note.");
            return ResponseEntity.status(204).body(res);
        }

        res.put("res",folder);
        return ResponseEntity.ok(res);
    }
}
