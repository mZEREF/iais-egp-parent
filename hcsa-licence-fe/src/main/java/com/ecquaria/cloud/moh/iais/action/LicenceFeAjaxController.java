package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
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
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName: LicenceFeAjaxController
 * @author: huangkun
 * @date: 2022/12/19 9:13
 */
@Slf4j
@RestController
public class LicenceFeAjaxController {

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private LicenceViewService licenceViewService;

    @Autowired
    private AppSubmissionService appSubmissionService;

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
                List<ApplicationDto> applicationDtoList = appSubmissionService.getApplicationsByLicenseeId(licenseeId);
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
                List<LicenceDto> licenceDtoList = licenceViewService.getApproveLicenceDtoByLicenseeId(licenseeId);
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

    private SearchResult<AppAlignLicQueryDto> getBundleLicPremInfo(List<String> excludeChkBase, String licenseeId,
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
        log.debug(StringUtil.changeForLog("alignMinExpiryMonth::" + alignMinExpiryMonth));
        searchParam.addFilter("alignMinExpiryMonth", alignMinExpiryMonth,true);

        searchParam.addFilter("licenseeId",licenseeId,true);
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySvcName",searchParam);
        return licenceViewService.getBundleLicence(searchParam);
    }
}
