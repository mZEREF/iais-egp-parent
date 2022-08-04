package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class RoutingHistoryDto extends BaseEntityDto {
    private String id;

    private String processDecision;

    private String appStatus;

    private String actionBy;

    private String roleId;

    private String applicationNo;

    private String remarks;
}
