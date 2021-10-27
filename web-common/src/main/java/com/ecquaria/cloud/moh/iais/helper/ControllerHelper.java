package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

    public static <T> T get(HttpServletRequest request, Class<T> clazz, String shortName) {
        if (clazz == null) {
            return null;
        }

        try {
            return get(request, clazz.newInstance(), shortName);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            throw new IaisRuntimeException(e);
        }
    }

    public static <T> T get(HttpServletRequest request, T obj) {
        return get(request, obj, null);
    }

    public static <T> T get(HttpServletRequest request, T obj, String shortName) {
        return get(request, obj, shortName, null);
    }

    public static <T> T get(HttpServletRequest request, T obj, String shortName, String suffix) {
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


            if (!isFieldExist(request, shortName, suffix, field)) {
                continue;
            }

            ReflectionUtil.setPropertyObj(field, getValue(request, shortName, suffix, field), obj);
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

    private static boolean isFieldExist(HttpServletRequest request, String shortName, String id, Field field) {
        if (field == null) {
            return false;
        }

        String name = getParamName(shortName, field.getName(), id);
        String _checkbox_radio = ParamUtil.getString(request, "_checkbox_radio");
        if (StringUtil.isIn(name, _checkbox_radio)) {
            return true;
        }

        return request.getParameterMap() == null ? false : request.getParameterMap().keySet().contains(name);
    }

    private static Object getValue(HttpServletRequest request, String shortName, String id, Field field) {
        if (field == null) {
            return null;
        }

        log.info("ControllerHelper - Getting value for field: " + StringUtil.changeForLog(
                field.getName()) + " (" + StringUtil.changeForLog(String.valueOf(field.getType())) + ")");
        Class<?> type = field.getType();
        String name = getParamName(shortName, field.getName(), id);
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
            value = ParamUtil.getInt(request, name);
        } else if (Long.class.isAssignableFrom(type)) {
            value = ParamUtil.getLong(request, name);
        } else if (Double.class.isAssignableFrom(type)) {
            value = ParamUtil.getDouble(request, name);
        } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            value = AppConsts.YES.equals(ParamUtil.getString(request, name));
        } else if (Date.class.isAssignableFrom(type)) {
            value = ParamUtil.getDate(request, name);
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

    public static String getParamName(String shortName, String name, String suffix) {
        StringBuffer param = new StringBuffer();
        if (!StringUtil.isEmpty(shortName)) {
            param.append(shortName).append(StringUtil.capitalize(name));
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
