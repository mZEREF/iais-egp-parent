package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description SimpleCsvReader
 * @Auther chenlei on 11/29/2021.
 */
@Slf4j
public class SimpleCsvReader {

    public static <T> List<T> readToBean(final File file, final Class<T> clz, boolean defaultValueNull) {
        if (file == null || !file.exists()) {
            throw new IaisRuntimeException("Please check excel source is exists");
        }
        if (clz == null) {
            throw new IaisRuntimeException("excel bean class error");
        }
        List<T> result = new ArrayList<>();
        try {
            Iterable<CSVRecord> csvRecord = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(new FileReader(file));
            Map<Integer, Method> methods = setMethods(clz);
            int min = methods.keySet().stream().min(Integer::compareTo).orElse(0);
            for (CSVRecord record : csvRecord) {
                T obj = readToBean(record, methods, clz, min, defaultValueNull);
                if (obj != null) {
                    result.add(obj);
                }
            }
        } catch (IOException e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return result;
    }

    private static <T> T readToBean(CSVRecord record, Map<Integer, Method> methods, Class<T> clz, int i, boolean defaultValueNull) {
        T obj;
        try {
            obj = clz.newInstance();
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            return null;
        }
        for (String val : record) {
            Method method = methods.get(i++);
            if (method == null) {
                continue;
            }
            try {
                method.invoke(obj, defaultValueNull ? StringUtil.getStringEmptyToNull(val) : val);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        return obj;
    }

    private static <T> Map<Integer, Method> setMethods(Class<T> clz) {
        Map<Integer, Method> result = new HashMap<>();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class) && field.getType() == String.class) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                try {
                    result.put(excelProperty.cellIndex(),
                            clz.getMethod("set" + StringUtil.capitalize(field.getName()), field.getType()));
                } catch (NoSuchMethodException e) {
                    log.error(StringUtil.changeForLog(e.getMessage()), e);
                }
            }
        }
        return result;
    }

    public static List<List<String>> readToList(final File file) {
        if (file == null || !file.exists()) {
            throw new IaisRuntimeException("Please check excel source is exists");
        }
        List<List<String>> result = new ArrayList<>();
        try {
            Iterable<CSVRecord> csvRecord = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(new FileReader(file));
            for (CSVRecord record : csvRecord) {
                result.add(readLine(record));
            }
        } catch (IOException e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return result;
    }

    private static List<String> readLine(CSVRecord record) {
        List<String> result = new ArrayList<>();
        for (String s : record) {
            result.add(s);
        }
        return result;
    }

}
