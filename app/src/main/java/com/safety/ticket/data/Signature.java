package com.safety.ticket.data;

public class Signature {
    public static final String TYPE_CREATOR = "creator";
    public static final String TYPE_SUPERVISOR = "supervisor";
    public static final String TYPE_WORKER = "worker";
    public static final String TYPE_APPROVER1 = "approver1"; // 作业负责人
    public static final String TYPE_APPROVER2 = "approver2"; // 所在单位/车间
    public static final String TYPE_APPROVER3 = "approver3"; // 安全管理部门
    public static final String TYPE_APPROVER4 = "approver4"; // 厂领导/总工程师
    public static final String TYPE_VERIFIER = "verifier"; // 动火前验票
    public static final String TYPE_FINAL = "final"; // 完工验收

    private long id;
    private long ticketId;
    private String signType;
    private String signPerson;
    private String signTime;
    private byte[] signImage;
    private String signComment;

    public String getTypeText() {
        switch (signType) {
            case TYPE_CREATOR: return "申请人";
            case TYPE_SUPERVISOR: return "监护人";
            case TYPE_WORKER: return "作业人";
            case TYPE_APPROVER1: return "作业负责人意见";
            case TYPE_APPROVER2: return "所在单位意见";
            case TYPE_APPROVER3: return "安全管理部门意见";
            case TYPE_APPROVER4: return "审批部门意见";
            case TYPE_VERIFIER: return "动火前验票";
            case TYPE_FINAL: return "完工验收";
            default: return signType;
        }
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTicketId() { return ticketId; }
    public void setTicketId(long ticketId) { this.ticketId = ticketId; }
    public String getSignType() { return signType; }
    public void setSignType(String signType) { this.signType = signType; }
    public String getSignPerson() { return signPerson; }
    public void setSignPerson(String signPerson) { this.signPerson = signPerson; }
    public String getSignTime() { return signTime; }
    public void setSignTime(String signTime) { this.signTime = signTime; }
    public byte[] getSignImage() { return signImage; }
    public void setSignImage(byte[] signImage) { this.signImage = signImage; }
    public String getSignComment() { return signComment; }
    public void setSignComment(String signComment) { this.signComment = signComment; }
}
