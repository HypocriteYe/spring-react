package com.hg.webflux.pojo;

import com.hg.webflux.pojo.entity.PermPO;
import com.hg.webflux.pojo.entity.RolesPO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/4 16:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesDTO implements Serializable {

    private Long id;

    /**
     * 角色名
     */

    private String name;

    /**
     * 角色的英文名
     */
    private String value;
//    private Instant updateTime;
    private List<PermPO> permList;
}
