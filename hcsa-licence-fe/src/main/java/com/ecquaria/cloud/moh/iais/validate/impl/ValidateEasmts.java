package com.ecquaria.cloud.moh.iais.validate.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import com.ecquaria.cloud.moh.iais.validate.abstractValidate.AbstractValidate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Wenkang
 * @date 2021/4/23 13:18
 */
@Component
public class ValidateEasmts extends AbstractValidate implements ValidateFlow {
    @Override
    public void doValidatePremises(Map<String, String> map, AppGrpPremisesDto appGrpPremisesDto,Integer index, String masterCodeDto,List<String> floorUnitList, List<String> floorUnitNo) {
        String easMtsHciName = appGrpPremisesDto.getEasMtsHciName();
        if(StringUtil.isEmpty(easMtsHciName)){
            map.put("easMtsHciName"+index, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of HCI", "field"));
        }else {
            if(easMtsHciName.length()>100){
                String general_err0041=NewApplicationHelper.repLength("HCI Name","100");
                map.put("easMtsHciName" + index, general_err0041);
            }else {
                int hciNameChanged = appGrpPremisesDto.getHciNameChanged();
                if(2==hciNameChanged){

                }else {
                    if(masterCodeDto!=null){
                        String[] s = masterCodeDto.split(" ");
                        StringBuilder sb=new StringBuilder();
                        Map<Integer,String> treeMap=new LinkedHashMap<>();
                        for (int i = 0; i < s.length; i++) {
                            if (easMtsHciName.toUpperCase().contains(s[index].toUpperCase())) {
                                treeMap.put(easMtsHciName.toUpperCase().indexOf(s[index].toUpperCase()),s[index]);
                            }
                        }
                        if(!treeMap.isEmpty()){
                            AtomicInteger length=new AtomicInteger();
                            treeMap.forEach((k,v)->{
                                length.getAndIncrement();
                                sb.append(v);
                                if(length.get()!=treeMap.size()){
                                    sb.append(',').append(' ');
                                }
                            });
                            map.put("easMtsHciName" + index, MessageUtil.replaceMessage("GENERAL_ERR0016", sb.toString(), "keywords"));
                        }
                    }
                }

            }
        }
        String easMtsPostalCode = appGrpPremisesDto.getEasMtsPostalCode();
        if(StringUtil.isEmpty(easMtsPostalCode)){
            map.put("easMtsPostalCode"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
        }else {
            if(easMtsPostalCode.length() > 6){
                String general_err0041= NewApplicationHelper.repLength("Postal Code","6");
                map.put("easMtsPostalCode" + index, general_err0041);
            }else if (easMtsPostalCode.length() < 6) {
                map.put("easMtsPostalCode" + index, "NEW_ERR0004");
            } else if (!easMtsPostalCode.matches("^[0-9]{6}$")) {
                map.put("easMtsPostalCode" + index, "NEW_ERR0004");
            }
        }
        String easMtsAddressType = appGrpPremisesDto.getEasMtsAddressType();
        boolean flag=true;
        if(StringUtil.isEmpty(easMtsAddressType)){
            map.put("easMtsAddressType"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            flag=false;
        }else {
            if(ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(easMtsAddressType)){
                String easMtsFloorNo = appGrpPremisesDto.getEasMtsFloorNo();
                String easMtsBlockNo = appGrpPremisesDto.getEasMtsBlockNo();
                String easMtsUnitNo = appGrpPremisesDto.getEasMtsUnitNo();
                List<String> list=new ArrayList<>(3);
                list.add("easMtsFloorNo");
                list.add("easMtsBlockNo");
                list.add("easMtsUnitNo");
                doValidateAdressType(easMtsFloorNo,easMtsBlockNo,easMtsUnitNo,index,map,list);
                if(!StringUtil.isEmpty(easMtsFloorNo)&&!StringUtil.isEmpty(easMtsBlockNo)&&!StringUtil.isEmpty(easMtsUnitNo)){
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append(easMtsFloorNo).append(easMtsBlockNo).append(easMtsUnitNo);
                    floorUnitNo.add(stringBuilder.toString());
                }
            }
        }
        String easMtsStreetName = appGrpPremisesDto.getEasMtsStreetName();
        String errStreet=MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field");
        if(StringUtil.isEmpty(easMtsStreetName)){
            map.put("easMtsStreetName"+index,errStreet);
        }else {

        }
        String easMtsUseOnly = appGrpPremisesDto.getEasMtsUseOnly();
        if(StringUtil.isEmpty(easMtsUseOnly)){
            map.put("easMtsUseOnly"+index,errStreet);
        }
        String easMtsPubEmail = appGrpPremisesDto.getEasMtsPubEmail();
        if(StringUtil.isEmpty(easMtsPubEmail)){
            map.put("easMtsPubEmail"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Public email", "field"));
        }else {
            boolean email = ValidationUtils.isEmail(easMtsPubEmail);
            if(!email){
                map.put("easMtsPubEmail"+index,MessageUtil.getMessageDesc("GENERAL_ERR0014"));
            }
        }
        String easMtsPubHotline = appGrpPremisesDto.getEasMtsPubHotline();
        if(StringUtil.isEmpty(easMtsPubHotline)){
            map.put("easMtsPubHotline"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Hotline", "field"));
        }else {
            if(!easMtsPubHotline.matches("^[0-9]{8}$")){
                map.put("easMtsPubHotline"+index,MessageUtil.getMessageDesc("GENERAL_ERR0007"));
            }
        }
        String easMtsCoLocation = appGrpPremisesDto.getEasMtsCoLocation();
        if(StringUtil.isEmpty(easMtsCoLocation)){
            map.put("easMtsCoLocation"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Hotline", "field"));
        }else {

        }
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
        if(flag){
            floorUnitList.add(appGrpPremisesDto.getEasMtsFloorNo()+appGrpPremisesDto.getEasMtsUnitNo());
        }
        if(appPremisesOperationalUnitDtos!=null&&!appPremisesOperationalUnitDtos.isEmpty()){
            //
            checkOperaionUnit(appPremisesOperationalUnitDtos,map,"opOffFloorNo"+index,"opOffUnitNo"+index,floorUnitList,"offFloorUnit"+index,floorUnitNo,appGrpPremisesDto);
        }
    }

    @Override
    public void doValidateAdressType(String floorNo, String blkNo, String unitNo, Integer index, Map<String, String> map, List<String> errorName) {
        if(errorName==null||errorName.size()!=3){
            throw new RuntimeException("errorName list is null or size not is 3");
        }
        if(StringUtil.isEmpty(floorNo)){
            map.put(errorName.get(0)+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
        }else if(floorNo.length()>3){
            String general_err0041=NewApplicationHelper.repLength("Floor No.","3");
            map.put(errorName.get(0)+index,general_err0041);
        }

        if(StringUtil.isEmpty(blkNo)){
            map.put(errorName.get(1)+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
        }else if(blkNo.length()>10){
            String general_err0041=NewApplicationHelper.repLength("Block / House No.","10");
            map.put(errorName.get(1)+index,general_err0041);
        }

        if(StringUtil.isEmpty(unitNo)){
            map.put(errorName.get(2)+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
        }else if(unitNo.length()>5){
            String general_err0041=NewApplicationHelper.repLength("Unit No.","5");
            map.put(errorName.get(2)+index,general_err0041);
        }
    }

    @Override
    public Map<String, String> validate(Object o,Integer index) {
        return super.validate(o,index);
    }

    @Override
    protected void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos, Map<String, String> errorMap, String floorErrName, String unitErrName, List<String> floorUnitList, String floorUnitErrName, List<String> floorUnitNo, AppGrpPremisesDto appGrpPremisesDto) {
        super.checkOperaionUnit(operationalUnitDtos, errorMap, floorErrName, unitErrName, floorUnitList, floorUnitErrName, floorUnitNo, appGrpPremisesDto);
    }
}
