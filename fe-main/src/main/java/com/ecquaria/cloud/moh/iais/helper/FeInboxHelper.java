package com.ecquaria.cloud.moh.iais.helper;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Slf4j
public final class FeInboxHelper {
    public final static List<String> allTypes = Arrays.asList(DataSubmissionConsts.DS_AR,DataSubmissionConsts.DS_DRP,
            DataSubmissionConsts.DS_LDT,DataSubmissionConsts.DS_TOP,DataSubmissionConsts.DS_VSS);
    public final static List<String> allDataSubmissionStatuses = Arrays.asList(DataSubmissionConsts.DS_STATUS_ACTIVE,DataSubmissionConsts.DS_STATUS_DRAFT,
                                                                              DataSubmissionConsts.DS_STATUS_AMENDED,DataSubmissionConsts.DS_STATUS_WITHDRAW,
                                                                              DataSubmissionConsts.DS_STATUS_LOCKED,DataSubmissionConsts.DS_STATUS_UNLOCKED);
    public final  static Map<String,String> SUBMISSIONNO_STATUS = getSubmissionNoStatus();

    public final static List<SelectOption> dataSubmissionStatusOptions = getInboxStatuses();

    private  static Map<String,String> getSubmissionNoStatus(){
        Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(7);
        //todo
        stringStringMap.put("DS",DataSubmissionConsts.DS_STATUS_DRAFT);
        stringStringMap.put("ART",DataSubmissionConsts.DS_STATUS_ACTIVE);
        return stringStringMap;
    }


    public static List<String> getDsTypes(List<String> roles){
        if(IaisCommonUtils.isEmpty(roles)){
            return null;
        }
        List<String> types = IaisCommonUtils.genNewArrayList(5);
        roles.stream().forEach(role ->{
            switch(role){
                case RoleConsts.USER_ROLE_DS_AR :
                    types.add(DataSubmissionConsts.DS_AR);
                    break;
                case RoleConsts.USER_ROLE_DS_DP :
                    types.add(DataSubmissionConsts.DS_DRP);
                    break;
                default: break;
            }
        });
        //todo delete
        types.add(DataSubmissionConsts.DS_AR);
        types.add(DataSubmissionConsts.DS_DRP);
        return types;
    }

    private static List<SelectOption> getInboxStatuses(){
        List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.DATA_SUBMISSION_STATUS);
        ListIterator<SelectOption> selectOptionVector = selectOptions.listIterator();
        if(selectOptionVector.hasNext()){
            SelectOption selectOption = selectOptionVector.next();
            if(!allDataSubmissionStatuses.contains(selectOption.getValue())){
                selectOptionVector.remove();
            }
        }
        return selectOptions;
    }

    private FeInboxHelper(){
        throw new IllegalStateException("Utility class");
    }
}
