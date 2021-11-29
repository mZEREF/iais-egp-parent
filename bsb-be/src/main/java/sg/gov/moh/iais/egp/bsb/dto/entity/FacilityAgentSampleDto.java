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
@JGlobalMap(excluded = {"facilityBiologicalAgentId"})
public class FacilityAgentSampleDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${facilityBiologicalAgent.id}")
    private String facilityBiologicalAgentId;

    private String sampleNature;

    private String sampleNatureOth;
}
