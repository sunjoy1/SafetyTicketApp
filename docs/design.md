# 安全管理作业票软件 - 设计文档

## 项目概述

基于 GB 30871-2022 标准，开发一款安卓安全管理软件，支持高处作业、有限空间作业、动火作业等特种作业票的数字化办理、签字、审批、拍照、存档与打印。

## 核心功能

1. **作业票创建**：依据标准模板填写作业票
2. **电子签字**：支持触屏手写签字，自动嵌入对应区域
3. **审批流程**：多级审批（作业负责人→车间→安全部门→厂领导）
4. **现场拍照**：集成相机拍照，存档留痕
5. **历史存档**：本地数据库管理所有作业票
6. **打印输出**：生成标准格式文档，签字自动填入对应区域

## 数据库设计

### 表结构

#### 1. tickets（作业票主表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER PK | 主键 |
| ticket_type | TEXT | 作业票类型：height/space/fire |
| ticket_no | TEXT | 作业票编号 |
| apply_unit | TEXT | 申请单位 |
| apply_time | TEXT | 申请时间 |
| location | TEXT | 作业地点 |
| content | TEXT | 作业内容 |
| risk_analysis | TEXT | 风险辨识结果 |
| work_time_start | TEXT | 作业开始时间 |
| work_time_end | TEXT | 作业结束时间 |
| status | INTEGER | 状态：0草稿 1待审批 2审批中 3已通过 4已驳回 5已完工 |
| create_time | TEXT | 创建时间 |
| update_time | TEXT | 更新时间 |

#### 2. ticket_fields（作业票字段表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER PK | 主键 |
| ticket_id | INTEGER FK | 关联作业票 |
| field_name | TEXT | 字段名称 |
| field_value | TEXT | 字段值 |

#### 3. safety_measures（安全措施表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER PK | 主键 |
| ticket_id | INTEGER FK | 关联作业票 |
| measure_no | INTEGER | 措施序号 |
| measure_content | TEXT | 措施内容 |
| is_involved | INTEGER | 是否涉及：0否 1是 2不涉及 |
| confirm_person | TEXT | 确认人 |

#### 4. signatures（签字表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER PK | 主键 |
| ticket_id | INTEGER FK | 关联作业票 |
| sign_type | TEXT | 签字类型：creator/supervisor/worker/approver1/approver2/approver3/approver4/final |
| sign_person | TEXT | 签字人姓名 |
| sign_time | TEXT | 签字时间 |
| sign_image | BLOB | 签字图片数据 |
| sign_comment | TEXT | 审批意见 |

#### 5. photos（现场照片表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER PK | 主键 |
| ticket_id | INTEGER FK | 关联作业票 |
| photo_path | TEXT | 照片存储路径 |
| photo_desc | TEXT | 照片描述 |
| take_time | TEXT | 拍摄时间 |

## 三种作业票标准模板

### 高处（登高）作业票
- 编号规则：DH + 年月日 + 序号
- 关键字段：申请单位、申请人、申请时间、作业地点、作业内容、作业高度、作业级别（一级2-5m/二级5-15m/三级15-30m/四级>30m）、作业单位、监护人、作业人、作业负责人、关联作业票、风险辨识、安全措施（14项）、采样分析、审批签字（5级）、完工验收

### 有限空间作业票
- 编号规则：YX + 年月日 + 序号
- 关键字段：工作地点、设备名称、原有介质、危险因素、作业班组、作业内容、作业人员（本人签名）、监护人、填写人、作业时间、关联作业票、安全措施（13项）、危险源辨别、审批签字（3级）、完工验收

### 动火作业票
- 编号规则：DHF + 年月日 + 序号
- 关键字段：申请单位、申请时间、作业内容、动火地点及部位、动火级别（特级/一级/二级）、动火方式、作业单位、作业负责人、动火人及证书编号、采样分析时间/结果、关联作业、风险辨识、安全措施（16项）、安全交底人、接受交底人、监护人、审批签字（5级+验票+完工验收）

## 审批流程

1. 作业负责人意见 → 2. 所在单位（车间）意见 → 3. 安全管理部门意见 → 4. 审批部门（厂领导/总工程师）意见 → 5. 动火前验票（动火作业特有）→ 6. 完工验收

每个环节必须签字+时间+意见才能进入下一环节。

## 打印模板

打印时从数据库读取数据，将签字图片自动插入PDF对应区域，生成标准格式作业票文档。

## 技术栈

- 语言：Java
- 数据库：SQLite
- 打印：Android PrintManager + PDF生成
- 签字：自定义Canvas手写View
- 拍照：Camera2 API / Intent调用系统相机
- 最低SDK：Android 7.0 (API 24)
- 目标SDK：Android 14 (API 34)
