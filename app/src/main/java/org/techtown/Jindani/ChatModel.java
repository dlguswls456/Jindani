package org.techtown.Jindani;

//채팅에 사용되는 클래스
class ChatModel {
    String chat;//채팅 내용
    boolean isBot;//발화자 구분


    public ChatModel(String chat, boolean isBot) {
        this.chat = chat;
        this.isBot = isBot;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }
}
