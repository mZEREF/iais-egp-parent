package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.sz.commons.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.renewal.defer.DeferRenewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DeferRenewConstants.KEY_CHECK;
import static sg.gov.moh.iais.egp.bsb.constant.DeferRenewConstants.KEY_DEFER_RENEW_DATE;
import static sg.gov.moh.iais.egp.bsb.constant.DeferRenewConstants.KEY_DEFER_RENEW_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DeferRenewConstants.KEY_DEFER_RENEW_REASON;
import static sg.gov.moh.iais.egp.bsb.constant.DeferRenewConstants.PARAM_NEW_FILES;
import static sg.gov.moh.iais.egp.bsb.constant.DeferRenewConstants.PARAM_SAVED_FILES;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_PRIMARY_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.ValidationConstants.IS_VALID;
import static sg.gov.moh.iais.egp.bsb.constant.ValidationConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.ValidationConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FAC_ID;

@Slf4j
@Delegator("deferRenewalDelegator")
@RequiredArgsConstructor
public class DeferRenewalDelegator {

    private final FacilityRegisterClient facilityRegisterClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_DEFER_RENEW_DTO);
        request.getSession().removeAttribute(PARAM_NEW_FILES);
        request.getSession().removeAttribute(PARAM_PRIMARY_DOC_DTO);
        request.getSession().removeAttribute(PARAM_SAVED_FILES);
    }

    @SneakyThrows(JsonProcessingException.class)
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        DeferRenewDto deferRenewDto = (DeferRenewDto)ParamUtil.getSessionAttr(request,KEY_DEFER_RENEW_DTO);
        if(deferRenewDto == null){
            String appId = request.getParameter(KEY_APP_ID);
            String facId = request.getParameter(KEY_FAC_ID);
            if(!StringUtils.isEmpty(appId)){
                appId = MaskUtil.unMaskValue(KEY_APP_ID,appId);
                deferRenewDto = facilityRegisterClient.getDeferViewByApplicationId(appId).getBody();
            }else if(!StringUtils.isEmpty(facId)) {
                facId = MaskUtil.unMaskValue(KEY_FAC_ID,facId);
                deferRenewDto = facilityRegisterClient.getDeferViewByFacilityId(facId).getBody();
            }else {
                throw new IllegalArgumentException("parameter is empty");
            }
            Assert.notNull(deferRenewDto,"facility is not exist!");

            ParamUtil.setSessionAttr(request,KEY_DEFER_RENEW_DTO, deferRenewDto);

            PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);
            if (!CollectionUtils.isEmpty(deferRenewDto.getSavedInfos())) {
                Map<String, DocRecordInfo> docRecordInfoMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, deferRenewDto.getSavedInfos());
                primaryDocDto.setSavedDocMap(docRecordInfoMap);
                ParamUtil.setSessionAttr(request, PARAM_PRIMARY_DOC_DTO, primaryDocDto);
                ParamUtil.setSessionAttr(request, PARAM_SAVED_FILES, (Serializable) primaryDocDto.getSavedDocMap());
            }
        }

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);
    }

    public void submitRenew(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        DeferRenewDto deferRenewDto = readFormData(mulReq);

        //doc
        PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);
        primaryDocDto.reqObjMapping(mulReq);
        //set need Validation value
        deferRenewDto.setDocMetas(primaryDocDto.convertToDocMetaList());

        ValidationResultDto validationResultDto = facilityRegisterClient.validateDeferRenew(deferRenewDto);
        if(!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, IS_VALID, NO);
        }else {
            ParamUtil.setRequestAttr(request, IS_VALID, YES);
        }
        ParamUtil.setSessionAttr(request,KEY_DEFER_RENEW_DTO, deferRenewDto);
        ParamUtil.setSessionAttr(request, PARAM_NEW_FILES, (Serializable) primaryDocDto.getNewDocMap());
        ParamUtil.setSessionAttr(request, PARAM_PRIMARY_DOC_DTO, primaryDocDto);
    }

    private DeferRenewDto readFormData(HttpServletRequest request) throws ParseException {
        DeferRenewDto deferRenewDto = (DeferRenewDto)ParamUtil.getSessionAttr(request,KEY_DEFER_RENEW_DTO);
        String deferDate = ParamUtil.getDate(request, KEY_DEFER_RENEW_DATE);
        String reason = ParamUtil.getString(request, KEY_DEFER_RENEW_REASON);
        String check = ParamUtil.getString(request, KEY_CHECK);
        if(!StringUtils.hasLength(check)){
            check = NO;
        }
        Date date = DateUtil.parseDateTime(deferDate, "dd/MM/yyyy");
        deferRenewDto.setDeferDate(date);
        deferRenewDto.setDeferReason(reason);
        deferRenewDto.setCheck(check);
        return deferRenewDto;
    }

    public void backToDefer(BaseProcessClass bpc){
        log.info("request for failed of defer submit {}",bpc.request);
    }

    public void preAcknowledge(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        DeferRenewDto dto = (DeferRenewDto)ParamUtil.getSessionAttr(request,KEY_DEFER_RENEW_DTO);
        PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);

        //complete simple save file to db and save data to dto for show in jsp
        MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
        List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
        //newFile change to saved File and save info to db
        List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocDto.newFileSaved(repoIds));
        dto.setSavedInfos(primaryDocDto.getExistDocTypeList());

        AppMainInfo appInfo = facilityRegisterClient.saveDeferRenewData(dto).getBody();
        ParamUtil.setRequestAttr(request,KEY_APP_INFO,appInfo);

        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (!newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                syncDto.setNewFiles(newFilesToSync);
                bsbFileClient.saveFiles(syncDto);
            }
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    private PrimaryDocDto getPrimaryDocDto(HttpServletRequest request) {
        PrimaryDocDto docDto = (PrimaryDocDto) ParamUtil.getSessionAttr(request, PARAM_PRIMARY_DOC_DTO);
        return docDto == null ? new PrimaryDocDto() : docDto;
    }
}
