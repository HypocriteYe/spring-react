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
 * 
 * 
 * @author yazi
 * @email 934752370@qq.com
 * @date 2024-03-04 13:55:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user_role")
public class UserRolePO {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	private Long userId;
	private Long roleId;
	private Instant updateTime;
	

}
