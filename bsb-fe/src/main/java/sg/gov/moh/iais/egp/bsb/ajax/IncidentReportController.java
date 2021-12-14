package sg.gov.moh.iais.egp.bsb.ajax;


import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.IncidentNotificationClient;
import sg.gov.moh.iais.egp.bsb.dto.report.BiologicalInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author YiMing
 * @version 2021/11/16 15:46
 **/

@Slf4j
@Controller
@RequestMapping("/incident")
public class IncidentReportController {
    private final IncidentNotificationClient incidentNotClient;
    private static final String PARAM_RESULT = "result";
    private static final String PARAM_ID = "id";
    private static final String PARAM_RESULT_SUCCESS = "success";
    private static final String PARAM_RESULT_EMPTY = "empty";
    private static final String PARAM_FACILITY_ACTIVITY_BAT_MAP = "activityBatMap";

    public IncidentReportController(IncidentNotificationClient incidentNotClient) {
        this.incidentNotClient = incidentNotClient;
    }

    /**
     * this ajax method is used to get biological info by schedule from FacListDto
     * queryBiologicalBySchedule
     * @return Map<String, Object>
     * */
    @PostMapping(value = "activity.do")
    public @ResponseBody
    Map<String, Object> queryFacilityAcivity(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        //get all activity type by facName
        Map<String,List<BiologicalInfo>> map = getFacilityActivityBatMap(request);
        //will use method return activity type and list<biological> map
        //get key for activity type,need masterCode of activity type
        if(CollectionUtils.isEmpty(map)){
            log.info("map is empty");
            jsonMap.put(PARAM_RESULT,PARAM_RESULT_EMPTY);
        }else{
            Set<String> strings = map.keySet();
            Map<String,String> masterCodeActMap = Maps.newLinkedHashMapWithExpectedSize(strings.size());
            for (String string : strings) {
                masterCodeActMap.put(string,MasterCodeUtil.getCodeDesc(string));
            }
            jsonMap.put("actResult",masterCodeActMap);
            //get value for biological
            //get all distinct biological type by facName
            if(CollectionUtils.isEmpty(map.values())){
                jsonMap.put(PARAM_RESULT,PARAM_RESULT_EMPTY);
            }else{
                Set<String> bioNameSet = map.values().stream()
                        .flatMap(Collection::stream)
                        .map(BiologicalInfo::getBioName)
                        .collect(Collectors.toSet());
                jsonMap.put("bioResult",bioNameSet);
                jsonMap.put(PARAM_RESULT, PARAM_RESULT_SUCCESS);
            }
            ParamUtil.setSessionAttr(request,PARAM_FACILITY_ACTIVITY_BAT_MAP, new HashMap<>(map));
        }
        return jsonMap;
    }

    @PostMapping(value = "bio.do")
    public @ResponseBody
    Map<String, Object> queryBioNamae(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        String activityType = ParamUtil.getString(request,"activityType");
        Map<String,List<BiologicalInfo>> map = getFacilityActivityBatMap(request);
        if(CollectionUtils.isEmpty(map)){
            log.info("activity type empty");
            jsonMap.put(PARAM_RESULT,PARAM_RESULT_EMPTY);
        }else{
            List<BiologicalInfo> biologicalInfos  = map.get(activityType);
            Set<String> bioNames = biologicalInfos.stream().map(BiologicalInfo::getBioName).collect(Collectors.toSet());
            jsonMap.put(PARAM_RESULT,PARAM_RESULT_SUCCESS);
            jsonMap.put("bioResult",bioNames);
        }
        return jsonMap;
    }

    public Map<String, List<BiologicalInfo>> getFacilityActivityBatMap(HttpServletRequest request){
        HashMap<String, List<BiologicalInfo>> map = (HashMap<String, List<BiologicalInfo>>) ParamUtil.getSessionAttr(request,PARAM_FACILITY_ACTIVITY_BAT_MAP);
        return map!= null?map:getDefaultFacilityActivityBatMap(request);
    }

    private Map<String, List<BiologicalInfo>> getDefaultFacilityActivityBatMap(HttpServletRequest request){
        String facName = ParamUtil.getString(request,"facName");
        assert StringUtils.hasLength(facName);
        String facId= MaskUtil.unMaskValue(PARAM_ID,facName);
        return incidentNotClient.queryFacilityActivityFacIdMap(facId);
    }

}
