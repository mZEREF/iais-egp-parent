package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = "applicationId")
public class SelfAssessmtChklDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${application.id}")
    private String applicationId;

    private String rfiId;

    private String chkLstConfigId;

    private String answer;

    private Integer version;

    private String status;
}
