package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APP_ID;


@Service
@Slf4j
public class ViewAppService {
    private ViewAppService() {}

    public static void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // get app id
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskHelper.unmask(KEY_APP_ID, maskedAppId);
        ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
    }
}
