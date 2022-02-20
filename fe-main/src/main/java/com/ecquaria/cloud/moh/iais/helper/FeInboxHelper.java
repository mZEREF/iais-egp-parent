package com.ecquaria.cloud.moh.iais.helper;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Slf4j
public final class FeInboxHelper {
    public final static List<String> allDataSubmissionStatuses = Arrays.asList(DataSubmissionConsts.DS_STATUS_ACTIVE,DataSubmissionConsts.DS_STATUS_DRAFT,
                                                                               DataSubmissionConsts.DS_STATUS_AMENDED, DataSubmissionConsts.DS_STATUS_LOCKED,
                                                                               DataSubmissionConsts.DS_STATUS_UNLOCKED);
    public final static List<String> dataInboxNoNeedShowStatuses =  Arrays.asList(DataSubmissionConsts.DS_STATUS_WITHDRAW);
    public final  static Map<String,String> SUBMISSIONNO_STATUS = getSubmissionNoStatus();

    public final static List<SelectOption> dataSubmissionStatusOptions = getInboxStatuses();

    private  static Map<String,String> getSubmissionNoStatus(){
        Map<String,String> stringStringMap = IaisCommonUtils.genNewHashMap(2);
        stringStringMap.put("DS",DataSubmissionConsts.DS_STATUS_DRAFT);
        stringStringMap.put("ART",DataSubmissionConsts.DS_STATUS_ACTIVE);
        return stringStringMap;
    }


    public static List<String> getDsTypes(List<String> privilegeIds){
       return HalpSearchResultHelper.getDsTypes(privilegeIds);
    }

    private static List<SelectOption> getInboxStatuses(){
        List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.DATA_SUBMISSION_STATUS);
        ListIterator<SelectOption> selectOptionVector = selectOptions.listIterator();
        while(selectOptionVector.hasNext()){
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
