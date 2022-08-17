package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityActivityInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityBiologicalAgentInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_HM_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_CERTIFICATION_REQUIRED;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_INSPECTION_REQUIRED;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_PROCESSING_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_SELECT_MOH_USER;


@Data
@Slf4j
public class MohProcessDto implements Serializable {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;

    private List<DocDisplayDto> supportDocDisplayDtoList;
    private List<SelectOption> selectRouteToMoh;
    private List<ProcessHistoryDto> processHistoryDtoList;

    private String lastRecommendation;
    private String lastRemarks;
    // process data
    private String remarks;
    private String processingDecision;
    private String inspectionRequired;
    private String certificationRequired;
    private String selectMohUser;


    public void reqObjMapping(HttpServletRequest request, String functionName){
        this.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        this.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        String facClassification = facilityDetailsInfo.getClassification();

        switch (functionName) {
            case FUNCTION_DO_SCREENING: {
                this.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
                if (MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(facClassification)) {
                    this.setCertificationRequired(ParamUtil.getString(request, KEY_CERTIFICATION_REQUIRED));
                }
                this.setInspectionRequired(ParamUtil.getString(request, KEY_INSPECTION_REQUIRED));
                List<FacilityActivityInfo> facilityActivityInfoList = facilityDetailsInfo.getFacilityActivityInfoList();
                for (FacilityActivityInfo facilityActivityInfo : facilityActivityInfoList) {
                    String checked = ParamUtil.getString(request, facilityActivityInfo.getId());
                    facilityActivityInfo.setStatus(checked);
                }
                List<FacilityBiologicalAgentInfo> facilityBiologicalAgentInfoList = facilityDetailsInfo.getFacilityBiologicalAgentInfoList();
                for (FacilityBiologicalAgentInfo facilityBiologicalAgentInfo : facilityBiologicalAgentInfoList) {
                    String checked = ParamUtil.getString(request, facilityBiologicalAgentInfo.getId());
                    facilityBiologicalAgentInfo.setStatus(checked);
                }
                break;
            }
            case FUNCTION_AO_SCREENING: {
                this.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
                if (MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(facClassification)) {
                    this.setCertificationRequired(ParamUtil.getString(request, KEY_CERTIFICATION_REQUIRED));
                }
                this.setInspectionRequired(ParamUtil.getString(request, KEY_INSPECTION_REQUIRED));
                break;
            }
            case FUNCTION_HM_DECISION: {
                if (MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(facClassification)) {
                    this.setCertificationRequired(ParamUtil.getString(request, KEY_CERTIFICATION_REQUIRED));
                }
                this.setInspectionRequired(ParamUtil.getString(request, KEY_INSPECTION_REQUIRED));
                break;
            }
            default:
                log.info("don't have such functionName {}", StringUtils.normalizeSpace(functionName));
                break;
        }
    }
}
