package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskView extends BaseEntity {
    private String id;
    private Application application;
    private RoutingStage routingStage;
    private String taskType;
    private Integer priority;
    private String refNo;
    private String userId;
    private String wkGrpId;
    private Date dateAssigned;
    private String taskStatus;
    private Integer slaInDays;
    private Integer slaAlertInDays;
    private Date slaDateCompleted;
    private Integer slaRemainInDays;
    private Integer score;
    private String processUrl;
    private String roleId;
    private int updateCount;
    private Date modifiedDate;
    private String curOwner;
}
