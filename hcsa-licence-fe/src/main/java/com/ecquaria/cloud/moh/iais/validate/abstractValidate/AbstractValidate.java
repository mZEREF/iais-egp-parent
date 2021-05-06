package com.ecquaria.cloud.moh.iais.validate.abstractValidate;

import com.ecquaria.cloud.moh.iais.common.annotation.validate.FieldNotNull;
import com.ecquaria.cloud.moh.iais.common.annotation.validate.FieldValidate;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validate.Validate;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/26 13:56
 */
@Slf4j
public abstract class AbstractValidate implements Validate {
    @Override
    public Map<String, String> validate(Object o,Integer index) {
        Map<String,String> map=new HashMap<>();
        Class<?> aClass = o.getClass();
        FieldNotNull annotation = aClass.getAnnotation(FieldNotNull.class);
        if(annotation!=null){
            Field[] declaredFields = aClass.getDeclaredFields();
            if(declaredFields!=null){
                validateFidld(map,declaredFields,o,index);
            }

        }else {
            Field[] declaredFields = aClass.getDeclaredFields();
            if(declaredFields!=null){
                validateFidld(map,declaredFields,o,index);
            }
        }

        return map;
    }
    private void validateFidld(Map<String,String> map,@NotNull Field[] declaredFields,Object o,Integer index){
        for(Field v : declaredFields){
            FieldNotNull annotation = v.getAnnotation(FieldNotNull.class);
            v.setAccessible(true);
            Object f;
            try {
                 f = v.get(o);
            } catch (IllegalAccessException e) {
                log.error("-----error-----");
                throw  new RuntimeException("-----error-----", e);
            }
            if(annotation!=null){
                if(f==null){
                    String s = annotation.notNullMessage();
                    String messageDesc = MessageUtil.replaceMessage(s,v.getName(),"field");
                    map.put(v.getName()+index,messageDesc);
                }else {
                    if(f instanceof String){
                        if("".equals(f)){
                            String s = annotation.notNullMessage();
                            String messageDesc = MessageUtil.replaceMessage(s,v.getName(),"field");
                            map.put(v.getName()+index,messageDesc);
                        }
                    }
                }

            }
            FieldValidate fieldValidate = v.getAnnotation(FieldValidate.class);
            if(fieldValidate!=null){
                if(f!=null){
                    String s = fieldValidate.fieldFormat();
                    if(s!=null||!"".equals(s)){
                        if(f instanceof String){
                            boolean matches = ((String) f).matches(s);
                            String message = fieldValidate.fieldFormatMessage();
                            String messageDesc = MessageUtil.getMessageDesc(message);
                            if(!matches){
                                map.put(v.getName()+index,messageDesc);
                            }
                            boolean email = fieldValidate.isEmail();
                            if(email){
                                boolean email1 = ValidationUtils.isEmail((String) f);
                                if(!email1){
                                    map.put(v.getName()+index,  MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                                }
                            }
                            int maxLength = fieldValidate.maxLength();
                            if(-1!=maxLength){
                                int length = ((String) f).length();
                                if(length>maxLength){
                                    map.put(v.getName()+index,  MessageUtil.getMessageDesc(fieldValidate.maxLengthMessage()));
                                }
                            }
                            int minLength = fieldValidate.minLength();
                            if(-1!=minLength){
                                int length = ((String) f).length();
                                if(length<minLength){
                                    map.put(v.getName()+index,  MessageUtil.getMessageDesc(fieldValidate.minLengthMessage()));
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
