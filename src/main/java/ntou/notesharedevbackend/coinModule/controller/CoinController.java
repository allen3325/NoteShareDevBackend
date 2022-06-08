package ntou.notesharedevbackend.coinModule.controller;

import ntou.notesharedevbackend.coinModule.entity.Coin;
import ntou.notesharedevbackend.coinModule.service.CoinService;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/coin",produces = MediaType.APPLICATION_JSON_VALUE)
//TODO: buy note. Buyer update.
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @PutMapping("/{email}")
    public ResponseEntity changeCoin(@PathVariable String email, @RequestBody Coin request){
        AppUser user = coinService.changeCoin(email,request);
        if(user==null){
            return ResponseEntity.status(412).body("money is not enough.");
        }
        return ResponseEntity.status(201).body(user);
    }

    @PutMapping("/note/{email}/{noteID}")
    public ResponseEntity<Object> buyNote(@PathVariable String email,@PathVariable String noteID){
        Note note = coinService.buyNote(email,noteID);
        HashMap<String,Object> res = new HashMap<>();
        if(note == null){
            res.put("msg","money is not enough or bought the note.");
            return ResponseEntity.status(412).body(res);
        }else{
            res.put("note",note);
            return ResponseEntity.ok(res);
        }
    }
}
