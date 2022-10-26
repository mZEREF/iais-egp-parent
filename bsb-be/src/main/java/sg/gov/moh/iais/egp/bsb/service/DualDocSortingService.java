package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.AppSupportingDocClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DualDocSortingDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE_SORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DUAL_DOC_SORTING_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.TAB_ACTIVE;


@Slf4j
@Service
@RequiredArgsConstructor
public class DualDocSortingService {
    private final AppSupportingDocClient appSupportingDocClient;
    private final InternalDocClient internalDocClient;


    public static void clearDualDocSortingInfo(HttpSession session) {
        session.removeAttribute(KEY_DUAL_DOC_SORTING_INFO);
    }

    public static void clearDualDocSortingInfo(HttpServletRequest request) {
        clearDualDocSortingInfo(request.getSession());
    }


    public static DualDocSortingDto retrieveDualDocSortingInfo(HttpServletRequest request) {
        DualDocSortingDto dto = (DualDocSortingDto) ParamUtil.getSessionAttr(request, KEY_DUAL_DOC_SORTING_INFO);
        if (dto == null) {
            dto = DualDocSortingDto.newInstance();
            ParamUtil.setSessionAttr(request, KEY_DUAL_DOC_SORTING_INFO, dto);
        }
        return dto;
    }

    public void retrieveDualDocDisplayDtoIntoRequest(HttpServletRequest request, String appId) {
        DualDocSortingDto dualDocSortingDto = retrieveDualDocSortingInfo(request);
        List<DocDisplayDto> supportDocList = appSupportingDocClient.getAppSupportingDocForProcessByAppId(appId, dualDocSortingDto.getSupportingDocSort());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, new ArrayList<>(supportDocList));
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getSortedInternalDocForDisplay(appId, dualDocSortingDto.getInternalDocSort());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }


    public static void readDualDocSortingInfo(HttpServletRequest request) {
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_TYPE_SORT.equals(actionType)) {
            DualDocSortingDto dualDocSortingDto = retrieveDualDocSortingInfo(request);
            readDualDocSortingInfo(request, dualDocSortingDto);
            ParamUtil.setSessionAttr(request, KEY_DUAL_DOC_SORTING_INFO, dualDocSortingDto);
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, ModuleCommonConstants.TAB_DOC);
        }
    }

    /**
     * @see #readDualDocSortingInfo(javax.servlet.http.HttpServletRequest, java.util.function.Consumer, java.util.function.Consumer)
     */
    public static void readDualDocSortingInfo(HttpServletRequest request, DualDocSortingDto dto) {
        readDualDocSortingInfo(request, dto::setSupportingDocSort, dto::setInternalDocSort);
    }

    /**
     * Read sort info for BE document tab (which contains a supporting doc and an internal doc),
     * and set the sort data (which can be used by Spring Sort) using the consumers.
     * @param supportingDocSortSetter setter for the sort string of supporting document in the DTO
     * @param internalDocSortSetter setter for the sort string of internal document in the DTO
     */
    public static void readDualDocSortingInfo(HttpServletRequest request, Consumer<String> supportingDocSortSetter, Consumer<String> internalDocSortSetter) {
        String fieldKey = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        char docSpec = fieldKey.charAt(0);
        String field = fieldKey.substring(1);
        String sortString = field + "," + sortType;
        if (ModuleCommonConstants.PREFIX_SORT_SUPPORTING_DOC == docSpec) {
            supportingDocSortSetter.accept(sortString);
        } else if (ModuleCommonConstants.PREFIX_SORT_INTERNAL_DOC == docSpec) {
            internalDocSortSetter.accept(sortString);
        } else {
            throw new IaisRuntimeException("Neither supporting doc nor internal doc");
        }
    }
}
