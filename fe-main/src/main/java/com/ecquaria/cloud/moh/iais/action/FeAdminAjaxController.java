package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppAlignAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-1-19 13:39
 **/
@Slf4j
@RestController
@RequestMapping("/feAdmin")
public class FeAdminAjaxController implements LoginAccessCheck {

    public static final String AUTO_BUNDLE = "autoBundle";

    @Autowired
    private OrgUserManageService orgUserManageService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private LicenceInboxClient licenceInboxClient;

    @Autowired
    private AssessmentGuideService assessmentGuideService;

    @RequestMapping(value = "active.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> active(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String targetStatus = request.getParameter("targetStatus");
        String res = orgUserManageService.ChangeActiveStatus(userId,targetStatus);
        Map<String, Object> map = new HashMap();
        if (res != null) {
            map.put("active", res);
            map.put("result", "Success");
            return map;
        }else{
            map.put("result", "Fail");
            return map;
        }

    }

    @GetMapping(value = "/sameAddressService")
    public Map<String,Object> getSameAddressService(HttpServletRequest request) {
        ParamUtil.clearSession(request,AUTO_BUNDLE);
        boolean noExistBaseLic = (boolean) ParamUtil.getSessionAttr(request, "noExistBaseLic");
        boolean noExistBaseApp = (boolean) ParamUtil.getSessionAttr(request, "noExistBaseApp");
        List<AppAlignLicQueryDto> bundleLic = (List<AppAlignLicQueryDto>) ParamUtil.getSessionAttr(request, "bundleLic");
        List<AppAlignAppQueryDto> bundleApp = (List<AppAlignAppQueryDto>) ParamUtil.getSessionAttr(request, "bundleApp");
        Map<String,Object> result = IaisCommonUtils.genNewHashMap();
        String number = ParamUtil.getString(request, "number");
        String serviceName = ParamUtil.getString(request, "serviceName");
        boolean isExistSame=false;
        result.put("serviceName1",serviceName);
        if (!noExistBaseLic){
            AppAlignLicQueryDto appAlignLicQueryDto = new AppAlignLicQueryDto();
            Optional<AppAlignLicQueryDto> alignLicQueryDto = bundleLic.stream().filter(item -> number.equals(item.getLicenceNo())).findAny();
            if (alignLicQueryDto.isPresent()){
                appAlignLicQueryDto = alignLicQueryDto.get();
            }
            String address = appAlignLicQueryDto.getAddress();
            List<AppAlignLicQueryDto> dtoList = bundleLic.stream().filter(item -> !serviceName.equals(item.getSvcName()))
                    .filter(item -> address.equals(item.getAddress()))
                    .collect(Collectors.toList());
            if (IaisCommonUtils.isNotEmpty(dtoList)){
                ParamUtil.setSessionAttr(request, AUTO_BUNDLE,dtoList.get(0));
                result.put("serviceName2", dtoList.get(0).getSvcName());
                isExistSame=true;
            }else {
                isExistSame=false;
            }
        }else if (!noExistBaseApp){
            AppAlignAppQueryDto appAlignAppQueryDto = bundleApp.stream().filter(item -> number.equals(item.getApplicationNo())).findAny().orElseGet(AppAlignAppQueryDto::new);
            String address = appAlignAppQueryDto.getAddress();
            List<AppAlignAppQueryDto> appAlignAppQueryDtoList = bundleApp.stream().filter(item -> !serviceName.equals(item.getSvcName()))
                    .filter(item -> address.equals(item.getAddress()))
                    .collect(Collectors.toList());
            if (IaisCommonUtils.isNotEmpty(appAlignAppQueryDtoList)){
                ParamUtil.setSessionAttr(request, AUTO_BUNDLE,appAlignAppQueryDtoList.get(0));
                result.put("serviceName2", appAlignAppQueryDtoList.get(0).getSvcName());
                isExistSame=true;
            }else {
                isExistSame=false;
            }
        }
        result.put("exist", isExistSame);
        return result;
    }

    @GetMapping(value = "/checkIsExistPendMs")
    public boolean checkIsExistPendMs(HttpServletRequest request) {
        List<String> svcNameList = ParamUtil.getListStrings(request, "svcNameList[]");
        boolean chooseMs=(svcNameList.contains(AppServicesConsts.SERVICE_NAME_MEDICAL_SERVICE)
                ||svcNameList.contains(AppServicesConsts.SERVICE_NAME_DENTAL_SERVICE))
                &&(!svcNameList.contains(AppServicesConsts.SERVICE_NAME_ACUTE_HOSPITAL));
        if (chooseMs){
            List<String> serviceNameList = svcNameList.stream().filter(item -> AppServicesConsts.SERVICE_NAME_MEDICAL_SERVICE.equals(item)|| AppServicesConsts.SERVICE_NAME_DENTAL_SERVICE.equals(item))
                    .collect(Collectors.toList());
            List<String> svcIdList = IaisCommonUtils.genNewArrayList();
            for (String s : serviceNameList) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(s);
                svcIdList.add(hcsaServiceDto.getId());
                svcIdList.add(hcsaServiceDto.getId());
            }
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            String licenseeId = "";
            if(loginContext!=null){
                licenseeId  = loginContext.getLicenseeId();
            }
            Set<String> premisesTypeList=IaisCommonUtils.genNewHashSet();
            premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_PERMANENT);
            premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_CONVEYANCE);
            premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_MOBILE);
            premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_REMOTE);
            int alignMinExpiryMonth = systemParamConfig.getAlignMinExpiryMonth();
            List<AppAlignLicQueryDto> bundleLic = getBundleLicPremInfo(svcIdList, licenseeId,premisesTypeList,alignMinExpiryMonth).getRows();
            if (IaisCommonUtils.isEmpty(bundleLic)||bundleLic.size()<=1){
                List<ApplicationDto> applicationDtoList = assessmentGuideService.getApplicationsByLicenseeId(licenseeId);
                if (IaisCommonUtils.isNotEmpty(applicationDtoList)){
                    boolean exist = applicationDtoList.stream().anyMatch(item -> {
                        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(item.getServiceId());
                        if (serviceNameList.contains(serviceDto.getSvcName())) {
                            return true;
                        }
                        return false;
                    });
                    if (exist){
                        return true;
                    }
                }
                List<LicenceDto> licenceDtoList = licenceInboxClient.getApproveLicenceDtoByLicenseeId(licenseeId).getEntity();
                if (IaisCommonUtils.isNotEmpty(licenceDtoList)){
                    boolean exist = licenceDtoList.stream().anyMatch(item -> serviceNameList.contains(item.getSvcName()));
                    if (exist){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private SearchResult<AppAlignLicQueryDto> getBundleLicPremInfo(List<String> excludeChkBase,String licenseeId,
                                                                   Set<String> premisesTypeList, int alignMinExpiryMonth){
        if(StringUtil.isEmpty(licenseeId)){
            return null;
        }
        if(IaisCommonUtils.isEmpty(excludeChkBase)){
            excludeChkBase = IaisCommonUtils.genNewArrayList();
        }
        SearchParam searchParam = new SearchParam(AppAlignLicQueryDto.class.getName());
        if(!IaisCommonUtils.isEmpty(excludeChkBase)){
            StringBuilder placeholder = new StringBuilder("(");
            int i =0;
            for(String baseSvcId:excludeChkBase){
                placeholder.append(":itemKey").append(i).append(',');
                i++;
            }
            String inSql = placeholder.substring(0, placeholder.length() - 1) + ")";
            searchParam.addParam("serName", inSql);
            i = 0;
            for(String baseSvcId:excludeChkBase){
                searchParam.addFilter("itemKey" + i,
                        HcsaServiceCacheHelper.getServiceById(baseSvcId).getSvcName());
                i ++;
            }
        }else{
            String serName = "('')";
            searchParam.addParam("serName", serName);
        }
        //add premType filter
        if(!IaisCommonUtils.isEmpty(premisesTypeList)){
            int i = 0;
            StringBuilder premTypeItem = new StringBuilder("(");
            for(String premisesType:premisesTypeList){
                premTypeItem.append(":premType").append(i).append(',');
                i++;
            }
            String premTypeItemStr = premTypeItem.substring(0, premTypeItem.length() - 1) + ")";
            searchParam.addParam("premTypeList", premTypeItemStr);
            i = 0;
            for(String premisesType:premisesTypeList){
                searchParam.addFilter("premType" + i, premisesType);
                i ++;
            }
        }else{
            String premType = "('')";
            searchParam.addParam("premTypeList", premType);
            log.debug(StringUtil.changeForLog("No intersection data ..."));
        }
        log.debug(StringUtil.changeForLog("alignMinExpiryMonth:" + alignMinExpiryMonth));
        searchParam.addFilter("alignMinExpiryMonth", alignMinExpiryMonth,true);

        searchParam.addFilter("licenseeId",licenseeId,true);
        QueryHelp.setMainSql("interInboxQuery", "getLicenceBySvcName",searchParam);
        return licenceInboxClient.getBundleLicence(searchParam).getEntity();
    }
}