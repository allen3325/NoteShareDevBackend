package ntou.notesharedevbackend.coinModule.service;

import ntou.notesharedevbackend.coinModule.entity.Coin;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import ntou.notesharedevbackend.noteModule.entity.Note;
import ntou.notesharedevbackend.noteModule.entity.NoteReturn;
import ntou.notesharedevbackend.noteModule.service.NoteService;
import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.notificationModule.service.*;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CoinService {

    private final NoteService noteService;
    private final AppUserService appUserService;
    private final FolderService folderService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public CoinService(AppUserService appUserService, NoteService noteService, FolderService folderService, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.appUserService = appUserService;
        this.noteService = noteService;
        this.folderService = folderService;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
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

        MessageReturn messageReturn = new MessageReturn();
        if (newCoin < 0)
            messageReturn.setMessage("你已花" + (-newCoin) + "點");
        else
            messageReturn.setMessage("你已獲得" + newCoin + "點");
        UserObj userObj = new UserObj();
        userObj.setUserObjEmail("noteshare@gmail.com");
        userObj.setUserObjName("NoteShare System");
        userObj.setUserObjAvatar("https://i.imgur.com/5V1waq3.png");
        messageReturn.setUserObj(userObj);
//        messageReturn.setType("collaboration");
//        messageReturn.setId(postID);
        messageReturn.setDate(new Date());
        messagingTemplate.convertAndSendToUser(email, "/topic/private-messages", messageReturn);
        notificationService.saveNotificationPrivate(email, messageReturn);

        return appUser;
    }

    public Note buyNote(String email, String noteID) {
        AppUser buyer = appUserService.getUserByEmail(email);
        Note note = noteService.getNote(noteID);
        Integer price = note.getPrice();
        Integer buyersCoin = buyer.getCoin();
        if (price > buyersCoin) {
            return null;
        } else {
            // update buyer's coin
            buyer.setCoin(buyersCoin - price);
            // update note's author's coin
            ArrayList<String> authorEmails = note.getAuthorEmail();
            for (String authorEmail : authorEmails) {
                AppUser noteAuthor = appUserService.getUserByEmail(authorEmail);
                Integer noteAuthorCoin = noteAuthor.getCoin();
                noteAuthor.setCoin(noteAuthorCoin + price);
                appUserService.replaceUser(noteAuthor);

                //通知作者有人購買筆記
                MessageReturn messageReturn = notificationService.getMessageReturn(email, "向你購買了筆記", "note", noteID);
                messagingTemplate.convertAndSendToUser(authorEmail, "/topic/private-messages", messageReturn);
                notificationService.saveNotificationPrivate(authorEmail, messageReturn);
            }
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
            noteService.replaceNote(note, note.getId()); // update unlockCount in replace.
            return noteService.getNote(noteID);
        }
    }

    public NoteReturn getNoteUserinfo(Note note) {
        NoteReturn noteReturn = noteService.getUserinfo(note);
        return noteReturn;
    }

    public AppUserReturn getUserInfo(AppUser appUser) {
        return appUserService.getAppUserInfo(appUser);
    }
}
