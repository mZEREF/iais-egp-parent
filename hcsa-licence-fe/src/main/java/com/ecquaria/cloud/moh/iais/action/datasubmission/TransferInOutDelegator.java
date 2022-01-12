package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Delegator("transferInOutDelegator")
@Slf4j
public class TransferInOutDelegator extends CommonDelegator {
    public static final String WHAT_WAS_TRANSFERREDs = "transferreds";

    @Autowired
    CessationFeService cessationFeService;
    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;
    @Autowired
    NotificationHelper notificationHelper;
    @Autowired
    ArDataSubmissionService arDataSubmissionService;

    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,WHAT_WAS_TRANSFERREDs, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.WHAT_WAS_TRANSFERRED));

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        PatientInventoryDto patientInventoryDto = DataSubmissionHelper.getCurrentPatientInventory(bpc.request);
        List<String> transferredList = transferInOutStageDto.getTransferredList();
        for (String transferred : transferredList){
            if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)){
                if(transferInOutStageDto.getOocyteNum() !=null){
                    patientInventoryDto.setChangeFrozenOocytes(-1*transferInOutStageDto.getOocyteNum());
                }
            }
            if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)){
                if(transferInOutStageDto.getEmbryoNum() !=null){
                    patientInventoryDto.setChangeFrozenEmbryos(-1*transferInOutStageDto.getEmbryoNum());
                }
            }
            if(transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)){
                if(transferInOutStageDto.getSpermVialsNum() !=null){
                    patientInventoryDto.setChangeFrozenSperms(-1*transferInOutStageDto.getSpermVialsNum());
                }
            }
        }
        //3.3.3.3.4 flag discrepancy
        if (transferInOutStageDto.getTransferType().equals("in")){
            CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
            if (cycleDto != null) {
                TransferInOutStageDto outStageDto = arDataSubmissionService.getCorrespondOutStageDto(cycleDto.getPatientCode(), cycleDto.getHciCode());
                if (outStageDto != null) {
                    flagInAndOutDiscrepancy(bpc.request, transferInOutStageDto, outStageDto);
                }
            }
        }
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        arSuperDataSubmissionDto = arSuperDataSubmissionDto  == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto() == null ? new TransferInOutStageDto() : arSuperDataSubmissionDto.getTransferInOutStageDto();
        String[] transferredList = ParamUtil.getStrings(request,"transferredList");
        String oocyteNo =  ParamUtil.getString(request,"oocyteNum");
        String embryoNo =  ParamUtil.getString(request,"embryoNum");
        String spermVialsNo =  ParamUtil.getString(request,"spermVialsNum");
        ControllerHelper.get(request,transferInOutStageDto);
        transferInOutStageDto.setOocyteNo(oocyteNo);
        transferInOutStageDto.setEmbryoNo(embryoNo);
        transferInOutStageDto.setSpermVialsNo(spermVialsNo);
        String fromDonor = ParamUtil.getString(request,"fromDonor");
        transferInOutStageDto.setFromDonor("true".equalsIgnoreCase(fromDonor));
        if( !IaisCommonUtils.isEmpty(transferredList)){
            transferInOutStageDto.setTransferredList(Arrays.asList(transferredList));
        }else{
            transferInOutStageDto.setTransferredList(null);
        }
        arSuperDataSubmissionDto.setTransferInOutStageDto(transferInOutStageDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        validatePageData(request, transferInOutStageDto,"save",ACTION_TYPE_CONFIRM);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override
    public void submission(BaseProcessClass bpc) {
        //3.3.3.3.2 local transfer Notification
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        String hciCode = getReceivedHciCode(transferInOutStageDto);
        if (StringUtil.isNotEmpty(hciCode)) {
            String licenseeId = getLicenseeId(hciCode);
            sendNeedTransferInNotification(licenseeId);
        }
    }

    @SneakyThrows
    private void sendNeedTransferInNotification(String licenseeId) {
        //TODO need EN-DSN-001 notification Template
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_MSG).getEntity();
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(licenseeId);
        msgParam.setReqRefNum(licenseeId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(licenseeId);
        notificationHelper.sendNotification(msgParam);
    }

    private String getReceivedHciCode(TransferInOutStageDto transferInOutStageDto) {
        if (transferInOutStageDto == null || "in".equals(transferInOutStageDto.getTransferType())) {
            return null;
        }
        //TODO getTransOutToHciCode
//        return transferInOutStageDto.getTransOutToHciCode();
        return null;
    }

    private String getLicenseeId(String hciCode) {
        String result = null;
        PremisesDto premisesDto = cessationFeService.getPremiseByHciCodeName(hciCode);
        if (premisesDto != null) {
            String organizationId = premisesDto.getOrganizationId();
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(organizationId);
            if (licenseeDto != null) {
                result = licenseeDto.getId();
            }
        }
        return result;
    }

    private void flagInAndOutDiscrepancy(HttpServletRequest request, TransferInOutStageDto inStageDto, TransferInOutStageDto outStageDto) {
        boolean diffWas = !inStageDto.getTransferredList().equals(outStageDto.getTransferredList());
        boolean diffOocyte = !inStageDto.getOocyteNum().equals(outStageDto.getOocyteNum());
        boolean diffEmbryo = !inStageDto.getEmbryoNum().equals(outStageDto.getEmbryoNum());
        boolean diffSpermVial = !inStageDto.getSpermVialsNum().equals(outStageDto.getSpermVialsNum());
        boolean diffIsDonor = inStageDto.isFromDonor() != outStageDto.isFromDonor();
        boolean diffDate = inStageDto.getTransferDate().equals(outStageDto.getTransferDate());

        ParamUtil.setRequestAttr(request, "diffWas", diffWas);
        ParamUtil.setRequestAttr(request, "diffOocyte", diffOocyte);
        ParamUtil.setRequestAttr(request, "diffEmbryo", diffEmbryo);
        ParamUtil.setRequestAttr(request, "diffSpermVial", diffSpermVial);
        ParamUtil.setRequestAttr(request, "diffIsDonor", diffIsDonor);
        ParamUtil.setRequestAttr(request, "diffDate", diffDate);
    }
}
