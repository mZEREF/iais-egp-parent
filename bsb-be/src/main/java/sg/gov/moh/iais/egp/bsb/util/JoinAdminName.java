package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAdmin;

import java.util.List;
import java.util.Set;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/18 17:26
 * DESCRIPTION: TODO
 **/
public class  JoinAdminName {

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
}
