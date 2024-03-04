package com.hg.webflux.pojo.entity;


import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author yazi
 * @email 934752370@qq.com
 * @date 2024-03-04 13:55:06
 */
@Data
@Table("t_roles")
@NoArgsConstructor
@AllArgsConstructor
public class RolesPO {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    /**
     * 角色名
     */

    private String name;

    /**
     * 角色的英文名
     */
    private String value;
    private Instant updateTime;


}
