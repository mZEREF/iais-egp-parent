package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MODULE_VIEW_NEW_FACILITY;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_FOLLOW_UP_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;

@Service
@Slf4j
public class InspectionService {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    public InspectionService(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }

    public void getInitFollowUpData(HttpServletRequest request,ReviewInsFollowUpDto dto){
        if (!StringUtils.hasLength(dto.getAppId())) {
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            // facility info
            dto = inspectionClient.getInitInsFollowUpData(appId);
            dto.setAppId(appId);
            ParamUtil.setSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO, dto);

        }
        // submission details info
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, dto.getSubmissionDetailsInfo());
        //support doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, dto.getSupportDocDisplayDtoList());
        // facility details
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, dto.getFacilityDetailsInfo());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, dto.getProcessHistoryDtoList());
        // view application need appId and moduleType
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, dto.getInsAppId());
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, MODULE_VIEW_NEW_FACILITY);
        // provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(dto.getSupportDocDisplayDtoList());
        repoIdDocNameMap.putAll(DocDisplayDtoUtil.getRepoIdDocNameMap(dto.getFollowUpDocDisplayDtoList()));
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        // display internal doc
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(dto.getAppId());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }
}
