package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jmapper.JMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.DraftClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sg.gov.moh.iais.egp.bsb.entity.Draft;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @author YiMing
 * @version 2022/1/15 9:04
 **/

@Slf4j
@Delegator("incidentCheckProcessDelegator")
public class IncidentCheckProcessDelegator {
    private final DraftClient draftClient;
    private static final String KEY_EDIT_APP_ID = "editId";
    private static final String KEY_PROCESS_TYPE = "processType";
    private static final String KEY_DRAFT = "draft";

    public IncidentCheckProcessDelegator(DraftClient draftClient) {
        this.draftClient = draftClient;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(ModuleCommonConstants.KEY_ACTION_TYPE);
        request.getSession().removeAttribute(KEY_DRAFT);
        request.getSession().removeAttribute("processKey");
    }

    public void checkProcess(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if(StringUtils.hasLength(maskedAppId)){
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID,maskedAppId);
            if(StringUtils.hasLength(appId)){
                DraftDto draftDto = draftClient.retrieveDraftByApplicationId(appId).getEntity();
                Assert.notNull(draftDto,"draft queried by application id is null");
                JMapper<Draft, DraftDto> draftJMapper = new JMapper<>(Draft.class,DraftDto.class);
                Draft draft = draftJMapper.getDestinationWithoutControl(draftDto);
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> draftMap;
                try {
                    draftMap = mapper.readValue(draft.getDraftData(), new TypeReference<Map<String, Object>>() {});
                    String processType = (String) draftMap.get(KEY_PROCESS_TYPE);
                    ParamUtil.setSessionAttr(request, ModuleCommonConstants.KEY_ACTION_TYPE,processType);
                    request.removeAttribute(KEY_EDIT_APP_ID);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                ParamUtil.setSessionAttr(request,KEY_DRAFT,draft);
                ParamUtil.setSessionAttr(request,"processKey",KEY_DRAFT);
            }
        }
    }
}
