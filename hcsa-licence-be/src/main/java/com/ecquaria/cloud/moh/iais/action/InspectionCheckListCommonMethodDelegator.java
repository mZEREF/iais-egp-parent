package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListItemValidate;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangyu
 * @Date: 2021/5/16
 */
@Delegator(value = "inspectionCheckListCommonMethodDelegator")
@Slf4j
public class InspectionCheckListCommonMethodDelegator {
    @Autowired
    private FillupChklistService fillupChklistService;
    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AdhocChecklistService adhocChecklistService;
    private static final String SERLISTDTO="serListDto";
    private static final String COMMONDTO="commonDto";
    private static final String ADHOCLDTO="adchklDto";
    private static final String TASKDTO="taskDto";
    private static final String APPLICATIONVIEWDTO = "applicationViewDto";
    private static final String CHECKLISTFILEDTO = "checkListFileDto";
    private static final String TASKDTOLIST = "InspectionNcCheckListDelegator_taskDtoList";
    private static final String INSPECTION_ADHOC_CHECKLIST_LIST_ATTR  = "inspection_adhoc_checklist_list_attr";
    private static final String INSPECTION_USERS = "inspectorsParticipant";
    private static final String INSPECTION_USER_FINISH = "inspectorUserFinishChecklistId";
    private static final String ACTION_ADHOC_OWN = "action_adhoc_own";
    private static final String BEFORE_FINISH_CHECK_LIST = "inspectionNcCheckListDelegator_before_finish_check_list";
    private static final String MOBILE_REMARK_GROUP = "mobile_remark_group";


    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COMMONDTO);
        if(cDto!=null&&cDto.getCheckList()!=null&&!cDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
            for(InspectionCheckQuestionDto temp:checkListDtoList){
                String answer = ParamUtil.getString(request,temp.getSectionNameShow()+temp.getItemId()+"comrad");
                temp.setChkanswer(answer);
                String remark = ParamUtil.getString(request,temp.getSectionNameShow()+temp.getItemId()+"comremark");
                String rectified = ParamUtil.getString(request,temp.getSectionNameShow()+temp.getItemId()+"comrec");
                String ncs  = ParamUtil.getString(request,temp.getSectionNameShow()+temp.getItemId()+"comFindNcs");
                temp.setNcs(ncs);
                if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
                    temp.setRectified(true);
                }else{
                    temp.setRectified(false);
                }
                temp.setRemark(remark);
            }
            fillupChklistService.fillInspectionFillCheckListDto(cDto);
        }
        return cDto;
    }


    public void getSpecServiceCheckListDataFormViewPage(HttpServletRequest request){
        List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
        if(IaisCommonUtils.isNotEmpty( fDtosDtos)){
            for(InspectionSpecServiceDto inspectionSpecServiceDto : fDtosDtos){
                if(!IaisCommonUtils.isEmpty(inspectionSpecServiceDto.getFdtoList())){
                    for(InspectionFillCheckListDto fdto: inspectionSpecServiceDto.getFdtoList()){
                        if(IaisCommonUtils.isNotEmpty(fdto.getCheckList())){
                            List<InspectionCheckQuestionDto> checkListDtoList = fdto.getCheckList();
                            for(InspectionCheckQuestionDto temp:checkListDtoList){
                                getServiceData(temp,fdto,request);
                            }
                            fillupChklistService.fillInspectionFillCheckListDto(fdto);
                        }
                    }
                }
                getAdhocSpecDtoFromPage(request,inspectionSpecServiceDto);
            }

        }
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,(Serializable) fDtosDtos);
    }

    public void getAdhocSpecDtoFromPage(HttpServletRequest request,InspectionSpecServiceDto inspectionSpecServiceDto){
        if(inspectionSpecServiceDto.getAdchklDto() != null && IaisCommonUtils.isNotEmpty(inspectionSpecServiceDto.getAdchklDto().getAdItemList())){
            List<AdhocNcCheckItemDto> itemDtoList = inspectionSpecServiceDto.getAdchklDto().getAdItemList();
            String identify = inspectionSpecServiceDto.getIdentify();
                for(AdhocNcCheckItemDto temp:itemDtoList){
                    String  prefix = temp.getId() + identify;
                    String answer = ParamUtil.getString(request, prefix +"adhocrad");
                    String remark = ParamUtil.getString(request, prefix +"adhocremark");
                    String rec = ParamUtil.getString(request, prefix +"adhocrec");
                    String ncs  = ParamUtil.getString(request, prefix +"adhocFindNcs");
                    temp.setNcs(ncs);
                    temp.setAdAnswer(answer);
                    temp.setRemark(remark);
                    if("No".equals(answer)&&!StringUtil.isEmpty(rec)){
                        temp.setRectified(Boolean.TRUE);
                    }else{
                        temp.setRectified(Boolean.FALSE);
                    }
                }
            inspectionSpecServiceDto.getAdchklDto().setAdItemList(itemDtoList);
        }

    }


    public void getServiceData(InspectionCheckQuestionDto temp,InspectionFillCheckListDto fdto,HttpServletRequest request){
        String answer = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"rad");
        temp.setChkanswer(answer);
        String remark = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"remark");
        String rectified = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"rec");
        String ncs = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"FindNcs");
        temp.setNcs(ncs);
        if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
            temp.setRectified(true);
        }else{
            temp.setRectified(false);
        }
        temp.setRemark(remark);
    }

    public AdCheckListShowDto getAdhocDtoFromPage(HttpServletRequest request){
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADHOCLDTO);
        List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
        if(itemDtoList!=null && !itemDtoList.isEmpty()){
            for(AdhocNcCheckItemDto temp:itemDtoList){
                String answer = ParamUtil.getString(request,temp.getId()+"adhocrad");
                String remark = ParamUtil.getString(request,temp.getId()+"adhocremark");
                String rec = ParamUtil.getString(request,temp.getId()+"adhocrec");
                String ncs  = ParamUtil.getString(request,temp.getId()+"adhocFindNcs");
                temp.setNcs(ncs);
                temp.setAdAnswer(answer);
                temp.setRemark(remark);
                if("No".equals(answer)&&!StringUtil.isEmpty(rec)){
                    temp.setRectified(Boolean.TRUE);
                }else{
                    temp.setRectified(Boolean.FALSE);
                }
            }
        }
        showDto.setAdItemList(itemDtoList);
        return showDto;
    }

    public InspectionFDtosDto getServiceCheckListDataFormViewPage(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fdto:serListDto.getFdtoList()){
                if(fdto!=null&&!IaisCommonUtils.isEmpty(fdto.getCheckList())){
                    List<InspectionCheckQuestionDto> checkListDtoList = fdto.getCheckList();
                    for(InspectionCheckQuestionDto temp:checkListDtoList){
                        getServiceData(temp,fdto,request);
                    }
                    fillupChklistService.fillInspectionFillCheckListDto(fdto);
                }
            }
        }
        return serListDto;
    }

}
