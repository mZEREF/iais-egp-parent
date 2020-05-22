package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.esotericsoftware.minlog.Log;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 10:06
 */
@Slf4j
public class InspectionCheckListItemValidate implements CustomizeValidator {
    private static final String ERR0010 = "ERR0010";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        fillUpVad(request,errMap);
        return errMap;
    }

    private void fillUpVad(HttpServletRequest request, Map<String, String> errMap) {
        if(!(commFillUpVad(request, errMap)&&serviceFillUpVad(request, errMap)&& adhocFillUpVad(request, errMap))){
            errMap.put("fillchkl","UC_INSTA004_ERR008");
        }
    }

    private boolean adhocFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        if(showDto!=null){
            List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                boolean isError = true;
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    if(StringUtil.isEmpty(temp.getAdAnswer())){
                        errMap.put(temp.getId()+"adhoc",MessageUtil.replaceMessage(ERR0010,"Yes No N/A","The field"));
                        if(isError)
                            isError = false;
                    } else if(!"Yes".equalsIgnoreCase(temp.getAdAnswer()) && StringUtil.isEmpty(temp.getRemark())){
                        errMap.put(temp.getId()+"adhoc",MessageUtil.replaceMessage(ERR0010,"Remark","The field"));
                        if(isError)
                            isError = false;
                    }
                }
                return  isError;
            }
        }
        return true;
    }

    private boolean serviceFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        int flagNum = 0;
        if(serListDto!=null&&!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fDto:serListDto.getFdtoList()){
                if(!fillServiceVad(fDto,errMap)){
                    flagNum++;
                };
            }
        }
        if(flagNum>0){
            return false;
        }
        return true;
    }

    private boolean commFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if (icDto != null) {
            boolean isError = true;
            List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
            if(cqDtoList!=null && !cqDtoList.isEmpty()){
                for(InspectionCheckQuestionDto temp:cqDtoList){
                    if(StringUtil.isEmpty(temp.getChkanswer())){
                        errMap.put(temp.getSectionNameShow()+temp.getItemId()+"com",MessageUtil.replaceMessage(ERR0010,"Yes No N/A","The field"));
                        if(isError)
                        isError = false;
                    }else if(!"Yes".equalsIgnoreCase(temp.getChkanswer()) && StringUtil.isEmpty(temp.getRemark())){
                        errMap.put(temp.getSectionNameShow()+temp.getItemId()+"com",MessageUtil.replaceMessage(ERR0010,"Remark","The field"));
                        if(isError)
                            isError = false;
                    }
                }
            }
            return isError;
        }
        return true;
    }

    public boolean fillServiceVad(InspectionFillCheckListDto fDto,Map<String, String> errMap){
        List<InspectionCheckQuestionDto> cqDtoList = fDto.getCheckList();
        if(!IaisCommonUtils.isEmpty(cqDtoList)){
            boolean isError = true;
            for(InspectionCheckQuestionDto temp:cqDtoList){
                if(StringUtil.isEmpty(temp.getChkanswer())){
                    errMap.put(fDto.getSubName()+temp.getSectionNameShow()+temp.getItemId(),MessageUtil.replaceMessage(ERR0010,"Yes No N/A","The field"));
                    if(isError)
                        isError = false;
                }else if(!"Yes".equalsIgnoreCase(temp.getChkanswer()) && StringUtil.isEmpty(temp.getRemark())){
                    errMap.put(fDto.getSubName()+temp.getSectionNameShow()+temp.getItemId(),MessageUtil.replaceMessage(ERR0010,"Remark","The field"));
                    if(isError)
                        isError = false;
                }
            }
            return  isError;
        }
        return true;
    }


}
