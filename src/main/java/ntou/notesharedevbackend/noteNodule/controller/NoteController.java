package ntou.notesharedevbackend.noteNodule.controller;

import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    @PostMapping("/{email}")
    public ResponseEntity<Note> createPost(@PathVariable("email") String email ,@RequestBody Note request) {
        Note note = noteService.createNote(request,email);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postID}")
                .buildAndExpand(note.getId())
                .toUri();

        return ResponseEntity.created(location).body(note);
    }

    @PutMapping("/admin/{noteID}/{email}")
    public ResponseEntity setManager(@PathVariable("noteID") String noteID, @PathVariable("email") String email) {
        noteService.setManager(noteID, email);
        return ResponseEntity.ok().build();
    }
}
