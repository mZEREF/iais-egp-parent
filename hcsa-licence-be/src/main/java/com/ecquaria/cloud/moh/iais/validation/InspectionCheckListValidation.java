package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 10:06
 */
public class InspectionCheckListValidation implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"fillCheckListDto");
        List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
        if(cqDtoList!=null && !cqDtoList.isEmpty()){
            for(InspectionCheckQuestionDto temp:cqDtoList){
                if(StringUtil.isEmpty(temp.getChkanswer())){
                    errMap.put(temp.getSectionName()+temp.getItemId(),"ERR0010");
                }
            }
        }else{
            errMap.put("allList","Please fill in checkList.");
        }
        tcuVad(icDto,errMap);
        commonVad(request,errMap);
        ahocVad(request,errMap);
        return errMap;
    }

    public void commonVad(HttpServletRequest request,Map<String, String> errMap){
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
        if(cqDtoList!=null && !cqDtoList.isEmpty()){
            for(InspectionCheckQuestionDto temp:cqDtoList){
                if(StringUtil.isEmpty(temp.getChkanswer())){
                    errMap.put(temp.getSectionName()+temp.getItemId()+"com","ERR0010");
                }
            }
        }else{
            errMap.put("allList","Please fill in checkList.");
        }
    }
    public void ahocVad(HttpServletRequest request,Map<String, String> errMap){
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
        if(itemDtoList!=null && !itemDtoList.isEmpty()){
            for(AdhocNcCheckItemDto temp:itemDtoList){
                if(StringUtil.isEmpty(temp.getAdAnswer())){
                    errMap.put(temp.getId()+"adhoc","ERR0010");
                }
            }
        }
    }
    public void tcuVad(InspectionFillCheckListDto icDto,Map<String, String> errMap){
        try {
            String dateStr = icDto.getTuc();
            if(!StringUtil.isEmpty(dateStr)){
                Date tcuDate = Formatter.parseDate(dateStr);
                if(tcuDate.getTime()< System.currentTimeMillis()){
                    errMap.put("tcuDate","UC_INSTA004_ERR002");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errMap.put("allList","UC_INSTA004_ERR003");
        }
    }

}
