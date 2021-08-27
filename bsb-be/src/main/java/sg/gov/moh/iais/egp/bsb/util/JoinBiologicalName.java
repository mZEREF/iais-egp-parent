package sg.gov.moh.iais.egp.bsb.util;

import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/27
 */
public class JoinBiologicalName {

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
}
