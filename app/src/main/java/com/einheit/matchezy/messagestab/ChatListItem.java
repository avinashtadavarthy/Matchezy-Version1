package com.einheit.matchezy.messagestab;

public class ChatListItem {

    private String name;
    private String profilePic;
    private String lastMessage;
    private long messageTime;
    private boolean read;
    private String userData;

    public ChatListItem() {

    }

    public ChatListItem(String name, String profilePic, String lastMessage,
                        long messageTime, boolean read, String userData) {
        this.name = name;
        this.profilePic = profilePic;
        this.lastMessage = lastMessage;
        this.messageTime = messageTime;
        this.read = read;
        this.userData = userData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getUserData() {
        return userData;
    }
}
