package com.gzqylc.da.entity;

import com.gzqylc.lang.web.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * 指定构建主机
 */
@Getter
@Setter
@Entity
@FieldNameConstants
public class Runner extends BaseEntity {

    @NotNull
    @OneToOne
    Host host;

    int seq;

    // 局域网的git地址，替换公网地址，加快速度
    String gitUrlReplaceSource;
    String gitUrlReplaceTarget;
}
