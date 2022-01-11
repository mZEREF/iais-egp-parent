package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import io.jsonwebtoken.lang.Assert;
import org.springframework.util.StringUtils;


public class MaskHelper {
    private MaskHelper() {}

    private static final String ERR_MSG_EMPTY_PARAM = "Mask param should not be empty";


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
}
