package com.safety.ticket.data;

public class SafetyMeasure {
    public static final int INVOLVED_NO = 0;
    public static final int INVOLVED_YES = 1;
    public static final int INVOLVED_NA = 2;

    private long id;
    private long ticketId;
    private int measureNo;
    private String measureContent;
    private int isInvolved;
    private String confirmPerson;

    public String getInvolvedText() {
        switch (isInvolved) {
            case INVOLVED_NO: return "否";
            case INVOLVED_YES: return "是";
            case INVOLVED_NA: return "不涉及";
            default: return "";
        }
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTicketId() { return ticketId; }
    public void setTicketId(long ticketId) { this.ticketId = ticketId; }
    public int getMeasureNo() { return measureNo; }
    public void setMeasureNo(int measureNo) { this.measureNo = measureNo; }
    public String getMeasureContent() { return measureContent; }
    public void setMeasureContent(String measureContent) { this.measureContent = measureContent; }
    public int getIsInvolved() { return isInvolved; }
    public void setIsInvolved(int isInvolved) { this.isInvolved = isInvolved; }
    public String getConfirmPerson() { return confirmPerson; }
    public void setConfirmPerson(String confirmPerson) { this.confirmPerson = confirmPerson; }
}
