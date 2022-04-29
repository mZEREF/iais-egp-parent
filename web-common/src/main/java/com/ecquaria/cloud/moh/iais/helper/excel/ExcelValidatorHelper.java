package com.ecquaria.cloud.moh.iais.helper.excel;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * @Auther chenlei on 4/21/2022.
 */
public class ExcelValidatorHelper {

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String profile, int startRowIndex, Class<?> excelClass) {
        return validateExcelList(objList, null, null, profile, startRowIndex, getFieldCellMap(excelClass));
    }

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String profile, int startRowIndex,
            Map<String, ExcelPropertyDto> fieldCellMap) {
        return validateExcelList(objList, null, null, profile, startRowIndex, fieldCellMap);
    }

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String sheetName, Function<T, String> indicator,
            String profile, int startRowIndex, Class<?> excelClass) {
        return validateExcelList(objList, sheetName, indicator, profile, startRowIndex, getFieldCellMap(excelClass));
    }

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String sheetName, Function<T, String> indicator,
            String profile, int startRowIndex, Map<String, ExcelPropertyDto> fieldCellMap) {
        if (objList == null || objList.isEmpty()) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        List<FileErrorMsg> result = IaisCommonUtils.genNewArrayList();
        int row = startRowIndex + 1;
        if (fieldCellMap == null) {
            fieldCellMap = getFieldCellMap(objList.get(0).getClass());
        }
        for (T t : objList) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(t, profile);
            if (validationResult.isHasErrors()) {
                String val = null;
                if (indicator != null) {
                    val = indicator.apply(t);
                }
                result.addAll(getExcelErrorMsgs(sheetName, row, val, validationResult.retrieveAll(), fieldCellMap));
            }
            row++;
        }
        return result;
    }

    public static Map<String, ExcelPropertyDto> getFieldCellMap(Class<?> clazz) {
        Map<String, ExcelPropertyDto> map = IaisCommonUtils.genNewHashMap();
        if (clazz == null) {
            return map;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                map.put(field.getName(), new ExcelPropertyDto(excelProperty.cellIndex(), excelProperty.cellName(), field.getName()));

            }
        }
        return map;
    }

    public static List<FileErrorMsg> getExcelErrorMsgs(String sheetName, int row, String indicator,
            Map<String, String> errorMap, Map<String, ExcelPropertyDto> fieldCellMap) {
        List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList(errorMap.size());

        errorMap.forEach((k, v) -> errorMsgs.add(new FileErrorMsg(sheetName, row, indicator, getFieldCell(k, fieldCellMap), v)));
        return errorMsgs;
    }

    private static ExcelPropertyDto getFieldCell(String k, Map<String, ExcelPropertyDto> fieldCellMap) {
        if (fieldCellMap == null || fieldCellMap.isEmpty()) {
            return null;
        }
        return fieldCellMap.getOrDefault(k, new ExcelPropertyDto());
    }

    public static boolean isValidUuid(String uuid) {
        if (StringUtil.isEmpty(uuid)) {
            return false;
        }
        try {
            UUID.fromString(uuid).toString();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
