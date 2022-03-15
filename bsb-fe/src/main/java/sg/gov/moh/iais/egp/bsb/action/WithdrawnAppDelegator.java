package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.PrimaryDocDto;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_NON_OBJECT_ERROR;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("bsbWithDrawnAppDelegator")
public class WithdrawnAppDelegator {
    private static final String MODULE_NAME = "Withdrawn Application";
    private static final String ACTION_TYPE_NEXT = "doNext";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";
    public static final String WITHDRAWN_APP_DTO = "withdrawnDto";
    private static final String PARAM_REASON = "reason";
    private static final String PARAM_REMARKS = "remarks";
    private static final String PARAM_BACK_URL = "backUrl";
    private static final String PARAM_FROM = "from";

    private final WithdrawnClient withdrawnClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public WithdrawnAppDelegator(WithdrawnClient withdrawnClient, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.withdrawnClient = withdrawnClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, null);
        ParamUtil.setSessionAttr(request, PARAM_FROM, null);
        ParamUtil.setSessionAttr(request, PARAM_BACK_URL, null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        if (StringUtils.isEmpty(dto.getAppId())) {
            String from = request.getParameter(PARAM_FROM);
            String maskedApplicationId = request.getParameter("withdrawnAppId");
            String applicationId = MaskUtil.unMaskValue("id", maskedApplicationId);
            if (maskedApplicationId == null || applicationId == null || maskedApplicationId.equals(applicationId)) {
                throw new IaisRuntimeException("Invalid Application ID");
            }
            ResponseDto<AppSubmitWithdrawnDto> responseDto = withdrawnClient.getWithdrawnDataByApplicationId(applicationId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
                dto.setFrom(from);
            } else {
                log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, WITHDRAWN_APP_DTO, new AppSubmitWithdrawnDto());
            }
            //
            ParamUtil.setSessionAttr(request, PARAM_FROM, from);
        }
        if (dto.getFrom().equals("application")){
            ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-fe/eservice/INTERNET/MohBSBInboxApp");
        }else if (dto.getFrom().equals("dataSubmission")){
            ParamUtil.setSessionAttr(request, PARAM_BACK_URL, "/bsb-fe/eservice/INTERNET/DataSubInbox");
        }
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String reason = ParamUtil.getRequestString(request, PARAM_REASON);
        String remarks = ParamUtil.getRequestString(request, PARAM_REMARKS);
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        dto.setReason(reason);
        dto.setRemarks(remarks);
        //
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        dto.setLoginUser(loginContext.getUserName());
        //fill doc
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqObjMapping(mulReq, request, "withdrawn");
        dto.setPrimaryDocDto(primaryDocDto);
        dto.setDocType("withdrawn");
        //joint repoId exist
        String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
        dto.setRepoIdNewString(newRepoId);
        //set newDocFiles
        dto.setNewDocInfos(primaryDocDto.getNewDocTypeList());
        //set need Validation value
        dto.setDocMetas(primaryDocDto.doValidation());

        //validation
        String actionType = "";
        ValidationResultDto validationResultDto = withdrawnClient.validateWithdrawnDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_NEXT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void submitWithdrawn(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        PrimaryDocDto primaryDocDto = dto.getPrimaryDocDto();
        List<NewFileSyncDto> newFilesToSync = null;
        if (primaryDocDto != null) {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(PrimaryDocDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            //newFile change to saved File and save info to db
            newFilesToSync = new ArrayList<>(primaryDocDto.newFileSaved(repoIds));
            dto.setSavedInfos(primaryDocDto.getExistDocTypeList());
        } else {
            log.info(KEY_NON_OBJECT_ERROR);
        }
        withdrawnClient.saveWithdrawnApp(dto);
        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (newFilesToSync != null && !newFilesToSync.isEmpty()) {
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

    private AppSubmitWithdrawnDto getWithdrawnDto(HttpServletRequest request) {
        AppSubmitWithdrawnDto auditDto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        return auditDto == null ? getDefaultWithdrawnDto() : auditDto;
    }

    private AppSubmitWithdrawnDto getDefaultWithdrawnDto() {
        return new AppSubmitWithdrawnDto();
    }
}
