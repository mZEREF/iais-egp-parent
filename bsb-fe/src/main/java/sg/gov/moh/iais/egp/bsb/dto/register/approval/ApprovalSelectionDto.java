package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;


/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalSelectionDto implements Serializable {
    private String facilityId;

    @JsonIgnore
    private String facilityName;

    private String processType;

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_FACILITY_ID       = "facilityId";
    private static final String KEY_FACILITY_NAME     = "facilityName";
    private static final String KEY_PROCESS_TYPE      = "processType";

    public void reqObjMapping(HttpServletRequest request) {
        String maskFacilityId = ParamUtil.getString(request, KEY_FACILITY_ID);
        String newFacilityId = MaskUtil.unMaskValue(KEY_FACILITY_ID, maskFacilityId);
        String newFacilityName = ParamUtil.getString(request, KEY_FACILITY_NAME);
        String newProcessType = ParamUtil.getString(request, KEY_PROCESS_TYPE);
        this.setFacilityId(newFacilityId);
        this.setFacilityName(newFacilityName);
        this.setProcessType(newProcessType);
    }
}
