package ntou.notesharedevbackend.notificationModule.controller;

import ntou.notesharedevbackend.notificationModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.*;
import org.springframework.web.socket.messaging.*;

import java.util.*;

@Controller
public class ChatController {

    Map<String, String> dict = new HashMap<>();
    Map<String, String> noteIdDict = new HashMap<>();
    Map<String, List<String>> dictionary = new HashMap<>();
    Map<String, String> nameDict = new HashMap<>();
    Map<String, Integer> localTS = new HashMap<>();
    ControllerState controllerState = ControllerState.LISTENING;
    //int localTS = 0;
    List<String> opHistory = new ArrayList<String>();
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /*
    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    */
    @MessageMapping("/chat.register/{noteId}")
    @SendTo("/topic/public/{noteId}")
    public ChatMessage register(@Header("simpSessionId") String sessionId, @Payload ChatMessage CtoS_msg, SimpMessageHeaderAccessor headerAccessor) {
        String sender = CtoS_msg.getSenderEmail();
        String noteId = CtoS_msg.getNoteId();
        //headerAccessor.getSessionAttributes().put("username", sender);
        headerAccessor.getSessionAttributes().put("email", sender);
        //System.out.println(headerAccessor);

        // map sessionId and noteId
        dict.put(sessionId, sender);
        noteIdDict.put(sessionId, noteId);
        nameDict.put(sender, CtoS_msg.getSenderName());
        // if the collaboration note is exist
        if(dictionary.containsKey(noteId)){
            // get record
            List<String> queue = dictionary.get(noteId);
            // add sender(newcomer) to the specific collaboration note's queue
            queue.add(sender);
            String queueStr = String.join(" ", queue);
            // convert to string and put into message
            CtoS_msg.setQueue(queueStr);
            CtoS_msg.setOp(Integer.toString(queue.size()));
        }
        // not exist
        else{
            // new a queue
            List<String> queue = new ArrayList<>();
            // add sender(newcomer) to the specific collaboration note's queue
            queue.add(sender);
            // record the queue
            dictionary.put(noteId,queue);
            localTS.put(noteId, 0);
            String queueStr = String.join(" ", queue);
            // convert to string and put into message
            CtoS_msg.setQueue(queueStr);
            CtoS_msg.setOp("1");
        }
        System.out.println(dictionary);

        CtoS_msg.setSessionId(sessionId);


        return CtoS_msg;
    }


    @MessageMapping("/chat.send/{noteId}")
    public void sendMessage(@Header("simpSessionId") String sessionId, @Payload ChatMessage CtoS_msg) throws InterruptedException {

        // print recent state
        System.out.println(controllerState);
        String sender = CtoS_msg.getSenderEmail();
        String senderName = CtoS_msg.getSenderName();
        String noteId = CtoS_msg.getNoteId();
        ChatMessage.MessageType type = CtoS_msg.getType();
        if(type== ChatMessage.MessageType.LEAVE ){

            System.out.println(type);
            List<String> queue = dictionary.get(noteId);
            dict.remove(sessionId);
            noteIdDict.remove(sessionId);
            nameDict.remove(sender);
            queue.remove(sender);
            if(queue.size()==0){
                localTS.put(noteId, 0);
                //opHistory.clear();
            }

            // send leave message with new queue to all user in collaboration note
            ChatMessage StoC_msg = new ChatMessage(senderName, sender,  sessionId , ChatMessage.MessageType.LEAVE, 0, "", "", String.join(" ", queue), "");
            for (String user : queue) {
                if( !sender.equals(user)){
                    simpMessagingTemplate.convertAndSendToUser(user, "/msg", StoC_msg);
                }
            }
            return;
        }
        int remoteTS = CtoS_msg.getTS();
        String remoteOp = CtoS_msg.getOp();
        System.out.println("op: " + remoteOp);
        // skip the OP
         System.out.println("remoteTS: " + remoteTS + ",  localTS: " + localTS.get(noteId));
        if(remoteTS <= localTS.get(noteId) && CtoS_msg.getType() != ChatMessage.MessageType.COPY){
            System.out.println("keep listening!!");
            return;
        }
        // handle this OP
        else{
            if(controllerState == ControllerState.LISTENING){
                // start handle this OP
                System.out.println("converting");
                controllerState = ControllerState.PROCESSING;
                /*try{
                    Thread.sleep(1500);
                }
                catch (InterruptedException e){}*/

                /***** PersistingNew *****/
                // step 1: set localTS to the value from the received CtoS Msg event
                localTS.put(noteId, remoteTS);
                // step 2: persist the Op as part of the official document history
                //opHistory.add(remoteOp);

                // copy OP
                if(CtoS_msg.getType() == ChatMessage.MessageType.COPY){
                    simpMessagingTemplate.convertAndSendToUser(CtoS_msg.getNewcomer(), "/msg", CtoS_msg);
                }
                // other OP
                else{
                    /***** SendingToRemainingClients *****/
                    CtoS_msg.setTS(localTS.get(noteId));
                    CtoS_msg.setSenderEmail(sender);
                    for (String user : dictionary.get(noteId)) {
                        if( !sender.equals(user)){
                            simpMessagingTemplate.convertAndSendToUser(user, "/msg", CtoS_msg);
                        }
                    }
                }

                /***** SendingACKToClient *****/
                // step 1: send StoC ACK event to the clientID of the accepted Op
                CtoS_msg.setType(ChatMessage.MessageType.ACK);
                CtoS_msg.setSenderEmail("Controller");
                simpMessagingTemplate.convertAndSendToUser(sender, "/msg", CtoS_msg);

                //finish
                controllerState = ControllerState.LISTENING;
            }
            // do nothing
            else{
                return;
            }
        }
        //dealMsg(sessionId, CtoS_msg);
    }

    synchronized public void dealMsg(String sessionId, ChatMessage CtoS_msg){

    }
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {

            // get user's sessionId
            String sessionId = event.getSessionId();
            // get username(email) from dict
            String leaver = dict.get(sessionId);
            String leaverName = nameDict.get(leaver);
            // get noteId from noteIdDict
            String noteId = noteIdDict.get(sessionId);
            System.out.print(sessionId + " " + leaver + " " + noteId);
            // get record
            List<String> queue = dictionary.get(noteId);
            // remove data from dictionaries
            dict.remove(sessionId);
            noteIdDict.remove(sessionId);
            nameDict.remove(leaver);
            // remove the user from the specific collaboration note's queue
            queue.remove(leaver);
            System.out.print("queue" + queue);

            // check size
            if(queue.size()==0){
                // reset the localTS
                localTS.put(noteId, 0);
                //opHistory.clear();
            }

            // send leave message with new queue to all user in collaboration note
            ChatMessage StoC_msg = new ChatMessage(leaverName, leaver,  sessionId , ChatMessage.MessageType.LEAVE, 0, "", "", String.join(" ", queue), "");
            for (String user : queue) {
                if( !leaver.equals(user)){
                    simpMessagingTemplate.convertAndSendToUser(user, "/msg", StoC_msg);
                }
            }

    }

}
