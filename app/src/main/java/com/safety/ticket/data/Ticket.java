package com.safety.ticket.data;

import java.util.ArrayList;
import java.util.List;

public class Ticket {
    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_APPROVING = 2;
    public static final int STATUS_APPROVED = 3;
    public static final int STATUS_REJECTED = 4;
    public static final int STATUS_COMPLETED = 5;

    public static final String TYPE_HEIGHT = "height";
    public static final String TYPE_SPACE = "space";
    public static final String TYPE_FIRE = "fire";

    private long id;
    private String ticketType;
    private String ticketNo;
    private String applyUnit;
    private String applyTime;
    private String location;
    private String content;
    private String riskAnalysis;
    private String workTimeStart;
    private String workTimeEnd;
    private int status;
    private String createTime;
    private String updateTime;

    private List<SafetyMeasure> measures = new ArrayList<>();
    private List<Signature> signatures = new ArrayList<>();
    private List<Photo> photos = new ArrayList<>();

    public String getStatusText() {
        switch (status) {
            case STATUS_DRAFT: return "草稿";
            case STATUS_PENDING: return "待提交";
            case STATUS_APPROVING: return "审批中";
            case STATUS_APPROVED: return "已通过";
            case STATUS_REJECTED: return "已驳回";
            case STATUS_COMPLETED: return "已完工";
            default: return "未知";
        }
    }

    public String getTypeText() {
        switch (ticketType) {
            case TYPE_HEIGHT: return "高处作业票";
            case TYPE_SPACE: return "有限空间作业票";
            case TYPE_FIRE: return "动火作业票";
            default: return "未知";
        }
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }
    public String getTicketNo() { return ticketNo; }
    public void setTicketNo(String ticketNo) { this.ticketNo = ticketNo; }
    public String getApplyUnit() { return applyUnit; }
    public void setApplyUnit(String applyUnit) { this.applyUnit = applyUnit; }
    public String getApplyTime() { return applyTime; }
    public void setApplyTime(String applyTime) { this.applyTime = applyTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getRiskAnalysis() { return riskAnalysis; }
    public void setRiskAnalysis(String riskAnalysis) { this.riskAnalysis = riskAnalysis; }
    public String getWorkTimeStart() { return workTimeStart; }
    public void setWorkTimeStart(String workTimeStart) { this.workTimeStart = workTimeStart; }
    public String getWorkTimeEnd() { return workTimeEnd; }
    public void setWorkTimeEnd(String workTimeEnd) { this.workTimeEnd = workTimeEnd; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
    public List<SafetyMeasure> getMeasures() { return measures; }
    public void setMeasures(List<SafetyMeasure> measures) { this.measures = measures; }
    public List<Signature> getSignatures() { return signatures; }
    public void setSignatures(List<Signature> signatures) { this.signatures = signatures; }
    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }
}
