package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Date;

/**
 * ControllerHelper
 *
 * @author chenlei
 */
@Slf4j
public final class ControllerHelper {

    public static <T> T get(HttpServletRequest request, Class<T> clazz) {
        return get(request, clazz, null);
    }

    public static <T> T get(HttpServletRequest request, Class<T> clazz, String suffix) {
        if (clazz == null) {
            return null;
        }

        try {
            return get(request, clazz.newInstance(), suffix);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            throw new IaisRuntimeException(e);
        }
    }
    public static <T> T get(HttpServletRequest request, Class<T> clazz, String shortName, String suffix) {
        return get(request,clazz,shortName,suffix,false);
    }
    public static <T> T get(HttpServletRequest request, Class<T> clazz, String shortName, String suffix, boolean isCapitalize) {
        if (clazz == null) {
            return null;
        }

        try {
            return get(request, clazz.newInstance(), shortName, suffix, isCapitalize);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            throw new IaisRuntimeException(e);
        }
    }

    public static <T> T get(HttpServletRequest request, T obj) {
        return get(request, obj, null);
    }

    public static <T> T get(HttpServletRequest request, T obj, String suffix) {
        return get(request, obj, null, suffix, false);
    }

    public static <T> T get(HttpServletRequest request, T obj, String shortName, String suffix, boolean isCapitalize) {
        if (obj == null) {
            return obj;
        }

        Field[] fields = getFields(obj.getClass());
        if (IaisCommonUtils.isEmpty(fields)) {
            return obj;
        }

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }


            if (!isFieldExist(request, shortName, suffix, field, isCapitalize)) {
                continue;
            }

            ReflectionUtil.setPropertyObj(field, getValue(request, shortName, suffix, field, isCapitalize), obj);
        }

        return obj;
    }

    private static Field[] getFields(Class<?> claszz) {
        return getFields(claszz, null);
    }

    private static Field[] getFields(Class<?> claszz, Field[] fields) {
        if (claszz == null) {
            return null;
        }

        if (fields == null) {
            fields = new Field[]{};
        }

        fields = (Field[]) ArrayUtils.addAll(fields, claszz.getDeclaredFields());
        Class<?> claszzSuper = claszz.getSuperclass();
        if (claszzSuper != null) {
            fields = getFields(claszzSuper, fields);
        }
        return fields;
    }

    private static boolean isFieldExist(HttpServletRequest request, String shortName, String id, Field field, boolean isCapitalize) {
        if (field == null) {
            return false;
        }

        String name = getParamName(shortName, field.getName(), id, isCapitalize);
        String _checkbox_radio = ParamUtil.getString(request, "_checkbox_radio");
        if (StringUtil.isIn(name, _checkbox_radio)) {
            return true;
        }

        return isFieldExist((MultipartHttpServletRequest) request.getAttribute("sop6.multipart.req"), name) || isFieldExist(request,
                name);
    }

    private static boolean isFieldExist(HttpServletRequest request, String name) {
        return request != null && request.getParameterMap() != null && request.getParameterMap().containsKey(name);
    }

    private static Object getValue(HttpServletRequest request, String shortName, String id, Field field, boolean isCapitalize) {
        if (field == null) {
            return null;
        }
        log.info("ControllerHelper - Getting value for field:->name={},type={}",field.getName(),field.getType());
        Class<?> type = field.getType();
        String name = getParamName(shortName, field.getName(), id, isCapitalize);
        Object value = null;
        if (String.class.isAssignableFrom(type)) {
            value = ParamUtil.getString(request, name);
        } else if (int.class.isAssignableFrom(type)) {
            value = ParamUtil.getInt(request, name, 0);
        } else if (long.class.isAssignableFrom(type)) {
            value = ParamUtil.getLong(request, name, 0);
        } else if (double.class.isAssignableFrom(type)) {
            value = ParamUtil.getDouble(request, name, 0);
        } else if (Integer.class.isAssignableFrom(type)) {
            value = ParamUtil.getString(request, name);
            if (value == null) {
                return null;
            }
            if (StringUtil.isDigit((String) value)) {
                return Integer.valueOf((String) value);
            }
        } else if (Long.class.isAssignableFrom(type)) {
            value = ParamUtil.getString(request, name);
            if (StringUtil.isDigit(value)) {
                return Long.valueOf((String) value);
            }
        } else if (Double.class.isAssignableFrom(type)) {
            value = ParamUtil.getString(request, name);
            if (StringUtil.isNumber((String) value)) {
                return Double.valueOf((String) value);
            }
            value = ParamUtil.getDouble(request, name);
        } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            value = AppConsts.YES.equals(ParamUtil.getString(request, name));
        } else if (Date.class.isAssignableFrom(type)) {
            value = ParamUtil.getString(request, name);
            if (CommonValidator.isDate((String) value)) {
                try {
                    value = Formatter.parseDate((String) value);
                } catch (ParseException e) {
                    value = null;
                }
            }
        } else if (String[].class.isAssignableFrom(type)) {
            value = ParamUtil.getStrings(request, name);
        } else if (Integer[].class.isAssignableFrom(type) || int[].class.isAssignableFrom(type)) {
            value = ParamUtil.getInts(request, name, -1);
        } else if (Long[].class.isAssignableFrom(type) || long[].class.isAssignableFrom(type)) {
            value = ParamUtil.getLongs(request, name, -1);
        } else if (Double[].class.isAssignableFrom(type) || double[].class.isAssignableFrom(type)) {
            value = ParamUtil.getDoubles(request, name, -1);
        } else {
            //TODO
        }

        return value;
    }

    public static String getParamName(String shortName, String name, String suffix, boolean isCapitalize) {
        StringBuilder param = new StringBuilder();
        if (!StringUtil.isEmpty(shortName)) {
            param.append(shortName);
        }
        if (isCapitalize) {
            param.append(StringUtil.capitalize(name));
        } else {
            param.append(name);
        }
        if (!StringUtil.isEmpty(suffix)) {
            param.append(suffix);
        }

        return param.toString();
    }

    private ControllerHelper() {}

}
