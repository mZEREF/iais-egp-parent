package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;
import sg.gov.moh.iais.egp.bsb.entity.FacilitySchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/27
 */
public class JoinBiologicalName {

    /**
     * This method is used to convert biologicalList to biologicalName
     */
    public static String joinBiologicalName(List<FacilitySchedule> facilityScheduleList,ProcessClient processClient){
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
}
