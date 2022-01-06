package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import io.jsonwebtoken.lang.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.MASK_PARAM_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


public class MaskHelper {
    private MaskHelper() {}

    private static final String ERR_MSG_EMPTY_PARAM = "Mask param should not be empty";
    private static final String ERR_MSG_EMPTY_KEY = "Key should not be empty";


    /** Public version, add a check of mask param.
     * @see #unmask0(String, String) */
    public static String unmask(String param, String maskedValue) {
        Assert.hasLength(param, ERR_MSG_EMPTY_PARAM);
        return unmask0(param, maskedValue);
    }

    /** To do Unmask, will throw exception if not valid */
    private static String unmask0(String param, String maskedValue) {
        String value = MaskUtil.unMaskValue(param, maskedValue);
        if (!StringUtils.hasLength(value) || value.equals(maskedValue)) {
            throw new IaisRuntimeException("Invalid masked value:" + LogUtil.escapeCrlf(maskedValue));
        }
        return value;
    }


    /** Process entered from task list will always get the two parameters.
     * This method will put the two key value into session with specific keys. */
    public static void taskProcessUnmask(HttpServletRequest request, String appIdSessionKey, String taskIdSessionKey) {
        Assert.hasLength(appIdSessionKey, ERR_MSG_EMPTY_KEY);
        Assert.hasLength(taskIdSessionKey, ERR_MSG_EMPTY_KEY);
        String maskedAppId = request.getParameter(PARAM_NAME_APP_ID);
        String appId = unmask0(MASK_PARAM_ID, maskedAppId);
        String maskedTaskId = request.getParameter(PARAM_NAME_TASK_ID);
        String taskId = unmask0(MASK_PARAM_ID, maskedTaskId);
        ParamUtil.setSessionAttr(request, appIdSessionKey, appId);
        ParamUtil.setSessionAttr(request, taskIdSessionKey, taskId);
    }
}
