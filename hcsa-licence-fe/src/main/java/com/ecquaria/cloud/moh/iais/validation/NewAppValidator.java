package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * NewAppValidator
 *
 * @author Jinhua
 * @date 2019/12/13 9:27
 */
public class NewAppValidator implements CustomizeValidator {

    private static final String CAN_NOT_BE_BLANK = "cannot be blank ";
    private static final String CAN_NOT_IS_BLANK = "can not is blank";
    private static final String CAN_NOT_IS_NULL = "can not is null";

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String,String> map = IaisCommonUtils.genNewHashMap();
        List<AppGrpPremisesDto> list  = (List<AppGrpPremisesDto>) ParamUtil.getRequestAttr(request, "valPremiseList");
        if(list!=null){


        for(int i=0;i<list.size();i++ ){
            String premisesType = list.get(i).getPremisesType();
            if(!StringUtil.isEmpty(premisesType)){
                if(ApplicationConsts.PREMISES_TYPE_PERMANENT.equals(premisesType)){
                     if(StringUtil.isEmpty(list.get(i).getHciName())){
                         map.put("hciName"+i,CAN_NOT_BE_BLANK);
                     }
                     if(StringUtil.isEmpty(list.get(i).getPostalCode())){
                         map.put("postalCode"+i,CAN_NOT_BE_BLANK);
                     }else {
                         if(!list.get(i).getPostalCode().matches("^[0-9]{6}$")){
                             map.put("postalCode"+i,"GENERAL_ERR0002");
                         }
                     }
                    String addrType = list.get(i).getAddrType();
                    if(StringUtil.isEmpty(addrType)){
                        map.put("addrType"+i, CAN_NOT_IS_BLANK);
                    }else {
                        if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
                            boolean empty = StringUtil.isEmpty(list.get(i).getFloorNo());
                            boolean empty1 = StringUtil.isEmpty(list.get(i).getBlkNo());
                            boolean empty2 = StringUtil.isEmpty(list.get(i).getUnitNo());
                            if (empty) {
                                map.put("floorNo"+i, CAN_NOT_IS_NULL);
                            }
                            if (empty1) {
                                map.put("blkNo"+i, CAN_NOT_IS_BLANK);
                            }
                            if (empty2) {
                                map.put("unitNo"+i, CAN_NOT_IS_BLANK);
                            }
                        }
                    }
                   /* if(StringUtil.isEmpty(list.get(i).getOffTelNo())){
                        map.put("offTelNo"+i,"cannot be blank");
                    }else {
                        if( !list.get(i).getOffTelNo().matches(IaisEGPConstant.OFFICE_TELNO_MATCH)){
                            map.put("offTelNo"+i,"GENERAL_ERR0015");
                        }
                    }*/
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
                        if(StringUtil.isEmpty( list.get(i).getVehicleNo())){
                            map.put("vehicleNo"+i,CAN_NOT_BE_BLANK);
                        }else {

                        }
                        if(StringUtil.isEmpty(list.get(i).getStreetName())){
                            map.put("conveyanceStreetName"+i,"cannot be blank");
                        }
                    String addrType = list.get(i).getAddrType();
                    if(StringUtil.isEmpty(addrType)){
                        map.put("conveyanceAddressType"+i, CAN_NOT_IS_NULL);
                    }else {
                        if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
                            boolean empty = StringUtil.isEmpty(list.get(i).getFloorNo());
                            boolean empty1 = StringUtil.isEmpty(list.get(i).getBlkNo());
                            boolean empty2 = StringUtil.isEmpty(list.get(i).getUnitNo());
                            if (empty) {
                                map.put("conveyanceFloorNo"+i, CAN_NOT_IS_NULL);
                            }
                            if (empty1) {
                                map.put("conveyanceBlockNos"+i, CAN_NOT_IS_NULL);
                            }
                            if (empty2) {
                                map.put("conveyanceUnitNo"+i, CAN_NOT_IS_NULL);
                            }
                        }
                    }
                }
            }

        }
        }
        prinOffice(request,map);
        return map;
    }

    public void prinOffice(HttpServletRequest request,Map map){
        List<AppSvcPrincipalOfficersDto> dto = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getRequestAttr(request, "prinOffice");
        if(dto!=null){
        for(int i=0;i<dto.size();i++){
            String assignSelect = dto.get(i).getAssignSelect();
            if (StringUtil.isEmpty(assignSelect)) {
                map.put("assignSelect", "assignSelect can not null");
            }else {
                String mobileNo = dto.get(i).getMobileNo();
                String officeTelNo = dto.get(i).getOfficeTelNo();
                String emailAddr = dto.get(i).getEmailAddr();
                String idNo = dto.get(i).getIdNo();
                String name = dto.get(i).getName();
                String salutation = dto.get(i).getSalutation();
                String designation = dto.get(i).getDesignation();
                if(StringUtil.isEmpty(name)){
                    map.put("name","cannot be blank");
                }
                if(StringUtil.isEmpty(salutation)){
                    map.put("salutation","cannot be blank");
                }
                if(StringUtil.isEmpty(designation)){
                    map.put("designation","cannot be blank");
                }
                if(!StringUtil.isEmpty(idNo)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!(b||b1)){
                        map.put("NRICFIN","Please key in a valid NRIC/FIN");
                    }
                }else {
                    map.put("NRICFIN","cannot be blank");
                }
                if(!StringUtil.isEmpty(mobileNo)){
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        map.put("mobileNo", "Please key in a valid mobile number");
                    }
                }else {
                    map.put("mobileNo", "cannot be blank");
                }
                if(!StringUtil.isEmpty(emailAddr)) {
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        map.put("emailAddr", "Please key in a valid email address");
                    }
                }else {
                    map.put("emailAddr", "cannot be blank");
                }
                if(!StringUtil.isEmpty(officeTelNo)) {
                    if (!officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                        map.put("officeTelNo", "Please key in a valid phone number");
                    }
                }else {
                    map.put("officeTelNo", "cannot be blank");
                }
            }
        }
        }
    }
}
