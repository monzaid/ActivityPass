package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;
/**
 * @ClassName: GetGroupList
 * @Description: TODO 人员库列表请求参数
 * @author:
 * @createDate:
 */
@Data
public class GetGroupList extends PublicParam {

    /**
     * 起始序号，默认值为0
     */
    private Integer offset;

    /**
     * 返回数量，默认值为10，最大值为1000
     */
    private Integer limit;
}