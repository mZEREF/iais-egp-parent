package com.ecquaria.cloud.moh.iais.validate.abstractValidate;

import com.ecquaria.cloud.moh.iais.common.annotation.validate.FieldNotNull;
import com.ecquaria.cloud.moh.iais.common.annotation.validate.FieldValidate;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.validate.Validate;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

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
        Field[] declaredFields = aClass.getDeclaredFields();
        if(declaredFields!=null){
            validateFidld(map,declaredFields,o,index);
        }

        return map;
    }
    private void validateFidld(Map<String,String> map,@NotNull Field[] declaredFields,Object o,Integer index){
        for(Field v : declaredFields){
            FieldNotNull annotation = v.getAnnotation(FieldNotNull.class);
            String stat = "get";
            if (v.getType().equals(boolean.class)) {
                stat = "is";
            }
            Method getMed = null;
            try {
                getMed = o.getClass().getMethod(stat + StringUtil.capitalize(v.getName()));
            } catch (NoSuchMethodException e) {
                return;
            }
            Object f = null;
            try {
                f = getMed.invoke(o, null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error(e.getMessage(), e);
                throw new IaisRuntimeException(e);
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
                    if(s!=null&&!"".equals(s)){
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
    protected void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos, Map<String, String> errorMap, String floorErrName, String unitErrName, List<String> floorUnitList, String floorUnitErrName, List<String> floorUnitNo, AppGrpPremisesDto appGrpPremisesDto){

        if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
            int opLength = 0;
            for(AppPremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                boolean flag = true;
                String floorNo = operationalUnitDto.getFloorNo();
                String unitNo = operationalUnitDto.getUnitNo();
                boolean floorNoFlag = StringUtil.isEmpty(floorNo);
                boolean unitNoFlag = StringUtil.isEmpty(unitNo);
                if(!(floorNoFlag && unitNoFlag)){
                    if(floorNoFlag){
                        flag = false;
                        errorMap.put(floorErrName + opLength, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                    }else if(unitNoFlag) {
                        flag = false;
                        errorMap.put(unitErrName + opLength, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                    }
                }

                if(!floorNoFlag && floorNo.length() > 3){
                    String general_err0041= NewApplicationHelper.repLength("Floor No.","3");
                    errorMap.put(floorErrName + opLength, general_err0041);
                }

                if(!unitNoFlag && unitNo.length() > 5){
                    String general_err0041=NewApplicationHelper.repLength("Unit No.","5");
                    errorMap.put(unitErrName + opLength, general_err0041);
                }

                String floorNoErr = errorMap.get(floorErrName + opLength);
                if (StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)) {
                    Pattern pattern = compile("[0-9]*");
                    boolean noFlag = pattern.matcher(floorNo).matches();
                    if (noFlag) {
                        int floorNum = Integer.parseInt(floorNo);
                        if (10 > floorNum) {
                            floorNo = "0" + floorNum;
                            operationalUnitDto.setFloorNo(floorNo);
                        }
                    }
                }
                if(flag){
                    if(!StringUtil.isEmpty(operationalUnitDto.getFloorNo()) && !StringUtil.isEmpty(operationalUnitDto.getUnitNo())){
                        String floorUnitStr = operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo();
                        if(floorUnitList.contains(floorUnitStr)){
                            errorMap.put(floorUnitErrName + opLength, "NEW_ERR0017");
                        }else{
                            floorUnitList.add(floorUnitStr);
                        }
                        String premisesType = appGrpPremisesDto.getPremisesType();
                        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
                            String addrType = appGrpPremisesDto.getAddrType();
                            String blkNo = appGrpPremisesDto.getBlkNo();
                            if(!StringUtil.isEmpty(blkNo)){
                                floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                            }
                        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)){
                            String offSiteAddressType = appGrpPremisesDto.getOffSiteAddressType();
                            if(!StringUtil.isEmpty(offSiteAddressType)){
                                String blkNo = appGrpPremisesDto.getOffSiteBlockNo();
                                if(!StringUtil.isEmpty(blkNo)){
                                    floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                                }
                            }

                        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
                            String conveyanceAddressType = appGrpPremisesDto.getConveyanceAddressType();
                            if(!StringUtil.isEmpty(conveyanceAddressType)){
                                String blkNo = appGrpPremisesDto.getConveyanceBlockNo();
                                if(!StringUtil.isEmpty(blkNo)){
                                    floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                                }
                            }

                        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType)){
                            String easMtsAddressType = appGrpPremisesDto.getEasMtsAddressType();
                            if(!StringUtil.isEmpty(easMtsAddressType)){
                                String blkNo = appGrpPremisesDto.getEasMtsBlockNo();
                                if(!StringUtil.isEmpty(blkNo)){
                                    floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                                }
                            }
                        }

                    }
                }
                opLength++;
            }
        }

    }
}
