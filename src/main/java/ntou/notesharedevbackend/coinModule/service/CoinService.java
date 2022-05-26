package ntou.notesharedevbackend.coinModule.service;

import ntou.notesharedevbackend.coinModule.entity.Coin;
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
                return null;
            }
            newCoin *= -1;
        }
        appUser.setCoin(oldCoin + newCoin);
        appUserService.replaceUser(appUser);
        return appUser;
    }

    public Note buyNote(String email, String noteID) {
        AppUser appUser = appUserService.getUserByEmail(email);
        Note note = noteService.getNote(noteID);
        Integer price = note.getPrice();
        Integer coin = appUser.getCoin();
        if (price > coin) {
            return null;
        } else {
            appUser.setCoin(coin - price);
            Folder buyFolder = folderService.getBuyFolderByUserEmail(email);
            ArrayList<String> folderNotes = buyFolder.getNotes();
            // check does this user bought the note
            if (!folderNotes.contains(noteID)) {
                folderNotes.add(noteID);
                buyFolder.setNotes(folderNotes);
                folderService.replaceFolder(buyFolder);
                appUserService.replaceUser(appUser);
            } else {
                return null;
            }
            return note;
        }
    }
}
