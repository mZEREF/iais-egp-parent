package sg.gov.moh.iais.egp.bsb.util;

import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;
import sg.gov.moh.iais.egp.bsb.entity.FacilitySchedule;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/27
 */
public class JoinBiologicalName {

    @Autowired
    private static BiosafetyEnquiryClient biosafetyEnquiryClient;

    /**
     * This method is used to convert biologicalList to biologicalName
     */
    public static String joinBiologicalName(List<Biological> biologicalList){
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

    public static String joinBioName(List<FacilitySchedule> facilitySchedules){
        StringBuilder stringBuilder = new StringBuilder();
        if(facilitySchedules != null & facilitySchedules.size()>0){
            for (FacilitySchedule schedule : facilitySchedules) {
                for (int i = 0; i < schedule.getFacilityBiologicalAgents().size(); i++) {
                    if(i+1 < schedule.getFacilityBiologicalAgents().size()){
                        stringBuilder.append(biosafetyEnquiryClient.getBiologicalById
                                (schedule.getFacilityBiologicalAgents().get(i).getBiologicalId())
                                .getEntity().getName()).append(",");
                    }else{
                        stringBuilder.append(biosafetyEnquiryClient.getBiologicalById
                                (schedule.getFacilityBiologicalAgents().get(i).getBiologicalId())
                                .getEntity().getName());
                    }
                }

            }
        }
        return stringBuilder.toString();
    }
}
