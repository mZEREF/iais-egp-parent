package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 10:06
 */
@Slf4j
public class InspectionCheckListValidation extends CheckListCommonValidate implements CustomizeValidator{

    private static String PLACEHOLDER =  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private static String MESSAGE_ERROR_TIME = PLACEHOLDER + MessageUtil.getMessageDesc("UC_INSTA004_ERR005");
    private static String MESSAGE_ERROR_TIME_LATTER = PLACEHOLDER + MessageUtil.getMessageDesc("UC_INSTA004_ERR006");
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        tcuVad(serListDto,errMap);
        //ahocVad(request,errMap);
        fillUpVad(request,errMap);
        otherinfoVad(serListDto,errMap);
        // validate file
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        long maxFile = applicationViewDto.getSystemMaxFileSize()* 1024L;
        List<String> fileTypes = Arrays.asList(applicationViewDto.getSystemFileType().split(","));
        if(serListDto!=null){
            if(serListDto.getAppPremisesSpecialDocDto() != null){
                if( maxFile < serListDto.getAppPremisesSpecialDocDto().getDocSize()){
                    errMap.put("litterFile",MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(applicationViewDto.getSystemMaxFileSize()),"sizeMax"));
                }
                String docName = serListDto.getAppPremisesSpecialDocDto().getDocName();
                if( !StringUtil.isEmpty(docName)){
                    String [] DGroup =  serListDto.getAppPremisesSpecialDocDto().getDocName().split("\\.");
                    String  docNameType = DGroup[DGroup.length-1];
                    boolean noType = false;
                    for(String s : fileTypes){
                        if( s.equalsIgnoreCase(docNameType)){
                            noType = true;
                            break;
                        }
                    }
                    if( !noType){
                        errMap.put("litterFile",MessageUtil.replaceMessage("GENERAL_ERR0018", applicationViewDto.getSystemFileType(),"fileType"));
                    }
                }
            }
        }
        auditVad( request,errMap);
        WebValidationHelper.saveAuditTrailForNoUseResult(applicationViewDto.getApplicationDto(),errMap);
        return errMap;
    }

    private void auditVad(HttpServletRequest request, Map<String, String> errMap){
        ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        if(appViewDto != null && appViewDto.getLicPremisesAuditDto() != null ){
            LicPremisesAuditDto licPremisesAuditDto =  appViewDto.getLicPremisesAuditDto();
            if(licPremisesAuditDto.getInRiskSocre().equals(0)){
                if(StringUtil.isEmpty(licPremisesAuditDto.getIncludeRiskType())){
                    errMap.put("periods",ERR0010);
                }else if(licPremisesAuditDto.getIncludeRiskType().equalsIgnoreCase(ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY)) {
                    if(StringUtil.isEmpty(licPremisesAuditDto.getLgrRemarks())){
                        errMap.put("frameworkRemarks",ERR0010);
                    }else if(licPremisesAuditDto.getIncludeRiskType().length() > 2000){
                        errMap.put("frameworkRemarks",MessageUtil.replaceMessage("UC_INSP_ERR0002","2000","4000"));
                    }
                }
            }
        }
    }

    private void fillUpVad(HttpServletRequest request, Map<String, String> errMap) {
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))) {
            if(! specServiceFillUpVad(request,errMap)){
                errMap.put("fillchkl","UC_INSTA004_ERR009");
            }
        }
        if(!(commFillUpVad(request,errMap)&&serviceFillUpVad(request,errMap)&& adhocFillUpVad(request,errMap))){
            errMap.put("fillchkl","UC_INSTA004_ERR009");
        }
    }

    private boolean  specServiceFillUpVad(HttpServletRequest request,Map<String, String> errMap){
        List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
        if(IaisCommonUtils.isNotEmpty( fDtosDtos)){
            for(InspectionSpecServiceDto inspectionSpecServiceDto : fDtosDtos){
                if( !adhocSpecFillUpVad(errMap,inspectionSpecServiceDto) ||  !serviceSpecFillUpVad(errMap,inspectionSpecServiceDto)){
                    return false;
                }
            }

        }
        return true;
    }

    private boolean adhocSpecFillUpVad(Map<String, String> errMap,InspectionSpecServiceDto inspectionSpecServiceDto){
        if(inspectionSpecServiceDto.getAdchklDto() != null && IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getAdchklDto().getAdItemList())){
            List<AdhocNcCheckItemDto> itemDtoList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                boolean isError = true;
                String identify = StringUtil.getNonNull(inspectionSpecServiceDto.getIdentify());
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    isError = verifyQuestionDto(temp.getAdAnswer(),temp.getRemark(),temp.getNcs(),isError,temp.getId() + identify+"adhoc",errMap);
                }
                return  isError;
            }
        }
        return true;
    }
    private boolean serviceSpecFillUpVad(Map<String, String> errMap,InspectionSpecServiceDto inspectionSpecServiceDto){
        if(!IaisCommonUtils.isEmpty(inspectionSpecServiceDto.getFdtoList())){
                int flagNum = 0;
                if(!IaisCommonUtils.isEmpty(inspectionSpecServiceDto.getFdtoList())){
                    for(InspectionFillCheckListDto fDto:inspectionSpecServiceDto.getFdtoList()){
                        if(!fillServiceVad(fDto,errMap)){
                            flagNum++;
                        };
                    }
                }
                if(flagNum>0){
                    return false;
                }
        }
        return true;
    }

    private boolean adhocFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        if(showDto!=null){
            List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                boolean isError = true;
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    isError = verifyQuestionDto(temp.getAdAnswer(),temp.getRemark(),temp.getNcs(),isError,temp.getId()+"adhoc",errMap);
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
                    isError =  verifyQuestionDto(temp.getChkanswer(),temp.getRemark(),temp.getNcs(),isError,StringUtil.getNonNull(temp.getSectionNameShow()) + temp.getItemId()+"com",errMap);
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
                isError =  verifyQuestionDto(temp.getChkanswer(),temp.getRemark(),temp.getNcs(),isError,fDto.getSubName()+StringUtil.getNonNull(temp.getSectionNameShow())+temp.getItemId(),errMap);
            }
            return  isError;
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
            errMap.put("sTime",MESSAGE_ERROR_TIME);
            return 1;
        }else if(StringUtil.isEmpty(startHour)&&!StringUtil.isEmpty(startMin)){
            errMap.put("sTime",MESSAGE_ERROR_TIME);
            return 1;
        }else{
          /* if("13".equalsIgnoreCase(startHour) ||"18".equalsIgnoreCase(startHour)){
                errMap.put("sTime","UC_INSTA004_ERR009");
                return 1;
            }*/
            return 2;
        }

    }

    private int isNullEndTimeVad(InspectionFDtosDto serListDto, Map<String, String> errMap) {
        String startHour = serListDto.getEndHour();
        String startMin = serListDto.getEndMin();
        if(StringUtil.isEmpty(startHour)&&StringUtil.isEmpty(startMin)){
            return 0;
        }else if(!StringUtil.isEmpty(startHour)&&StringUtil.isEmpty(startMin)){
            errMap.put("eTime",MESSAGE_ERROR_TIME);
            return 1;
        }else if(StringUtil.isEmpty(startHour)&&!StringUtil.isEmpty(startMin)){
            errMap.put("eTime",MESSAGE_ERROR_TIME);
            return 1;
        }else{
          /*  if(("13".equalsIgnoreCase(startHour) && !"00".equalsIgnoreCase(startMin)) ||("18".equalsIgnoreCase(startHour) && !"00".equalsIgnoreCase(startMin))){
                errMap.put("eTime","UC_INSTA004_ERR009");
                return 1;
            }*/
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
                errMap.put("timevad",MESSAGE_ERROR_TIME_LATTER);
            } else if(sh==eh&&sm>em){
                errMap.put("timevad",MESSAGE_ERROR_TIME_LATTER);
            }
        }catch (Exception e){
            log.debug(e.toString());
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
                errMap.put("sTime",MESSAGE_ERROR_TIME);
            }
        }catch (Exception e){
            log.debug(e.toString());
            flagNum++;
            errMap.put("sTime",MESSAGE_ERROR_TIME);
        }
        try {
            int sh = Integer.parseInt(endHour);
            int sm = Integer.parseInt(endMin);
            if(sh>=0&&sh<=24&&sm>=0&&sh<=60){
            }else{
                flagNum++;
                errMap.put("eTime",MESSAGE_ERROR_TIME);
            }
        }catch (Exception e){
            log.debug(e.toString());
            flagNum++;
            errMap.put("eTime",MESSAGE_ERROR_TIME);
        }
        if(flagNum==0){
            return true;
        }
        return false;
    }



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
            errMap.put("tcuDate","SYSPAM_ERROR0008");
            log.debug(e.toString());
        }
        if(icDto.isTcuFlag() && StringUtil.isEmpty(icDto.getTuc())){
            errMap.put("tcuDate",ERR0010);
        }
        String tcuRemark = icDto.getTcuRemark();
        if(tcuRemark!=null&&tcuRemark.length()>300){
            errMap.put("tcuRemark","UC_INSP_ERR0001");
        }
        String bestPractice = icDto.getBestPractice();
        if(bestPractice!=null&&bestPractice.length()>500){
            errMap.put("bestPractice","UC_INSTA004_ERR004");
        }
        String observation = icDto.getObservation();
        if(observation!=null&&observation.length()>500){
            errMap.put("observation","UC_INSTA004_ERR004");
        }
        String otherofficer = icDto.getOtherinspectionofficer();
        if (otherofficer!=null&&otherofficer.length()>300) {
            errMap.put("otherofficer","UC_INSP_ERR0001");
        }
    }

}
