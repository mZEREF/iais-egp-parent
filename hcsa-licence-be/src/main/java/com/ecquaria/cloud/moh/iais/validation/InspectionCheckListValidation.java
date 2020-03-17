package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.esotericsoftware.minlog.Log;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 10:06
 */
public class InspectionCheckListValidation implements CustomizeValidator {
    private static final String ERR0010 = "ERR0010";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        if(serListDto!=null&&!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fDto:serListDto.getFdtoList()){
                getServiceMsg(fDto,errMap);
            }
        }
        tcuVad(serListDto,errMap);
        commonVad(request,errMap);
        //ahocVad(request,errMap);
        fillUpVad(request,errMap);
        otherinfoVad(serListDto,errMap);
        return errMap;
    }

    private void fillUpVad(HttpServletRequest request, Map<String, String> errMap) {
        if(commFillUpVad(request)&&serviceFillUpVad(request)&& adhocFillUpVad(request)){

        }else{
            errMap.put("fillchkl","UC_INSTA004_ERR008");
        }
    }

    private boolean adhocFillUpVad(HttpServletRequest request) {
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        if(showDto!=null){
            List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    if(StringUtil.isEmpty(temp.getAdAnswer())){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean serviceFillUpVad(HttpServletRequest request) {
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        int flagNum = 0;
        if(serListDto!=null&&!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fDto:serListDto.getFdtoList()){
                if(!fillServiceVad(fDto)){
                    flagNum++;
                };
            }
        }
        if(flagNum>0){
            return false;
        }
        return true;
    }

    private boolean commFillUpVad(HttpServletRequest request) {
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if (icDto != null) {
            List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
            if(cqDtoList!=null && !cqDtoList.isEmpty()){
                for(InspectionCheckQuestionDto temp:cqDtoList){
                    if(StringUtil.isEmpty(temp.getChkanswer())){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private void otherinfoVad(InspectionFDtosDto serListDto, Map<String, String> errMap) {
        int startFlagNum = isNullStartTimeVad(serListDto,errMap);
        int endFLagNum = isNullEndTimeVad(serListDto,errMap);
        if(startFlagNum==2&&endFLagNum==2){
            boolean timeflag = timeVad(serListDto,errMap);
            if(timeflag){
                timeLaterVad(serListDto,errMap);
            }
        }
    }

    private int isNullStartTimeVad(InspectionFDtosDto serListDto, Map<String, String> errMap) {
        String startHour = serListDto.getStartHour();
        String startMin = serListDto.getStartMin();
        if(StringUtil.isEmpty(startHour)&&StringUtil.isEmpty(startMin)){
            return 0;
        }else if(!StringUtil.isEmpty(startHour)&&StringUtil.isEmpty(startMin)){
            errMap.put("sTime","UC_INSTA004_ERR005");
            return 1;
        }else if(StringUtil.isEmpty(startHour)&&!StringUtil.isEmpty(startMin)){
            errMap.put("sTime","UC_INSTA004_ERR005");
            return 1;
        }else{
            return 2;
        }

    }

    private int isNullEndTimeVad(InspectionFDtosDto serListDto, Map<String, String> errMap) {
        String startHour = serListDto.getEndHour();
        String startMin = serListDto.getEndMin();
        if(StringUtil.isEmpty(startHour)&&StringUtil.isEmpty(startMin)){
            return 0;
        }else if(!StringUtil.isEmpty(startHour)&&StringUtil.isEmpty(startMin)){
            errMap.put("eTime","UC_INSTA004_ERR005");
            return 1;
        }else if(StringUtil.isEmpty(startHour)&&!StringUtil.isEmpty(startMin)){
            errMap.put("eTime","UC_INSTA004_ERR005");
            return 1;
        }else{
            return 2;
        }

    }

    private void timeLaterVad(InspectionFDtosDto serListDto, Map<String, String> errMap) {
        String startHour = serListDto.getStartHour();
        String startMin = serListDto.getStartMin();
        String endHour = serListDto.getEndHour();
        String endMin = serListDto.getEndMin();
        try {
            int sh = Integer.parseInt(startHour);
            int sm = Integer.parseInt(startMin);
            int eh = Integer.parseInt(endHour);
            int em = Integer.parseInt(endMin);
            if(sh>eh){
                errMap.put("timevad","UC_INSTA004_ERR006");
            } else if(sh==eh&&sm>em){
                errMap.put("timevad","UC_INSTA004_ERR006");
            }
        }catch (Exception e){
            Log.debug(e.toString());
        }

    }



    private boolean timeVad(InspectionFDtosDto serListDto, Map<String, String> errMap) {
        int flagNum = 0;
        String startHour = serListDto.getStartHour();
        String startMin = serListDto.getStartMin();
        String endHour = serListDto.getEndHour();
        String endMin = serListDto.getEndMin();
        try {
            int sh = Integer.parseInt(startHour);
            int sm = Integer.parseInt(startMin);
            if(sh>=0&&sh<=24&&sm>=0&&sm<=60){
            }else{
                flagNum++;
                errMap.put("sTime","UC_INSTA004_ERR005");
            }
        }catch (Exception e){
            Log.debug(e.toString());
            flagNum++;
            errMap.put("sTime","UC_INSTA004_ERR005");
        }
        try {
            int sh = Integer.parseInt(endHour);
            int sm = Integer.parseInt(endMin);
            if(sh>=0&&sh<=24&&sm>=0&&sh<=60){
            }else{
                flagNum++;
                errMap.put("eTime","UC_INSTA004_ERR005");
            }
        }catch (Exception e){
            Log.debug(e.toString());
            flagNum++;
            errMap.put("eTime","UC_INSTA004_ERR005");
        }
        if(flagNum==0){
            return true;
        }
        return false;
    }

    public boolean fillServiceVad(InspectionFillCheckListDto fDto){
        List<InspectionCheckQuestionDto> cqDtoList = fDto.getCheckList();
        if(!IaisCommonUtils.isEmpty(cqDtoList)){
            for(InspectionCheckQuestionDto temp:cqDtoList){
                if(StringUtil.isEmpty(temp.getChkanswer())){
                   return false;
                }
            }
        }
        return true;
    }

    public void getServiceMsg(InspectionFillCheckListDto fDto,Map<String, String> errMap){
        List<InspectionCheckQuestionDto> cqDtoList = fDto.getCheckList();
        if(!IaisCommonUtils.isEmpty(cqDtoList)){
            for(InspectionCheckQuestionDto temp:cqDtoList){
                if(StringUtil.isEmpty(temp.getChkanswer())){
                    errMap.put(fDto.getSubName()+temp.getSectionNameSub()+temp.getItemId(),ERR0010);
                }
            }
        }
    }

    public void commonVad(HttpServletRequest request,Map<String, String> errMap){
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if (icDto != null) {
            List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
            if(cqDtoList!=null && !cqDtoList.isEmpty()){
                for(InspectionCheckQuestionDto temp:cqDtoList){
                    if(StringUtil.isEmpty(temp.getChkanswer())){
                        errMap.put(temp.getSectionNameSub()+temp.getItemId()+"com",ERR0010);
                    }
                }
            }else{
                errMap.put("allList","Please fill in checkList.");
            }
        }
    }
//    public void ahocVad(HttpServletRequest request,Map<String, String> errMap){
//        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
//        if(showDto!=null){
//            List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
//            if(itemDtoList!=null && !itemDtoList.isEmpty()){
//                for(AdhocNcCheckItemDto temp:itemDtoList){
//                    if(StringUtil.isEmpty(temp.getAdAnswer())){
//                        errMap.put(temp.getId()+"adhoc",ERR0010);
//                    }
//                }
//            }
//        }
//    }
    public void tcuVad(InspectionFDtosDto icDto,Map<String, String> errMap){
        try {
            String dateStr = icDto.getTuc();
            if(!StringUtil.isEmpty(dateStr)){
                Date tcuDate = Formatter.parseDate(dateStr);
                if(tcuDate.getTime()< System.currentTimeMillis()){
                    errMap.put("tcuDate","UC_INSTA004_ERR002");
                }
            }
        }catch (Exception e){
            errMap.put("tcuDate","UC_CHKLMD001_ERR003");
            Log.debug(e.toString());
        }
        String tcuRemark = icDto.getTcuRemark();
        if(tcuRemark!=null&&tcuRemark.length()>300){
            errMap.put("tcuRemark","UC_INSP_ERR0001");
        }
        String bestPractice = icDto.getBestPractice();
        if(bestPractice!=null&&bestPractice.length()>500){
            errMap.put("bestPractice","UC_INSTA004_ERR004");
        }
        String otherofficer = icDto.getOtherinspectionofficer();
        if (otherofficer!=null&&otherofficer.length()>300) {
            errMap.put("otherofficer","UC_INSP_ERR0001");
        }
    }

}
