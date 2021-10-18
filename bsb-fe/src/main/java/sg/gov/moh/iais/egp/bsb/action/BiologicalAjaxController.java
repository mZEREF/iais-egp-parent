package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.ApprovalApplicationClient;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Slf4j
@Controller
@RequestMapping("/bio-info")
public class BiologicalAjaxController {
    @Autowired
    private ApprovalApplicationClient approvalApplicationClient;

    @RequestMapping(value = "bio.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> queryBiologicalBySchedule(HttpServletRequest request) {
        Map<String, Object> jsonMap = IaisCommonUtils.genNewHashMap();
        String schedule = ParamUtil.getString(request,"schedule");
        List<Biological> biologicalQueryDtoList = approvalApplicationClient.getBiologicalBySchedule(schedule).getEntity();
        if(biologicalQueryDtoList != null && biologicalQueryDtoList.size()>0) {
            List<Biological> biologicalQueryDto = IaisCommonUtils.genNewArrayList();
            for (Biological biological : biologicalQueryDtoList) {
                biologicalQueryDto.add(biological);
            }
            jsonMap.put("result", "success");
            jsonMap.put("queryResult",biologicalQueryDto);
        } else {
            jsonMap.put("result", "Fail");
        }
        return jsonMap;
    }

}
