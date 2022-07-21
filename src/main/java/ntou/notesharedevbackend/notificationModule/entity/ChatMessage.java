package ntou.notesharedevbackend.notificationModule.entity;

public class ChatMessage {

    private String senderEmail;
    private String senderName;
    private String sessionId;
    private MessageType type;
    private int ts;
    private String op;
    private String newcomer;
    private String queue;
    private String noteId;

    public enum MessageType {
        OP, LEAVE, JOIN, ACK, COPY
    }

    public ChatMessage(String senderName, String senderEmail, String sessionId, MessageType type, int ts, String op, String newcomer, String queue, String noteId){
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.sessionId = sessionId;
        this.type = type;
        this.ts = ts;
        this.op = op;
        this.newcomer = newcomer;
        this.queue = queue;
        this.noteId = noteId;
    }


    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public int getTS() {
        return ts;
    }

    public void setTS(int ts) {
        this.ts = ts;
    }

    public String getNewcomer() {
        return newcomer;
    }

    public void setNewcomer(String newcomer) {
        this.newcomer = newcomer;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    @Override
    public String toString(){
        return String.format("op: %s", op);
    }
}

