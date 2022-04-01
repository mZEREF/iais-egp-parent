package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/3/14
 */
@Data
public class MohProcessDto implements Serializable {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;

    private List<DocDisplayDto> supportDocDisplayDtoList;
    private List<DocDisplayDto> internalDocDisplayDtoList;
    private List<SelectOption> selectRouteToMoh;
    private List<ProcessHistoryDto> processHistoryDtoList;

    // process data
    private String remarks;
    private String processingDecision;
    private String inspectionRequired;
    private String certificationRequired;
    private String selectMohUser;

    private String lastRecommendation;
    private String lastRemarks;
}
