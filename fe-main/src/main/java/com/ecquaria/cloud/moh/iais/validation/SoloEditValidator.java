package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SoloEditValidator
 *
 * @author wangyu
 * @date 2021/08/04
 */
@Component
public class SoloEditValidator implements CustomizeValidator {

    @Autowired
    OrgUserManageService orgUserManageService;
    @Autowired
    FeUserClient feUserClient;
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        LicenseeDto licenseeDto = (LicenseeDto) ParamUtil.getSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION);
        if (Objects.isNull(licenseeDto)) {
            return map;
        }

        valCol("postalCode",6,licenseeDto.getPostalCode(),map);
        valCol("addrType",10,licenseeDto.getAddrType(),map);
        if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(licenseeDto.getAddrType())) {
            String blkNo = licenseeDto.getBlkNo();
            String floorNo = licenseeDto.getFloorNo();
            String unitNo = licenseeDto.getUnitNo();
            valCol("blkNo",10,blkNo,map);
            valCol("floorNo",3,floorNo,map);
            valCol("unitNo",3,unitNo,map);
        }
        valCol("streetName",32,licenseeDto.getStreetName(),map);
        valCol("buildingName",66,licenseeDto.getBuildingName(),map);
        String mobileNo = licenseeDto.getMobileNo();
        boolean verifyMobEor = valCol("telephoneNo",8,mobileNo,map);
        if (!verifyMobEor && !CommonValidator.isMobile(mobileNo)) {
            map.put("telephoneNo", MessageUtil.getMessageDesc("GENERAL_ERR0015"));
        }
        boolean errorEmail = valCol("emailAddr",320,licenseeDto.getEmilAddr(),map);
        if(!errorEmail && !ValidationUtils.isEmail(licenseeDto.getEmilAddr())){
            map.put("emailAddr", MessageUtil.getMessageDesc("GENERAL_ERR0014"));
        }
        return map;
    }

    private boolean valCol(String showEorArea,int maxLength,String code, Map<String, String> map){
        if(StringUtil.isEmpty(code)){
            String MANDATORY_MSG = MessageUtil.getMessageDesc("GENERAL_ERR0006");
            map.put(showEorArea, MANDATORY_MSG);
            return true;
        }else if(maxLength >0 && code.length() > maxLength){
            map.put(showEorArea, MessageUtil.replaceMessage("GENERAL_ERR0041",String.valueOf(maxLength),"maxlength"));
            return true;
        }
        return false;
    }

}