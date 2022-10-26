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

	private String refNo;

	private String processUrl;

	private String userId;

	private String roleId;

	private Date assignedAt;

	private Date completedAt;

	private String status;

	private AppAndMiscDto application;

	private String mainApplicationNo;
}
