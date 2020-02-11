package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 *
 *   @date 1/4/2020
 *   @author zixian
 */
@Slf4j
@Delegator("MohRequestForChangeMenuDelegator")
public class RequestForChangeMenuDelegator {
    @Autowired
    RequestForChangeService requestForChangeService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private ServiceConfigService serviceConfigService;
    /**
     *
     * @param bpc
     * @Decription start
     */
    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do start start ...."));
        ParamUtil.setSessionAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,null);

        log.debug(StringUtil.changeForLog("the do start end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription personnleListStart
     */
    public void personnleListStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do personnleListStart start ...."));
        ParamUtil.setSessionAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,null);

        log.debug(StringUtil.changeForLog("the do personnleListStart end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prepare  premises List entrance
     */
    public void prepare(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the prepare start ...."));
        String action = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(action)){
            action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
            if(StringUtil.isEmpty(action)){
                action = "prePremisesList";
            }
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM,action);
        log.debug(StringUtil.changeForLog("the prepare end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription preparePersonnel  personnel List entrance
     */
    public void preparePersonnel(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preparePersonnel start ...."));
        String action = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(action)){
            action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
            if(StringUtil.isEmpty(action)){
                action = "prePersonnelList";
            }
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM,action);
        log.debug(StringUtil.changeForLog("the preparePersonnel end ...."));
    }
    /**
     *
     * @param bpc
     * @Decription controlSwitch
     */
    public void controlSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the controlSwitch start ...."));
        String switchValue = (String) ParamUtil.getRequestAttr(bpc.request, "switch_value");
        if(StringUtil.isEmpty(switchValue)){
            switchValue = "loading";
        }
        ParamUtil.setRequestAttr(bpc.request,"switch",switchValue);
        log.debug(StringUtil.changeForLog("the controlSwitch end ...."));
    }
    /**
     *
     * @param bpc
     * @Decription preparePremisesList
     */
    public void preparePremisesList(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePremisesList start ...."));
        //todo
        String licenseeId = "111";
        List<PremisesListQueryDto> premisesDtos = requestForChangeService.getPremisesList(licenseeId);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, (Serializable) premisesDtos);


        log.debug(StringUtil.changeForLog("the do preparePremisesList end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription preparePremisesEdit
     */
    public void preparePremisesEdit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePremisesEdit start ...."));

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);

        List<AppGrpPremisesDto> reloadPremisesDtoList = new ArrayList<>();
        AppGrpPremisesDto appGrpPremisesDto = null;
        if(appSubmissionDto != null){
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList) && premisesListQueryDto != null) {
                String premType = premisesListQueryDto.getPremisesType();
                String premHciOrConvName = "";
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)) {
                    premHciOrConvName = premisesListQueryDto.getHciName();
                } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)) {
                    premHciOrConvName = premisesListQueryDto.getVehicleNo();
                }
                appGrpPremisesDto = getAppGrpPremisesDtoFromAppGrpPremisesDtoList(appGrpPremisesDtoList, premType, premHciOrConvName);
                reloadPremisesDtoList.add(appGrpPremisesDto);
            }
        }
        ParamUtil.setRequestAttr(bpc.request, RfcConst.RELOADPREMISES, reloadPremisesDtoList);

        log.debug(StringUtil.changeForLog("the do preparePremisesEdit end ...."));
    }



    /**
     *
     * @param bpc
     * @Decription doPremisesList
     */
    public void doPremisesList(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPremisesList start ...."));
            String crudActionType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(crudActionType)){
            crudActionType = "back";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionType);
            return;
        }
        String index = ParamUtil.getString(bpc.request, "hiddenIndex");
        String licId = ParamUtil.getMaskedString(bpc.request, "licId" + index);
        String premId = ParamUtil.getMaskedString(bpc.request, "premisesId" + index);
        PremisesListQueryDto premisesListQueryDto = new PremisesListQueryDto();
        AppSubmissionDto appSubmissionDto = null;
        if (!StringUtil.isEmpty(licId) && !StringUtil.isEmpty(premId)) {
            List<PremisesListQueryDto> premisesListQueryDtos = (List<PremisesListQueryDto>) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS);
            if (!IaisCommonUtils.isEmpty(premisesListQueryDtos)) {
                premisesListQueryDto = getPremisesListQueryDto(premisesListQueryDtos, licId, premId);
                if (premisesListQueryDto != null) {
                    appSubmissionDto = requestForChangeService.getAppSubmissionDtoByLicenceId(premisesListQueryDto.getLicenceId());
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO, premisesListQueryDto);


        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionType);
        log.debug(StringUtil.changeForLog("the do doPremisesList end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription doPremisesEdit
     */
    public void doPremisesEdit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPremisesEdit start ...."));
        String action =  ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE) ;
        if("back".equals(action)){
            return;
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        AppGrpPremisesDto newPremisesDto = genAppGrpPremisesDto(premisesListQueryDto, bpc.request);

        List<AppGrpPremisesDto> appGrpPremisesDtoList = new ArrayList<>();
        appGrpPremisesDtoList.add(newPremisesDto);
        ParamUtil.setRequestAttr(bpc.request, RfcConst.RELOADPREMISES, appGrpPremisesDtoList);
        //validate
        ApplicationDto applicationDto = requestForChangeService.getApplicationByLicenceId(premisesListQueryDto.getLicenceId());
        String appStatus = "";
        if(applicationDto != null){
            appStatus = applicationDto.getStatus();
        }
        Map<String, String> errorMap = new HashMap<>();
        if(ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(premisesListQueryDto.getStatus())){
            errorMap.put("Globle", "licence must be active");
        }
        //wait constats
        /*else if(ApplicationConsts.XXX.equals(appStatus)){
            errorMap.put("Globle", "had ongoing application for licence");
        }*/else{
            errorMap = doValidatePremiss(bpc.request, newPremisesDto);
        }
        if(errorMap.size() >0){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg",WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.FORM_TAB, "prepareEdit");
            return;
        }

        String appType = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION;
        if(!StringUtil.isEmpty(appSubmissionDto.getAppType())){
            appType = appSubmissionDto.getAppType();
        }
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(appType);
        appSubmissionDto.setAppGrpNo(appGroupNo);

        //amount
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(false);
        amendmentFeeDto.setChangeInHCIName(false);
        boolean isSame = compareLocation(premisesListQueryDto, newPremisesDto);
        amendmentFeeDto.setChangeInLocation(!isSame);
        FeeDto feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        if(feeDto!= null){
            appSubmissionDto.setAmount(feeDto.getTotal());
        }

        appSubmissionDto.setAutoRfc(isSame);
        //update status
        /*LicenceDto licenceDto = new LicenceDto();
        licenceDto.setId(appSubmissionDto.getLicenceId());
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_REQUEST_FOR_CHANGE);
        requestForChangeService.upDateLicStatus(licenceDto);*/

        //save data
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        appSubmissionDto = appSubmissionService.submitRequestChange(appSubmissionDto, bpc.process);


        log.debug(StringUtil.changeForLog("the do doPremisesEdit end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription preparePersonnelList
     */
    public void preparePersonnelList(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePersonnelList start ...."));
        String licenseeId = "123";
        List<PersonnelListQueryDto> licenseeKeyApptPersonDtoList =  requestForChangeService.getLicencePersonnelListQueryDto(licenseeId);
        Map<String,List<PersonnelListQueryDto>> personnelListMap = new HashMap<>();
        for(PersonnelListQueryDto personnelListQueryDto:licenseeKeyApptPersonDtoList){
            String idNo = personnelListQueryDto.getIdNo();
            List<PersonnelListQueryDto> personnelListQueryDtos  = personnelListMap.get(idNo);
            if(IaisCommonUtils.isEmpty(personnelListQueryDtos)){
                personnelListQueryDtos = new ArrayList<>();
            }
            personnelListQueryDtos.add(personnelListQueryDto);
            personnelListMap.put(idNo,personnelListQueryDtos);
        }
        ParamUtil.setSessionAttr(bpc.request,RfcConst.PERSONNELLISTMAP, (Serializable) personnelListMap);
        log.debug(StringUtil.changeForLog("the do preparePersonnelList end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription doPersonnelList
     */
    public void doPersonnelList(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPersonnelList start ...."));
        String hiddenIndex = ParamUtil.getString(bpc.request,"hiddenIndex");
        String idNo = ParamUtil.getMaskedString(bpc.request,"personnelNo"+hiddenIndex);
        Map<String,List<PersonnelListQueryDto>> personnelListMap  = (Map<String, List<PersonnelListQueryDto>>) ParamUtil.getSessionAttr(bpc.request,RfcConst.PERSONNELLISTMAP);
        List<PersonnelListQueryDto> personnelEditList = personnelListMap.get(idNo);
        ParamUtil.setSessionAttr(bpc.request,RfcConst.PERSONNELEDITLIST, (Serializable) personnelEditList);
        log.debug(StringUtil.changeForLog("the do doPersonnelList end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription preparePersonnelEdit
     */
    public void preparePersonnelEdit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePersonnelEdit start ...."));

        log.debug(StringUtil.changeForLog("the do preparePersonnelEdit end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription preparePersonnelList
     */
    public void doPersonnelEdit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPersonnelEdit start ...."));
        List<PersonnelListQueryDto> personnelEditList = (List<PersonnelListQueryDto>) ParamUtil.getSessionAttr(bpc.request,RfcConst.PERSONNELEDITLIST);
        String email = ParamUtil.getString(bpc.request ,"emailAddress" ) ;
        String mobile = ParamUtil.getString(bpc .request, "mobileNo");
        PersonnelListQueryDto personnelListQueryDto = personnelEditList.get(0);
        personnelListQueryDto.setEmailAddr(email);
        personnelListQueryDto.setMobileNo(mobile);
        /*personnelListQueryDto.setDesignation();
        personnelListQueryDto.setProfessionType();
        personnelListQueryDto.setProfessionRegnNo();*/

        Map<String,String> errMap = new HashMap<>();
        if(StringUtil.isEmpty(email)){
            errMap.put("emailAddr", "UC_CHKLMD001_ERR001");
        }else if (!StringUtil.isEmpty(email)) {
            if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                errMap.put("emailAddr", "CHKLMD001_ERR006");
            }
        }

        if(StringUtil.isEmpty(mobile)){
            errMap.put("mobileNo", "UC_CHKLMD001_ERR001");
        }else if (!StringUtil.isEmpty(mobile)) {
            if (!mobile.matches("^[8|9][0-9]{7}$")) {
                errMap.put("mobileNo", "CHKLMD001_ERR004");
            }
        }

        boolean active = true;
        for(PersonnelListQueryDto item:personnelEditList){
            if(!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(item.getLicenceStatus())){
                active = false;
            }
        }
        if(!active){
            errMap.put("licenceStatus","");
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return;
        }
        List<String> licenceIds = new ArrayList<>();
        for(PersonnelListQueryDto item:personnelEditList){
            licenceIds.add(item.getLicenceId());
        }
        List<AppSubmissionDto> appSubmissionDtos = requestForChangeService.getAppSubmissionDtoByLicenceIds(licenceIds);
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        for(AppSubmissionDto appSubmissionDto:appSubmissionDtos){
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
            appSubmissionDto.setAppGrpNo(appGroupNo);
            appSubmissionDto.setAmount(0.0);
            appSubmissionDto.setAutoRfc(true);
            appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appSubmissionDto = setPersonnelDate(appSubmissionDto,personnelListQueryDto);
        }
        //update status wait change api
       /* for(AppSubmissionDto appSubmissionDto:appSubmissionDtos){
            LicenceDto licenceDto = new LicenceDto();
            licenceDto.setId(appSubmissionDto.getLicenceId());
            licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_REQUEST_FOR_CHANGE);
            requestForChangeService.upDateLicStatus(licenceDto);
        }*/




        requestForChangeService.saveAppsBySubmissionDtos(appSubmissionDtos);

        log.debug(StringUtil.changeForLog("the do doPersonnelEdit end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription prepareAckPage
     */
    public void prepareAckPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareAckPage start ...."));


        log.debug(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prePayment
     */
    public void prePayment(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prePayment start ...."));


        log.debug(StringUtil.changeForLog("the do prePayment end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription jumpBank
     */
    public void jumpBank(BaseProcessClass bpc)  throws IOException {
        log.debug(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        if(StringUtil.isEmpty(payMethod)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM,"prePayment");
            return;
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.APPSUBMISSIONDTO);

        if("Credit".equals(payMethod)){
            String backUrl = "hcsa-licence-web/eservice/INTERNET/MohRfcPermisesList/1/doPayment";
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(appSubmissionDto.getAmount())
                    .append("&payMethod=").append(payMethod)
                    .append("&reqNo=").append(appSubmissionDto.getAppGrpNo())
                    .append("&backUrl=").append(backUrl);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }else if("GIRO".equals(payMethod)){
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO);
            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", "GIRO");
        }

        log.debug(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription doPayment
     */
    public void doPayment(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPayment start ...."));
        ParamUtil.setRequestAttr(bpc.request,"switch_value","ack");

        log.debug(StringUtil.changeForLog("the do doPayment end ...."));
    }


    private PremisesListQueryDto getPremisesListQueryDto(List<PremisesListQueryDto> premisesListQueryDtos, String liceId, String premId) {
        PremisesListQueryDto result = null;
        for (PremisesListQueryDto premisesListQueryDto : premisesListQueryDtos) {
            String thisLicId = premisesListQueryDto.getLicenceId();
            String thisPremId = premisesListQueryDto.getPremisesId();
            if (liceId.equals(thisLicId) && premId.equals(thisPremId)) {
                result = premisesListQueryDto;
                break;
            }
        }
        return result;
    }


    private AppGrpPremisesDto getAppGrpPremisesDtoFromAppGrpPremisesDtoList(List<AppGrpPremisesDto> appGrpPremisesDtoList, String premType, String premHciOrConvName) {
        AppGrpPremisesDto result = null;
        if (!StringUtil.isEmpty(premType)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                String premisesVal = getPremisesVal(appGrpPremisesDto);
                if (premType.equals(appGrpPremisesDto.getPremisesType()) && premisesVal.equals(premHciOrConvName)) {
                    result = appGrpPremisesDto;
                    break;
                }
            }
        }
        return result;
    }

    private String getPremisesVal(AppGrpPremisesDto appGrpPremisesDto){
        String premisesVal = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            premisesVal = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            premisesVal = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return premisesVal;
    }

    private String getPremisesVal(PremisesListQueryDto premisesListQueryDto){
        String premisesVal = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())) {
            premisesVal = premisesListQueryDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            premisesVal = premisesListQueryDto.getVehicleNo();
        }
        return premisesVal;
    }

    private AppGrpPremisesDto genAppGrpPremisesDto(PremisesListQueryDto premisesListQueryDto, HttpServletRequest request) {

        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        String premisesType = premisesListQueryDto.getPremisesType();
        appGrpPremisesDto.setPremisesType(premisesType);
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
            String postalCode = ParamUtil.getString(request, "postalCode");
            String blkNo = ParamUtil.getString(request, "blkNo");
            String streetName = ParamUtil.getString(request, "streetName");
            String floorNo = ParamUtil.getString(request, "floorNo");
            String unitNo = ParamUtil.getString(request, "unitNo");
            String buildingName = ParamUtil.getString(request, "buildingName");
            String siteAddressType = ParamUtil.getString(request, "siteAddressType");
            String scdfRefNo = ParamUtil.getString(request, "scdfRefNo");
            appGrpPremisesDto.setHciName(premisesListQueryDto.getHciName());
            appGrpPremisesDto.setPostalCode(postalCode);
            appGrpPremisesDto.setBlkNo(blkNo);
            appGrpPremisesDto.setStreetName(streetName);
            appGrpPremisesDto.setFloorNo(floorNo);
            appGrpPremisesDto.setUnitNo(unitNo);
            appGrpPremisesDto.setBuildingName(buildingName);
            appGrpPremisesDto.setAddrType(siteAddressType);
            appGrpPremisesDto.setScdfRefNo(scdfRefNo);
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
            String conPostalCode = ParamUtil.getString(request, "conveyancePostalCode");
            String conBlkNo = ParamUtil.getString(request, "conveyanceBlockNo");
            String conStreetName = ParamUtil.getString(request, "conveyanceStreetName");
            String conFloorNo = ParamUtil.getString(request, "conveyanceFloorNo");
            String conUnitNo = ParamUtil.getString(request, "conveyanceUnitNo");
            String conBuildingName = ParamUtil.getString(request, "conveyanceBuildingName");
            String conSiteAddressType = ParamUtil.getString(request, "conveyanceAddrType");
            appGrpPremisesDto.setConveyanceVehicleNo(premisesListQueryDto.getVehicleNo());
            appGrpPremisesDto.setConveyancePostalCode(conPostalCode);
            appGrpPremisesDto.setConveyanceBlockNo(conBlkNo);
            appGrpPremisesDto.setConveyanceStreetName(conStreetName);
            appGrpPremisesDto.setConveyanceFloorNo(conFloorNo);
            appGrpPremisesDto.setConveyanceUnitNo(conUnitNo);
            appGrpPremisesDto.setConveyanceUnitNo(conUnitNo);
            appGrpPremisesDto.setConveyanceBuildingName(conBuildingName);
            appGrpPremisesDto.setConveyanceAddressType(conSiteAddressType);
        }
        return appGrpPremisesDto;
    }

    private Map<String, String> doValidatePremiss(HttpServletRequest request, AppGrpPremisesDto appGrpPremisesDto) {
        String premiseType = appGrpPremisesDto.getPremisesType();
        Map<String, String> errorMap = new HashMap<>();
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
            String postalCode = appGrpPremisesDto.getPostalCode();
            if (!StringUtil.isEmpty(postalCode)) {
                if (!postalCode.matches("^[0-9]{6}$")) {
                    errorMap.put("postalCode", "CHKLMD001_ERR003");
                }
            }else {
                errorMap.put("postalCode", "UC_CHKLMD001_ERR001");
            }

            String streetName = appGrpPremisesDto.getStreetName();
            if(StringUtil.isEmpty(streetName)){
                errorMap.put("streetName","UC_CHKLMD001_ERR002");
            }

            String addrType = appGrpPremisesDto.getAddrType();
            if(StringUtil.isEmpty(addrType)){
                errorMap.put("addrType", "UC_CHKLMD001_ERR002");
            }else {
                boolean empty = StringUtil.isEmpty(appGrpPremisesDto.getFloorNo());
                boolean empty1 = StringUtil.isEmpty(appGrpPremisesDto.getBlkNo());
                boolean empty2 = StringUtil.isEmpty(appGrpPremisesDto.getUnitNo());
                if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
                    if (empty) {
                        errorMap.put("floorNo", "UC_CHKLMD001_ERR001");
                    }else{
                        //todo validate
                    }
                    if (empty1) {
                        errorMap.put("blkNo", "UC_CHKLMD001_ERR001");
                    }else{
                        //todo validate
                    }
                    if (empty2) {
                        errorMap.put("unitNo", "UC_CHKLMD001_ERR001");
                    }else{
                        //todo validate
                    }
                }else if(ApplicationConsts.ADDRESS_TYPE_WITHOUT_APT_BLK.equals(addrType)){
                    if (!empty) {
                        //todo validate
                    }
                    if (!empty1) {
                        //todo validate
                    }
                    if (!empty2) {
                        //todo validate
                    }
                }
            }
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {

            String conveyancePostalCode = appGrpPremisesDto.getConveyancePostalCode();
            if(StringUtil.isEmpty(conveyancePostalCode)){
                errorMap.put("conveyancePostalCode","UC_CHKLMD001_ERR001");
            }else {
                if(!conveyancePostalCode.matches("^[0-9]{6}$")){
                    errorMap.put("conveyancePostalCode", "CHKLMD001_ERR003");
                }
            }

            String cStreetName = appGrpPremisesDto.getConveyanceStreetName();

            if(StringUtil.isEmpty(cStreetName)){
                errorMap.put("conveyanceStreetName","UC_CHKLMD001_ERR001");
            }
            String conveyanceAddressType = appGrpPremisesDto.getConveyanceAddressType();
            if(StringUtil.isEmpty(conveyanceAddressType)){
                errorMap.put("conveyanceAddressType", "UC_CHKLMD001_ERR002");
            }else {
                if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(conveyanceAddressType)) {
                    boolean empty = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceFloorNo());
                    boolean empty1 = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceBlockNo());
                    boolean empty2 = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceUnitNo());
                    if (empty) {
                        errorMap.put("conveyanceFloorNo", "UC_CHKLMD001_ERR001");
                    }
                    if (empty1) {
                        errorMap.put("conveyanceBlockNos", "UC_CHKLMD001_ERR001");
                    }
                    if (empty2) {
                        errorMap.put("conveyanceUnitNo", "UC_CHKLMD001_ERR001");
                    }
                }
            }
        }
        return  errorMap;
    }


    private boolean compareLocation(PremisesListQueryDto premisesListQueryDto, AppGrpPremisesDto appGrpPremisesDto){
        String oldAddress = premisesListQueryDto.getAddress();
        String newAddress = appGrpPremisesDto.getAddress();
        if(!oldAddress.equals(newAddress)){
            return false;
        }
        return true;
    }

    private AppSubmissionDto setPersonnelDate(AppSubmissionDto appSubmissionDto,PersonnelListQueryDto personnelListQueryDto){
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtos){
                    if(personnelListQueryDto.getIdNo().equals(appSvcCgoDto.getIdNo())){
                        appSvcCgoDto.setEmailAddr(personnelListQueryDto.getEmailAddr());
                        appSvcCgoDto.setMobileNo(personnelListQueryDto.getMobileNo());
                   /* appSvcCgoDto.setDesignation(personnelListQueryDto.getDesignation());
                    appSvcCgoDto.setProfessionType(personnelListQueryDto.getProfessionType());
                    appSvcCgoDto.setProfessionRegoNo(personnelListQueryDto.getProfessionRegnNo());*/
                    }
                }
            }


            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos =appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)){
                for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcPrincipalOfficersDtos){
                    if(personnelListQueryDto.getIdNo().equals(appSvcPrincipalOfficersDto.getIdNo())){
                        appSvcPrincipalOfficersDto.setEmailAddr(personnelListQueryDto.getEmailAddr());
                        appSvcPrincipalOfficersDto.setMobileNo(personnelListQueryDto.getMobileNo());
                        appSvcPrincipalOfficersDto.setDesignation(personnelListQueryDto.getDesignation());
                    }

                }
            }


        }
        return appSubmissionDto;
    }

}
