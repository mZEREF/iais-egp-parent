package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Data
public class RfiApplicationDto implements Serializable {
    private String facilityInfoSelectCheckBox;
    private String batSelectCheckBox;
    private String docSelectCheckBox;

    private static final String KEY_FACILITY_INFO_SELECT_CHECK_BOX = "facilityInfoSelectCheckBox";
    private static final String KEY_BAT_SELECT_CHECK_BOX           = "batSelectCheckBox";
    private static final String KEY_DOC_SELECT_CHECK_BOX           = "docSelectCheckBox";

    public void reqObjMapping(HttpServletRequest request) {
        this.facilityInfoSelectCheckBox = ParamUtil.getString(request, KEY_FACILITY_INFO_SELECT_CHECK_BOX);
        this.batSelectCheckBox = ParamUtil.getString(request, KEY_BAT_SELECT_CHECK_BOX);
        this.docSelectCheckBox = ParamUtil.getString(request, KEY_DOC_SELECT_CHECK_BOX);
    }
}
