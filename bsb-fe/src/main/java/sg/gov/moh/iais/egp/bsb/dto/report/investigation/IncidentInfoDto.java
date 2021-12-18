package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YiMing
 * @version 2021/12/14 20:30
 **/

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentInfoDto extends ValidatableNodeValue {
    private String investLeader;
    private String otherInvest;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    @Override
    public boolean doValidation() {
        return true;
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    public String getInvestLeader() {
        return investLeader;
    }

    public void setInvestLeader(String investLeader) {
        this.investLeader = investLeader;
    }

    public String getOtherInvest() {
        return otherInvest;
    }

    public void setOtherInvest(String otherInvest) {
        this.otherInvest = otherInvest;
    }


    private static final String KEY_INVESTIGATION_LEADER = "investLeader";
    private static final String KEY_OTHER_INVESTIGATION = "otherInvest";

    public void reqObjMapping(HttpServletRequest request){
        this.investLeader = ParamUtil.getString(request,KEY_INVESTIGATION_LEADER);
        this.otherInvest = ParamUtil.getString(request,KEY_OTHER_INVESTIGATION);
    }
}
