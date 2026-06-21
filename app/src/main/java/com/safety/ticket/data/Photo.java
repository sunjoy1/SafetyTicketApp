package com.safety.ticket.data;

public class Photo {
    private long id;
    private long ticketId;
    private String photoPath;
    private String photoDesc;
    private String takeTime;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTicketId() { return ticketId; }
    public void setTicketId(long ticketId) { this.ticketId = ticketId; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public String getPhotoDesc() { return photoDesc; }
    public void setPhotoDesc(String photoDesc) { this.photoDesc = photoDesc; }
    public String getTakeTime() { return takeTime; }
    public void setTakeTime(String takeTime) { this.takeTime = takeTime; }
}
