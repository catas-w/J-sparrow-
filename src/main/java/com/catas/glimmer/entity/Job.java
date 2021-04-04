package com.catas.glimmer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author catas
 * @since 2021-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("glimmer_job")
public class Job implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 关联PLAN
     */
    private Integer planId;

    /**
     * 顺序
     */
    private Integer order;

    /**
     * 任务类型 ssh:scp
     */
    private Integer taskType;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 命令
     */
    private String command;

    /**
     * 本地路径
     */
    private String localPath;

    /**
     * 远程路径
     */
    private String remotePath;

    /**
     * scp 类型 上传或下载
     */
    private Integer scpType;

    /**
     * 描述
     */
    private String description;


}
