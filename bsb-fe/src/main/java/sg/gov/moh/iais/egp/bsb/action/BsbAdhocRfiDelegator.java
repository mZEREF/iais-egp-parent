package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.AdhocRfiClient;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiDetailDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiQueryResultDto;
import sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiViewDto;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_ADHOC_RFI;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.action.WithdrawnAppDelegator.PARAM_NEW_FILES;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_PRIMARY_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;

/**
 * BsbAdhocRfiDelegator
 * @author junyu
 * @date 2022/4/15
 */
@Slf4j
@Delegator("bsbAdhocRfiDelegator")
@RequiredArgsConstructor
public class BsbAdhocRfiDelegator {
    private static final String KEY_ADHOC_RFI_LIST = "reqForInfoSearchList";
    private static final String KEY_ADHOC_LIST_SEARCH_DTO = "adhocSearchDto";
    private static final String KEY_ADHOC_PAGE_INFO = "pageInfo";
    private static final String KEY_USER_REPLY = "userReply";
    private static final String FILE_UPLOAD_ERROR = "fileUploadError";
    private static final String ADHOC_REQ_FOR_INFO_DTO = "adhocReqForInfoDto";

    private final AdhocRfiClient adhocRfiClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        HttpServletRequest request = bpc.request;
        String facilityNo = ParamUtil.getMaskedString(request,"facilityNo");
        AdhocRfiQueryDto dto = new AdhocRfiQueryDto();
        dto.defaultPaging();
        dto.setFacilityNo(facilityNo);
        dto.setStatus("RFIST001");
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, dto);

        AuditTrailHelper.auditFunction(MODULE_REQUEST_FOR_INFORMATION, FUNCTION_ADHOC_RFI);
    }

    public void preRfiList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.removeAttribute(ADHOC_REQ_FOR_INFO_DTO);
        AdhocRfiQueryDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO, searchDto);
        ResponseDto<AdhocRfiQueryResultDto> resultDto = adhocRfiClient.queryAdhocRfi(searchDto);
        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_ADHOC_RFI_LIST, (Serializable) resultDto.getEntity().getRfiList());
        } else {
            log.warn("get adhocRfi API doesn't return ok, the response is {}", resultDto);
            ParamUtil.setRequestAttr(request, KEY_ADHOC_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_ADHOC_RFI_LIST, new ArrayList<>());
        }
    }

    public void preAdhocRfiDetail(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        AdhocRfiViewDto adhocRfiViewDto= (AdhocRfiViewDto) ParamUtil.getSessionAttr(request,ADHOC_REQ_FOR_INFO_DTO);
        try {
            if(adhocRfiViewDto==null){
                adhocRfiViewDto=new AdhocRfiViewDto();
            }
            String id = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if(!StringUtils.isEmpty(id)){
                adhocRfiViewDto=adhocRfiClient.getAdhocRfiById(id).getEntity();
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        ParamUtil.setSessionAttr(request,ADHOC_REQ_FOR_INFO_DTO,  adhocRfiViewDto);

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);

        ParamUtil.setSessionAttr(request, PARAM_NEW_FILES, null);
        ParamUtil.setSessionAttr(request, PARAM_PRIMARY_DOC_DTO, null);
    }
    public void next(BaseProcessClass bpc) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);

    }

    public void submintAdhocRfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);

        AdhocRfiViewDto adhocRfiViewDto = (AdhocRfiViewDto) ParamUtil.getSessionAttr(request,ADHOC_REQ_FOR_INFO_DTO);

        ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, "Y");

        AdhocRfiDetailDto detailDto = adhocRfiViewDto.getDetailDto();
        String userReply=mulReq.getParameter(KEY_USER_REPLY);
        detailDto.setSuppliedInformation(userReply);
        //doc
        PrimaryDocDto primaryDocDto = getPrimaryDocDto(request);
        primaryDocDto.reqObjMapping(mulReq);
        //set need Validation value
        adhocRfiViewDto.setDocMetas(primaryDocDto.convertToDocMetaList());
        ParamUtil.setSessionAttr(request, PARAM_NEW_FILES, (Serializable) primaryDocDto.getNewDocMap());
        ParamUtil.setSessionAttr(request, PARAM_PRIMARY_DOC_DTO, primaryDocDto);
        //todo call method validate
        ParamUtil.setSessionAttr(request,ADHOC_REQ_FOR_INFO_DTO,adhocRfiViewDto);
        ValidationResultDto validationResultDto = adhocRfiClient.validateAdhocRfiViewDto(adhocRfiViewDto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, "N");
        } else {
            //complete simple save file to db and save data to dto for show in jsp
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            //newFile change to saved File and save info to db
            List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocDto.newFileSaved(repoIds));
            adhocRfiViewDto.setApplicationDocDtos(primaryDocDto.getExistDocTypeList());
            detailDto.setStatus(RequestForInformationConstants.RFI_CLOSE);
            adhocRfiClient.saveAdhocRfi(adhocRfiViewDto);

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
            ParamUtil.setSessionAttr(request,ADHOC_REQ_FOR_INFO_DTO,  null);
        }
    }

    public void backList(BaseProcessClass bpc) {
        // do nothing now
    }

    private AdhocRfiQueryDto getSearchDto(HttpServletRequest request) {
        AdhocRfiQueryDto searchDto = (AdhocRfiQueryDto) ParamUtil.getSessionAttr(request, KEY_ADHOC_LIST_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private AdhocRfiQueryDto getDefaultSearchDto() {
        AdhocRfiQueryDto dto = new AdhocRfiQueryDto();
        dto.defaultPaging();
        return dto;
    }

    private PrimaryDocDto getPrimaryDocDto(HttpServletRequest request) {
        PrimaryDocDto docDto = (PrimaryDocDto) ParamUtil.getSessionAttr(request, PARAM_PRIMARY_DOC_DTO);
        return docDto == null ? new PrimaryDocDto() : docDto;
    }

}
