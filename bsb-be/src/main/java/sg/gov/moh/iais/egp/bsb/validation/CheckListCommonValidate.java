package sg.gov.moh.iais.egp.bsb.validation;

import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * CheckListCommonValidate
 *
 * @author junyu
 * @date 2022/4/28
 */
@Slf4j
public class CheckListCommonValidate {
    public static final String ERR0010 = "GENERAL_ERR0006";
    public static final String messageCommon = MessageUtil.getMessageDesc(ERR0010);
    public static final String general_err0041 = MessageUtil.getMessageDesc("GENERAL_ERR0041");

    public static boolean verifyQuestionDto(String answer, String remark, String ncs,String followupItem, String observeFollowup, String followupAction,String dueDate, boolean isError, String prefix, Map<String, String> errMap, boolean moreIns){
        if( !(StringUtil.isEmpty(answer) && StringUtil.isEmpty(remark) && StringUtil.isEmpty(ncs))){
            if(StringUtil.isEmpty(answer)){
                if( !StringUtil.isEmpty(remark) || !StringUtil.isEmpty(ncs)) {
                    errMap.put(prefix, messageCommon);
                    if (isError){
                        isError = false;
                    }
                }
            }else if("NO".equalsIgnoreCase(answer)){
                boolean needShowAllErrMsg = true;
                if(StringUtil.isEmpty(remark)){
                    errMap.put(prefix+"Remark",messageCommon);
                    if(moreIns){
                        needShowAllErrMsg = false;
                    }
                    if(isError){
                        isError = false;
                    }
                }

                if(StringUtil.isEmpty(ncs)){
                    if(needShowAllErrMsg){
                        errMap.put(prefix+"FindNcs",messageCommon);
                    }
                    if(isError){
                        isError = false;
                    }
                }
            }
        }else if(StringUtil.isEmpty(answer)){
            errMap.put(prefix,messageCommon);
            if (isError){
                isError = false;
            }
        }
        if(StringUtil.isEmpty(followupItem)){
            errMap.put(prefix,messageCommon);
            if (isError){
                isError = false;
            }
        }else {
            if(followupItem.equals("YES")){
                if(StringUtil.isEmpty(followupAction)){
                    errMap.put(prefix,messageCommon);
                    if (isError){
                        isError = false;
                    }
                }else if(followupAction.length()>500){
                    String field = general_err0041.replace("{field}", "Action Required");
                    field = field.replace("{maxlength}", "500");
                    errMap.put(prefix,field);
                    if (isError){
                        isError = false;
                    }
                }
                if( StringUtil.isEmpty(observeFollowup)){
                    errMap.put(prefix,messageCommon);
                    if (isError){
                        isError = false;
                    }
                }else if(observeFollowup.length()>500){
                    String field = general_err0041.replace("{field}", "Observations for Follow-up");
                    field = field.replace("{maxlength}", "500");
                    errMap.put(prefix,field);
                    if (isError){
                        isError = false;
                    }
                }
                if( StringUtil.isEmpty(dueDate)){
                    errMap.put(prefix,messageCommon);
                    if (isError){
                        isError = false;
                    }
                }else {
                    try {
                        Date date= Formatter.parseDate(dueDate);
                        if(date.before(new Date())){
                            errMap.put(prefix, MessageUtil.replaceMessage("GENERAL_ERR0026", "Due Date", "field"));
                            if (isError){
                                isError = false;
                            }
                        }
                    } catch (ParseException e) {
                        errMap.put(prefix,messageCommon);
                        if (isError){
                            isError = false;
                        }
                    }


                }

            }
        }

        return isError;
    }

    public static boolean  verifyQuestionDto(String answer, String remark, String ncs,String followupItem, String observeFollowup, String followupAction,String dueDate, boolean isError, String prefix, Map<String, String> errMap){
        return verifyQuestionDto(answer, remark, ncs,followupItem,observeFollowup,followupAction,dueDate, isError, prefix, errMap,false);
    }
}
