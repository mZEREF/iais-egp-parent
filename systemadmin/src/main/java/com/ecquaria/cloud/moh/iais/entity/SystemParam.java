package com.ecquaria.cloud.moh.iais.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "SYSTEM_PARAMETERS")
@Entity
public class SystemParam implements Serializable {


	@Setter
	@Getter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Setter @Getter
	@Column(name="ROWGUID",  columnDefinition="uniqueidentifier")
	private String rowguid;

	@Setter
	@Getter
	@Column(name="DESCRIPTION")
	private String description;

	@Setter
	@Getter
	@Column(name="VALUE")
	private String value;

	@Setter
	@Getter
	@Column(name="PARAM_TYPE")
	private String paramType;

	@Setter
	@Getter
	@Column(name="IS_MANDATORY")
	private boolean manDatory;

	@Setter
	@Getter
	@Column(name="CAN_UPDATE")
	private boolean canUpdate;

	@Setter
	@Getter
	@Column(name="MAX_LENGTH")
	private Integer maxLength;

	@Setter
	@Getter
	@Column(name="IS_ENABLE")
	private boolean enable;

	@Setter
	@Getter
	@Column(name="ENABLE_CHECKBOX")
	private boolean enableCheckBox;


	@Column(name = "CREATED_BY")
	@Getter @Setter private String createdBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATED_AT")
	@Getter @Setter private Date createdAt;

	@Column(name = "MODIFIED_BY")
	@Getter @Setter private String modifiedBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "MODIFIED_AT")
	@Getter @Setter private Date modifiedAt;


	@Override
	public String toString() {
		return "SystemParam{" +
				"id=" + id +
				", rowGuid='" + rowguid + '\'' +
				", description='" + description + '\'' +
				", value='" + value + '\'' +
				", paramType='" + paramType + '\'' +
				", manDatory=" + manDatory +
				", canUpdate=" + canUpdate +
				", maxLength=" + maxLength +
				", enable=" + enable +
				", enableCheckBox=" + enableCheckBox +
				'}';
	}
}
