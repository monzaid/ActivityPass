package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: GetGroupListResp
 * @Description: TODO
 * @author: martin-wj
 * @createDate: 2020-12-21
 */
@Data
public class GetGroupListResp extends Response {

    private String groupNum;

    private List<GroupInfos> groupInfos;

}