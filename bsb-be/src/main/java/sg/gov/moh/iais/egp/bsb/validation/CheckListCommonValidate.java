package sg.gov.moh.iais.egp.bsb.validation;

import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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
    private CheckListCommonValidate() {}

    public static final String ERR0010 = "GENERAL_ERR0006";
    public static final String MESSAGE_COMMON = MessageUtil.getMessageDesc(ERR0010);
    public static final String GENERAL_ERR0041 = MessageUtil.getMessageDesc("GENERAL_ERR0041");

    public static boolean verifyQuestionDto(String answer, String remark, String ncs,String followupItem, String observeFollowup, String followupAction,String dueDate, boolean isError, String prefix, Map<String, String> errMap, boolean moreIns){
        if( !(StringUtils.isEmpty(answer) && StringUtils.isEmpty(remark) && StringUtils.isEmpty(ncs))){
            if(StringUtils.isEmpty(answer)){
                if( !StringUtils.isEmpty(remark) || !StringUtils.isEmpty(ncs)) {
                    errMap.put(prefix, MESSAGE_COMMON);
                    if (isError){
                        isError = false;
                    }
                }
            }else if("NO".equalsIgnoreCase(answer)){
                boolean needShowAllErrMsg = true;
                if(StringUtils.isEmpty(remark)){
                    errMap.put(prefix+"Remark",MESSAGE_COMMON);
                    if(moreIns){
                        needShowAllErrMsg = false;
                    }
                    if(isError){
                        isError = false;
                    }
                }

                if(StringUtils.isEmpty(ncs)){
                    if(needShowAllErrMsg){
                        errMap.put(prefix+"FindNcs",MESSAGE_COMMON);
                    }
                    if(isError){
                        isError = false;
                    }
                }
            }
        }else if(StringUtils.isEmpty(answer)){
            errMap.put(prefix,MESSAGE_COMMON);
            if (isError){
                isError = false;
            }
        }
        if(StringUtils.isEmpty(followupItem)){
            errMap.put(prefix,MESSAGE_COMMON);
            if (isError){
                isError = false;
            }
        }else {
            if(followupItem.equals("YES")){
                if(StringUtils.isEmpty(followupAction)){
                    errMap.put(prefix,MESSAGE_COMMON);
                    if (isError){
                        isError = false;
                    }
                }else if(followupAction.length()>500){
                    String field = GENERAL_ERR0041.replace("{field}", "Action Required");
                    field = field.replace("{maxlength}", "500");
                    errMap.put(prefix,field);
                    if (isError){
                        isError = false;
                    }
                }
                if( StringUtils.isEmpty(observeFollowup)){
                    errMap.put(prefix,MESSAGE_COMMON);
                    if (isError){
                        isError = false;
                    }
                }else if(observeFollowup.length()>500){
                    String field = GENERAL_ERR0041.replace("{field}", "Observations for Follow-up");
                    field = field.replace("{maxlength}", "500");
                    errMap.put(prefix,field);
                    if (isError){
                        isError = false;
                    }
                }
                if( StringUtils.isEmpty(dueDate)){
                    errMap.put(prefix,MESSAGE_COMMON);
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
                        errMap.put(prefix,MESSAGE_COMMON);
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
