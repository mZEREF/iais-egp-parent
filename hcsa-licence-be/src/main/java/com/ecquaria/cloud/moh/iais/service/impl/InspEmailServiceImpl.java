package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.InsEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * InspEmailServiceImpl
 *
 * @author junyu
 * @date 2019/11/23
 */
@Service
public class InspEmailServiceImpl implements InspEmailService {
    @Autowired
    private InsEmailClient insEmailClient;
    @Autowired
    AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private SystemBeLicClient systemClient;

    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    OrganizationClient licenseeClient;

    @Override
    public String updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        return insEmailClient.updateEmailDraft(inspectionEmailTemplateDto).getEntity();
    }

    @Override
    public String insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        return insEmailClient.insertEmailDraft(inspectionEmailTemplateDto).getEntity();
    }


    @Override
    public InspectionEmailTemplateDto getInsertEmail(String appPremCorrId) {
        return insEmailClient.getInspectionEmail(appPremCorrId).getEntity();
    }

    @Override
    public ApplicationViewDto getAppViewByCorrelationId(String correlationId) {
        return applicationViewService.getApplicationViewDtoByCorrId(correlationId);
    }

    @Override
    public InspectionEmailTemplateDto loadingEmailTemplate(String id) {
        return systemClient.loadingEmailTemplate(id).getEntity();
    }

    @Override
    public List<ChecklistQuestionDto> getcheckListQuestionDtoList(String svcCode,String svcType){
        return hcsaChklClient.getcheckListQuestionDtoList(svcCode,"Inspection").getEntity();
    }



    @Override
    public Map<String, String > SendAndSaveEmail(EmailDto emailDto){
        return insEmailClient.sendAndSaveEmail(emailDto).getEntity();
    }

    @Override
    public List<NcAnswerDto> getNcAnswerDtoList(InspectionFillCheckListDto cDto, InspectionFillCheckListDto commonDto, AdCheckListShowDto adchklDto, List<NcAnswerDto> acDtoList) {
        List<NcAnswerDto> ncList = IaisCommonUtils.genNewArrayList();
        List<InspectionCheckQuestionDto> genCheckList = cDto.getCheckList();
        List<InspectionCheckQuestionDto> comCheckList = commonDto.getCheckList();
        List<AdhocNcCheckItemDto> adhocItemList = adchklDto.getAdItemList();
        NcAnswerDto ncDto = null;
        if(genCheckList!=null && !genCheckList.isEmpty()){
            for(InspectionCheckQuestionDto temp:genCheckList){
               if("No".equals(temp.getChkanswer())){
                   //ncDto.setItemId(temp.getItemId());
                   ncDto = new NcAnswerDto();
                   ncDto.setCaluseNo(temp.getRegClauseNo());
                   ncDto.setClause(temp.getRegClause());
                   ncDto.setRemark(temp.getRemark());
                   ncDto.setItemQuestion(temp.getChecklistItem());
                   ncList.add(ncDto);
               }
            }
        }
        if(comCheckList!=null && !comCheckList.isEmpty() ){
            for(InspectionCheckQuestionDto temp:comCheckList){
                if("No".equals(temp.getChkanswer())){
                    //ncDto.setItemId(temp.getItemId());
                    ncDto = new NcAnswerDto();
                    ncDto.setCaluseNo(temp.getRegClauseNo());
                    ncDto.setClause(temp.getRegClause());
                    ncDto.setRemark(temp.getRemark());
                    ncDto.setItemQuestion(temp.getChecklistItem());
                    ncList.add(ncDto);
                }
            }
        }
        getAdhocNcItem(adhocItemList,ncList);
        return ncList;
    }

    @Override
    public LicenseeDto getLicenseeDtoById(String id) {
        return licenseeClient.getLicenseeDtoById(id).getEntity();
    }

    @Override
    public List<ApplicationDto> getApplicationDtosByCorreId(String appCorreId) {
        return appPremisesCorrClient.getApplicationDtosByCorreId(appCorreId).getEntity();
    }

    @Override
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationsByPremises(String appCorrId) {
        return appPremisesCorrClient.getAppPremisesCorrelationsByPremises(appCorrId).getEntity();
    }

    public void getAdhocNcItem(List<AdhocNcCheckItemDto> adhocItemList,List<NcAnswerDto> ncList){
        if(adhocItemList!=null && !adhocItemList.isEmpty()){
            NcAnswerDto ncDto = null;
            for(AdhocNcCheckItemDto temp:adhocItemList){
                if("No".equals(temp.getAdAnswer())) {
                    ncDto = new NcAnswerDto();
                    ncDto.setRemark(temp.getRemark());
                    ncDto.setItemQuestion(temp.getQuestion());
                    ncList.add(ncDto);
                }
            }
        }
    }
}
