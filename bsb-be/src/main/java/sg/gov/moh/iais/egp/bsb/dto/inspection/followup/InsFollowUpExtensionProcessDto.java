package sg.gov.moh.iais.egp.bsb.dto.inspection.followup;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsFollowUpExtensionProcessDto extends InsFollowUpProcessDto {
    private String newDueDate;


    private static final String KEY_NEW_DUE_DATE = "newDueDate";
    private static final String KEY_EXTENSION_DECISION = "processingExtensionDecision";

    @Override
    public void reqObjMapping(HttpServletRequest request) {
        super.reqObjMapping(request);
        this.newDueDate = ParamUtil.getString(request, KEY_NEW_DUE_DATE);
        this.setDecision(ParamUtil.getString(request, KEY_EXTENSION_DECISION));
    }
}
