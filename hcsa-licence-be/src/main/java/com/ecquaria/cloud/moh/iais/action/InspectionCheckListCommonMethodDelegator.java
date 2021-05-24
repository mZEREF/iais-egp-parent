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

    public void  getSpecServiceCheckListMoreData(HttpServletRequest request){
        List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        if(IaisCommonUtils.isNotEmpty( fDtosDtos)){
            for(InspectionSpecServiceDto inspectionSpecServiceDto : fDtosDtos){
                getOneServiceMoreData( request,inspectionSpecServiceDto.getFdtoList(),userId);
                getAhocCheckListMoreData(request,inspectionSpecServiceDto.getAdchklDto(),inspectionSpecServiceDto.getIdentify(),userId);
            }

        }
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,(Serializable) fDtosDtos);
    }

    public void getOneServiceMoreData(HttpServletRequest request,List<InspectionFillCheckListDto> inspectionFillCheckListDtos,String userId){
        if(!IaisCommonUtils.isEmpty(inspectionFillCheckListDtos)){
            for(InspectionFillCheckListDto inspectionFillCheckListDto : inspectionFillCheckListDtos){
                List<InspectionCheckQuestionDto> checkList = inspectionFillCheckListDto.getCheckList();
                for(InspectionCheckQuestionDto inspectionCheckQuestionDto : checkList){
                    String prefix = inspectionFillCheckListDto.getSubName()+ inspectionCheckQuestionDto.getSectionNameShow()+inspectionCheckQuestionDto.getItemId();
                    List<AnswerForDifDto> answerForDifDtos = inspectionCheckQuestionDto.getAnswerForDifDtos();
                    int index = 0;
                    for(AnswerForDifDto answerForDifDto : answerForDifDtos){
                        if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                            String answer =  ParamUtil.getString(request,prefix +"radIns" + index);
                            String remark = ParamUtil.getString(request,prefix +"remarkIns" + index);
                            String raf =  ParamUtil.getString(request,prefix +"recIns" + index);
                            String ncs = ParamUtil.getString(request,prefix +"FindNcsIns" + index);
                            answerForDifDto.setNcs(ncs);
                            answerForDifDto.setAnswer(answer);
                            answerForDifDto.setRemark(remark);
                            if("No".equalsIgnoreCase(answer) && "rec".equalsIgnoreCase(raf)){
                                answerForDifDto.setIsRec("1");
                            }else {
                                answerForDifDto.setIsRec("0");
                            }
                            break;
                        }
                        index++;
                    }
                    String deconflict = ParamUtil.getString(request,prefix +"Deconflict");
                    fillupChklistService.setInspectionCheckQuestionDtoByAnswerForDifDtosAndDeconflict(inspectionCheckQuestionDto,answerForDifDtos,deconflict);
                }
            }
        }
    }

    public void getAhocCheckListMoreData(HttpServletRequest request,AdCheckListShowDto adchklDto,String identify,String userId){
        if( adchklDto == null){
            return;
        }
        List<AdhocNcCheckItemDto> adItemList = adchklDto.getAdItemList();
        if( !IaisCommonUtils.isEmpty( adItemList)){
            for(AdhocNcCheckItemDto temp:adItemList){
                List<AnswerForDifDto> adhocAnswerForDifDtos = temp.getAdhocAnswerForDifDtos();
                String prefix = temp.getId()+identify;
                int index = 0;
                for(AnswerForDifDto answerForDifDto : adhocAnswerForDifDtos){
                    if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                        String answer = ParamUtil.getString(request,prefix+"adhocradIns"+index);
                        String remark = ParamUtil.getString(request,prefix+"adhocremarkIns" + index);
                        String rec = ParamUtil.getString(request,prefix+"adhocrecIns"+index);
                        String ncs = ParamUtil.getString(request,prefix+"adhocFindNcsIns" + index);
                        answerForDifDto.setNcs(ncs);
                        answerForDifDto.setAnswer(answer);
                        answerForDifDto.setRemark(remark);
                        if("No".equalsIgnoreCase(answer) && "rec".equalsIgnoreCase(rec)){
                            answerForDifDto.setIsRec("1");
                        }else {
                            answerForDifDto.setIsRec("0");
                        }
                        break;
                    }
                    index++;
                }
                String deconflict = ParamUtil.getString(request,prefix +"adhocDeconflict");
                fillupChklistService.setAdhocNcCheckItemDtoByAnswerForDifDtosAndDeconflict(temp,adhocAnswerForDifDtos,deconflict);
            }
        }
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
        String remark = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"remark");
        String rectified = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"rec");
        String ncs = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"FindNcs");
        temp.setNcs(ncs);
        if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
            temp.setRectified(true);
        }else{
            temp.setRectified(false);
        }
        temp.setChkanswer(answer);
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
    public void  clearSessionForStartCheckList(HttpServletRequest request){
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,AppConsts.NO);
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,null);
        ParamUtil.setSessionAttr(request, ADHOCLDTO, null);
        ParamUtil.setSessionAttr(request, COMMONDTO, null);
        ParamUtil.setSessionAttr(request, TASKDTO, null);
        ParamUtil.setSessionAttr(request, APPLICATIONVIEWDTO, null);
        ParamUtil.setSessionAttr(request, CHECKLISTFILEDTO, null);
        ParamUtil.setSessionAttr(request, TASKDTOLIST, null);
        ParamUtil.setSessionAttr(request, INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(request,INSPECTION_USERS,null);
        ParamUtil.setSessionAttr(request,INSPECTION_USER_FINISH,null);
        ParamUtil.setSessionAttr(request,BEFORE_FINISH_CHECK_LIST,AppConsts.NO);
        ParamUtil.setSessionAttr(request, MOBILE_REMARK_GROUP,null);
    }


    public InspectionFDtosDto getOtherInfo(MultipartHttpServletRequest request) throws IOException {
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        String tcuflag = ParamUtil.getString(request,"tcuType");
        String tcu = null;
        if(!StringUtil.isEmpty(tcuflag)){
            tcu = ParamUtil.getString(request,"tuc");
        }
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        String otherOfficers = ParamUtil.getString(request,"otherinspector");
        serListDto.setRemarksForHistory( ParamUtil.getString(request,"RemarksForHistory"));
        //startHour   startHourMin  endHour endHourMin
        String inspectionDate = ParamUtil.getString(request,"inspectionDate");
        String startHour = ParamUtil.getString(request,"startHour");
        String startMin = ParamUtil.getString(request,"startHourMin");
        String endHour = ParamUtil.getString(request,"endHour");
        String endMin = ParamUtil.getString(request,"endHourMin");
        String startTime = startHour+" : "+startMin;
        String endTime =  endHour+" : "+endMin;
        serListDto.setStartTime(startTime);
        serListDto.setEndTime(endTime);
        serListDto.setStartHour(startHour);
        serListDto.setEndHour(endHour);
        serListDto.setStartMin(startMin);
        serListDto.setEndMin(endMin);
        serListDto.setInspectionDate(inspectionDate);
        serListDto.setOtherinspectionofficer(otherOfficers);
        serListDto.setTcuRemark(tcuremark);
        if(!StringUtil.isEmpty(tcuflag)){
            serListDto.setTcuFlag(true);
            serListDto.setTuc(tcu);
        }else{
            serListDto.setTcuFlag(false);
            serListDto.setTuc(null);
        }
        serListDto.setBestPractice(bestpractice);

        // set litter file
        String litterFile =  ParamUtil.getString(request,"litterFile" );
        if(!StringUtil.isEmpty(litterFile)){
            String litterFileId =  ParamUtil.getString(request,"litterFileId" );
            CommonsMultipartFile file= (CommonsMultipartFile) request.getFile("selectedFileView");
            if(StringUtil.isEmpty(litterFileId) && file != null && file.getSize() != 0){
                if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                    file.getFileItem().setFieldName("selectedFile");
                    TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request, TASKDTO);
                    String correlationId = taskDto.getRefNo();
                    AppPremisesSpecialDocDto appIntranetDocDto = new AppPremisesSpecialDocDto();
                    appIntranetDocDto.setDocName(litterFile);
                    appIntranetDocDto.setAppPremCorreId(correlationId);
                    appIntranetDocDto.setMd5Code(FileUtil.genMd5FileChecksum(file.getBytes()));
                    long size = file.getSize()/1024;
                    if(size <= Integer.MAX_VALUE ){
                        appIntranetDocDto.setDocSize((int)size);
                    }else {
                        appIntranetDocDto.setDocSize(Integer.MAX_VALUE);
                    }
                    //delete file
                    insepctionNcCheckListService.deleteInvalidFile(serListDto);
                    //save file
                    if( size <= 10240)
                        appIntranetDocDto.setFileRepoId(insepctionNcCheckListService.saveFiles(file));
                    serListDto.setAppPremisesSpecialDocDto(appIntranetDocDto);
                }
            }
        }else {
            //delete file
            insepctionNcCheckListService.deleteInvalidFile(serListDto);
            serListDto.setAppPremisesSpecialDocDto(null);
            // serListDto.setFile(null);
        }

        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        getAuditData(request);
        return serListDto;
    }

    private void  getAuditData(MultipartHttpServletRequest request){
        ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,APPLICATIONVIEWDTO);
        if (appViewDto != null && appViewDto.getLicPremisesAuditDto() != null){
            LicPremisesAuditDto licPremisesAuditDto =  appViewDto.getLicPremisesAuditDto();
            String framework = ParamUtil.getString(request,"framework");
            String periods = ParamUtil.getString(request,"periods");
            String frameworkRemarks = ParamUtil.getString(request,"frameworkRemarks");
            if( !StringUtil.isEmpty(framework) && framework.equalsIgnoreCase("0")){
                licPremisesAuditDto.setInRiskSocre(0);
                if(!StringUtil.isEmpty(periods)){
                    licPremisesAuditDto.setIncludeRiskType(periods);
                    if(periods.equalsIgnoreCase(ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY))
                        licPremisesAuditDto.setLgrRemarks(frameworkRemarks );
                    else
                        licPremisesAuditDto.setLgrRemarks(null);
                }else {
                    licPremisesAuditDto.setIncludeRiskType(null);
                    licPremisesAuditDto.setLgrRemarks(null);
                }
            }else {
                licPremisesAuditDto.setInRiskSocre(1);
                licPremisesAuditDto.setIncludeRiskType(null);
                licPremisesAuditDto.setLgrRemarks(null);
            }
            ParamUtil.setSessionAttr(request,APPLICATIONVIEWDTO,appViewDto);
        }
    }

    public InspectionFDtosDto getDataFromPage(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        /*if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fdto:serListDto.getFdtoList()){
                if(fdto!=null&&!IaisCommonUtils.isEmpty(fdto.getCheckList())){
                    List<InspectionCheckQuestionDto> checkListDtoList = fdto.getCheckList();
                    for(InspectionCheckQuestionDto temp:checkListDtoList){
                        getServiceData(temp,fdto,request);
                    }
                    fillupChklistService.fillInspectionFillCheckListDto(fdto);
                }
            }
        }*/
        String tcu = ParamUtil.getString(request,"tuc");
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        String otherOfficers = ParamUtil.getString(request,"otherinspector");
        String startTime = ParamUtil.getString(request,"startTime");
        String endTime =  ParamUtil.getString(request,"endTime");
        serListDto.setStartTime(startTime);
        serListDto.setEndTime(endTime);
        serListDto.setOtherinspectionofficer(otherOfficers);
        serListDto.setTcuRemark(tcuremark);
        serListDto.setTuc(tcu);
        serListDto.setBestPractice(bestpractice);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        return serListDto;
    }

    public void setChangeTabForChecklist( HttpServletRequest request ){
        String errTab = (String) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.CHECK_LIST_ERROR_TAB_NAME);
        if (!StringUtil.isEmpty(errTab)) {
            ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.CHECK_LIST_ERROR_TAB_NAME, null);
            if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
                ParamUtil.setRequestAttr(request, HcsaLicenceBeConstant.CHECK_LIST_ERROR_SPEC_TAB_NAME, errTab);
            }
            ParamUtil.setRequestAttr(request, HcsaLicenceBeConstant.CHECK_LIST_COM_TAB_NAME, errTab);
        }
    }


    public void setSpecServiceCheckListData(HttpServletRequest request,InspectionFDtosDto serListDto,AdCheckListShowDto adchklDto,boolean beforeFinishList, List<OrgUserDto> orgUserDtoUsers){
       /* ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,AppConsts.YES);
        List<InspectionSpecServiceDto> fDtosDtos = IaisCommonUtils.genNewArrayList();
        for(int i= 0;i<1;i++){
            InspectionSpecServiceDto inspectionSpecServiceDto = MiscUtil.transferEntityDto(serListDto,InspectionSpecServiceDto.class);
            inspectionSpecServiceDto.setIdentify("88888888"+i);
            List<InspectionFillCheckListDto> fdtoList = IaisCommonUtils.genNewArrayList();
            for(InspectionFillCheckListDto inspectionFillCheckListDto : serListDto.getFdtoList()){
                try{
                    InspectionFillCheckListDto inspectionFillCheckListDtoCopy = (InspectionFillCheckListDto )com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject( inspectionFillCheckListDto);
                    inspectionFillCheckListDtoCopy.setSubName(inspectionFillCheckListDto.getSubName() + i);
                    fdtoList.add(inspectionFillCheckListDtoCopy);
                }catch (Exception e){
                    log.error(e.getMessage());
                }

            }
            inspectionSpecServiceDto.setFdtoList(fdtoList);
            inspectionSpecServiceDto.setAdchklDto(fillupChklistService.getSpecAhocData(adchklDto,inspectionSpecServiceDto.getIdentify(),beforeFinishList,orgUserDtoUsers));
            fDtosDtos.add(inspectionSpecServiceDto);
        }
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,(Serializable) fDtosDtos);*/
    }


    public void setRate(HttpServletRequest request){
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            setSpecRate(request);
        }else {
            InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
            InspectionFillCheckListDto commonDto =  (InspectionFillCheckListDto) ParamUtil.getSessionAttr(request,COMMONDTO);
            AdCheckListShowDto adchklDto = (AdCheckListShowDto) ParamUtil.getSessionAttr(request,ADHOCLDTO);
            fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
            ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
            ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
            ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        }
    }

    public void setSpecRate(HttpServletRequest request){
        InspectionFillCheckListDto commonDto =  (InspectionFillCheckListDto) ParamUtil.getSessionAttr(request,COMMONDTO);
        List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        fillupChklistService.getRateOfSpecCheckList(fDtosDtos,commonDto, serListDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,(Serializable) fDtosDtos);
    }

    public void saveCheckList(HttpServletRequest request,InspectionFillCheckListDto commonDto,AdCheckListShowDto adchklDto, InspectionFDtosDto serListDto,String appPremId){
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
            insepctionNcCheckListService.submitSpecService(commonDto, fDtosDtos,serListDto,appPremId);
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,(Serializable) fDtosDtos);
        }else {
            insepctionNcCheckListService.submit(commonDto,adchklDto,serListDto,appPremId);
        }
    }

    // one person
    public void setCheckListData(HttpServletRequest request){
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            getSpecServiceCheckListDataFormViewPage(request);
        }else {
            InspectionFDtosDto serListDto = getServiceCheckListDataFormViewPage(request);
            AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
            ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
            ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        }
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
    }
}
