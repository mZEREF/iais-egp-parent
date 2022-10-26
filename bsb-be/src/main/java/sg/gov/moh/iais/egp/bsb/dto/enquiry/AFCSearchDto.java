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
import static sg.gov.moh.iais.egp.bsb.constant.BioSafetyEnquiryConstants.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AFCSearchDto extends PagingAndSortingDto {
    private String orgName;
    private String adminName;
    private String afcStatus;
    private String teamMemberName;
    private String teamMemberId;
    private String approvedAfcDtFrom;
    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkApprovedDtTo", parameterType = String.class, profiles = {"afc"})
    private String approvedAfcDtTo;

    private boolean checkApprovedDtTo(String approvedDtTo) {// NOSONAR
        if(!StringUtils.hasLength(approvedDtTo) || !StringUtils.hasLength(approvedAfcDtFrom)){
            return true;
        }
        return DateUtil.parseToLocalDateTime(approvedDtTo).after(DateUtil.parseToLocalDateTime(approvedAfcDtFrom));
    }

    public void clearAllFields(){
        this.orgName = "";
        this.adminName = "";
        this.afcStatus = "";
        this.teamMemberName = "";
        this.teamMemberId = "";
        this.approvedAfcDtFrom = "";
        this.approvedAfcDtTo = "";
    }

    public void reqObjMapping(HttpServletRequest request){
        this.orgName = ParamUtil.getString(request,PARAM_ORGANISATION_NAME);
        this.adminName = ParamUtil.getString(request,PARAM_FACILITY_ADMINISTRATOR);
        this.afcStatus = ParamUtil.getString(request,PARAM_AFC_STATUS);
        this.teamMemberName = ParamUtil.getString(request,PARAM_TEAM_MEMBER_NAME);
        this.teamMemberId = ParamUtil.getString(request,PARAM_TEAM_MEMBER_ID);
        this.approvedAfcDtFrom = ParamUtil.getString(request,PARAM_APPROVED_AFC_DATE_FROM);
        this.approvedAfcDtTo = ParamUtil.getString(request,PARAM_APPROVED_AFC_DATE_TO);
    }
}
