package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.ApprovalAppClient;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : LiRan
 * @date : 2021/10/22
 */
@Slf4j
@Controller
@RequestMapping("/bsbApprovalSelect")
public class ApprovalAppAjaxController {
    @Autowired
    private ApprovalAppClient approvalAppClient;

    @RequestMapping(value = "facilityIdSelect", method = RequestMethod.POST)
    public @ResponseBody
    Map<String,Object> getActivityByFacilityId(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        String facilityId = request.getParameter("facilityId");
        if (StringUtil.hasLength(facilityId)){
            List<FacilityActivity> facilityActivityList = approvalAppClient.getApprovalFAByFacId(facilityId).getEntity();
            jsonMap.put("result", "success");
            jsonMap.put("queryResult",facilityActivityList);
        }else {
            jsonMap.put("result", "Fail");
        }
        return jsonMap;
    }

}
