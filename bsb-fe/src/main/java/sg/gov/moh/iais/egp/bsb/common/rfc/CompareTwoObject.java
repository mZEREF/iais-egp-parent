package sg.gov.moh.iais.egp.bsb.common.rfc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : LiRan
 * @date : 2021/11/8
 */
@Slf4j
public class CompareTwoObject {

    private CompareTwoObject() {}

    /**
     * By default, the map(size and key) is already compared, since the activities are compared in SelectDto, there is no more comparison here.
     * Warning: This method is only suitable for special cases, such as activity and BAT linkage.
     */
    public static <T> void diffMap(Map<String, T> beforeBatMap, Map<String, T> afterBatMap, List<DiffContent> diffs, Class<?>... customerClass){
        List<String> beforeKeyList = new ArrayList<>(beforeBatMap.keySet());
        List<String> afterKeyList = new ArrayList<>(afterBatMap.keySet());
        //If size and key are equal, then compare; if size and key are not equal, as the diffContent information is already presented in the compare SelectDto, we will do nothing
        if (beforeBatMap.size() == afterBatMap.size() && beforeKeyList.equals(afterKeyList)){
            for(Map.Entry<String,T> entry : beforeBatMap.entrySet()){
                diff(beforeBatMap.get(entry.getKey()),afterBatMap.get(entry.getKey()), diffs, customerClass);
            }
        }
    }

    /**
     * This method is used recursively to get modify field diffs.
     * @param before oldData
     * @param after newData
     * @param diffs the different data List
     * @param customerClass the list of objects in the T object
     */
    public static <T> void diff(T before, T after, List<DiffContent> diffs, Class<?>... customerClass) {
        //can not be null for all
        if (before == null && after == null) {
            return;
        }
        //get all field for none null
        T allFieldsObj = before == null ? after : before;
        List<Field> finalField = getClassAllField(allFieldsObj);
        //iterator field
        for (Field declaredField : finalField) {
            getDiffInfo(before, after, diffs, declaredField, customerClass);
        }
    }

    /**
     * Get all declared fields list which has @RfcAttributeDesc.
     * Declared fields includes public, protected, default (package) access,
     * and private fields, but excludes inherited fields.
     */
    private static List<Field> getClassAllField(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        return Arrays.stream(fields).filter(field -> field.isAnnotationPresent(RfcAttributeDesc.class)).collect(Collectors.toList());
    }

    /**
     * Get object by invoke, then get diff by object.
     */
    private static <T> void getDiffInfo(T before, T after, List<DiffContent> diffs, Field declaredField, Class<?>... customerClass) {
        //attribute value
        Object beforeInvoke = null;
        Object afterInvoke = null;
        try {
            if (before != null) {
                //get before object by invoke
                beforeInvoke = new PropertyDescriptor(declaredField.getName(), before.getClass()).getReadMethod().invoke(before);
            }
            if (after != null) {
                //get after object by invoke
                afterInvoke = new PropertyDescriptor(declaredField.getName(), after.getClass()).getReadMethod().invoke(after);
            }
            //adding diffs
            addingDiff(diffs, declaredField, beforeInvoke, afterInvoke, customerClass);
            //beforeInvoke type is ArrayList, and two list not null, special processing for list
            if (afterInvoke != null && (beforeInvoke instanceof ArrayList)){
                specialListHandle(before,diffs,declaredField,beforeInvoke,afterInvoke,customerClass);
            }
        } catch (IntrospectionException e) {
            log.info("IntrospectionException");
        } catch (InvocationTargetException e) {
            log.info("InvocationTargetException");
        } catch (IllegalAccessException e) {
            log.info("IllegalAccessException");
        }
    }

    /**
     * This method is special processing for list
     * Reason: The list object must first compare the size,
     * and the getClassAllField method cannot get a no @RfcAttributeDesc field,
     * there is only process type "java.util.List<java.lang.String>"
     */
    private static <T> void specialListHandle(T before, List<DiffContent> diffs, Field declaredField, Object beforeInvoke, Object afterInvoke, Class<?>... customerClass){
        //get field annotation
        RfcAttributeDesc annotation = declaredField.getAnnotation(RfcAttributeDesc.class);
        List<?> beforeList = (List<?>) beforeInvoke;
        List<?> afterList = (List<?>) afterInvoke;
        //return when two list size not equals
        if (afterList.size() != beforeList.size()){
            DiffContent diff = new DiffContent();
            diff.setModifyField(annotation.aliasName().equals("") ? declaredField.getName() : annotation.aliasName());
            diff.setOldValue(String.valueOf(beforeList.size()));
            diff.setNewValue(String.valueOf(afterList.size()));
            diffs.add(diff);
            return;
        }
        //two list size is same, condition genericity yes or no "String"
        String genericType = declaredField.getGenericType().getTypeName();
        if (genericType.equals("java.util.List<java.lang.String>") && !beforeList.equals(afterList)){
            DiffContent diff = new DiffContent();
            diff.setModifyField(annotation.aliasName().equals("") ? declaredField.getName() : annotation.aliasName());
            diff.setOldValue(beforeList.toString());
            diff.setNewValue(afterList.toString());
            diffs.add(diff);
            return;
        }
        //genericity is others, continue to compare each object in the list recursively
        int index = 0;
        for (Object beforeObj : beforeList) {
            //avoid recursive reference
            if (beforeObj.getClass() == before.getClass()){
                log.error("Can not recursive depend");
            }
            //recursive call
            diff(beforeObj, afterList.get(index), diffs, customerClass);
            index++;
        }
    }

    /**
     * Add diff by different Object type
     */
    private static <T> void addingDiff(List<DiffContent> diffs, Field declaredField, T beforeInvoke, T afterInvoke, Class<?>... customerClass) {
        //adding string diff
        if (beforeInvoke instanceof String){
            addingStringDiff(diffs, declaredField, (String)beforeInvoke, (String) afterInvoke);
        }
        //adding integer diff
        if (beforeInvoke instanceof Integer){
            addingIntegerDiff(diffs, declaredField, (Integer) beforeInvoke, (Integer) afterInvoke);
        }
        //adding long diff
        if (beforeInvoke instanceof Long){
            addingLongDiff(diffs, declaredField, (Long)beforeInvoke, (Long)afterInvoke);
        }
        //adding customer class diff
        if (customerClass != null && customerClass.length > 0) {
            List<String> customerClassNames = Arrays.stream(customerClass).map(Class::getName).collect(Collectors.toList());
            if (customerClassNames.stream().anyMatch(declaredField.getType().getName()::equals)) {
                //recursive call
                diff(beforeInvoke, afterInvoke, diffs, customerClass);
            }
        }
    }

    private static void addingStringDiff(List<DiffContent> diffs, Field declaredField, String beforeInvoke, String afterInvoke){
        //get field annotation
        RfcAttributeDesc annotation = declaredField.getAnnotation(RfcAttributeDesc.class);
        String beforeField = StringUtils.hasLength(beforeInvoke) ? "" : beforeInvoke;
        String afterField = StringUtils.hasLength(afterInvoke) ? "" : afterInvoke;
        if (!beforeField.equals(afterField)) {
            DiffContent diff = new DiffContent();
            diff.setModifyField(annotation.aliasName().equals("") ? declaredField.getName() : annotation.aliasName());
            diff.setOldValue(beforeField);
            diff.setNewValue(afterField);
            diffs.add(diff);
        }
    }

    private static void addingIntegerDiff(List<DiffContent> diffs, Field declaredField, Integer beforeInvoke, Integer afterInvoke){
        //get field annotation
        RfcAttributeDesc annotation = declaredField.getAnnotation(RfcAttributeDesc.class);
        if (!beforeInvoke.equals(afterInvoke)) {
            DiffContent diff = new DiffContent();
            diff.setModifyField(annotation.aliasName().equals("") ? declaredField.getName() : annotation.aliasName());
            diff.setOldValue(String.valueOf(beforeInvoke));
            diff.setNewValue(String.valueOf(afterInvoke));
            diffs.add(diff);
        }
    }

    private static void addingLongDiff(List<DiffContent> diffs, Field declaredField, Long beforeInvoke, Long afterInvoke){
        //get field annotation
        RfcAttributeDesc annotation = declaredField.getAnnotation(RfcAttributeDesc.class);
        if (!Objects.equals(beforeInvoke, afterInvoke)) {
            DiffContent diff = new DiffContent();
            diff.setModifyField(annotation.aliasName().equals("") ? declaredField.getName() : annotation.aliasName());
            diff.setOldValue(String.valueOf(beforeInvoke));
            diff.setNewValue(String.valueOf(afterInvoke));
            diffs.add(diff);
        }
    }
}
