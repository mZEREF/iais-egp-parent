package sg.gov.moh.iais.egp.bsb.dto.enquiry;


import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.oval.constraint.ValidateWithMethod;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApprovalSearchDto extends PagingAndSortingDto {
    private String facName;
    private String facilityClassification;
    private List<String> facTypes;
    private String scheduleType;
    private String batName;
    private List<String> sampleNatures;
    private String riskLevel;
    private String approvalType;
    private String approvalSubDtFrom;
    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkApprovalSubDtTo", parameterType = String.class, profiles = {"approval"})
    private String approvalSubDtTo;
    private String approvalStatus;
    private String approvedDtFrom;
    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkApprovedDtTo", parameterType = String.class, profiles = {"approval"})
    private String approvedDtTo;

    private boolean checkApprovalSubDtTo(String approvalSubDtTo) {// NOSONAR
        if(!StringUtils.hasLength(approvalSubDtTo) || !StringUtils.hasLength(approvalSubDtFrom)){
            return true;
        }
        return DateUtil.parseToLocalDateTime(approvalSubDtTo).after(DateUtil.parseToLocalDateTime(approvalSubDtFrom));
    }

    private boolean checkApprovedDtTo(String approvedDtTo) {// NOSONAR
        if(!StringUtils.hasLength(approvedDtTo) || !StringUtils.hasLength(approvedDtFrom)){
            return true;
        }
        return DateUtil.parseToLocalDateTime(approvedDtTo).after(DateUtil.parseToLocalDateTime(approvedDtFrom));
    }

    public void clearAllFields(){
        this.facName = "";
        this.facilityClassification = "";
        this.facTypes = null;
        this.scheduleType = "";
        this.batName = "";
        this.sampleNatures = null;
        this.riskLevel = "";
        this.approvalType = "";
        this.approvalSubDtFrom = "";
        this.approvalSubDtTo = "";
        this.approvalStatus = "";
        this.approvedDtFrom = "";
        this.approvedDtTo = "";
    }

    public void reqObjMapping(HttpServletRequest request){
        this.facName = ParamUtil.getString(request,PARAM_FACILITY_NAME);
        this.facilityClassification = ParamUtil.getString(request,PARAM_FACILITY_CLASSIFICATION);
        String[] facilityTypes = ParamUtil.getStrings(request,PARAM_FACILITY_TYPES);
        if(facilityTypes != null && facilityTypes.length > 0){
            this.facTypes = new ArrayList<>(Arrays.asList(facilityTypes));
        }
        this.scheduleType = ParamUtil.getString(request,PARAM_SCHEDULE_TYPE);
        this.batName = ParamUtil.getString(request,PARAM_BIOLOGICAL_AGENT);
        String[] sampleNatureStrings = ParamUtil.getStrings(request,PARAM_NATURE_OF_SAMPLE);
        if(sampleNatureStrings != null && sampleNatureStrings.length > 0){
            this.sampleNatures = new ArrayList<>(Arrays.asList(sampleNatureStrings));
        }
        this.riskLevel = ParamUtil.getString(request,PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT);
        this.approvalType = ParamUtil.getString(request,PARAM_APPROVAL_TYPE);
        this.approvalSubDtFrom = ParamUtil.getString(request,PARAM_APPROVAL_SUBMISSION_DATE_FROM);
        this.approvalSubDtTo = ParamUtil.getString(request,PARAM_APPROVAL_SUBMISSION_DATE_TO);
        this.approvalStatus = ParamUtil.getString(request,PARAM_APPROVAL_STATUS);
        this.approvedDtFrom = ParamUtil.getString(request,PARAM_APPROVED_DATE_FROM);
        this.approvedDtTo = ParamUtil.getString(request,PARAM_APPROVED_DATE_TO);
    }
}
