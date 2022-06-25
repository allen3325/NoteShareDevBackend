package ntou.notesharedevbackend.coinModule.service;

import ntou.notesharedevbackend.coinModule.entity.Coin;
import ntou.notesharedevbackend.exception.BadRequestException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CoinService {

    private final NoteService noteService;
    private final AppUserService appUserService;
    private final FolderService folderService;

    public CoinService(AppUserService appUserService, NoteService noteService, FolderService folderService) {
        this.appUserService = appUserService;
        this.noteService = noteService;
        this.folderService = folderService;
    }

    public AppUser changeCoin(String email, Coin request) {
        AppUser appUser = appUserService.getUserByEmail(email);
        Integer oldCoin = appUser.getCoin();
        String dealCoin = request.getCoin();
        Integer newCoin = Integer.valueOf(dealCoin.substring(1));
        if (dealCoin.charAt(0) == '-') {
            if (newCoin > oldCoin) {
                appUser.setCoin(0);
                appUserService.replaceUser(appUser);
                return null;
            }
            newCoin *= -1;
        }
        appUser.setCoin(oldCoin + newCoin);
        appUserService.replaceUser(appUser);
        return appUser;
    }

    public Note buyNote(String email, String noteID) {
        AppUser buyer = appUserService.getUserByEmail(email);
        AppUser notesAuthor = appUserService.getUserByEmail(email);
        Note note = noteService.getNote(noteID);
        Integer price = note.getPrice();
        Integer buyersCoin = buyer.getCoin();
        Integer noteAuthorCoin = notesAuthor.getCoin();
        if (price > buyersCoin) {
            return null;
        } else {
            // update buyer's coin
            buyer.setCoin(buyersCoin - price);
            // update note's author's coin
            notesAuthor.setCoin(noteAuthorCoin + price);
            appUserService.replaceUser(notesAuthor);
            // check does this user bought the note and update folder and buyer
            Folder buyFolder = folderService.getBuyFolderByUserEmail(email);
            ArrayList<String> folderNotes = buyFolder.getNotes();
            if (!folderNotes.contains(noteID)) {
                folderNotes.add(noteID);
                buyFolder.setNotes(folderNotes);
                folderService.replaceFolder(buyFolder);
                appUserService.replaceUser(buyer);
            } else {
                return null;
            }
            // update note's unlockCount and Buyer
            ArrayList<String> noteBuyerList = note.getBuyer();
            noteBuyerList.add(email);
            note.setBuyer(noteBuyerList);
            noteService.replaceNote(note,note.getId()); // update unlockCount in replace.

            return note;
        }
    }
}
