package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDataDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_MASKED_RFT_DATA_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RfiService {
    /**
     * rfi start method clear app id, and un mask app id
     */
    public void clearAndSetAppIdInSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_RFT_DATA_ID);
        // rfi inspection self-assessment, nc, follow-up need
        session.removeAttribute(KEY_CONFIRM_RFI);
        // get app id
        String maskedRfiAppId = ParamUtil.getString(request, KEY_RFI_APP_ID);
        String maskedRfiDataId = ParamUtil.getString(request, KEY_MASKED_RFT_DATA_ID);
        if (StringUtils.hasLength(maskedRfiAppId) && StringUtils.hasLength(maskedRfiDataId)) {
            String appId = MaskHelper.unmask(KEY_RFI_APP_ID, maskedRfiAppId);
            String rfiDataId = MaskHelper.unmask(KEY_MASKED_RFT_DATA_ID, maskedRfiDataId);
            ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
            ParamUtil.setSessionAttr(request, KEY_RFT_DATA_ID, rfiDataId);
            // rfi inspection self-assessment, nc, follow-up need
            ParamUtil.setSessionAttr(request, KEY_CONFIRM_RFI, KEY_CONFIRM_RFI_Y);
        }
    }

    public boolean judgeAllCompleted(RfiDisplayDto rfiDisplayDto) {
        for (RfiDataDisplayDto rfiData : rfiDisplayDto.getRfiDataList()) {
            if (rfiData.getCompleted() == Boolean.FALSE) {
                return false;
            }
        }
        return true;
    }
}
