package sg.gov.moh.iais.egp.bsb.dto.task;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2020/4/17 10:34
 **/
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApptAppInfoShowDto extends ApplicationDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String srcSystemId;
    private List<String> userDisName;
    private List<String> apptRefNo;
    private List<String> groupNames;
    private String userLoginId;
    private List<String> sysUserCorrIds;
    private String calendarStatus;
    private List<String> clsStatus;
    private String groupName;
    private String stageId;
    private List<String> userIdList;
    private Date inspDate;
    private Date inspEndDate;
    private List<ApplicationGroupDto> applicationGroupDtos;
}
