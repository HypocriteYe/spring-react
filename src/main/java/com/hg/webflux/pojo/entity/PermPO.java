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
@Table("t_perm")
public class PermPO {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
		
	/**
	 * 权限字段
	 */
	private String value;
		
	/**
	 * 资源路径
	 */
	private String uri;
		
	/**
	 * 资源描述
	 */
	private String description;

	private Instant updateTime;
	

}
