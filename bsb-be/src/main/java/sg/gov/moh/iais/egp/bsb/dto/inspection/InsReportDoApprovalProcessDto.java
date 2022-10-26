package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsReportDoApprovalProcessDto extends InsProcessDto{
    private String ncEmailRequired;

    private static final String KEY_NC_EMAIL_REQUIRED   = "ncEmailRequired";


    @Override
    public void reqObjMapping(HttpServletRequest request) {
        super.reqObjMapping(request);
        this.setNcEmailRequired(ParamUtil.getString(request,KEY_NC_EMAIL_REQUIRED));
    }
}
