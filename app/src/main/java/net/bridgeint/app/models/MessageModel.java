package net.bridgeint.app.models;

public class MessageModel {

    private int id;
    private String text = null;
    private int fileType;
    private String filePath = null;
    private String time;
    private String tempId;
    private int message_status;
    private int isRead;
    private boolean isSenderMessage;

    public MessageModel(int id, String text, String time, int message_status, int fileType, String filePath, String tempId, boolean isSenderMessage, int isread) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.message_status = message_status;
        this.isSenderMessage = isSenderMessage;
        this.fileType = fileType;
        this.tempId = tempId;
        this.filePath = filePath;
        this.isRead = isread;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSenderMessage() {
        return isSenderMessage;
    }

    public void setSenderMessage(boolean senderMessage) {
        isSenderMessage = senderMessage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMessage_status() {
        return message_status;
    }

    public void setMessage_status(int message_status) {
        this.message_status = message_status;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }
}