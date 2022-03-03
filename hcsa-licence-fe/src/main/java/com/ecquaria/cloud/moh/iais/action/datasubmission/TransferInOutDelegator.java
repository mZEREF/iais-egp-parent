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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
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
import java.util.stream.Collectors;

@Delegator("transferInOutDelegator")
@Slf4j
public class TransferInOutDelegator extends CommonDelegator {
    public static final String WHAT_WAS_TRANSFERREDS = "transferreds";

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
        initSelectOpts(request);
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
        for (String transferred : transferredList) {
            if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_OOCYTES)) {
                if (transferInOutStageDto.getOocyteNum() != null) {
                    arChangeInventoryDto.setFrozenOocyteNum(num * Integer.parseInt(transferInOutStageDto.getOocyteNum()));
                }
            }
            if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_EMBRYOS)) {
                if (transferInOutStageDto.getEmbryoNum() != null) {
                    arChangeInventoryDto.setFrozenEmbryoNum(num * Integer.parseInt(transferInOutStageDto.getEmbryoNum()));
                }
            }
            if (transferred.equals(DataSubmissionConsts.WHAT_WAS_TRANSFERRED_SPERM)) {
                if (transferInOutStageDto.getSpermVialsNum() != null) {
                    arChangeInventoryDto.setFrozenSpermNum(num * Integer.parseInt(transferInOutStageDto.getSpermVialsNum()));
                }
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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        arSuperDataSubmissionDto = arSuperDataSubmissionDto == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto() == null ? new TransferInOutStageDto() : arSuperDataSubmissionDto.getTransferInOutStageDto();

        String outStageDsNo = ParamUtil.getString(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_NO);
        if (StringUtil.isEmpty(outStageDsNo)) {
            setDataFromPage(request, transferInOutStageDto);
        } else {
            String oocyteNum = ParamUtil.getString(request, "oocyteNum");
            String embryoNum = ParamUtil.getString(request, "embryoNum");
            String spermVialsNum = ParamUtil.getString(request, "spermVialsNum");
            transferInOutStageDto.setOocyteNum(oocyteNum);
            transferInOutStageDto.setEmbryoNum(embryoNum);
            transferInOutStageDto.setSpermVialsNum(spermVialsNum);
            flagInAndOutDiscrepancy(request, transferInOutStageDto);
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
            sendNeedTransferInNotification(transferInOutStageDto.getTransOutToLicenseeId(), arSuperDataSubmissionDto.getHciCode(), arSuperDataSubmissionDto.getDataSubmissionDto().getSubmissionNo());
        }
    }

    public void initSelectOpts(HttpServletRequest request) {
        PremisesDto currentPremisesDto = DataSubmissionHelper.getCurrentArDataSubmission(request).getPremisesDto();
        List<PremisesDto> premisesDtos = dsLicenceService.getArCenterPremises();
        premisesDtos = premisesDtos.stream().filter(premisesDto ->
                !premisesDto.getHciCode().equals(currentPremisesDto.getHciCode()) || !premisesDto.getOrganizationId().equals(currentPremisesDto.getOrganizationId())
        ).collect(Collectors.toList());
        List<SelectOption> premisesSel = IaisCommonUtils.genNewArrayList();
        for (PremisesDto premisesDto : premisesDtos) {
            String licenseeId = getLicenseeId(premisesDto.getOrganizationId());
            premisesSel.add(new SelectOption(licenseeId + "/" + premisesDto.getHciCode(), premisesDto.getBusinessName()));
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
        transferInOutStageDto.setOocyteNum(oocyteNum);
        transferInOutStageDto.setEmbryoNum(embryoNum);
        transferInOutStageDto.setSpermVialsNum(spermVialsNum);
        String fromDonor = ParamUtil.getString(request, "fromDonor");
        transferInOutStageDto.setFromDonor("true".equalsIgnoreCase(fromDonor));
        if (!IaisCommonUtils.isEmpty(transferredList)) {
            transferInOutStageDto.setTransferredList(Arrays.asList(transferredList));
        } else {
            transferInOutStageDto.setTransferredList(null);
        }

        String licenseeId = DataSubmissionHelper.getLicenseeId(request);
        String hciCode = DataSubmissionHelper.getCurrentArDataSubmission(request).getHciCode();
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
            transferInOutStageDto.setTransOutToLicenseeId(licenseeId);
            transferInOutStageDto.setTransOutToHciCode(hciCode);
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
            transferInOutStageDto.setTransInFromLicenseeId(licenseeId);
            transferInOutStageDto.setTransInFromHciCode(hciCode);
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

    private boolean isNeedEmail(TransferInOutStageDto transferInOutStageDto) {
        if (transferInOutStageDto != null && "out".equals(transferInOutStageDto.getTransferType()) && !"Others".equals(transferInOutStageDto.getTransOutToHciCode())) {
            return true;
        }
        return false;
    }

    private void initReceive(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        String outStageDsNo = ParamUtil.getRequestString(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_NO);
        if (arSuperDataSubmissionDto != null){
            TransferInOutStageDto transferInOutStageDto = arSuperDataSubmissionDto.getTransferInOutStageDto();
            if (StringUtil.isEmpty(outStageDsNo)) {
                if (transferInOutStageDto != null && StringUtil.isNotEmpty(transferInOutStageDto.getOutStageDsNo())){
                    outStageDsNo = transferInOutStageDto.getOutStageDsNo();
                }
            }
        }
        if (StringUtil.isEmpty(outStageDsNo)) {
            return;
        }
        DataSubmissionHelper.clearSession(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_TRANSFER_OUT_STAGE_NO, outStageDsNo);
        ArSuperDataSubmissionDto outStageArSuperDto = initOutStageDto(request, outStageDsNo);
        initArSuper(request, outStageArSuperDto, outStageDsNo);
    }

    private ArSuperDataSubmissionDto initArSuper(HttpServletRequest request, ArSuperDataSubmissionDto outArDto, String outStageDsNo) {
        TransferInOutStageDto outStageDto = outArDto.getTransferInOutStageDto();
        String hciCode = outStageDto.getTransOutToHciCode();
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

        String patientCode = outArDto.getCycleDto().getPatientCode();
        ArSuperDataSubmissionDto newDto = arDataSubmissionService.getArSuperDataSubmissionDto(patientCode,
                hciCode, null);

        String svcName = arSuper.getSvcName();
        if (newDto != null) {
            log.info("-----Retieve ArSuperDataSubmissionDto from DB-----");
            CycleStageSelectionDto selectionDto = newDto.getSelectionDto();
            selectionDto.setStage(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
            CycleDto cycleDto = DataSubmissionHelper.initCycleDto(selectionDto, svcName, hciCode, licenseeId);
            arSuper.setCycleDto(cycleDto);
            arSuper.setSelectionDto(selectionDto);
            arSuper.setPatientInfoDto(newDto.getPatientInfoDto());
        }

        ArCurrentInventoryDto arCurrentInventoryDto = arDataSubmissionService.getArCurrentInventoryDtoByConds(hciCode, licenseeId, patientCode);
        if (arCurrentInventoryDto == null) {
            arCurrentInventoryDto = new ArCurrentInventoryDto();
            arCurrentInventoryDto.setHciCode(hciCode);
            arCurrentInventoryDto.setSvcName(svcName);
            arCurrentInventoryDto.setLicenseeId(licenseeId);
            arCurrentInventoryDto.setPatientCode(patientCode);
        }
        arSuper.setArCurrentInventoryDto(arCurrentInventoryDto);

        TransferInOutStageDto transferInOutStageDto = (TransferInOutStageDto) CopyUtil.copyMutableObject(outStageDto);
        transferInOutStageDto.setId(null);
        transferInOutStageDto.setSubmissionId(null);
        transferInOutStageDto.setTransOutToHciCode(null);
        transferInOutStageDto.setTransferType("in");
        transferInOutStageDto.setTransInFromHciCode(outArDto.getCycleDto().getHciCode());
        transferInOutStageDto.setOutStageDsNo(outStageDsNo);
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
            boolean diffOocyte = !outStageDto.getOocyteNum().equals(transferInOutStageDto.getOocyteNum());
            boolean diffEmbryo = !outStageDto.getEmbryoNum().equals(transferInOutStageDto.getEmbryoNum());
            boolean diffSpermVial = !outStageDto.getSpermVialsNum().equals(transferInOutStageDto.getSpermVialsNum());
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
}
