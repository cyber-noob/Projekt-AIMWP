package org.aj.application.records;

public record Mail(String sender, String subject, String snippet, String date) {
    public Mail(String sender, String subject, String snippet, String date) {
        this.sender = sender;
        this.subject = subject;
        this.snippet = snippet;
        this.date = date;
    }
}
