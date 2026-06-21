package com.safety.ticket.data;

import java.util.ArrayList;
import java.util.List;

public class TicketTemplate {

    public static List<SafetyMeasure> getHeightMeasures() {
        List<SafetyMeasure> list = new ArrayList<>();
        String[] measures = {
            "作业人员身体条件符合要求",
            "作业人员着装符合作业要求",
            "作业人员佩戴符合标准要求的安全帽、安全带，有可能散发有毒气体的场所携带正压式空气呼吸器或面罩备用",
            "作业人员携带有工具袋及安全绳",
            "现场搭设的脚手架、防护网、围栏符合安全规定",
            "垂直分层作业中间有隔离设施",
            "梯子、绳子符合安全规定",
            "轻型棚的承重梁、柱能承重作业过程最大负荷的要求",
            "作业人员在不承重物处作业所搭设的承重板稳定牢固",
            "采光、夜间作业照明符合作业要求",
            "30m以上高处作业时，作业人员已配备通信、联络工具",
            "作业现场四周已设警戒区",
            "露天作业，风力满足作业安全要求",
            "其他相关特殊作业已办理相应安全作业票",
            "其他安全措施："
        };
        for (int i = 0; i < measures.length; i++) {
            SafetyMeasure m = new SafetyMeasure();
            m.setMeasureNo(i + 1);
            m.setMeasureContent(measures[i]);
            m.setIsInvolved(SafetyMeasure.INVOLVED_NA);
            list.add(m);
        }
        return list;
    }

    public static List<SafetyMeasure> getSpaceMeasures() {
        List<SafetyMeasure> list = new ArrayList<>();
        String[] measures = {
            "作业前对进入受限空间危险性进行分析",
            "所有与受限空间有联系的阀门、管线加盲板隔离，列出盲板清单，并落实拆盲板责任人",
            "与作业场所相关联的设备停电、挂牌、找专人监护",
            "打开设备通风口、孔自然通风，温度适宜人员作业。必要时采用强制通风或佩戴隔离式防毒面具，但设备内缺氧时，严禁用通氧的方法补氧",
            "进入转动设备内部，在低压室停电上锁。挂禁止合闸标志牌，设专人监护",
            "盛装过可燃、有毒液体、气体的受限空间，应检测可燃、有毒有害气体含量",
            "检查受限空间内部是否具备作业条件，清罐时应用防爆工具",
            "指出受限空间内存在的其他危害因素，如内部附件等",
            "作业人员应清楚受限空间内存在的其他危害因素，如内部附件等",
            "检查受限空间进出口通道，不得有阻碍人员进出的障碍物",
            "再次检测有害气体浓度、氧含量浓度，检查通讯工具是否畅通",
            "作业监护措施：消防器材、救生绳、应急装备已配备",
            "其他补充安全措施："
        };
        for (int i = 0; i < measures.length; i++) {
            SafetyMeasure m = new SafetyMeasure();
            m.setMeasureNo(i + 1);
            m.setMeasureContent(measures[i]);
            m.setIsInvolved(SafetyMeasure.INVOLVED_NA);
            list.add(m);
        }
        return list;
    }

    public static List<SafetyMeasure> getFireMeasures() {
        List<SafetyMeasure> list = new ArrayList<>();
        String[] measures = {
            "动火设备内部构件已清洗干净，蒸汽吹扫或水洗、置换合格，达到动火条件",
            "与动火设备相连接的所有管线已断开，加盲板，未采取水封或仅关闭阀门的方式代替盲板",
            "动火点周围及附近的孔洞、窖井、地沟、水封设施、污水井等已清除易燃物，并已采取覆盖、铺沙等手段进行隔离",
            "在较大设备内动火，应对上、中、下（左、中、右）各部位进行气体检测分析",
            "高处作业已采取防火花飞溅措施，作业人员已佩戴必要的个体防护装备",
            "在有可燃物构件和使用可燃物做防腐内衬的设备内部动火作业，已采取防火隔绝措施",
            "乙炔气瓶直立放置，已采取防倾倒措施并安装防回火装置；乙炔气瓶、氧气瓶与火源间的距离不应小于10m，两气瓶相互间距不应小于5m",
            "现场配备灭火器、灭火毯、消防蒸汽带或消防水带",
            "电焊机所处位置已考虑防火防爆要求，且已可靠接地",
            "动火点周围规定距离内没有易燃易爆化学品的装卸、排放、喷漆等可能引起火灾爆炸的危险作业",
            "动火点30m内垂直空间未排放可燃气体；15m内垂直空间未排放可燃液体；10m范围内及动火点下方未同时进行可燃溶剂清洗或喷漆等作业，10m范围内未见有可燃性粉尘清扫作业",
            "已开展作业危害分析，制定相应的安全风险管控措施，交叉作业已明确协调人",
            "用于连续检测的移动式可燃气体检测仪已配备到位",
            "配备的摄录设备已到位，且防爆级别满足安全要求",
            "其他相关特殊作业已办理相应安全作业票，作业现场四周已设立警戒区",
            "其他安全措施："
        };
        for (int i = 0; i < measures.length; i++) {
            SafetyMeasure m = new SafetyMeasure();
            m.setMeasureNo(i + 1);
            m.setMeasureContent(measures[i]);
            m.setIsInvolved(SafetyMeasure.INVOLVED_NA);
            list.add(m);
        }
        return list;
    }

    public static String[] getHeightLevels() {
        return new String[]{"一级(2m-5m含)", "二级(5m-15m含)", "三级(15m-30m含)", "四级(>30m)"};
    }

    public static String[] getFireLevels() {
        return new String[]{"特级", "一级", "二级"};
    }

    public static String[] getFireMethods() {
        return new String[]{"电焊", "氩弧焊", "电割", "气焊", "气割", "研磨切割", "其他"};
    }

    public static String generateTicketNo(String type) {
        String prefix;
        switch (type) {
            case Ticket.TYPE_HEIGHT: prefix = "DH"; break;
            case Ticket.TYPE_SPACE: prefix = "YX"; break;
            case Ticket.TYPE_FIRE: prefix = "DHF"; break;
            default: prefix = "TK";
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new java.util.Date());
        int random = (int)(Math.random() * 900) + 100;
        return prefix + dateStr + random;
    }
}
