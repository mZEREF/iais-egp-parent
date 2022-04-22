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
    private String letterTitle;
    private String letterContent;

    private static final String KEY_EMAIL_TITLE = "letterTitle";
    private static final String KEY_EMAIL_CONTENT = "letterContent";
    private static final String KEY_DECISION            = "processingDecision";
    private static final String KEY_REMARK              = "remarks";
    private static final String KEY_SELECT_MOH_USER     = "selectMohUser";

    @Override
    public void reqObjMapping(HttpServletRequest request){
        this.setLetterTitle(ParamUtil.getString(request,KEY_EMAIL_TITLE));
        this.setLetterContent(ParamUtil.getString(request,KEY_EMAIL_CONTENT));
        this.setDecision(ParamUtil.getString(request, KEY_DECISION));
        this.setRemark(ParamUtil.getString(request, KEY_REMARK));
        this.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
    }
}
