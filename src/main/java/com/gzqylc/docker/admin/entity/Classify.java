package com.gzqylc.docker.admin.entity;

import com.gzqylc.lang.web.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * 分组表 描述当前已有分组
 */
@Entity
@Getter
@Setter
@FieldNameConstants
public class Classify extends BaseEntity {

      /**
     * 分组自己的名字
     */
    @Column(unique = true)
    @NotNull
    String groupName;

    @Transient
    public static String DEFAULT_GROUP_ID = "0";

    public Classify(String id){
        this.id = id;
    }

    public Classify() {

    }
}
