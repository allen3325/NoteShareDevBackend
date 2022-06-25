package ntou.notesharedevbackend.noteNodule.entity;

public class NoteFolderReturn {
    // field
    private String id;
    private String subject;
    private String title; // title
    private String description;
    // constructor
    public NoteFolderReturn(Note note) {
        this.id = note.getId();
        this.subject = note.getSubject();
        this.title = note.getTitle();
        this.description = note.getDescription();
    }
    // getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
