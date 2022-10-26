package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;

@Data
public class InspectionProcessDto implements Serializable {
    private String appId;
    private String taskId;
    private String otherInspector;
    private String internalRemarks;
    private String observation;
    private String remark;
    private String decision;

    private static final String KEY_OTHER_INSPECTOR              = "otherInspector";
    private static final String KEY_INTERNAL_REMARK              = "internalRemarks";
    private static final String KEY_OBSERVATION                  = "observation";
    private static final String KEY_REMARK                       = "remark";
    private static final String KEY_DECISION             = "decision";

    public void reqObjMapping(HttpServletRequest request) {
        this.appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        this.taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        this.otherInspector = ParamUtil.getRequestString(request, KEY_OTHER_INSPECTOR);
        this.internalRemarks = ParamUtil.getRequestString(request, KEY_INTERNAL_REMARK);
        this.observation = ParamUtil.getRequestString(request, KEY_OBSERVATION);
        this.remark = ParamUtil.getRequestString(request, KEY_REMARK);
        this.decision = ParamUtil.getRequestString(request, KEY_DECISION);
    }
}
