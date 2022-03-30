package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityDetailsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/3/14
 */
@Data
public class MohProcessDto implements Serializable {
    private String moduleName;

    private AppBasicInfo appBasicInfo;
    private FacilityDetailsInfo facilityDetailsInfo;

    private List<SelectOption> selectRouteToMoh;
    private List<DocDisplayDto> docDisplayDtoList;

    // process data
    private String remarks;
    private String processingDecision;
    private String inspectionRequired;
    private String selectAO;
}
