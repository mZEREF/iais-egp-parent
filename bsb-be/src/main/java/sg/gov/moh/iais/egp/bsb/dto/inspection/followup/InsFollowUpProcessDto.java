package sg.gov.moh.iais.egp.bsb.dto.inspection.followup;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;

import javax.servlet.http.HttpServletRequest;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsFollowUpProcessDto extends InsProcessDto {
    private String remarkToFacility;


    private static final String KEY_REMARK_TO_FACILITY = "remarksToFacility";


    @Override
    public void reqObjMapping(HttpServletRequest request) {
        super.reqObjMapping(request);
        this.remarkToFacility = ParamUtil.getString(request, KEY_REMARK_TO_FACILITY);
    }
}
