package com.yxx.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yxx.common.core.domain.BaseColumnEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户和岗位关联 sys_user_post
 */
public class SysUserPost extends BaseColumnEntity
{
    /** 表ID */
    @TableId(type = IdType.AUTO)
    private Long userPostId;

    /** 用户ID */
    private Long userId;
    
    /** 岗位ID */
    private Long postId;

    public Long getUserPostId()
    {
        return userPostId;
    }

    public void setUserPostId(Long userPostId)
    {
        this.userPostId = userPostId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userPostId", getUserPostId())
            .append("userId", getUserId())
            .append("postId", getPostId())
            .append("super=>", super.toString())
            .toString();
    }
}
