package ntou.notesharedevbackend.noteNodule.controller;

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
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping("/{noteID}")
    public ResponseEntity<Note> getNoteById(@PathVariable("noteID") String id){
        Note note = noteService.getNote(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{noteID}")
                .buildAndExpand(note.getId())
                .toUri();

        return ResponseEntity.created(location).body(note);
    }

    @GetMapping("/{noteID}/{version}")
    public ResponseEntity<VersionContent> getNoteVersion(@PathVariable("noteID") String id, @PathVariable("version") int version) {
        VersionContent versionContent = noteService.getNoteVersion(id, version);
        return ResponseEntity.ok(versionContent);
    }

    @GetMapping("/tags/{noteID}")
    public ResponseEntity<ArrayList<String>> getNoteTags(@PathVariable("noteID") String id) {
        ArrayList<String> tags = noteService.getNoteTags(id);
        return ResponseEntity.ok(tags);
    }

    @PostMapping("/{email}")
    public ResponseEntity<Note> createPost(@PathVariable("email") String email ,@RequestBody Note request) {
        Note note = noteService.createNote(request,email);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{noteID}")
                .buildAndExpand(note.getId())
                .toUri();

        return ResponseEntity.created(location).body(note);
    }

    @PutMapping("/{noteID}/{version}")
    public ResponseEntity updateNoteContent(@RequestBody VersionContent versionContent,@PathVariable("noteID")String id,
                                            @PathVariable(
            "version")int version){
        Note res = noteService.updateNoteVersion(id,version,versionContent);

        return ResponseEntity.ok(res.getVersion().get(version));
    }

    @PutMapping("/admin/{noteID}/{email}")
    public ResponseEntity setManager(@PathVariable("noteID") String noteID, @PathVariable("email") String email) {
        noteService.setManager(noteID, email);
        Map<String,String> res = new HashMap<>();
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/kick/{noteID}/{email}")
    public ResponseEntity kickUserFromCollaboration(@PathVariable("noteID") String noteID, @PathVariable("email") String email) {
        noteService.kickUserFromCollaboration(noteID, email);
        Map<String,String> res = new HashMap<>();
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }
}
