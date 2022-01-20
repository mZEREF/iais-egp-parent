package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DsLicenceService;
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
import java.util.Optional;

@Delegator("transferInOutDelegator")
@Slf4j
public class TransferInOutDelegator extends CommonDelegator {
    public static final String WHAT_WAS_TRANSFERREDs = "transferreds";

    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;
    @Autowired
    NotificationHelper notificationHelper;
    @Autowired
    ArDataSubmissionService arDataSubmissionService;
    @Autowired
    DsLicenceService dsLicenceService;

    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        initReceive(request);
        ParamUtil.setSessionAttr(request, WHAT_WAS_TRANSFERREDs, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.WHAT_WAS_TRANSFERRED));
        initSelectOptions(request);
    }

    private void initSelectOptions(HttpServletRequest request) {
        PremisesDto currentPremisesDto = DataSubmissionHelper.getCurrentArDataSubmission(request).getPremisesDto();
        List<PremisesDto> premisesDtos = dsLicenceService.getArCenterPremises();
        List<SelectOption> premisesSel = IaisCommonUtils.genNewArrayList();
        for (PremisesDto premisesDto : premisesDtos) {
            if (!premisesDto.getHciCode().equals(currentPremisesDto.getHciCode()) || !premisesDto.getOrganizationId().equals(currentPremisesDto.getOrganizationId())) {
                premisesSel.add(new SelectOption(premisesDto.getHciCode(), premisesDto.getBusinessName()));
            }
        }
        premisesSel.add(new SelectOption("Others", "Others"));
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_OUT_IN_PREMISES_SEL, (Serializable) premisesSel);
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

        ArSuperDataSubmissionDto outStageArSuperDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_SUPER_DTO);
        if (outStageArSuperDto != null) {
            TransferInOutStageDto outStageDto = outStageArSuperDto.getTransferInOutStageDto();
            ParamUtil.setRequestAttr(request, "outStageOocyte", outStageDto.getOocyteNum());
            ParamUtil.setRequestAttr(request, "outStageEmbryo", outStageDto.getEmbryoNum());
            ParamUtil.setRequestAttr(request, "outStageSpermVial", outStageDto.getSpermVialsNum());
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        PatientInventoryDto patientInventoryDto = DataSubmissionHelper.getCurrentPatientInventory(bpc.request);
        List<String> transferredList = transferInOutStageDto.getTransferredList();
        for (String transferred : transferredList) {
            if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)) {
                if (transferInOutStageDto.getOocyteNum() != null) {
                    patientInventoryDto.setChangeFrozenOocytes(-1 * transferInOutStageDto.getOocyteNum());
                }
            }
            if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)) {
                if (transferInOutStageDto.getEmbryoNum() != null) {
                    patientInventoryDto.setChangeFrozenEmbryos(-1 * transferInOutStageDto.getEmbryoNum());
                }
            }
            if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)) {
                if (transferInOutStageDto.getSpermVialsNum() != null) {
                    patientInventoryDto.setChangeFrozenSperms(-1 * transferInOutStageDto.getSpermVialsNum());
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
        arSuperDataSubmissionDto = arSuperDataSubmissionDto == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto() == null ? new TransferInOutStageDto() : arSuperDataSubmissionDto.getTransferInOutStageDto();

        String outStageDsNo = ParamUtil.getString(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_NO);
        if (StringUtil.isEmpty(outStageDsNo)) {
            setDataFromPage(request, transferInOutStageDto);
        } else {
            String oocyteNo = ParamUtil.getString(request, "oocyteNum");
            String embryoNo = ParamUtil.getString(request, "embryoNum");
            String spermVialsNo = ParamUtil.getString(request, "spermVialsNum");
            transferInOutStageDto.setOocyteNo(oocyteNo);
            transferInOutStageDto.setEmbryoNo(embryoNo);
            transferInOutStageDto.setSpermVialsNo(spermVialsNo);
            flagInAndOutDiscrepancy(request, transferInOutStageDto);
        }

        arSuperDataSubmissionDto.setTransferInOutStageDto(transferInOutStageDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        validatePageData(request, transferInOutStageDto, "save", ACTION_TYPE_CONFIRM);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

    }

    private void setDataFromPage(HttpServletRequest request, TransferInOutStageDto transferInOutStageDto) {
        String[] transferredList = ParamUtil.getStrings(request, "transferredList");
        String oocyteNo = ParamUtil.getString(request, "oocyteNum");
        String embryoNo = ParamUtil.getString(request, "embryoNum");
        String spermVialsNo = ParamUtil.getString(request, "spermVialsNum");
        ControllerHelper.get(request, transferInOutStageDto);
        transferInOutStageDto.setOocyteNo(oocyteNo);
        transferInOutStageDto.setEmbryoNo(embryoNo);
        transferInOutStageDto.setSpermVialsNo(spermVialsNo);
        String fromDonor = ParamUtil.getString(request, "fromDonor");
        transferInOutStageDto.setFromDonor("true".equalsIgnoreCase(fromDonor));
        if (!IaisCommonUtils.isEmpty(transferredList)) {
            transferInOutStageDto.setTransferredList(Arrays.asList(transferredList));
        } else {
            transferInOutStageDto.setTransferredList(null);
        }
    }

    @Override
    public void doSubmission(BaseProcessClass bpc) {
        super.doSubmission(bpc);
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");
        String hciCode = getReceivedHciCode(transferInOutStageDto);
        if (StringUtil.isNotEmpty(hciCode)) {
            String licenseeId = getLicenseeId(orgId, hciCode);
            sendNeedTransferInNotification(licenseeId, arSuperDataSubmissionDto.getHciCode(), arSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo());
        }
    }

    @SneakyThrows
    private void sendNeedTransferInNotification(String licenseeId, String hciCode, String submissionNo) {
        //TODO need EN-DSN-001 notification Template
        String templateId = "67F85A8B-3E74-EC11-BE6B-000C29FAAE4D";
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(templateId).getEntity();
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("HciCode", hciCode);
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(templateId);
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        String uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohTransferInOut?outStageDsNo=" + submissionNo;
        msgContentMap.put("systemLink", uri);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(licenseeId);
        msgParam.setReqRefNum(licenseeId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(licenseeId);
        notificationHelper.sendNotification(msgParam);
    }

    private String getReceivedHciCode(TransferInOutStageDto transferInOutStageDto) {
        if (transferInOutStageDto != null && "out".equals(transferInOutStageDto.getTransferType()) && !"Others".equals(transferInOutStageDto.getTransOutToHciCode())) {
            return transferInOutStageDto.getTransOutToHciCode();
        }
        return null;
    }

    private String getLicenseeId(String orgId, String hciCode) {
        String result = null;
        PremisesDto premisesDto = dsLicenceService.getPremisesDto(orgId, hciCode);
        if (premisesDto != null) {
            String organizationId = premisesDto.getOrganizationId();
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(organizationId);
            if (licenseeDto != null) {
                result = licenseeDto.getId();
            }
        }
        return result;
    }

    private void initReceive(HttpServletRequest request) {
        String outStageDsNo = ParamUtil.getRequestString(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_NO);
        if (StringUtil.isEmpty(outStageDsNo)) {
            return;
        }
        DataSubmissionHelper.clearSession(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_NO, outStageDsNo);
        ArSuperDataSubmissionDto outStageArSuperDto = initOutStageDto(request, outStageDsNo);
        initArSuper(request, outStageArSuperDto);
    }

    private ArSuperDataSubmissionDto initArSuper(HttpServletRequest request, ArSuperDataSubmissionDto outArDto) {
        TransferInOutStageDto outStageDto = outArDto.getTransferInOutStageDto();
        String hciCode = outStageDto.getTransOutToHciCode();
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");
        PremisesDto premisesDto = dsLicenceService.getPremisesDto(orgId, hciCode);

        ArSuperDataSubmissionDto arSuper = new ArSuperDataSubmissionDto();
        arSuper.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE);
        arSuper.setSubmissionMethod(DataSubmissionConsts.DS_METHOD_MANUAL_ENTRY);
        arSuper.setPremisesDto(premisesDto);
        arSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        arSuper.setOrgId(orgId);
        arSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        arSuper.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(arSuper, false));
        arSuper.getDataSubmissionDto().setCycleStage(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        String licenseeId = DataSubmissionHelper.getLicenseeId(request);
        arSuper.setCycleDto(DataSubmissionHelper.initCycleDto(arSuper, licenseeId, false));

        ArSuperDataSubmissionDto newDto = arDataSubmissionService.getArSuperDataSubmissionDto(outArDto.getCycleDto().getPatientCode(),
                hciCode, null);

        if (newDto != null) {
            log.info("-----Retieve ArSuperDataSubmissionDto from DB-----");
            CycleStageSelectionDto selectionDto = newDto.getSelectionDto();
            selectionDto.setStage(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
            CycleDto cycleDto = DataSubmissionHelper.initCycleDto(selectionDto, arSuper.getSvcName(), hciCode, licenseeId);
            arSuper.setCycleDto(cycleDto);
            arSuper.setSelectionDto(selectionDto);
            arSuper.setPatientInfoDto(newDto.getPatientInfoDto());
            arSuper.setPatientInventoryDto(newDto.getPatientInventoryDto());
        }

        TransferInOutStageDto transferInOutStageDto = (TransferInOutStageDto) CopyUtil.copyMutableObject(outStageDto);
        transferInOutStageDto.setId(null);
        transferInOutStageDto.setSubmissionId(null);
        transferInOutStageDto.setTransOutToHciCode(null);
        transferInOutStageDto.setTransferType("in");
        transferInOutStageDto.setTransInFromHciCode(outArDto.getCycleDto().getHciCode());
        arSuper.setTransferInOutStageDto(transferInOutStageDto);

        DataSubmissionHelper.setCurrentArDataSubmission(arSuper, request);
        return arSuper;
    }

    private ArSuperDataSubmissionDto initOutStageDto(HttpServletRequest request, String outStageDsNo) {
        ArSuperDataSubmissionDto outStageArSuperDto = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(outStageDsNo);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_SUPER_DTO, outStageArSuperDto);
        return outStageArSuperDto;
    }

    private void flagInAndOutDiscrepancy(HttpServletRequest request, TransferInOutStageDto transferInOutStageDto) {
        ArSuperDataSubmissionDto outArSuperDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_SUPER_DTO);
        if (outArSuperDto != null) {
            TransferInOutStageDto outStageDto = outArSuperDto.getTransferInOutStageDto();
            boolean diffOocyte = !outStageDto.getOocyteNo().equals(transferInOutStageDto.getOocyteNo());
            boolean diffEmbryo = !outStageDto.getEmbryoNo().equals(transferInOutStageDto.getEmbryoNo());
            boolean diffSpermVial = !outStageDto.getSpermVialsNo().equals(transferInOutStageDto.getSpermVialsNo());
            ParamUtil.setRequestAttr(request, "diffOocyte", diffOocyte);
            ParamUtil.setRequestAttr(request, "diffEmbryo", diffEmbryo);
            ParamUtil.setRequestAttr(request, "diffSpermVial", diffSpermVial);
        }
    }
}
