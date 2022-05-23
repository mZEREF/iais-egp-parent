package com.ecquaria.cloud.moh.iais.helper;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.privilege.PrivilegeConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public final class FeInboxHelper {
    public final static Set<String> allDataSubmissionStatuses = new HashSet<>(Arrays.asList(DataSubmissionConsts.DS_STATUS_ACTIVE,
                                                                               DataSubmissionConsts.DS_STATUS_DRAFT,
                                                                               DataSubmissionConsts.DS_STATUS_AMENDED, DataSubmissionConsts.DS_STATUS_LOCKED,
                                                                               DataSubmissionConsts.DS_STATUS_UNLOCKED));
    public final static List<String> dataSubmissionStatusesForArPrivate = Arrays.asList(DataSubmissionConsts.DS_STATUS_LOCKED,DataSubmissionConsts.DS_STATUS_UNLOCKED);
    public final static List<String> dataInboxNoNeedShowStatuses =  Collections.singletonList(DataSubmissionConsts.DS_STATUS_WITHDRAW);
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

    public static List<SelectOption> getInboxStatuses(List<String> privilegeIds){
         for(String privilegeId : privilegeIds){
             if(PrivilegeConsts.USER_PRIVILEGE_DS_AR.equalsIgnoreCase(privilegeId)){
                 return dataSubmissionStatusOptions;
             }
         }
         return dataSubmissionStatusOptions.stream().filter( s-> !StringUtil.isIn(s.getValue(),dataSubmissionStatusesForArPrivate)).collect(Collectors.toList());
    }

    public static List<SelectOption> getSubmissionTypes(List<String> privilegeIds){
        List<String> subTypes = IaisCommonUtils.genNewArrayList(13);
        privilegeIds.stream().forEach(privilegeId ->{
            switch(privilegeId){
                case PrivilegeConsts.USER_PRIVILEGE_DS_AR :
                    subTypes.addAll(DataSubmissionConsts.DS_CYCLE_AR_TYPE_LIST);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_DP :
                    subTypes.addAll(DataSubmissionConsts.DS_CYCLE_DP_TYPE_LIST);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_TOP:
                    subTypes.addAll(DataSubmissionConsts.DS_CYCLE_TOP_TYPE_LIST);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_VSS:
                    subTypes.addAll(DataSubmissionConsts.DS_CYCLE_VSS_TYPE_LIST);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_LDT:
                    subTypes.addAll(DataSubmissionConsts.DS_CYCLE_LDT_TYPE_LIST);
                    break;
                default: break;
            }
        });
        return IaisCommonUtils.isNotEmpty(subTypes) ?
                MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.DATA_SUBMISSION_TYPE).stream().filter( selectOption -> subTypes.contains(selectOption.getValue())).collect(Collectors.toList()) : null;
    }
    public static <T extends SelectOption> String getCaseWhenSql(List<T> list,String replaceArea,String tabCol,String renameTabCol,String sql,boolean elseDefaultEmpty){
        if(StringUtil.isNotEmpty(replaceArea) && StringUtil.isNotEmpty(sql) && IaisCommonUtils.isNotEmpty(list)){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" CASE ");
            list.stream().forEach( s->  stringBuilder.append(" WHEN ").append(tabCol).append(" ='").append(s.getValue()).append("' THEN '").append(s.getText()).append("' "));
            stringBuilder.append(" ELSE ").append(elseDefaultEmpty ? "''" : tabCol ).append(" END ").append(renameTabCol).append("  ");
            return sql.replace(replaceArea,stringBuilder);
        }
        return sql;
    }
    public static <T extends SelectOption> String getCaseWhenSql(List<T> list,String replaceArea,String tabCol,String renameTabCol,String sql){
            return getCaseWhenSql(list,replaceArea,tabCol,renameTabCol,sql,true);
    }

    private FeInboxHelper(){
        throw new IllegalStateException("Utility class");
    }
}
