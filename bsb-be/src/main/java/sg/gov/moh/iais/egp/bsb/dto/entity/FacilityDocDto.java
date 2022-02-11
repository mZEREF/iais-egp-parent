package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"facilityId"})
public class FacilityDocDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${facility.id}")
    private String facilityId;

    private String docName;

    private long docSize;

    private String fileRepoId;

    private Date submitDt;

    private String submitBy;

    private String docType;
}
