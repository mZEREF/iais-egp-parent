package com.ecquaria.cloud.moh.iais.action;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.engine5.SOPEngineRequest;

/**
 * HalpAuthorisationHandler
 *
 * @author Jinhua
 * @date 2020/10/28 10:54
 */
@Slf4j
public class HalpAuthorisationHandler  {
    public void onAuthorisationFailure(HttpServletRequest request, SOPEngineRequest sopReq) {
        log.info("<==== HALP auth ====>");
    }
}
