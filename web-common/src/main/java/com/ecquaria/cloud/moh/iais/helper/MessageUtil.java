package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.MapFormat;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Map;

/**
 * MessageUtil
 *
 * @author Jinhua
 * @date 2019/12/18 10:51
 */
@Slf4j
public class MessageUtil {
    private static final String CACHE_NAME_MSG                    = "iaisMsgCache";

    private MessageUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void loadMessages(Map<String, String> map) {
        log.debug("##########################Load Iais Messages Start##############################");
        if (CollectionUtils.isEmpty(map)) {
            return;
        }

        map.forEach((k, v) -> {
            SpringContextHelper.getContext().getBean(RedisCacheHelper.class).set(CACHE_NAME_MSG,
                    k, v, RedisCacheHelper.NOT_EXPIRE);
        });

        log.debug("##########################Load Iais Messages End##############################");
    }

    public static String getMessageDesc(String key) {
        String msg = SpringContextHelper.getContext().getBean(RedisCacheHelper.class).get(CACHE_NAME_MSG, key);
        if (StringUtil.isEmpty(msg)) {
            msg = key;
        }

        return msg;
    }

    /**
     * Designated method. It gets the string, initializes HashFormat object
     * and returns converted string. It scans  <code>pattern</code>
     * for {} brackets, then parses enclosed string and replaces it
     * with argument's  <code>get()</code> value.
     * @param key The msg code to be parsed.
     * @param arguments Map with key-value pairs to replace.
     * @return Formatted string
     */
    public static String getMessageDesc(String key, Map arguments) {
        String msg = getMessageDesc(key);
        MapFormat temp = new MapFormat(arguments);
        return temp.format(msg);
    }

    public static String formatMessage(String msg, String paramVal){
        if (StringUtils.isEmpty(msg)){
            return null;
        }

        return  msg.replace("%d", paramVal);
    }

    public static String dateIntoMessage(String codeKey){
        return dateIntoMessage(codeKey, AppConsts.DEFAULT_DATE_TIME_FORMAT);
    }

    private static String dateIntoMessage(String codeKey, String pattern){
        if (StringUtils.isEmpty(codeKey) || StringUtils.isEmpty(pattern)){
            return null;
        }

        String msg = MessageUtil.getMessageDesc(codeKey);
        String dateStr = "";
        String timeStr = "";
        try {
            String dateTimeStr = Formatter.formatDateTime(new Date());
            String[] arr = dateTimeStr.split(" ");
            dateStr = arr[0];
            timeStr = arr[1];
        }catch (NullPointerException | IndexOutOfBoundsException e){
            log.error(e.getMessage(), e);
        }

        return msg.replace("{Date}", dateStr).replace("{Time}", timeStr);

    }

    //only replace first or only one curly brace
    public static String replaceMessage(String codeKey,String replaceString,String replacePart){
        String msg = MessageUtil.getMessageDesc(codeKey);
        if(StringUtil.isEmpty(msg)) {
            return codeKey;
        } else if(msg.contains("{") && msg.contains("}")){
            return msg.replace(replacePart,replaceString).replace("{","").replace("}","");
        } else {
            return msg.replace(replacePart, replaceString);
        }
    }
}
