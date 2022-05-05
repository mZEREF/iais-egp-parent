package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class TaskDto extends BaseEntityDto {

	private String id;

	private String taskKey;

	private String taskType;

	private Integer priority;

	private String refNo;

	private String userId;

	private Date dateAssigned;

	private String taskStatus;

	private String processUrl;

	private String roleId;

	private ApplicationDto application;

	private Integer slaInDays;

	private Integer slaAlertInDays;

	private Date slaDateCompleted;

	private Integer slaRemainInDays;

	private Integer score;

	private String curOwner;
}
