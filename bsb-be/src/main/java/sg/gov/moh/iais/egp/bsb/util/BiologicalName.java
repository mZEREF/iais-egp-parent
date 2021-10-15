package sg.gov.moh.iais.egp.bsb.util;

import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.BiosafetyEnquiryClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/1 13:58
 * DESCRIPTION: TODO
 **/
@Slf4j
public class BiologicalName {
    public static <T> List<T> setBioName(String pkName, List<T> list, Object object) {
        try {
            Iterator each = list.iterator();
            while (each.hasNext()) {
                T obj = (T) each.next();
                //加载类
                Class<?> exp = Class.forName(pkName);
                Class[] parameterTypes = new Class[1];
                //获取成员变量
                Field[] member = exp.getDeclaredFields();
                ResponseDto<Biological> bio = null;
                //遍历成员变量
                for (Field method : member) {
                    parameterTypes[0] = method.getType();
                    String memberName = method.getName();
                    log.info("memberName:  " + memberName + "-------");
                    if ("biologicalId".equals(memberName)) {
                        //替换
                        String goRraces = memberName.replace("()", "");
                        String mName = "get" + goRraces.substring(0, 1).toUpperCase() + goRraces.substring(1);
                        Method m = exp.getMethod(mName);
                        Method m2 = object.getClass().getMethod("getBiologicalById",String.class);
                         bio = (ResponseDto<Biological>) m2.invoke(object,(String) m.invoke(obj));
                    }
                    if ("bioName".equals(memberName)) {
                        String goRraces = memberName.replace("()", "");
                        String mName = "set" + goRraces.substring(0, 1).toUpperCase() + goRraces.substring(1);
                        Method m = exp.getMethod(mName, parameterTypes);
                        m.invoke(obj, bio.getEntity().getName());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    public  Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  void invokeSet(Object o, String fieldName, String bioId, BiosafetyEnquiryClient biosafetyEnquiryClient) {
        Method method = getSetMethod(o.getClass(), fieldName);
        try {
            method.invoke(o, new Object[] { biosafetyEnquiryClient.getBiologicalById(bioId).getEntity().getName() });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
