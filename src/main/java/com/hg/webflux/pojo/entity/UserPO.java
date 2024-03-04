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
@Table("t_user")
public class UserPO {
	private static final long serialVersionUID = 1L;

	@Id
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
	private Instant updateTime;
	

}
