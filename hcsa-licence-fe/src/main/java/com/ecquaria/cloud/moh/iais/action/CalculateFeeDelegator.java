package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.CalculateFeeConditionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * CalculateFeeDelegator
 *
 * @author junyu
 * @date 2023/1/12
 */
@Delegator("CalculateFeeDelegate")
@Slf4j
public class CalculateFeeDelegator {
    @Autowired
    private HcsaConfigFeClient hcsaConfigClient;
    @Autowired
    private ConfigCommClient configCommClient;
    private static final String EMPTY = "";
    public static final String[] EMPTYARRAY = {EMPTY, EMPTY, EMPTY};
    public void start(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"calculateFeeConditionDto",null);
        ParamUtil.setSessionAttr(bpc.request,"addConditionList", null );

    }

    public void perSvc(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE,"Calculate Fee");
        ParamUtil.setRequestAttr(bpc.request,"licSvcTypeOption", getLicSvcTypeOption());
    }

    public void calculateFee(BaseProcessClass bpc) throws ParseException {
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE,"Calculate Fee");
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, IaisEGPConstant.YES);
        CalculateFeeConditionDto mainCalculateFeeConditionDto=setCalculateFeeConditionDto(request);
        String[] lengths=ParamUtil.getStrings(request,"lengthsInfo");

        List<CalculateFeeConditionDto> addConditionList=IaisCommonUtils.genNewArrayList();
        if(lengths!=null &&lengths.length>0){
            for (String len:lengths
            ) {
                CalculateFeeConditionDto filterDto=new CalculateFeeConditionDto();
                String mosdType=ParamUtil.getString(request,"mosdType"+len);
                filterDto.setMosdType(mosdType);
                String serviceName=ParamUtil.getString(request,"serviceName"+len);
                filterDto.setServiceName(serviceName);
                String simpleNum=ParamUtil.getString(request,"simpleNum"+len);
                filterDto.setSimpleNum(simpleNum);
                String complexNum=ParamUtil.getString(request,"complexNum"+len);
                filterDto.setComplexNum(complexNum);
                String numVehicles=ParamUtil.getString(request,"numVehicles"+len);
                filterDto.setNumVehicles(numVehicles);
                String numBeds=ParamUtil.getString(request,"numBeds"+len);
                filterDto.setNumBeds(numBeds);

                addConditionList.add(filterDto);
            }
            ParamUtil.setSessionAttr(request,"addConditionList", (Serializable) addConditionList);
        }else {
            ParamUtil.setSessionAttr(request,"addConditionList", null );
        }
        Map<String, String> errMap=validate(mainCalculateFeeConditionDto,addConditionList);
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, IaisEGPConstant.NO);
            bpc. request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errMap));
            return;
        }
        List<LicenceFeeDto> licenceFeeQuaryDtos =null;
        AmendmentFeeDto amendmentFeeDto=null;
        FeeDto feeDto=null;
        switch (mainCalculateFeeConditionDto.getApplicationType()){
            case ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION:
                licenceFeeQuaryDtos = newOrRenewalFeeCondition(mainCalculateFeeConditionDto,addConditionList);
                feeDto=configCommClient.newFee(licenceFeeQuaryDtos).getEntity();

                break;
            case ApplicationConsts.APPLICATION_TYPE_RENEWAL:
                licenceFeeQuaryDtos = newOrRenewalFeeCondition(mainCalculateFeeConditionDto,addConditionList);
                feeDto=configCommClient.renewFee(licenceFeeQuaryDtos).getEntity();
                HashMap<String, List<FeeExtDto>> laterFeeDetailsMap = WithOutRenewalDelegator.getLaterFeeDetailsMap(feeDto.getFeeInfoDtos());
                ParamUtil.setRequestAttr(bpc.request, "laterFeeDetailsMap", laterFeeDetailsMap);
                break;
            case ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE:
                amendmentFeeDto = amendmentFeeCondition(mainCalculateFeeConditionDto);
                feeDto=configCommClient.amendmentFee(amendmentFeeDto).getEntity();

                break;
            default:
        }
        ParamUtil.setSessionAttr(bpc.request, "feeDto", feeDto);

    }

    private AmendmentFeeDto amendmentFeeCondition(CalculateFeeConditionDto mainCalculateFeeConditionDto) {
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        boolean isCharity="Y".equals(mainCalculateFeeConditionDto.getRadioCharitable());
        boolean isAmendment="Y".equals(mainCalculateFeeConditionDto.getRadioAmendment());
        amendmentFeeDto.setIsCharity(isCharity);
        amendmentFeeDto.setChangeBusinessName(isAmendment);
        amendmentFeeDto.setAppGrpNo("AQXXXXXXXXX");
        amendmentFeeDto.setLicenceExpiryDate(mainCalculateFeeConditionDto.getLicenceDateTo());
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceByServiceName(mainCalculateFeeConditionDto.getServiceName());
        amendmentFeeDto.setServiceCode(serviceDto.getSvcCode());
        amendmentFeeDto.setAddress(mainCalculateFeeConditionDto.getMosdType()+"Address");
        amendmentFeeDto.setServiceName(serviceDto.getSvcName());

        List<LicenceFeeDto> licenceFeeSpecDtos = IaisCommonUtils.genNewArrayList();

        if (StringUtil.isNotEmpty(mainCalculateFeeConditionDto.getComplexNum())&& CommonValidator.isPositiveInteger(mainCalculateFeeConditionDto.getComplexNum())) {
            amendmentFeeDto.setAdditionOrRemovalSpecialisedServices(Boolean.TRUE);
            int complexNum=Integer.parseInt(mainCalculateFeeConditionDto.getComplexNum());
            for (int i=0 ; i<complexNum ;i++){
                LicenceFeeDto specFeeDto = new LicenceFeeDto();
                specFeeDto.setBundle(0);
                specFeeDto.setBaseService(serviceDto.getSvcCode());
                specFeeDto.setServiceCode("S40");
                specFeeDto.setServiceName("Complex Specified Service");
                specFeeDto.setPremises(mainCalculateFeeConditionDto.getMosdType()+"Address");
                specFeeDto.setCharity(isCharity);
                licenceFeeSpecDtos.add(specFeeDto);
            }
        }
        if (StringUtil.isNotEmpty(mainCalculateFeeConditionDto.getSimpleNum())&& CommonValidator.isPositiveInteger(mainCalculateFeeConditionDto.getSimpleNum())) {
            amendmentFeeDto.setAdditionOrRemovalSpecialisedServices(Boolean.TRUE);
            int simpleNum=Integer.parseInt(mainCalculateFeeConditionDto.getSimpleNum());
            for (int i=0 ; i<simpleNum ;i++){
                LicenceFeeDto specFeeDto = new LicenceFeeDto();
                specFeeDto.setBundle(0);
                specFeeDto.setBaseService(serviceDto.getSvcCode());
                specFeeDto.setServiceCode("S60");
                specFeeDto.setServiceName("Simple Specified Service");
                specFeeDto.setPremises(mainCalculateFeeConditionDto.getMosdType()+"Address");
                specFeeDto.setCharity(isCharity);
                licenceFeeSpecDtos.add(specFeeDto);
            }
        }
        if(IaisCommonUtils.isNotEmpty(licenceFeeSpecDtos)){
            amendmentFeeDto.setSpecifiedLicenceFeeDto(licenceFeeSpecDtos);
        }

        return amendmentFeeDto;
    }

    private List<LicenceFeeDto> newOrRenewalFeeCondition(CalculateFeeConditionDto mainCalculateFeeConditionDto, List<CalculateFeeConditionDto> addConditionList) {
        List<CalculateFeeConditionDto> allConditionList=IaisCommonUtils.genNewArrayList();
        allConditionList.add(mainCalculateFeeConditionDto);
        allConditionList.addAll(addConditionList);

        boolean isCharity="Y".equals(mainCalculateFeeConditionDto.getRadioCharitable());
        boolean isAlign="Y".equals(mainCalculateFeeConditionDto.getRadioAlign());
        List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = configCommClient.getActiveBundleDtoList().getEntity();
        Set<String> bundleSvcCodes = IaisCommonUtils.genNewHashSet();
        if (IaisCommonUtils.isNotEmpty(hcsaFeeBundleItemDtos)) {
            hcsaFeeBundleItemDtos.forEach(o -> bundleSvcCodes.add(o.getSvcCode()));
        }
        List<LicenceFeeDto> licenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        List<String[]> msList = IaisCommonUtils.genNewArrayList();
        String[] msPreOrConArray = EMPTYARRAY;
        msList.add(msPreOrConArray);
        List<String[]> dsList = IaisCommonUtils.genNewArrayList();
        String[] dsPreOrConArray = EMPTYARRAY;
        dsList.add(dsPreOrConArray);

        boolean hadEas = false;
        boolean hadMts = false;
        boolean hadAch = false;
        int vehicleCount = 0;
        for (CalculateFeeConditionDto conditionDto : allConditionList) {
            HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceByServiceName(conditionDto.getServiceName());
            String serviceCode = svcDto.getSvcCode();

            if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)) {
                int vehicleNum=Integer.parseInt(conditionDto.getNumVehicles());
                vehicleCount+=vehicleNum;
                hadEas = true;
            } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)) {
                int vehicleNum=Integer.parseInt(conditionDto.getNumVehicles());
                vehicleCount+=vehicleNum;
                hadMts = true;
            } else if (AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
                hadAch = true;
            }
        }
        List<LicenceFeeDto> achLicenceFeeDtoList = IaisCommonUtils.genNewArrayList();

        for (CalculateFeeConditionDto conditionDto : allConditionList) {
            HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceByServiceName(conditionDto.getServiceName());
            LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
            licenceFeeDto.setBundle(0);
            String serviceCode = svcDto.getSvcCode();
            licenceFeeDto.setBaseService(serviceCode);
            licenceFeeDto.setServiceCode(serviceCode);
            licenceFeeDto.setServiceName(svcDto.getSvcName());
            licenceFeeDto.setPremises(conditionDto.getMosdType()+"Address");
            licenceFeeDto.setCharity(isCharity);
            if(mainCalculateFeeConditionDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)){
                licenceFeeDto.setAppGrpNo("ANXXXXXXXXX");
                if(isAlign){
                    licenceFeeDto.setExpiryDate(mainCalculateFeeConditionDto.getLicenceDateTo());
                }
            }else {
                licenceFeeDto.setAppGrpNo("ARXXXXXXXXX");
                licenceFeeDto.setExpiryDate(mainCalculateFeeConditionDto.getLicenceExpiryDate());
            }

            //set mosd bundle
            if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)) {
                if (conditionDto.getMosdType().equals(
                        ApplicationConsts.PREMISES_TYPE_PERMANENT) || conditionDto.getMosdType().equals(
                        ApplicationConsts.PREMISES_TYPE_CONVEYANCE)) {
                    boolean find = false;
                    for (String[] ms : msList) {
                        if (StringUtil.isEmpty(ms[0])) {
                            ms[0]=conditionDto.getMosdType();
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {conditionDto.getMosdType(), EMPTY, EMPTY};
                        msList.add(newArray);
                    }
                }

                if (conditionDto.getMosdType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                    boolean find = false;
                    for (String[] ms : msList) {
                        if (StringUtil.isEmpty(ms[1])) {
                            find = true;
                            ms[1]=conditionDto.getMosdType();
                            if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[2])){
                                licenceFeeDto.setBundle(3);
                            }
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {EMPTY, conditionDto.getMosdType(), EMPTY};
                        msList.add(newArray);
                    }
                }
                if (conditionDto.getMosdType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                    boolean find = false;
                    for (String[] ms : msList
                    ) {
                        if (StringUtil.isEmpty(ms[2])) {
                            find = true;
                            ms[2]=conditionDto.getMosdType();
                            if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[1])){
                                licenceFeeDto.setBundle(3);
                            }
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {EMPTY, EMPTY, conditionDto.getMosdType()};
                        msList.add(newArray);
                    }
                }
            }
            if (serviceCode.equals(AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE)) {
                if (conditionDto.getMosdType().equals(
                        ApplicationConsts.PREMISES_TYPE_PERMANENT) || conditionDto.getMosdType().equals(
                        ApplicationConsts.PREMISES_TYPE_CONVEYANCE)) {
                    boolean find = false;
                    for (String[] ms : dsList) {
                        if (StringUtil.isEmpty(ms[0])) {
                            ms[0]=conditionDto.getMosdType();
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {conditionDto.getMosdType(), EMPTY, EMPTY};
                        dsList.add(newArray);
                    }
                }

                if (conditionDto.getMosdType().equals(ApplicationConsts.PREMISES_TYPE_MOBILE)) {
                    boolean find = false;
                    for (String[] ms : dsList) {
                        if (StringUtil.isEmpty(ms[1])) {
                            find = true;
                            ms[1]=conditionDto.getMosdType();
                            if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[2])){
                                licenceFeeDto.setBundle(3);
                            }
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {EMPTY, conditionDto.getMosdType(), EMPTY};
                        dsList.add(newArray);
                    }
                }
                if (conditionDto.getMosdType().equals(ApplicationConsts.PREMISES_TYPE_REMOTE)) {
                    boolean find = false;
                    for (String[] ms : dsList
                    ) {
                        if (StringUtil.isEmpty(ms[2])) {
                            find = true;
                            ms[2]=conditionDto.getMosdType();
                            if(!EMPTY.equals(ms[0]) || !EMPTY.equals(ms[1])){
                                licenceFeeDto.setBundle(3);
                            }
                            break;
                        }
                    }
                    if (!find) {
                        String[] newArray = {EMPTY, EMPTY, conditionDto.getMosdType()};
                        dsList.add(newArray);
                    }
                }
            }

            //set EAS/MTS bundle
            if (!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos) && bundleSvcCodes.contains(serviceCode)) {
                log.debug(StringUtil.changeForLog("set bundle info ..."));
                if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)) {
                    if (hadEas && hadMts) {
                        //judge vehicle count
                        int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();
                        if (vehicleCount <= matchingTh) {
                            licenceFeeDto.setBundle(1);
                        } else {
                            licenceFeeDto.setBundle(2);
                        }
                    } else {
                        setEasMtsBundleInfo(licenceFeeDto, serviceCode, vehicleCount);
                    }
                } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)) {
                    if (hadEas && hadMts) {
                        //new eas and mts
                        licenceFeeDto.setBundle(3);
                    } else {
                        setEasMtsBundleInfo(licenceFeeDto, serviceCode, vehicleCount);
                    }
                }
            }

            if (AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL.equals(
                    serviceCode) || AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
                int bedTotal = Integer.parseInt(conditionDto.getNumBeds());
                int beds = 0;
                if (AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL.equals(serviceCode)) {
                    beds = 100;
                }
                if (AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode)) {
                    beds = 1000;
                }
                if (bedTotal >= beds) {
                    licenceFeeDto.setBundle(2);
                } else {
                    licenceFeeDto.setBundle(1);
                }
            }
            List<LicenceFeeDto> licenceFeeSpecDtos = IaisCommonUtils.genNewArrayList();

            if (StringUtil.isNotEmpty(conditionDto.getComplexNum())&& CommonValidator.isPositiveInteger(conditionDto.getComplexNum())) {
                int complexNum=Integer.parseInt(conditionDto.getComplexNum());
                for (int i=0 ; i<complexNum ;i++){
                    LicenceFeeDto specFeeDto = new LicenceFeeDto();
                    specFeeDto.setBundle(0);
                    specFeeDto.setBaseService(serviceCode);
                    specFeeDto.setServiceCode("S40");
                    specFeeDto.setServiceName("Complex Specified Service");
                    specFeeDto.setPremises(conditionDto.getMosdType()+"Address");
                    specFeeDto.setCharity(isCharity);
                    licenceFeeSpecDtos.add(specFeeDto);
                }

            }
            if (StringUtil.isNotEmpty(conditionDto.getSimpleNum())&& CommonValidator.isPositiveInteger(conditionDto.getSimpleNum())) {
                int simpleNum=Integer.parseInt(conditionDto.getSimpleNum());
                for (int i=0 ; i<simpleNum ;i++){
                    LicenceFeeDto specFeeDto = new LicenceFeeDto();
                    specFeeDto.setBundle(0);
                    specFeeDto.setBaseService(serviceCode);
                    specFeeDto.setServiceCode("S60");
                    specFeeDto.setServiceName("Simple Specified Service");
                    specFeeDto.setPremises(conditionDto.getMosdType()+"Address");
                    specFeeDto.setCharity(isCharity);
                    licenceFeeSpecDtos.add(specFeeDto);
                }

            }
            if (IaisCommonUtils.isNotEmpty(licenceFeeSpecDtos)) {
                licenceFeeDto.setSpecifiedLicenceFeeDto(licenceFeeSpecDtos);
            }

            if (hadAch && (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(serviceCode)
                    || AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(serviceCode))) {
                achLicenceFeeDtoList.add(licenceFeeDto);
            } else {
                licenceFeeQuaryDtos.add(licenceFeeDto);
            }
            if (IaisCommonUtils.isNotEmpty(achLicenceFeeDtoList)) {
                for (LicenceFeeDto svcFee : licenceFeeQuaryDtos
                ) {
                    if (svcFee.getServiceCode().equals(AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL)) {
                        svcFee.setMosdBundlesLicenceFeeDto(achLicenceFeeDtoList);
                    }
                }
            }
        }

        return licenceFeeQuaryDtos;
    }

    private Map<String, String> validate(CalculateFeeConditionDto mainCalculateFeeConditionDto, List<CalculateFeeConditionDto> addConditionList) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        return errorMap;
    }

    public CalculateFeeConditionDto setCalculateFeeConditionDto(HttpServletRequest request) throws ParseException {
        CalculateFeeConditionDto filterDto=new CalculateFeeConditionDto();
        String applicationType=ParamUtil.getString(request,"applicationType");
        filterDto.setApplicationType(applicationType);
        String mosdType=ParamUtil.getString(request,"mosdType");
        filterDto.setMosdType(mosdType);
        String serviceName=ParamUtil.getString(request,"serviceName");
        filterDto.setServiceName(serviceName);
        String simpleNum=ParamUtil.getString(request,"simpleNum");
        filterDto.setSimpleNum(simpleNum);
        String complexNum=ParamUtil.getString(request,"complexNum");
        filterDto.setComplexNum(complexNum);
        String numVehicles=ParamUtil.getString(request,"numVehicles");
        filterDto.setNumVehicles(numVehicles);
        String numBeds=ParamUtil.getString(request,"numBeds");
        filterDto.setNumBeds(numBeds);
        String radioAlign=ParamUtil.getString(request,"radioAlign");
        filterDto.setRadioAlign(radioAlign);

        Date licenceDateFrom=Formatter.parseDate(ParamUtil.getString(request, "licenceDateFrom"));
        filterDto.setLicenceDateFrom(licenceDateFrom);
        Date licenceDateTo=Formatter.parseDate(ParamUtil.getString(request, "licenceDateTo"));
        filterDto.setLicenceDateTo(licenceDateTo);
        String radioCharitable=ParamUtil.getString(request,"radioCharitable");
        filterDto.setRadioCharitable(radioCharitable);
        Date licenceExpiryDate=Formatter.parseDate(ParamUtil.getString(request, "licenceExpiryDate"));
        filterDto.setLicenceExpiryDate(licenceExpiryDate);
        String radioRejected=ParamUtil.getString(request,"radioRejected");
        filterDto.setRadioRejected(radioRejected);
        String radioAmendment=ParamUtil.getString(request,"radioAmendment");
        filterDto.setRadioAmendment(radioAmendment);
        ParamUtil.setSessionAttr(request,"calculateFeeConditionDto",filterDto);
        return filterDto;
    }

    @GetMapping(value = "/fee-jsp-add-condition-html")
    public @ResponseBody
    String genNewRfiInfoHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the start ...."));
        String length = ParamUtil.getString(request,"Length");

        if(length==null){
            length="0";
        }
        String sql = SqlMap.INSTANCE.getSql("giroPayee", "fee-jsp-add-condition");

        sql = sql.replace("(serviceOption)", generateDropDownHtml(getLicSvcTypeOption(), "serviceName"+length,"serviceNameSel"+length, HcsaAppConst.FIRESTOPTION));
        sql=sql.replaceAll("indexSvc",length);

        return sql;
    }

    private String generateDropDownHtml(List<SelectOption> options, String name, String className, String firstOption) {
        Map<String, String> attrs = IaisCommonUtils.genNewHashMap();
        attrs.put("class", className);
        attrs.put("name", name);
        attrs.put("style", "display: none;");
        return ApplicationHelper.generateDropDownHtml(attrs, options, firstOption, null);
    }

    private List<SelectOption> getLicSvcTypeOption() {
        List<HcsaServiceDto>  specHcsaServiceDtos = hcsaConfigClient.baseHcsaService().getEntity();
        List<SelectOption> selectOptions= IaisCommonUtils.genNewArrayList();
        for (HcsaServiceDto svc:specHcsaServiceDtos
        ) {
            SelectOption selectOption=new SelectOption();
            selectOption.setText(svc.getSvcName());
            selectOption.setValue(svc.getSvcName());
            selectOptions.add(selectOption);
        }
        HashSet<SelectOption> set = new HashSet<>(selectOptions);
        selectOptions.clear();
        selectOptions.addAll(set);
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    private void setEasMtsBundleInfo(LicenceFeeDto licenceFeeDto, String serviceCode, int easMtsVehicleCount) {
        int matchingTh = configCommClient.getFeeMaxMatchingThByServiceCode(serviceCode).getEntity();

        if(licenceFeeDto.getBundle()<=2){
            if (easMtsVehicleCount  <= matchingTh) {
                licenceFeeDto.setBundle(1);
            } else {
                licenceFeeDto.setBundle(2);
            }
        }
    }
}
