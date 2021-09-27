package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAdmin;

import java.util.List;
import java.util.Set;

/**
 * @author Zhu Tangtang
 * @date 2021/9/27 13:41
 */
public class JoinActivityType {

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

    private static <T> List<T> listDeduplicate(List<T> list) {
        Set<T> set = IaisCommonUtils.genNewHashSet();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
