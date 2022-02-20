package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionNCsClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyFindingFormDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2022/2/11 13:10
 **/
@Slf4j
@Delegator("rectifiesNCsDelegator")
public class BsbRectifiesNCsDelegator {
    private final InspectionNCsClient nCsClient;
    private static final String KEY_RECTIFY_FINDING_FORM = "ncsPreData";
    private static final String KEY_RECTIFY_SAVED_DTO = "ncsSavedDto";
    private static final String KEY_RECTIFY_SAVED_DOC_DTO = "ncsSavedDocDto";
    private static final String KEY_APPLICATION_ID = "appId";
    private static final String KEY_IS_RECTIFY = "isRectify";

    public BsbRectifiesNCsDelegator(InspectionNCsClient nCsClient) {
        this.nCsClient = nCsClient;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_APPLICATION_ID);
        request.getSession().removeAttribute(KEY_RECTIFY_SAVED_DTO);
        request.getSession().removeAttribute(KEY_RECTIFY_FINDING_FORM);
        request.getSession().removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);
        AuditTrailHelper.auditFunction("Applicant rectifies NCs", "Applicant rectifies NCs");
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //get application id
        //search NCs list info
        RectifyFindingFormDto rectifyFindingFormDto = nCsClient.getRectifyFindingFormDtoByAppId("B861509B-946F-EC11-BE74-000C298D317C").getEntity();
        //data->session
        ParamUtil.setSessionAttr(request,KEY_RECTIFY_FINDING_FORM,rectifyFindingFormDto);
        //pay attention to clear session
    }


    public void prepareNCsData(BaseProcessClass bpc){
        //turn icon N->Y if dto
        //mark - request
    }

    public void handleNCs(BaseProcessClass bpc){
    }

    public void submitNCs(BaseProcessClass bpc){
    }

    public void prepareRectifyData(BaseProcessClass bpc){
    }

    public void handleRectifyPage(BaseProcessClass bpc){
    }

    public void loadOriginIconStatus(){

    }


}
