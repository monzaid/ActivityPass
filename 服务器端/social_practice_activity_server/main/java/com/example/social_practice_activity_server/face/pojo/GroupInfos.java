package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

/**
 * @ClassName: GroupInfos
 * @Description: TODO
 * @author: martin-wj
 * @createDate: 2020-12-21
 */
@Data
public class GroupInfos {

    private String groupName;

    private String groupId;

    private String[] groupExDescriptions;

    private String tag;
}