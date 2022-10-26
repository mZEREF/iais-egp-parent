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

import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.MESSAGE_END_DATE_EARLIER_THAN_START_DATE;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_APPLICATION_NO;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_APPLICATION_STATUS;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_FROM;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_APPLICATION_SUBMISSION_DATE_TO;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_APPLICATION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_BIOLOGICAL_AGENT;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_FACILITY_CLASSIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_FACILITY_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT;
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.PARAM_SCHEDULE_TYPE;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppSearchDto extends PagingAndSortingDto {
    private String appNo;
    private String appType;
    private String appStatus;
    private String appSubmissionDtFrom;
    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkAppSubmissionDtTo", parameterType = String.class, profiles = {"app"})
    private String appSubmissionDtTo;
    private String facClassification;
    private List<String> facTypes;
    private String facName;
    private String scheduleType;
    private String batName;
    private String riskLevel;
    private String processType;


    public void clearAllFields(){
        this.appNo = "";
        this.appType = "";
        this.appStatus = "";
        this.appSubmissionDtFrom = "";
        this.appSubmissionDtTo = "";
        this.facClassification = "";
        this.facTypes = null;
        this.facName = "";
        this.scheduleType = "";
        this.batName = "";
        this.riskLevel = "";
        this.processType = "";
    }

    private boolean checkAppSubmissionDtTo(String appSubmissionDtTo) {// NOSONAR
        if(!StringUtils.hasLength(appSubmissionDtTo) || !StringUtils.hasLength(appSubmissionDtFrom)){
            return true;
        }
        return DateUtil.parseToLocalDateTime(appSubmissionDtTo).after(DateUtil.parseToLocalDateTime(appSubmissionDtFrom));
    }

    public void reqObjMapping(HttpServletRequest request){
       this.appNo = ParamUtil.getString(request,PARAM_APPLICATION_NO);
       this.appType = ParamUtil.getString(request,PARAM_APPLICATION_TYPE);
       this.appStatus = ParamUtil.getString(request,PARAM_APPLICATION_STATUS);
       this.appSubmissionDtFrom = ParamUtil.getString(request,PARAM_APPLICATION_SUBMISSION_DATE_FROM);
       this.appSubmissionDtTo = ParamUtil.getString(request,PARAM_APPLICATION_SUBMISSION_DATE_TO);
       this.facClassification = ParamUtil.getString(request,PARAM_FACILITY_CLASSIFICATION);
       String[] facilityTypes = ParamUtil.getStrings(request,PARAM_FACILITY_TYPES);
       if(facilityTypes != null && facilityTypes.length > 0){
           this.facTypes = new ArrayList<>(Arrays.asList(facilityTypes));
       }
       this.scheduleType = ParamUtil.getString(request,PARAM_SCHEDULE_TYPE);
       this.batName = ParamUtil.getString(request,PARAM_BIOLOGICAL_AGENT);
       this.riskLevel = ParamUtil.getString(request,PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT);
       this.processType = ParamUtil.getString(request,PARAM_PROCESS_TYPE);
    }
}
