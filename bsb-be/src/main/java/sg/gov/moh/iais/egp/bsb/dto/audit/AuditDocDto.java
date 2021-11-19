package sg.gov.moh.iais.egp.bsb.dto.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;
import sg.gov.moh.iais.egp.bsb.entity.FacilityDoc;
import java.io.Serializable;
import java.util.List;

/**
 * @author Zhu Tangtang
 * @date 2021/8/31 15:35
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuditDocDto extends PagingAndSortingDto implements Serializable {

    private List<FacilityDoc> facilityDocs;

    private String applicationNoOverAll;
    private String serviceType;
    private String submissionDate;
    private String currentStatus;
    private String appPremisesCorrelationId;
    private String hciName;
    private String hciPostalCode;
    private String hciAddress;
    private String hciCode;
    private String telephone;
    private String applicationType;
    private Boolean isUpload;
    private boolean canFastTracking;
    private Integer systemMaxFileSize;
    private String systemFileType;
    private int cgoMandatoryCount;
    private String appealNo;
    private String svcCode;
    private boolean showTcu;
    private boolean editTcu;
    private boolean tcuFlag;
    private String tuc;

}
