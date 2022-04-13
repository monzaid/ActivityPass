package com.example.social_practice_activity_server.face.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: CreateGroup
 * @Description: TODO 创建人员组请求参数
 * @author: martin-wj
 * @createDate: 2020-12-21
 */
@Data
@Builder
public class CreateGroup extends PublicParam {

    /**
     * 人员库名称，[1,60]个字符，可修改，不可重复
     */
    private String groupName;

    /**
     * 人员库 ID，不可修改，不可重复。支持英文、数字、-%@#&_，长度限制64B
     */
    private String groupId;

    /**
     * 人员库自定义描述字段，用于描述人员库中人员属性，该人员库下所有人员将拥有此描述字段。
     * 最多可以创建5个。
     * 每个自定义描述字段支持[1,30]个字符。
     * 在同一人员库中自定义描述字段不可重复。
     * 例： 设置某人员库“自定义描述字段”为["学号","工号","手机号"]，
     * 则该人员库下所有人员将拥有名为“学号”、“工号”、“手机号”的描述字段，
     * 可在对应人员描述字段中填写内容，登记该人员的学号、工号、手机号等信息。
     */
    private String groupExDescriptions01;

    private String groupExDescriptions02;

    private String groupExDescriptions03;

    /**
     * 人员库信息备注，[0，40]个字符
     */
    private String tag;
}