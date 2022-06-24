package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;

import javax.servlet.http.HttpServletRequest;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsReportProcessDto extends InsProcessDto{
    private String hasNonCompliance;
    private String ncEmailRequired;

    private static final String KEY_NC_EMAIL_REQUIRED   = "ncEmailRequired";
    private static final String KEY_DECISION            = "processingDecision";
    private static final String KEY_REMARK              = "remarks";
    private static final String KEY_SELECT_MOH_USER     = "selectMohUser";


    @Override
    public void reqObjMapping(HttpServletRequest request) {
        this.setHasNonCompliance((String) ParamUtil.getSessionAttr(request, InspectionConstants.KEY_HAS_NON_COMPLIANCE));
        this.setNcEmailRequired(ParamUtil.getString(request,KEY_NC_EMAIL_REQUIRED));
        this.setDecision(ParamUtil.getString(request,KEY_DECISION));
        this.setRemark(ParamUtil.getString(request,KEY_REMARK));
        this.setSelectMohUser(ParamUtil.getString(request,KEY_SELECT_MOH_USER));
    }
}
