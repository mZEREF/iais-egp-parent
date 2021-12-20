package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ERR_MSG_INVALID_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_INSTRUCTION;

/**
 * @author : LiRan
 * @date : 2021/12/18
 * TODO
 */
@Slf4j
@Delegator("renewalFacilityCertifierRegisterDelegator")
public class RenewalFacilityCertifierRegisterDelegator {
    private static final String MODULE_NAME = "Renewal Facility Certifier Registration";

    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final FacilityCertifierRegistrationService facilityCertifierRegistrationService;

    public RenewalFacilityCertifierRegisterDelegator(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient, FacilityCertifierRegistrationService facilityCertifierRegistrationService) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facilityCertifierRegistrationService = facilityCertifierRegistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        request.getSession().removeAttribute(KEY_INSTRUCTION_INFO);
        request.getSession().removeAttribute(KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //charge if maskedAppId is null
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if(StringUtils.hasLength(maskedAppId)){
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID,maskedAppId);
            if(appId != null && !maskedAppId.equals(appId)){
                FacilityCertifierRegisterDto dto = facilityCertifierRegistrationService.getCertifierRegistrationAppDataByApprovalId(appId);
                NodeGroup viewApprovalRoot = dto.toRenewalReviewRootGroup(KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
                NodeGroup facRegRoot = dto.toRenewalFacCerRegRootGroup(KEY_ROOT_NODE_GROUP);
                ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                ParamUtil.setSessionAttr(request, KEY_INSTRUCTION_INFO, dto.getInstructionDto());
                failRetrieveEditData = false;
            }
            if(failRetrieveEditData){
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
    }

    public void preInstruction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void doInstruction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        Node instructionNode = viewApprovalRoot.getNode(NODE_NAME_INSTRUCTION);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityCertifierRegistrationService.jumpHandler(request, viewApprovalRoot, NODE_NAME_INSTRUCTION, instructionNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
    }

    public void preReview(BaseProcessClass bpc) {

    }

    public void doReview(BaseProcessClass bpc) {

    }

    public void preCompInfo(BaseProcessClass bpc) {

    }

    public void doCompInfo(BaseProcessClass bpc) {

    }

    public void preAdministrator(BaseProcessClass bpc) {

    }

    public void doAdministrator(BaseProcessClass bpc) {

    }

    public void preCertifyingTeam(BaseProcessClass bpc) {

    }

    public void doCertifyingTeam(BaseProcessClass bpc) {

    }

    public void preOrganisationInfo(BaseProcessClass bpc) {

    }

    public void doOrganisationProfile(BaseProcessClass bpc) {

    }

    public void prepareDocuments(BaseProcessClass bpc) {

    }

    public void doDocument(BaseProcessClass bpc) {

    }

    public void preAcknowledge(BaseProcessClass bpc) {

    }

    public void prepare(BaseProcessClass bpc) {

    }

    public void controlSwitch(BaseProcessClass bpc) {

    }
}
