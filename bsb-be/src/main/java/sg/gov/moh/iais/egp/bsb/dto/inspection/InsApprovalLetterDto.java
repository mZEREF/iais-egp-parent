package sg.gov.moh.iais.egp.bsb.dto.inspection;


import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InsApprovalLetterDto extends InsProcessDto {
    private String emailTitle;
    private String emailContent;

    private static final String KEY_EMAIL_TITLE = "emailTitle";
    private static final String KEY_EMAIL_CONTENT = "emailContent";
    private static final String KEY_DECISION            = "processingDecision";
    private static final String KEY_REMARK              = "remarks";
    private static final String KEY_SELECT_MOH_USER     = "selectMohUser";

    @Override
    public void reqObjMapping(HttpServletRequest request){
        this.setEmailTitle(ParamUtil.getString(request,KEY_EMAIL_TITLE));
        this.setEmailContent(ParamUtil.getString(request,KEY_EMAIL_CONTENT));
        this.setDecision(ParamUtil.getString(request, KEY_DECISION));
        this.setRemark(ParamUtil.getString(request, KEY_REMARK));
        this.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
    }
}
