package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JoinParamUtil {
    /**
     * This method is used to convert biologicalList to biologicalName
     */
    //join biological name
    public static String joinBiologicalName(List<FacilitySchedule> facilityScheduleList, ProcessClient processClient){
        List<Biological> biologicalList=getBioListByFacilityScheduleList(facilityScheduleList,processClient);
        StringBuilder stringBuilder = new StringBuilder();
        if (biologicalList != null && biologicalList.size() > 0){
            for (int i = 0; i < biologicalList.size(); i++) {
                stringBuilder.append(biologicalList.get(i).getName());
                if (i < biologicalList.size()-1){
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    public static String joinRiskLevel(List<FacilitySchedule> facilityScheduleList,ProcessClient processClient){
        List<Biological> biologicalList=getBioListByFacilityScheduleList(facilityScheduleList,processClient);
        StringBuilder stringBuilder = new StringBuilder();
        if (biologicalList != null && biologicalList.size() > 0){
            for (int i = 0; i < biologicalList.size(); i++) {
                stringBuilder.append(MasterCodeUtil.getCodeDesc(biologicalList.get(i).getRiskLevel()));
                if (i < biologicalList.size()-1){
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * This method is used to search biologicalList by facilityScheduleList
     */
    public static List<Biological> getBioListByFacilityScheduleList(List<FacilitySchedule> facilityScheduleList,ProcessClient processClient){
        List<Biological> biologicalList = new ArrayList<>();
        if (facilityScheduleList != null && facilityScheduleList.size() > 0){
            for (int i = 0; i < facilityScheduleList.size(); i++) {
                List<FacilityBiologicalAgent> facilityBiologicalAgentList = facilityScheduleList.get(i).getFacilityBiologicalAgents();
                if (facilityBiologicalAgentList != null && facilityBiologicalAgentList.size() > 0){
                    for (int j = 0; j < facilityBiologicalAgentList.size(); j++) {
                        String biologicalId = facilityBiologicalAgentList.get(j).getBiologicalId();
                        Biological biological = processClient.getBiologicalById(biologicalId).getEntity();
                        biologicalList.add(biological);
                    }
                }
            }
        }
        return biologicalList;
    }

    //join admin name
    public static String joinAdminNames(List<FacilityAdmin> admins){
        StringBuilder s = new StringBuilder();
        if(!IaisCommonUtils.isEmpty(admins)){
            listDeduplicate(admins);
            for (int i = 0; i < admins.size(); i++) {
                s.append(admins.get(i).getName());
                if (i < admins.size()-1){
                    s.append(",");
                }
            }
        }
        return s.toString();
    }

    private static <T> List<T> listDeduplicate(List<T> list) {
        Set<T> set = IaisCommonUtils.genNewHashSet();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    //join activity type
    public static String joinActivityType(List<FacilityActivity> activityList){
        StringBuilder s = new StringBuilder();
        if(!IaisCommonUtils.isEmpty(activityList)){
            listDeduplicate(activityList);
            for (int i = 0; i < activityList.size(); i++) {
                s.append(activityList.get(i));
                if (i < activityList.size()-1){
                    s.append(",");
                }
            }
        }
        return s.toString();
    }

}
