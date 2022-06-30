package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.FacCertifierRegisterClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.ViewAppService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_ADMINISTRATOR;
import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_CERTIFYING_TEAM_DETAIL;
import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_COMPANY_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_FACILITY_CERTIFIER_REGISTER;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_MASKED_EDIT_APP_ID;

/**
 * @author YiMing
 * @version 2021/10/22 10:38
 **/

@Delegator(value = "bsbViewCertRegAppDelegator")
@Slf4j
public class ViewCertifierRegistrationDelegator {
    private static final String MODULE_NAME = "Facility Certifier Registration";
    private static final String FUNCTION_NAME = "View Application";

    private final FacCertifierRegisterClient certifierRegisterClient;
    private final DocSettingService docSettingService;

    @Autowired
    public ViewCertifierRegistrationDelegator(FacCertifierRegisterClient certifierRegisterClient, DocSettingService docSettingService) {
        this.certifierRegisterClient = certifierRegisterClient;
        this.docSettingService = docSettingService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_MASKED_EDIT_APP_ID);
        session.removeAttribute(KEY_FACILITY_CERTIFIER_REGISTER);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void init(BaseProcessClass bpc) {
        ViewAppService.init(bpc);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getRequestAttr(request, KEY_APP_ID);
        FacilityCertifierRegisterDto facilityCertifierRegisterDto = getFacilityCertifierRegisterDto(request, appId);
        ParamUtil.setRequestAttr(request, NODE_NAME_COMPANY_PROFILE, facilityCertifierRegisterDto.getProfileDto());
        ParamUtil.setRequestAttr(request, NODE_NAME_CERTIFYING_TEAM_DETAIL, facilityCertifierRegisterDto.getCertifyingTeamDto());
        ParamUtil.setRequestAttr(request, NODE_NAME_ADMINISTRATOR, facilityCertifierRegisterDto.getAdministratorDto());

        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getFacCerRegDocSettings());
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(facilityCertifierRegisterDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
    }

    public FacilityCertifierRegisterDto getFacilityCertifierRegisterDto(HttpServletRequest request, String appId) {
        FacilityCertifierRegisterDto facilityCertifierRegisterDto = (FacilityCertifierRegisterDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_CERTIFIER_REGISTER);
        if (facilityCertifierRegisterDto == null) {
            ResponseDto<FacilityCertifierRegisterDto> resultDto = certifierRegisterClient.getCertifierRegistrationAppData(appId);
            if (!resultDto.ok()) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
            facilityCertifierRegisterDto = resultDto.getEntity();
            ParamUtil.setSessionAttr(request, KEY_FACILITY_CERTIFIER_REGISTER, facilityCertifierRegisterDto);
        }
        return facilityCertifierRegisterDto;
    }
}
