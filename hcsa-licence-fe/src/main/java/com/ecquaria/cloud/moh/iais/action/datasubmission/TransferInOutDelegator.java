package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Delegator("transferInOutDelegator")
@Slf4j
public class TransferInOutDelegator extends CommonDelegator {
    public static final String WHAT_WAS_TRANSFERREDS = "transferreds";
    public static final String TRANSFER_TYPE_IN = "in";
    public static final String TRANSFER_TYPE_OUT = "out";

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
    @Autowired
    LicenceViewService licenceViewService;

    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        initReceive(request);
        initSelectOpts(request);
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

        ArSuperDataSubmissionDto bindStageArSuperDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_SUPER_DTO);
        if (bindStageArSuperDto != null) {
            TransferInOutStageDto outStageDto = bindStageArSuperDto.getTransferInOutStageDto();
            ParamUtil.setRequestAttr(request, "outStageOocyte", outStageDto.getOocyteNum());
            ParamUtil.setRequestAttr(request, "outStageEmbryo", outStageDto.getEmbryoNum());
            ParamUtil.setRequestAttr(request, "outStageSpermVial", outStageDto.getSpermVialsNum());
        }
        ParamUtil.setRequestAttr(request, WHAT_WAS_TRANSFERREDS, MasterCodeUtil.retrieveByCategory(MasterCodeUtil.WHAT_WAS_TRANSFERRED));
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        ArChangeInventoryDto arChangeInventoryDto = new ArChangeInventoryDto();
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
        List<String> transferredList = transferInOutStageDto.getTransferredList();
        int num = "in".equals(transferInOutStageDto.getTransferType()) ? 1 : -1;
        if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)) {
            if (transferInOutStageDto.getOocyteNum() != null) {
                arChangeInventoryDto.setFrozenOocyteNum(num * Integer.parseInt(transferInOutStageDto.getOocyteNum()));
            }
        }
        if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)) {
            if (transferInOutStageDto.getEmbryoNum() != null) {
                arChangeInventoryDto.setFrozenEmbryoNum(num * Integer.parseInt(transferInOutStageDto.getEmbryoNum()));
            }
        }
        if (transferredList.contains(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)) {
            if (transferInOutStageDto.getSpermVialsNum() != null) {
                arChangeInventoryDto.setFrozenSpermNum(num * Integer.parseInt(transferInOutStageDto.getSpermVialsNum()));
            }
        }
        ParamUtil.setRequestAttr(request, WHAT_WAS_TRANSFERREDS, MasterCodeUtil.retrieveByCategory(MasterCodeUtil.WHAT_WAS_TRANSFERRED));
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // has draftAction
        if (draftAction(request)) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            return;
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        arSuperDataSubmissionDto = arSuperDataSubmissionDto == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto() == null ? new TransferInOutStageDto() : arSuperDataSubmissionDto.getTransferInOutStageDto();

        String bindStageDsId = (String) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_ID);
        if (StringUtil.isEmpty(bindStageDsId)) {
            setDataFromPage(request, transferInOutStageDto);
        } else {
            String oocyteNum = ParamUtil.getString(request, "oocyteNum");
            String embryoNum = ParamUtil.getString(request, "embryoNum");
            String spermVialsNum = ParamUtil.getString(request, "spermVialsNum");
            transferInOutStageDto.setOocyteNum(oocyteNum);
            transferInOutStageDto.setEmbryoNum(embryoNum);
            transferInOutStageDto.setSpermVialsNum(spermVialsNum);
            ArSuperDataSubmissionDto bindArSuperDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_SUPER_DTO);
            flagInAndOutDiscrepancy(request, transferInOutStageDto, bindArSuperDto);
        }

        arSuperDataSubmissionDto.setTransferInOutStageDto(transferInOutStageDto);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(transferInOutStageDto, "save");
            errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            valRFC(request, transferInOutStageDto);
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
    }

    @Override
    public void doSubmission(BaseProcessClass bpc) {
        super.doSubmission(bpc);
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();

        if (isNeedEmail(transferInOutStageDto)) {
//            TODO fill email
            if (TRANSFER_TYPE_OUT.equals(transferInOutStageDto.getTransferType())) {
                sendTransferOutNotification(arSuperDataSubmissionDto);
            } else {
                sendTransferInNotification(arSuperDataSubmissionDto);
            }
        }
    }

    public void initSelectOpts(HttpServletRequest request) {
        PremisesDto currentPremisesDto = DataSubmissionHelper.getCurrentArDataSubmission(request).getPremisesDto();
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");
        List<PremisesDto> premisesDtos = dsLicenceService.getArCenterPremiseList(orgId);
        premisesDtos = premisesDtos.stream().filter(premisesDto ->
                !premisesDto.getHciCode().equals(currentPremisesDto.getHciCode()) || !premisesDto.getOrganizationId().equals(currentPremisesDto.getOrganizationId())
        ).collect(Collectors.toList());
        List<SelectOption> premisesSel = IaisCommonUtils.genNewArrayList();
        for (PremisesDto premisesDto : premisesDtos) {
            String licenseeId = getLicenseeId(premisesDto.getOrganizationId());
            premisesSel.add(new SelectOption(licenseeId + "/" + premisesDto.getHciCode(), premisesDto.getPremiseLabel()));
        }
        premisesSel.add(new SelectOption("Others", "Others"));
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_OUT_IN_PREMISES_SEL, (Serializable) premisesSel);
    }

    private void setDataFromPage(HttpServletRequest request, TransferInOutStageDto transferInOutStageDto) {
        String[] transferredList = ParamUtil.getStrings(request, "transferredList");
        String oocyteNum = ParamUtil.getString(request, "oocyteNum");
        String embryoNum = ParamUtil.getString(request, "embryoNum");
        String spermVialsNum = ParamUtil.getString(request, "spermVialsNum");
        ControllerHelper.get(request, transferInOutStageDto);
        String fromDonor = ParamUtil.getString(request, "fromDonor");
        transferInOutStageDto.setFromDonor("true".equalsIgnoreCase(fromDonor));
        if (!IaisCommonUtils.isEmpty(transferredList)) {
            transferInOutStageDto.setTransferredList(Arrays.asList(transferredList));
            for (String transferred : transferInOutStageDto.getTransferredList()) {
                if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)) {
                    transferInOutStageDto.setOocyteNum(oocyteNum);
                }
                if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)) {
                    transferInOutStageDto.setEmbryoNum(embryoNum);
                }
                if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)) {
                    transferInOutStageDto.setSpermVialsNum(spermVialsNum);
                }
            }
        } else {
            transferInOutStageDto.setTransferredList(null);
        }

        String licenseeId = DataSubmissionHelper.getLicenseeId(request);
        if ("in".equals(transferInOutStageDto.getTransferType())) {
            String selectKey = ParamUtil.getString(request, "transInFromHciCode");
            if (StringUtil.isNotEmpty(selectKey) && !"Others".equals(selectKey)) {
                String[] values = parseSelectKey(selectKey);
                transferInOutStageDto.setTransInFromLicenseeId(values[0]);
                transferInOutStageDto.setTransInFromHciCode(values[1]);
            } else if ("Others".equals(selectKey)) {
                transferInOutStageDto.setTransInFromHciCode(selectKey);
                transferInOutStageDto.setTransInFromLicenseeId(licenseeId);
            }
        } else if ("out".equals(transferInOutStageDto.getTransferType())) {
            String selectKey = ParamUtil.getString(request, "transOutToHciCode");
            if (StringUtil.isNotEmpty(selectKey) && !"Others".equals(selectKey)) {
                String[] values = parseSelectKey(selectKey);
                transferInOutStageDto.setTransOutToLicenseeId(values[0]);
                transferInOutStageDto.setTransOutToHciCode(values[1]);
            } else if ("Others".equals(selectKey)) {
                transferInOutStageDto.setTransOutToHciCode(selectKey);
                transferInOutStageDto.setTransOutToLicenseeId(licenseeId);
            }
        }
    }

    @SneakyThrows
    private void sendTransferOutNotification(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        LicenseeDto receiveLicenseeDto = licenceViewService.getLicenseeDtoBylicenseeId(transferInOutStageDto.getTransOutToLicenseeId());
        String receiveOrgId = receiveLicenseeDto.getOrganizationId();
        PremisesDto receivePremises = dsLicenceService.getArPremisesDto(receiveOrgId, transferInOutStageDto.getTransOutToHciCode());
        String licenseeId = transferInOutStageDto.getTransOutToLicenseeId();
        String hciCode = arSuperDataSubmissionDto.getHciCode();
        String submissionId = arSuperDataSubmissionDto.getDataSubmissionDto().getId();
        String submitterName = receiveLicenseeDto.getName();
        String transferCenter = arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel();
        String receivingCenter = receivePremises.getPremiseLabel();
        String templateId = "67F85A8B-3E74-EC11-BE6B-000C29FAAE4D";
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(templateId).getEntity();
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("HciCode", hciCode);
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(templateId);
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        String uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohTransferInOut?bindStageSubmissionId="
                + submissionId;
        msgContentMap.put("submissionerName", submitterName);
        msgContentMap.put("transferringCenter", transferCenter);
        msgContentMap.put("receivingCenter", receivingCenter);
        msgContentMap.put("systemLink", uri);
        msgContentMap.put("date", Formatter.formatDate(new Date()));
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(licenseeId);
        msgParam.setReqRefNum(licenseeId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(licenseeId);
        notificationHelper.sendNotification(msgParam);
    }

    @SneakyThrows
    private void sendTransferInNotification(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        LicenseeDto transferringLicenseeDto = licenceViewService.getLicenseeDtoBylicenseeId(transferInOutStageDto.getTransInFromLicenseeId());
        String transferringOrgId = transferringLicenseeDto.getOrganizationId();
        PremisesDto transferringPremises = dsLicenceService.getArPremisesDto(transferringOrgId, transferInOutStageDto.getTransInFromHciCode());
        String licenseeId = transferInOutStageDto.getTransInFromLicenseeId();
        String templateId = "67F85A8B-3E74-EC11-BE6B-000C29FAAE4D";
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(templateId).getEntity();
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("HciCode", arSuperDataSubmissionDto.getHciCode());
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), msgSubjectMap);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(templateId);
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        String uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohTransferInOut?bindStageSubmissionId="
                + arSuperDataSubmissionDto.getDataSubmissionDto().getId();
        msgContentMap.put("submissionerName", transferringLicenseeDto.getName());
        msgContentMap.put("transferringCenter", transferringPremises.getPremiseLabel());
        msgContentMap.put("receivingCenter", arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());
        msgContentMap.put("systemLink", uri);
        msgContentMap.put("date", Formatter.formatDate(new Date()));
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(licenseeId);
        msgParam.setReqRefNum(licenseeId);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(licenseeId);
        notificationHelper.sendNotification(msgParam);
    }

    private boolean isNeedEmail(TransferInOutStageDto transferInOutStageDto) {
        return transferInOutStageDto != null
                && StringUtil.isEmpty(transferInOutStageDto.getBindSubmissionId())
                && (
                (TRANSFER_TYPE_OUT.equals(transferInOutStageDto.getTransferType()) && !"Others".equals(transferInOutStageDto.getTransOutToHciCode()))
                        || (TRANSFER_TYPE_IN.equals(transferInOutStageDto.getTransferType()) && !"Others".equals(transferInOutStageDto.getTransInFromHciCode()))
        );
    }

    private void initReceive(HttpServletRequest request) {
        String bindStageDsId = ParamUtil.getRequestString(request, DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_ID);
        // from link
        if (StringUtil.isNotEmpty(bindStageDsId)) {
            DataSubmissionHelper.clearSession(request);
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        // resume in rfc or draft
        if (arSuperDataSubmissionDto != null) {
            TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
            if (StringUtil.isEmpty(bindStageDsId)) {
                if (transferInOutStageDto != null && StringUtil.isNotEmpty(transferInOutStageDto.getBindSubmissionId())) {
                    bindStageDsId = transferInOutStageDto.getBindSubmissionId();
                }
            }
        }
        if (StringUtil.isNotEmpty(bindStageDsId)) {
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_ID, bindStageDsId);
            ArSuperDataSubmissionDto bindStageArSuperDto = initBindStageDto(request, bindStageDsId);
            if (arSuperDataSubmissionDto == null) {
                initArSuper(request, bindStageArSuperDto, bindStageDsId);
                hasDraft(request);
                bindStageIsInaction(request, bindStageArSuperDto);
                hasConfirmationStage(request, bindStageDsId);
            }
        }
    }

    private ArSuperDataSubmissionDto initArSuper(HttpServletRequest request, ArSuperDataSubmissionDto bindArDto, String bindStageDsId) {
        TransferInOutStageDto bindStageDto = bindArDto.getTransferInOutStageDto();
        String hciCode;
        if (TRANSFER_TYPE_OUT.equals(bindStageDto.getTransferType())) {
            hciCode = bindStageDto.getTransOutToHciCode();
        } else {
            hciCode = bindStageDto.getTransInFromHciCode();
        }
        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");
        PremisesDto premisesDto = dsLicenceService.getArPremisesDto(orgId, hciCode);

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
        arSuper.setLicenseeId(licenseeId);
        arSuper.setCycleDto(DataSubmissionHelper.initCycleDto(arSuper, false));

        String patientCode = bindArDto.getCycleDto().getPatientCode();
        ArSuperDataSubmissionDto newDto = arDataSubmissionService.getArSuperDataSubmissionDto(patientCode,
                hciCode, null);

        String svcName = arSuper.getSvcName();
        if (newDto != null) {
            log.info("-----Retieve ArSuperDataSubmissionDto from DB-----");
            CycleStageSelectionDto selectionDto = newDto.getSelectionDto();
            selectionDto.setStage(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
            CycleDto cycleDto = DataSubmissionHelper.initCycleDto(selectionDto, svcName, hciCode, licenseeId);
            cycleDto.setCycleType(DataSubmissionConsts.DS_CYCLE_NON);
            selectionDto.setCycle(DataSubmissionConsts.DS_CYCLE_NON);
            arSuper.setCycleDto(cycleDto);
            arSuper.setSelectionDto(selectionDto);
            arSuper.setPatientInfoDto(newDto.getPatientInfoDto());
        }

        ArCurrentInventoryDto arCurrentInventoryDto = arDataSubmissionService.getArCurrentInventoryDtoByConds(hciCode, licenseeId, patientCode, svcName);
        if (arCurrentInventoryDto == null) {
            arCurrentInventoryDto = new ArCurrentInventoryDto();
            arCurrentInventoryDto.setHciCode(hciCode);
            arCurrentInventoryDto.setSvcName(svcName);
            arCurrentInventoryDto.setLicenseeId(licenseeId);
            arCurrentInventoryDto.setPatientCode(patientCode);
        }
        arSuper.setArCurrentInventoryDto(arCurrentInventoryDto);

        TransferInOutStageDto transferInOutStageDto = (TransferInOutStageDto) CopyUtil.copyMutableObject(bindStageDto);
        transferInOutStageDto.setId(null);
        transferInOutStageDto.setSubmissionId(null);
        transferInOutStageDto.setBindSubmissionId(bindStageDsId);
        if (TRANSFER_TYPE_OUT.equals(bindStageDto.getTransferType())) {
            transferInOutStageDto.setTransferType(TRANSFER_TYPE_IN);
            transferInOutStageDto.setTransInFromHciCode(bindArDto.getCycleDto().getHciCode());
            transferInOutStageDto.setTransInFromLicenseeId(bindArDto.getLicenseeId());
        } else {
            transferInOutStageDto.setTransferType(TRANSFER_TYPE_OUT);
            transferInOutStageDto.setTransOutToHciCode(bindArDto.getCycleDto().getHciCode());
            transferInOutStageDto.setTransOutToLicenseeId(bindArDto.getLicenseeId());
        }
        transferInOutStageDto.setBindSubmissionId(bindStageDsId);
        arSuper.setTransferInOutStageDto(transferInOutStageDto);

        DataSubmissionHelper.setCurrentArDataSubmission(arSuper, request);
        return arSuper;
    }

    private ArSuperDataSubmissionDto initBindStageDto(HttpServletRequest request, String bindStageDsId) {
        ArSuperDataSubmissionDto bindStageArSuperDto = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionId(bindStageDsId);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_SUPER_DTO, bindStageArSuperDto);
        return bindStageArSuperDto;
    }

    @Override
    public void returnStep(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto outArSuperDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_SUPER_DTO);
        // if is bind stage, back to inbox
        if (outArSuperDto != null) {
            ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
            arSuperDataSubmission.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        }
    }

    public static void flagInAndOutDiscrepancy(HttpServletRequest request, TransferInOutStageDto currentTransferInOutStageDto, ArSuperDataSubmissionDto bindArSuperDto) {
        if (bindArSuperDto != null) {
            TransferInOutStageDto bindStageDto = bindArSuperDto.getTransferInOutStageDto();
            String oocyteNum = bindStageDto.getOocyteNum();
            String embryoNum = bindStageDto.getEmbryoNum();
            String spermVialsNum = bindStageDto.getSpermVialsNum();
            boolean diffOocyte = !(oocyteNum == null || oocyteNum.equals(currentTransferInOutStageDto.getOocyteNum()));
            boolean diffEmbryo = !(embryoNum == null || embryoNum.equals(currentTransferInOutStageDto.getEmbryoNum()));
            boolean diffSpermVial = !(spermVialsNum == null || spermVialsNum.equals(currentTransferInOutStageDto.getSpermVialsNum()));
            ParamUtil.setRequestAttr(request, "diffOocyte", diffOocyte);
            ParamUtil.setRequestAttr(request, "diffEmbryo", diffEmbryo);
            ParamUtil.setRequestAttr(request, "diffSpermVial", diffSpermVial);
        }
    }

    private String getLicenseeId(String orgId) {
        LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(orgId);
        return licenseeDto.getId();
    }

    private String[] parseSelectKey(String selectKey) {
        String[] result = new String[2];
        int index = selectKey.indexOf("/");
        if (StringUtil.isEmpty(selectKey) || index < 0) {
            return result;
        }
        result[0] = selectKey.substring(0, index);
        result[1] = selectKey.substring(index + 1);
        return result;
    }

    protected void valRFC(HttpServletRequest request, TransferInOutStageDto transferInOutStageDto) {
        if (isRfc(request)) {
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if (arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getTransferInOutStageDto() != null && transferInOutStageDto.equals(arOldSuperDataSubmissionDto.getTransferInOutStageDto())) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }
    }

    private void hasDraft(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        PatientDto patient = arSuperDataSubmissionDto.getPatientInfoDto().getPatient();
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
        List<ArSuperDataSubmissionDto> dataSubmissionDraftList = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                patient.getIdType(), patient.getIdNumber(), patient.getNationality(),
                arSuperDataSubmissionDto.getOrgId(), arSuperDataSubmissionDto.getHciCode(), true);
        if (DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT.equals(arSuperDataSubmissionDto.getDataSubmissionDto().getCycleStage()) && IaisCommonUtils.isNotEmpty(dataSubmissionDraftList)) {
            dataSubmissionDraftList = dataSubmissionDraftList.stream()
                    .filter(draft -> transferInOutStageDto.getBindSubmissionId().equals(draft.getTransferInOutStageDto().getBindSubmissionId()))
                    .collect(Collectors.toList());
        }
        ArSuperDataSubmissionDto dataSubmissionDraft;
        if (IaisCommonUtils.isEmpty(dataSubmissionDraftList)) {
            dataSubmissionDraft = null;
        } else {
            dataSubmissionDraft = dataSubmissionDraftList.get(0);
        }
        if (dataSubmissionDraft != null) {
            arSuperDataSubmissionDto.setDraftId(dataSubmissionDraft.getDraftId());
            arSuperDataSubmissionDto.setDraftNo(dataSubmissionDraft.getDraftNo());
            ParamUtil.setRequestAttr(request, "hasDraft", Boolean.TRUE);
        }
    }

    private void bindStageIsInaction(HttpServletRequest request, ArSuperDataSubmissionDto bindStageArSuperDto) {
        List<String> inactionStatus = Arrays.asList(DataSubmissionConsts.DS_STATUS_INACTIVE, DataSubmissionConsts.DS_STATUS_WITHDRAW);
        if (inactionStatus.contains(bindStageArSuperDto.getDataSubmissionDto().getStatus())) {
            Map<String, String> repMap = IaisCommonUtils.genNewHashMap(1);
            repMap.put("centerName", bindStageArSuperDto.getPremisesDto().getPremiseLabel());
            String message = MessageUtil.getMessageDesc("DS_MSG021", repMap);
            ParamUtil.setRequestAttr(request, "bindStageIsRfc", message);
        }
    }

    private void hasConfirmationStage(HttpServletRequest request, String bindStageDsId) {
        ArSuperDataSubmissionDto currentArSuperDataSubmssionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        String transferConfirmationDsNo = arDataSubmissionService.getTransferConfirmationDsNoByBaseDsId(currentArSuperDataSubmssionDto.getPatientInfoDto().getPatient().getPatientCode(),
                currentArSuperDataSubmssionDto.getHciCode(),
                currentArSuperDataSubmssionDto.getSvcName(),
                bindStageDsId);
        if (StringUtil.isNotEmpty(transferConfirmationDsNo)) {
            Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
            repMap.put("transferType", getTransferTypeDes(currentArSuperDataSubmssionDto));
            repMap.put("submissionID", transferConfirmationDsNo);
            String message = MessageUtil.getMessageDesc("DS_MSG020", repMap);
            ParamUtil.setRequestAttr(request, "hasConfirmationStage", message);
        }
    }

    private String getTransferTypeDes(ArSuperDataSubmissionDto currentArSuperDataSubmssionDto) {
        TransferInOutStageDto transferInOutStageDto = currentArSuperDataSubmssionDto.getTransferInOutStageDto();
        if (TRANSFER_TYPE_IN.equals(transferInOutStageDto.getTransferType())) {
            return "transfer in";
        } else {
            return "transfer out";
        }
    }

    private boolean draftAction(HttpServletRequest request) {
        boolean hasDraft = false;
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        String actionValue = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if ("resume".equals(actionValue)) {
            hasDraft = true;
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                    currentArDataSubmission.getDraftId());
            if (arSuperDataSubmissionDtoDraft != null) {
                DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDtoDraft, request);
            } else {
                log.warn(StringUtil.changeForLog("The draft is null for " + currentArDataSubmission.getDraftId()));
            }
        } else if ("delete".equals(actionValue)) {
            hasDraft = true;
            PatientDto patient = currentArDataSubmission.getPatientInfoDto().getPatient();
            arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(patient.getIdType(),
                    patient.getIdNumber(), patient.getNationality(),
                    currentArDataSubmission.getOrgId(), currentArDataSubmission.getHciCode());
            currentArDataSubmission = DataSubmissionHelper.reNew(currentArDataSubmission);
            currentArDataSubmission.setDraftNo(null);
            currentArDataSubmission.setDraftId(null);
        }
        return hasDraft;
    }
}
