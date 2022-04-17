package sg.gov.moh.iais.egp.bsb.dto.enquiry;


import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.oval.constraint.ValidateWithMethod;
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
public class FacSearchDto extends PagingAndSortingDto {
    private String facName;
    private String facilityClassification;
    private List<String> facTypes;
    private String scheduleType;
    private String batName;
    private String riskLevel;
    private String facExpiryDtFrom;
    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkFacExpiryDtTo", parameterType = String.class, profiles = {"fac"})
    private String facExpiryDtTo;
    private String gazettedArea;
    private String facOperator;
    private String facAdmin;
    private String facAuthorisedPerson;
    private String facCommittee;
    private String facStatus;
    private String afcName;

    private boolean checkFacExpiryDtTo(String facExpiryDtTo) {// NOSONAR
        if (facExpiryDtTo == null || facExpiryDtFrom == null){
            return true;
        }
        return DateUtil.parseToLocalDateTime(facExpiryDtTo).after(DateUtil.parseToLocalDateTime(facExpiryDtFrom));
    }

    public void clearAllFields(){
        this.facName = "";
        this.facilityClassification = "";
        this.facTypes = null;
        this.scheduleType = "";
        this.batName = "";
        this.riskLevel = "";
        this.facExpiryDtFrom = "";
        this.facExpiryDtTo = "";
        this.gazettedArea = "";
        this.facOperator = "";
        this.facAdmin = "";
        this.facAuthorisedPerson = "";
        this.facCommittee = "";
        this.facStatus = "";
        this.afcName = "";
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
        this.riskLevel = ParamUtil.getString(request,PARAM_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT);
        this.facExpiryDtFrom = ParamUtil.getString(request,PARAM_FACILITY_EXPIRY_DATE_FROM);
        this.facExpiryDtTo = ParamUtil.getString(request,PARAM_FACILITY_EXPIRY_DATE_TO);
        this.gazettedArea = ParamUtil.getString(request,PARAM_GAZETTED_AREA);
        this.facOperator = ParamUtil.getString(request,PARAM_FACILITY_OPERATOR);
        this.facAdmin = ParamUtil.getString(request,PARAM_FACILITY_ADMIN);
        this.facAuthorisedPerson = ParamUtil.getString(request,PARAM_AUTHORISED_PERSONNEL);
        this.facCommittee = ParamUtil.getString(request,PARAM_COMMITTEE_PERSONNEL);
        this.facStatus = ParamUtil.getString(request,PARAM_FACILITY_STATUS);
        this.afcName = ParamUtil.getString(request,PARAM_APPROVED_FACILITY_CERTIFIER);
    }
}
