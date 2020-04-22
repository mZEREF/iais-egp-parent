package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author guyin
 * @date 2019/12/12 20:54
 */
@Slf4j
@Delegator("serviceMenuDelegator")
public class ServiceMenuDelegator {
    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";


    private static final String BASE_SERVICE_CHECK_BOX_ATTR = "basechk";
    private static final String SPECIFIED_SERVICE_CHECK_BOX_ATTR = "sepcifiedchk";
    private static final String BASE_SERVICE_ATTR = "baseService";
    private static final String BASE_SERVICE_ATTR_CHECKED = "baseServiceChecked";
    private static final String SPECIFIED_SERVICE_ATTR = "specifiedService";
    private static final String SPECIFIED_SERVICE_ATTR_CHECKED = "specifiedServiceChecked";
    private static final String VALIDATION_ATTR = "switch_action_type";

    private static final String CHOOSE_BASE = "chooseBase";
    private static final String ERROR_ATTR = "err";
    private static final String NEXT = "next";
    private static final String CHOOSE_LICENSE = "chooseLicense";

    List<HcsaServiceDto> allbaseService;
    List<HcsaServiceDto> allspecifiedService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    private ApplicationClient applicationClient;
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the  doStart start 1...."));
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR,null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, null);

        log.debug(StringUtil.changeForLog("the  doStart end 1...."));
    }

    public void beforeJump(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the  before jump start 1...."));
        List<String> baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "baseService");
        List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "specifiedService");
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(baseServiceIds)){
            serviceConfigIds.addAll(baseServiceIds);
        }
        if(!IaisCommonUtils.isEmpty(specifiedServiceIds)){
            serviceConfigIds.addAll(specifiedServiceIds);
        }

        List<HcsaServiceDto> hcsaServiceDtosById = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        List<String> serviceCodeList=new ArrayList<>(hcsaServiceDtosById.size());

        for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtosById){
            serviceCodeList.add(hcsaServiceDto.getSvcCode());
        }
        serviceCodeList.sort(String::compareTo);
        Map<String,Object> map=new HashMap<>();
        map.put("serviceCodesList",serviceCodeList);
        map.put("appType", ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        String entity = applicationClient.selectDarft(map).getEntity();
        bpc.request.getSession().setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO,entity);
    }

    public void serviceMenuSelection(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, null);

        log.debug(StringUtil.changeForLog("the do Start start 1...."));

        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }

        allbaseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());

        allspecifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());


        ParamUtil.setRequestAttr(bpc.request, BASE_SERVICE_ATTR, allbaseService);
        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR, allspecifiedService);

    }

    public void validation(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do validation start ...."));
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        String[] sepcifiedchk = ParamUtil.getStrings(bpc.request, SPECIFIED_SERVICE_CHECK_BOX_ATTR);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String err = "";
        String nextstep = "";
        List<String> basechkslist = IaisCommonUtils.genNewArrayList();
        List<String> sepcifiedchkslist = IaisCommonUtils.genNewArrayList();
        if(basechks == null){
            for (String item:sepcifiedchk) {
                sepcifiedchkslist.add(item);
            }
            //no base service
            if(sepcifiedchk != null){
                //spe choose base
                List<HcsaServiceDto> hcsaServiceDtosMap = getBaseInSpe(sepcifiedchkslist);
                if (hcsaServiceDtosMap.size() == 0){
                    nextstep = ERROR_ATTR;
                    err = "There is no base service in specified services.";
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                }else{
                    nextstep = CHOOSE_BASE;
                }
            }else{
                //no spe err
                nextstep = ERROR_ATTR;
                err = "Please select at least one service.";
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
            }
        }else{
            for (String item:basechks) {
                basechkslist.add(item);
            }
            //base
            Map<String ,String> specifiedName = new HashMap<>();
            Map<String ,String> baseName = new HashMap<>();

            for (HcsaServiceDto item:allbaseService
            ) {
                baseName.put(item.getId(),item.getSvcName());
            }
            for (HcsaServiceDto item:allspecifiedService
                 ) {
                specifiedName.put(item.getId(),item.getSvcName());
            }

            if(sepcifiedchk != null){
                for (String item:sepcifiedchk) {
                    sepcifiedchkslist.add(item);
                }
                //spe
                List<String> removeBaseList = new ArrayList<>();
                Map<String ,String> necessaryBaseServiceList = necessaryBase(basechkslist,sepcifiedchkslist);
                List<String> surplusbaselist = basechkslist.stream().filter(item -> !removeBaseList.contains(item)).collect(Collectors.toList());
               if(necessaryBaseServiceList.size() > 0){
                    //no match
                    nextstep = ERROR_ATTR;
                    if(surplusbaselist.size() == 0){
                        err = baseName.get(getKeyOrNull(necessaryBaseServiceList)) + " should be selected.";
                    }else{
                        err = "The chosen base service ";
                        err = err + baseName.get(surplusbaselist.get(0));
                        err = err + " is not the prerequisite of ";
                        err = err + specifiedName.get(getValueOrNull(necessaryBaseServiceList)) + ".";
                    }
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                }else{
                    //match
                    nextstep = NEXT;
                }
            }else{
                //new app
                nextstep = NEXT;
            }

        }
        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, nextstep);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedchkslist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basechkslist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) allbaseService);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) allspecifiedService);
    }

    public void chooseBase(BaseProcessClass bpc){
        //get correlatrion base service
        List<String> speString = (List<String>)ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED);
        List<HcsaServiceDto> hcsaServiceDtosMap = getBaseInSpe(speString);
        Map<String ,HcsaServiceDto> specifiedName = new HashMap<>();
        for (HcsaServiceDto item:allspecifiedService
        ) {
            specifiedName.put(item.getId(),item);
        }

        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) specifiedName);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) hcsaServiceDtosMap);
        ParamUtil.setRequestAttr(bpc.request, "baseInSpe", (Serializable) hcsaServiceDtosMap);
    }

    public void baseValidation(BaseProcessClass bpc){
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        if(basechks == null){
            //no choose all
            String err = "Base service should be selected.";
            ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, AppConsts.FALSE);
        }else{
            List<String> basechkslist = IaisCommonUtils.genNewArrayList();
            for (String item:basechks
            ) {
                basechkslist.add(item);
            }
            if(basechkslist.size() == 1){
                List<String> basechksNamelist = IaisCommonUtils.genNewArrayList();
                basechksNamelist = BaseIdToName(basechkslist);
                SearchResult searchResult = getLicense(basechksNamelist);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, AppConsts.TRUE);
                ParamUtil.setRequestAttr(bpc.request, "licence", searchResult);
                ParamUtil.setSessionAttr(bpc.request, "baseName", basechksNamelist.get(0));
            }else{
                String err = "Base service can choose one only.";
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, AppConsts.FALSE);
            }


            ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basechkslist);
        }

    }

    public void licenseValidation(BaseProcessClass bpc){
        String licenceJudge = ParamUtil.getString(bpc.request, "licenceJudge");
        if("1".equals(licenceJudge)){
            String[] licence = ParamUtil.getStrings(bpc.request, "licence");
            List<String> licenceList = IaisCommonUtils.genNewArrayList();
            for (String item:licence
                 ) {
                licenceList.add(item);
            }
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
            ParamUtil.setSessionAttr(bpc.request, "licence", (Serializable) licenceList);
        }else{
            ParamUtil.setSessionAttr(bpc.request, "licence",null);
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
        }
    }

    private List<String> BaseIdToName(List<String> baseId){
        List<String> baseName = IaisCommonUtils.genNewArrayList();
        for (HcsaServiceDto item:allbaseService
             ) {
            for (String id:baseId
                 ) {
                if(item.getId().equals(id)){
                    baseName.add(item.getSvcName());
                }
            }

        }
        return baseName;
    }

    public void doBeforStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doBeforStart start ...."));

        log.debug(StringUtil.changeForLog("the doBeforStart end ...."));
    }

    private SearchResult getLicense(List<String> baselist){
        SearchParam searchParamGroup;
        searchParamGroup = new SearchParam(MenuLicenceDto.class.getName());
        searchParamGroup.setPageSize(10);
        searchParamGroup.setPageNo(1);
        searchParamGroup.setSort("START_DATE", SearchParam.ASCENDING);
        StringBuilder sb = new StringBuilder("(");
        int i =0;
        for (String item: baselist) {
            sb.append(":itemKey" + i).append(",");
            i++;
        }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
        searchParamGroup.addParam("serName", inSql);
        i = 0;
        for (String item: baselist) {
            searchParamGroup.addFilter("itemKey" + i,
                    item);
            i ++;
        }
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySerName",searchParamGroup);
        SearchResult<MenuLicenceDto> searchResult = licenceViewService.getMenuLicence(searchParamGroup);
        return  searchResult;
    }

    private Map<String ,String> necessaryBase(List<String> basechkslist, List<String> sepcifiedchkslist){

        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
        Map<String ,String> necessaryBaseServiceList = new HashMap<>();
        for (String item: sepcifiedchkslist) {
            for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList) {
                if(dto.getSpecifiedSvcId().equals(item)){
                    necessaryBaseServiceList.put(dto.getBaseSvcId(),dto.getSpecifiedSvcId());
                }
            }
        }
        //remove chosed base service
        Iterator<String> iter = necessaryBaseServiceList.keySet().iterator();
        while(iter.hasNext()){
            String key = iter.next();
            for (String basechk:basechkslist) {
                if (basechk.equals(key)) {
                    iter.remove();
                }
            }
        }
        return necessaryBaseServiceList;

    }

    private List<HcsaServiceDto> getBaseInSpe(List<String> sepcifiedlist){
        Map<String ,HcsaServiceDto> baseIdMap = new HashMap<>();
        for (HcsaServiceDto item:allbaseService
        ) {
            baseIdMap.put(item.getId(),item);
        }

        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
        List<HcsaServiceDto> same = IaisCommonUtils.genNewArrayList();
        for(int i = 0; i < sepcifiedlist.size() ;i++){
            List<HcsaServiceDto> baseListInSpe = IaisCommonUtils.genNewArrayList();
            for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList) {
                if(dto.getSpecifiedSvcId().equals(sepcifiedlist.get(i))){
                    baseListInSpe.add(baseIdMap.get(dto.getBaseSvcId()));
                }
            }
            if(i == 0){
                same = baseListInSpe;
            }else{
                same.retainAll(baseListInSpe);
            }
        }

        return same;
    }

    private String getValueOrNull(Map<String, String> map) {
        String obj = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return  obj;
    }

    private String getKeyOrNull(Map<String, String> map) {
        String obj = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return  obj;
    }
}
