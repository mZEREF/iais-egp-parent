package com.ecquaria.cloud.moh.iais.validation;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
public class CheckListCommonValidate {
    public static final String ERR0010 = "GENERAL_ERR0006";


    public boolean verifyQuestionDto(String answer,String remark,String ncs,boolean isError,String prefix,Map<String, String> errMap,boolean moreIns){
        if( !(StringUtil.isEmpty(answer) && StringUtil.isEmpty(remark) && StringUtil.isEmpty(ncs))){
            if(StringUtil.isEmpty(answer)){
                if( !StringUtil.isEmpty(remark) || !StringUtil.isEmpty(ncs)) {
                    errMap.put(prefix, MessageUtil.replaceMessage(ERR0010, "Yes No N/A", "field"));
                    if (isError){
                        isError = false;
                    }
                }
            }else if("No".equalsIgnoreCase(answer)){
                boolean needShowAllErrMsg = true;
                if(StringUtil.isEmpty(remark)){
                    errMap.put(prefix+"Remark",MessageUtil.replaceMessage(ERR0010,"Remarks","field"));
                    if(moreIns){
                        needShowAllErrMsg = false;
                    }
                    if(isError){
                        isError = false;
                    }
                }

                if(StringUtil.isEmpty(ncs)){
                    if(needShowAllErrMsg){
                        errMap.put(prefix+"FindNcs",MessageUtil.replaceMessage(ERR0010,"Findings/NCs","field"));
                    }
                    if(isError){
                        isError = false;
                    }
                }
            }
        }else if(StringUtil.isEmpty(answer)){
            errMap.put(prefix, MessageUtil.replaceMessage(ERR0010, "Yes No N/A", "field"));
            if (isError){
                isError = false;
            }
        }
      return isError;
    }

    public boolean  verifyQuestionDto(String answer,String remark,String ncs,boolean isError,String prefix,Map<String, String> errMap){
        return verifyQuestionDto(answer, remark, ncs, isError, prefix, errMap,false);
    }


}
