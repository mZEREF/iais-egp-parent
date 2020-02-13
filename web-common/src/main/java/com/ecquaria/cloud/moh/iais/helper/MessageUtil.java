package com.ecquaria.cloud.moh.iais.helper;

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
            RedisCacheHelper.getInstance().set(CACHE_NAME_MSG, k, v, RedisCacheHelper.NOT_EXPIRE);
        });

        log.debug("##########################Load Iais Messages End##############################");
    }

    public static String getMessageDesc(String key) {
        String msg = RedisCacheHelper.getInstance().get(CACHE_NAME_MSG, key);
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
     * @param pattern String to be parsed.
     * @param arguments Map with key-value pairs to replace.
     * @return Formatted string
     */
    public static String getMessageDesc(String pattern, Map arguments) {
        MapFormat temp = new MapFormat(arguments);
        return temp.format(pattern);
    }

    public static String formatMessage(String msg, String paramVal){
        if (StringUtils.isEmpty(msg)){
            return null;
        }

        return  msg.replace("%d", paramVal);
    }


    public static String dateIntoMessage(String codeKey, String pattern){
        if (StringUtils.isEmpty(codeKey) || StringUtils.isEmpty(pattern)){
            return null;
        }

        String msg = MessageUtil.getMessageDesc(codeKey);
        return msg.replace("<Date>", IaisEGPHelper.parseToString(new Date(),
                pattern)).replace("<Time>", IaisEGPHelper.parseToString(new Date(),
                "hh:mm"));
    }
}
