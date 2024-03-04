package com.hg.webflux.pojo;

import com.hg.webflux.pojo.entity.PermPO;
import com.hg.webflux.pojo.entity.RolesPO;
import com.hg.webflux.pojo.entity.UserPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/4 16:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable, UserDetails {

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */

    private String email;

    /**
     * 电话
     */

    private String phone;

    /**
     * 更新时间
     */
//    private Instant updateTime;

    private List<RolesDTO> roleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
