package pl.bajtas.squaremoose.api.domain;

/**
 * Created by Bajtas on 26.11.2016.
 */
public class Email {
    private String sender;
    private String receiver;
    private String subject;
    private String text;

    public Email() {

    }

    public Email(String sender, String receiver, String subject, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {this.sender = sender; };

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
