package com.ecquaria.cloud.moh.iais.validate.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import com.ecquaria.cloud.moh.iais.validate.abstractValidate.AbstractValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Wenkang
 * @date 2021/4/23 13:18
 */
@Component
@Slf4j
public class ValidateEasmts extends AbstractValidate implements ValidateFlow {
    @Autowired
    private LicenceClient licenceClient;

    @Override
    public void doValidatePremises(Map<String, String> map, AppGrpPremisesDto appGrpPremisesDto,Integer index, String masterCodeDto,
            List<String> floorUnitList, List<String> floorUnitNo,String licenseeId, String appType, String licenceId) {
        String easMtsHciName = appGrpPremisesDto.getEasMtsHciName();
        if(StringUtil.isEmpty(easMtsHciName)){
            map.put("easMtsHciName"+index, MessageUtil.replaceMessage("GENERAL_ERR0006", "Business Name", "field"));
        }else {
            if(easMtsHciName.length()>100){
                String general_err0041=NewApplicationHelper.repLength("HCI Name","100");
                map.put("easMtsHciName" + index, general_err0041);
            }else {
                int hciNameChanged = 0;
                if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    hciNameChanged = NewApplicationHelper.checkNameChanged(easMtsHciName, null, licenceId);
                }
                if (2 == hciNameChanged || 4 == hciNameChanged) {
                    //no need validate hci name have keyword (is migrated and hci name never changed)
                } else {
                    Map<Integer, String> blacklistMap = NewApplicationHelper.checkBlacklist(easMtsHciName);
                    if (!blacklistMap.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        AtomicInteger length = new AtomicInteger();
                        blacklistMap.forEach((k, v) -> {
                            length.getAndIncrement();
                            sb.append(v);
                            if (map.size() != length.get()) {
                                sb.append(',').append(' ');
                            }
                        });
                        map.put("easMtsHciName" + index, MessageUtil.replaceMessage("GENERAL_ERR0016", sb.toString(), "keywords"));
                    }
                }

                List<PremisesDto> premisesDtos = licenceClient.getPremisesDtoByHciNameAndPremType(easMtsHciName,ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE,licenseeId).getEntity();
                if(!IaisCommonUtils.isEmpty(premisesDtos)){
                    map.put("hciNameUsed", MessageUtil.getMessageDesc("NEW_ACK011"));
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
        String easMtsPubEmail = appGrpPremisesDto.getEasMtsPubEmail();
        String easMtsPubHotline = appGrpPremisesDto.getEasMtsPubHotline();
        if(StringUtil.isEmpty(easMtsUseOnly)){
            map.put("easMtsUseOnly"+index,errStreet);
        }else {

        }

        if(StringUtil.isEmpty(easMtsPubEmail)){
            if(!"UOT002".equals(easMtsUseOnly)){
                map.put("easMtsPubEmail"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Email", "field"));
            }
        }else {
            boolean email = ValidationUtils.isEmail(easMtsPubEmail);
            if(!email){
                map.put("easMtsPubEmail"+index,MessageUtil.getMessageDesc("GENERAL_ERR0014"));
            }
        }

        if(StringUtil.isEmpty(easMtsPubHotline)){
            if(!"UOT002".equals(easMtsUseOnly)){
                map.put("easMtsPubHotline"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Hotline", "field"));
            }
        }else {
            if(!easMtsPubHotline.matches("^[6|8|9][0-9]{7}$")){
                map.put("easMtsPubHotline"+index,MessageUtil.getMessageDesc("GENERAL_ERR0007"));
            }
        }
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
        if(flag){
            floorUnitList.add(appGrpPremisesDto.getEasMtsFloorNo()+appGrpPremisesDto.getEasMtsUnitNo());
        }
        if(appPremisesOperationalUnitDtos!=null&&!appPremisesOperationalUnitDtos.isEmpty()){
            //
            checkOperaionUnit(appPremisesOperationalUnitDtos,map,"opEasMtsFloorNo"+index,"opEasMtsUnitNo"+index,floorUnitList,"EasMtsFloorUnit"+index,floorUnitNo,appGrpPremisesDto);
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
    public void doValidatePremises(Map<String, String> map,String type,Integer index,String licenseeId, AppGrpPremisesDto appGrpPremisesDto, boolean needAppendMsg ,boolean rfi, List<String> premisesHciList,List<String> oldPremiseHciList) {
        String PostalCode = map.get("easMtsPostalCode" + index);
        String FloorNo = map.get("easMtsFloorNo" + index);
        String BlockNo = map.get("easMtsBlockNo" + index);
        String UnitNo = map.get("easMtsUnitNo" + index);
        String HciName = map.get("easMtsHciName" + index);
        String currentHci = IaisCommonUtils.genPremisesKey(appGrpPremisesDto.getEasMtsPostalCode(), appGrpPremisesDto.getEasMtsBlockNo(), appGrpPremisesDto.getEasMtsFloorNo(), appGrpPremisesDto.getEasMtsUnitNo());
        boolean clickEdit = appGrpPremisesDto.isClickEdit();
        boolean hciFlag=StringUtil.isEmpty(PostalCode)&&StringUtil.isEmpty(FloorNo)&&StringUtil.isEmpty(BlockNo)
                &&StringUtil.isEmpty(UnitNo)&&StringUtil.isEmpty(HciName);
        log.info(StringUtil.changeForLog("hciFlag:-->easm" + hciFlag));
        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type);
        if (newTypeFlag && hciFlag && !rfi) {
            //new
            if (!IaisCommonUtils.isEmpty(premisesHciList)) {
                checkHciIsSame(appGrpPremisesDto,premisesHciList,map,"premisesHci" + index);
            }
        } else if (newTypeFlag && hciFlag && rfi && clickEdit) {
            //new rfi
            if (!IaisCommonUtils.isEmpty(premisesHciList)
                    && !IaisCommonUtils.isEmpty(oldPremiseHciList)
                    && !oldPremiseHciList.contains(currentHci)) {
                checkHciIsSame(appGrpPremisesDto, premisesHciList, map, "premisesHci" + index);
            }
        }
        if (hciFlag) {
            CheckCoLocationDto checkCoLocationDto = new CheckCoLocationDto();
            checkCoLocationDto.setLicenseeId(licenseeId);
            checkCoLocationDto.setAppGrpPremisesDto(appGrpPremisesDto);
            Boolean flag = licenceClient.getOtherLicseePremises(checkCoLocationDto).getEntity();
            if (flag) {
                needAppendMsg = true;
            }
        }
        String hciNameUsed = map.get("hciNameUsed");
        String errMsg = MessageUtil.getMessageDesc("NEW_ACK004");
        if (needAppendMsg) {
            if (StringUtil.isEmpty(hciNameUsed)) {
                map.put("hciNameUsed", errMsg);
            } else {
                String hciNameMsg = MessageUtil.getMessageDesc(hciNameUsed);
                map.put("hciNameUsed", hciNameMsg + "<br/>" + errMsg);
            }
        }
    }

    @Override
    protected void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos, Map<String, String> errorMap, String floorErrName, String unitErrName, List<String> floorUnitList, String floorUnitErrName, List<String> floorUnitNo, AppGrpPremisesDto appGrpPremisesDto) {
        super.checkOperaionUnit(operationalUnitDtos, errorMap, floorErrName, unitErrName, floorUnitList, floorUnitErrName, floorUnitNo, appGrpPremisesDto);
    }

    protected void  checkHciIsSame(AppGrpPremisesDto appGrpPremisesDto,List<String> premisesHciList,Map<String, String> errorMap,String errName){
        List<String> currHciList = NewApplicationHelper.genPremisesHciList(appGrpPremisesDto);
        for(String hci:currHciList){
            if(premisesHciList.contains(hci)){
                errorMap.put(errName, "NEW_ERR0005");
            }
        }
    }
}
