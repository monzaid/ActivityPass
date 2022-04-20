package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

/**
 * @ClassName: ModifyGroup
 * @Description: TODO
 * @author:
 * @createDate:
 */
@Data
public class ModifyGroup extends PublicParam {

    /**
     * 人员库ID
     */
    private String groupId;

    /**
     * 需要修改的人员库自定义描述字段，key-value
     */
    private String groupExDescriptionInfos01;

    /**
     * 需要修改的人员库自定义描述字段，key-value
     */
    private String groupExDescriptionInfosIndex01;

    /**
     * 需要修改的人员库自定义描述字段，key-value
     */
    private String groupExDescriptionInfos02;

    /**
     * 需要修改的人员库自定义描述字段，key-value
     */
    private String groupExDescriptionInfosIndex02;

    /**
     * 人员库信息备注
     */
    private String tag;
}
