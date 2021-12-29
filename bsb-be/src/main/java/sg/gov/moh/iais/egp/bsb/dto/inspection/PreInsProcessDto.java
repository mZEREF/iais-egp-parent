package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


@Data
public class PreInsProcessDto implements Serializable {
    private String remark;
    private String decision;




    private static final String KEY_DECISION = "processingDecision";
    private static final String KEY_REMARK = "remarks";

    public void reqObjMapping(HttpServletRequest request) {
        this.decision = ParamUtil.getString(request, KEY_DECISION);
        this.remark = ParamUtil.getString(request, KEY_REMARK);
    }
}
