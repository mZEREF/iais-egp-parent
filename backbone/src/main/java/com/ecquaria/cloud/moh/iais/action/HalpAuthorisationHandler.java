package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.security.DefaultAuthorisationHandler;
import sop.webflow.rt.engine5.SOPEngineRequest;

/**
 * HalpAuthorisationHandler
 *
 * @author Jinhua
 * @date 2020/10/28 10:54
 */
@Slf4j
public class HalpAuthorisationHandler extends DefaultAuthorisationHandler {
    @Override
    public void onAuthorisationFailure(HttpServletRequest request, SOPEngineRequest sopReq) {
        log.info("<==== HALP auth ====>");
        AuditTrailDto atd = IaisEGPHelper.getCurrentAuditTrailDto();
        if (atd != null) {
            List<AuditTrailDto> list = IaisCommonUtils.genNewArrayList(1);
            AuditTrailDto ad = MiscUtil.transferEntityDto(atd, AuditTrailDto.class);
            ad.setOperation(AuditTrailConsts.OPERATION_UNAUTHORISED_ACCESS_SOURCES);
            ad.setFunctionName(sopReq.getProcessName());
            list.add(ad);
            AuditLogUtil.callWithEventDriven(list, SpringContextHelper.getContext().getBean(SubmissionClient.class));
        }
    }
}
