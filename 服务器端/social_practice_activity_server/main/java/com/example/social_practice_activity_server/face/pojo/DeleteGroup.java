package com.example.social_practice_activity_server.face.pojo;

import lombok.Data;

/**
 * @ClassName: DeleteGroup
 * @Description: TODO 删除人员库请求参数 https://cloud.tencent.com/document/product/867/32791
 * @author: martin-wj
 * @createDate: 2020-12-21
 */
@Data
public class DeleteGroup extends PublicParam{

    /**
     * 人员库ID
     */
    private String groupId;
}
