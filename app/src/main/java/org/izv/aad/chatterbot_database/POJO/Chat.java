package org.izv.aad.chatterbot_database.POJO;

public class Chat {

    private String name;
    private String conversation;

    public Chat(String name, String conversation) {
        this.name = name;
        this.conversation = conversation;
    }

    public String getName() {
        return name;
    }

    public String getConversation() {
        return conversation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }
}
