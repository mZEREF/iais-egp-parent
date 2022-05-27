package com.ecquaria.cloud.moh.iais.validation.decaration;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.Declarations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 13:52
 */
public class Statements implements Declarations {

    private final static  SystemParamConfig systemParamConfig;
    static {
        systemParamConfig=SpringContextHelper.getContext().getBean(SystemParamConfig.class);
    }
    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if(appDeclarationMessageDto==null){
            return;
        }
        String manatoryMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        String preliminaryQuestionItem1 = appDeclarationMessageDto.getPreliminaryQuestionItem1();
        if(StringUtil.isEmpty(preliminaryQuestionItem1)){
            map.put("preliminaryQuestionItem1", manatoryMsg);
        }
        String preliminaryQuestiontem2 = appDeclarationMessageDto.getPreliminaryQuestiontem2();
        if(StringUtil.isEmpty(preliminaryQuestiontem2)){
            map.put("preliminaryQuestiontem2", manatoryMsg);
        }
        Date effectiveDt = appDeclarationMessageDto.getEffectiveDt();
        Date date = new Date();
        if(effectiveDt!=null){
            int configDateSize = systemParamConfig.getRfcPeriodEffdate();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH,configDateSize);
            if(effectiveDt.before(date)){
                map.put("effectiveDt", MessageUtil.getMessageDesc("RFC_ERR012"));
            }else if(effectiveDt.after(calendar.getTime())){
                String errorMsg = MessageUtil.getMessageDesc("RFC_ERR008");
                errorMsg = errorMsg.replace("{date}",new SimpleDateFormat(Formatter.DATE).format(calendar.getTime()));
                map.put("effectiveDt", errorMsg);
            }else if(date.compareTo(effectiveDt)==0){
                map.put("effectiveDt", MessageUtil.getMessageDesc("RFC_ERR012"));
            }
        }
    }
}
