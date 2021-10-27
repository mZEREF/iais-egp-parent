package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * EmbryoCreatedStageDtoValidator
 *
 * @author junyu
 * @date 2021/10/26
 */
@Component
@Slf4j
public class EmbryoCreatedStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        Integer transEmbrFreshOccNum =  null;
        try {
            transEmbrFreshOccNum =  ParamUtil.getInt(httpServletRequest, "transEmbrFreshOccNum");
        }catch (Exception e){
            log.error("no int");
        }

        if (transEmbrFreshOccNum==null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("othersReason", errMsg);
        }else if(transEmbrFreshOccNum>99){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("transEmbrFreshOccNum", errMsg);
        }
        Integer poorDevFreshOccNum = null;
        try {
            poorDevFreshOccNum = ParamUtil.getInt(httpServletRequest, "poorDevFreshOccNum");
        }catch (Exception e){
            log.error("no int");
        }
        if (poorDevFreshOccNum==null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("poorDevFreshOccNum", errMsg);
        }else if(poorDevFreshOccNum>99){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("poorDevFreshOccNum", errMsg);
        }
        Integer transEmbrThawOccNum = null;
        try {
            transEmbrThawOccNum =  ParamUtil.getInt(httpServletRequest, "transEmbrThawOccNum");
        }catch (Exception e){
            log.error("no int");
        }
        if (transEmbrThawOccNum == null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("transEmbrThawOccNum", errMsg);
        }else if(transEmbrThawOccNum>99){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("transEmbrThawOccNum", errMsg);
        }
        Integer poorDevThawOccNum = null;
        try {
            poorDevThawOccNum =  ParamUtil.getInt(httpServletRequest, "poorDevThawOccNum");
        }catch (Exception e){
            log.error("no int");
        }
        if (poorDevThawOccNum == null) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","This field", "field");
            errorMap.put("poorDevThawOccNum", errMsg);
        }else if(poorDevThawOccNum>99){
            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
            repMap.put("number","2");
            repMap.put("fieldNo","This field");
            String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
            errorMap.put("poorDevThawOccNum", errMsg);
        }
        return errorMap;
    }
}
