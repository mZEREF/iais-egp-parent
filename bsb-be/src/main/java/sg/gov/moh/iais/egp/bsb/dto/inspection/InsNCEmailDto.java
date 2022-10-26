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
public class InsNCEmailDto extends InsProcessDto{
    private String title;

    private String content;

    private static final String KEY_EMAIL_TITLE = "title";
    private static final String KEY_EMAIL_CONTENT = "content";
    private static final String KEY_DECISION            = "processingDecision";
    private static final String KEY_REMARK              = "remarks";
    private static final String KEY_SELECT_MOH_USER     = "selectMohUser";

    @Override
    public void reqObjMapping(HttpServletRequest request){
        this.setTitle(ParamUtil.getString(request,KEY_EMAIL_TITLE));
        this.setContent(ParamUtil.getString(request,KEY_EMAIL_CONTENT));
        this.setDecision(ParamUtil.getString(request, KEY_DECISION));
        this.setRemark(ParamUtil.getString(request, KEY_REMARK));
        this.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
    }
}
