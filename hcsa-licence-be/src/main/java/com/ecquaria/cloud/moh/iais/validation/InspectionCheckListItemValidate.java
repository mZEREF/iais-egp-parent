package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 10:06
 */
@Slf4j
public class InspectionCheckListItemValidate extends CheckListCommonValidate implements CustomizeValidator {
    private static final String SUBMIT_CHECK_LIST = "sumbit_check_list_pend_ins";
    private static final String MESSAGE_TAG_DRAFT = "Draft" ;
    private static final String DECONFLICT  = "deconflict_check_list";
    public static final String NEXT_ACTION = "next";

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        setSubmitCheckList(request);
        fillUpVad(request,errMap);
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        ParamUtil.setSessionAttr(request,SUBMIT_CHECK_LIST,null);
        ParamUtil.setSessionAttr(request,DECONFLICT,null);
        return errMap;
    }

    private void setSubmitCheckList(HttpServletRequest request){
        String doSubmitAction = ParamUtil.getString(request,"doSubmitAction");
        if(!NEXT_ACTION.equalsIgnoreCase(doSubmitAction)){
            List<OrgUserDto> orgUserDtoUsers = (List<OrgUserDto>) ParamUtil.getSessionAttr(request,"inspectorsParticipant");
            if(!IaisCommonUtils.isEmpty(orgUserDtoUsers) && orgUserDtoUsers.size() > 1){
                ParamUtil.setSessionAttr(request,SUBMIT_CHECK_LIST,IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            }
        }else if(NEXT_ACTION.equalsIgnoreCase(doSubmitAction)){
            List<OrgUserDto> orgUserDtoUsers = (List<OrgUserDto>) ParamUtil.getSessionAttr(request,"inspectorsParticipant");
            if(!IaisCommonUtils.isEmpty(orgUserDtoUsers) && orgUserDtoUsers.size() > 1){
                ParamUtil.setSessionAttr(request,DECONFLICT,AppConsts.YES);
            }
        }
    }

    private void fillUpVad(HttpServletRequest request, Map<String, String> errMap) {
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            if(!specServiceFillUpVad(request, errMap)){
                errMap.put("fillchkl","UC_INSTA004_ERR009");
            }
        }
        if(!( serviceFillUpVad(request, errMap)& adhocFillUpVad(request, errMap)&commFillUpVad(request, errMap))){
            errMap.put("fillchkl","UC_INSTA004_ERR009");
        }
    }

    private boolean  specServiceFillUpVad(HttpServletRequest request,Map<String, String> errMap){
        List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
        if(IaisCommonUtils.isNotEmpty( fDtosDtos)){
            boolean  noError = true;
            for(InspectionSpecServiceDto inspectionSpecServiceDto : fDtosDtos){
                if( !adhocSpecFillUpVad(request,errMap,inspectionSpecServiceDto) | !serviceSpecFillUpVad(request,errMap,inspectionSpecServiceDto)){
                    if(noError){
                        noError = false;
                        ParamUtil.setSessionAttr(request,"errorTab","ServiceInfo"+inspectionSpecServiceDto.getIdentify());
                    }
                }
            }
            return noError;
        }
        return true;
    }

    private boolean adhocSpecFillUpVad(HttpServletRequest request,Map<String, String> errMap,InspectionSpecServiceDto inspectionSpecServiceDto){
        if(inspectionSpecServiceDto.getAdchklDto() != null && IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getAdchklDto().getAdItemList())){
            List<AdhocNcCheckItemDto> itemDtoList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                String draftTag =  getErrorMessageDraftStringTag(request);
                boolean isError = true;
                boolean moreIns = isMoreIns(request);
                String identify = StringUtil.getNonNull(inspectionSpecServiceDto.getIdentify());
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    isError= verifyQuestionDto(temp.getAdAnswer(),temp.getRemark(),temp.getNcs(),isError,temp.getId() + identify + draftTag +"adhoc",errMap,moreIns);
                }
                return  isError;
            }
        }
        return true;
    }

    private boolean isMoreIns(HttpServletRequest request){
        return AppConsts.YES.equalsIgnoreCase((String)ParamUtil.getSessionAttr(request,DECONFLICT));
    }

    private boolean serviceSpecFillUpVad(HttpServletRequest request,Map<String, String> errMap,InspectionSpecServiceDto inspectionSpecServiceDto){
        if(!IaisCommonUtils.isEmpty(inspectionSpecServiceDto.getFdtoList())){
            int flagNum = 0;
            if(!IaisCommonUtils.isEmpty(inspectionSpecServiceDto.getFdtoList())){
                for(InspectionFillCheckListDto fDto:inspectionSpecServiceDto.getFdtoList()){
                    if(!fillServiceVad(request,fDto,errMap)){
                        flagNum++;
                    }
                }
            }
            if(flagNum>0){
                return false;
            }
        }
        return true;
    }

    private boolean serviceFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        int flagNum = 0;
        if(serListDto!=null&&!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fDto:serListDto.getFdtoList()){
                if(!fillServiceVad(request,fDto,errMap)){
                    flagNum++;
                };
            }
        }
        if(flagNum>0){
            ParamUtil.setSessionAttr(request,"errorTab","ServiceInfo");
            return false;
        }
        return true;
    }

    private boolean commFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if (icDto != null) {
            boolean isError = true;
            List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
            String draftTag =  getErrorMessageDraftStringTag(request);
            boolean moreIns = isMoreIns(request);
            if(cqDtoList!=null && !cqDtoList.isEmpty()){
                for(InspectionCheckQuestionDto temp:cqDtoList){
                    temp = getTempByMap(request,temp);
                    isError = verifyQuestionDto(temp.getChkanswer(),temp.getRemark(),temp.getNcs(),isError,StringUtil.getNonNull(temp.getSectionNameShow()) + temp.getItemId()+draftTag+"com",errMap,moreIns);
                }
                if( !isError){
                    ParamUtil.setSessionAttr(request,"errorTab","General");
                }
            }
            return isError;
        }
        return true;
    }

    public boolean fillServiceVad(HttpServletRequest request,InspectionFillCheckListDto fDto,Map<String, String> errMap){
        List<InspectionCheckQuestionDto> cqDtoList = fDto.getCheckList();
        if(!IaisCommonUtils.isEmpty(cqDtoList)){
            boolean isError = true;
            String draftTag =  getErrorMessageDraftStringTag(request);
            boolean moreIns = isMoreIns(request);
            for(InspectionCheckQuestionDto temp:cqDtoList){
                temp = getTempByMap(request,temp);
                isError = verifyQuestionDto(temp.getChkanswer(),temp.getRemark(),temp.getNcs(),isError,fDto.getSubName() + StringUtil.getNonNull(temp.getSectionNameShow()) + temp.getItemId()+draftTag,errMap,moreIns);
            }
            return  isError;
        }
        return true;
    }

    private  String getErrorMessageDraftStringTag(HttpServletRequest request){
        String submitUser = (String) ParamUtil.getSessionAttr(request,SUBMIT_CHECK_LIST);
        if( !StringUtil.isEmpty(submitUser)){
            return submitUser+MESSAGE_TAG_DRAFT;
        }
        return "";
    }

    private InspectionCheckQuestionDto getTempByMap(HttpServletRequest request,InspectionCheckQuestionDto temp){
        String submitUser = (String) ParamUtil.getSessionAttr(request,SUBMIT_CHECK_LIST);
        if( !StringUtil.isEmpty(submitUser)){
            Map<String, AnswerForDifDto> answerForDifDtoMaps = temp.getAnswerForDifDtoMaps();
            if(!IaisCommonUtils.isEmpty(answerForDifDtoMaps)){
                AnswerForDifDto answerForDifDto = answerForDifDtoMaps.get(submitUser);
                if(answerForDifDto != null){
                    String sectionNameShow =  temp.getSectionNameShow();
                    String itemId = temp.getItemId();
                    temp = MiscUtil.transferEntityDto(answerForDifDto,InspectionCheckQuestionDto.class);
                    temp.setChkanswer( answerForDifDto .getAnswer());
                    temp.setItemId(itemId);
                    temp.setSectionNameShow(sectionNameShow);
                }
            }
        }
        return temp;
    }
    private boolean adhocFillUpVad(HttpServletRequest request,Map<String, String> errMap) {
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        if(showDto!=null){
            List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
            if(itemDtoList!=null && !itemDtoList.isEmpty()){
                boolean isError = true;
                String draftTag =  getErrorMessageDraftStringTag(request);
                boolean moreIns = isMoreIns(request);
                for(AdhocNcCheckItemDto temp:itemDtoList){
                 String submitUser = (String) ParamUtil.getSessionAttr(request,SUBMIT_CHECK_LIST);
                 if( !StringUtil.isEmpty(submitUser)){
                     Map<String, AnswerForDifDto> answerForDifDtoMaps = temp.getAnswerForDifDtoMaps();
                     if(!IaisCommonUtils.isEmpty(answerForDifDtoMaps)){
                         AnswerForDifDto answerForDifDto = answerForDifDtoMaps.get(submitUser);
                         if(answerForDifDto != null){
                             String id = temp.getId();
                             temp = MiscUtil.transferEntityDto(answerForDifDto,AdhocNcCheckItemDto.class);
                             temp.setId(id);
                             temp.setAdAnswer( answerForDifDto .getAnswer());
                         }
                     }
                 }
                    isError=  verifyQuestionDto(temp.getAdAnswer(),temp.getRemark(),temp.getNcs(),isError,temp.getId()+draftTag+"adhoc",errMap,moreIns);
                }
                if(!isError){
                    ParamUtil.setSessionAttr(request,"errorTab","ServiceInfo");
                }
                return  isError;
            }
        }
        return true;
    }


}
