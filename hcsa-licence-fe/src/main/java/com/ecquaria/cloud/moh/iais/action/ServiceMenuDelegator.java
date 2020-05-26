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
    private static final String BACK_ATTR = "back";
    private static final String NEXT = "next";

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
        ParamUtil.setSessionAttr(bpc.request, "licence", null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, null);

        log.debug(StringUtil.changeForLog("the  doStart end 1...."));
    }

    public void beforeJump(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the  before jump start 1...."));
    }

    public void serviceMenuSelection(BaseProcessClass bpc){

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
        String err = "";
        String nextstep = "";
        List<String> basecheckedlist = IaisCommonUtils.genNewArrayList();
        List<String> sepcifiedcheckedlist = IaisCommonUtils.genNewArrayList();
        if(basechks == null){
            //no base service
            if(sepcifiedchk != null){
                //spe choose base
                for (String item:sepcifiedchk) {
                    sepcifiedcheckedlist.add(item);
                }
                List<HcsaServiceDto> hcsaServiceDtosMap = getBaseInSpe(sepcifiedcheckedlist);
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
                basecheckedlist.add(item);
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
                    sepcifiedcheckedlist.add(item);
                }
                //spe
                Map<String ,String> necessaryBaseServiceList = necessaryBase(basecheckedlist,sepcifiedcheckedlist);
                List<String> extrabaselist = IaisCommonUtils.genNewArrayList();
                extrabaselist.addAll(basecheckedlist);
                List<HcsaServiceDto> hcsaServiceDtosMap = getBaseInSpe(sepcifiedcheckedlist);
                List<String> removeNecessary = IaisCommonUtils.genNewArrayList();
                for (String extra: extrabaselist
                ) {
                    for (HcsaServiceDto hc:hcsaServiceDtosMap
                    ) {
                        if(extra.equals(hc.getId())){
                            removeNecessary.add(extra);
                        }
                    }
                }
                extrabaselist.removeAll(removeNecessary);
                if(necessaryBaseServiceList.size() > 0){
                    //no match
                    nextstep = ERROR_ATTR;
                    if(extrabaselist.size() == 0){
                        err = baseName.get(getKeyOrNull(necessaryBaseServiceList)) + " should be selected.";
                    }else{
                        for(Map.Entry<String, String> entry : necessaryBaseServiceList.entrySet()){
                            err = "The chosen base service ";
                            err = err + baseName.get(extrabaselist.get(0));
                            err = err + " is not the prerequisite of ";
                            err = err + specifiedName.get(entry.getValue()) + ".";
                            break;
                        }
                    }
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                }else{
                    if (hcsaServiceDtosMap.size() == 0){
                        nextstep = ERROR_ATTR;
                        err = "There is no base service in specified services.";
                        ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    }else{
                        nextstep = CHOOSE_BASE;
                    }
                }
            }else{
                //new app
                nextstep = NEXT;
            }

        }

        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedcheckedlist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basecheckedlist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) allbaseService);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) allspecifiedService);
        if(NEXT.equals(nextstep)){
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String crud_action_additional  =bpc.request.getParameter("crud_action_additional");
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(NewApplicationDelegator.SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                 bpc.request.getSession().setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, crud_action_additional);
                 bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", crud_action_additional);
            }else if(attribute!=null){
                 nextstep=ERROR_ATTR;
             }
        }
        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, nextstep);
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
        String action = ParamUtil.getString(bpc.request, "action");
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        if(BACK_ATTR.equals(action)){
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, BACK_ATTR);
        }else{
            if(basechks == null){
                //no choose all
                String err = "Base service should be selected.";
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
            }else{
                List<String> basechkslist = IaisCommonUtils.genNewArrayList();
                for (String item:basechks
                ) {
                    basechkslist.add(item);
                }
                if(basechkslist.size() == 1){
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR,NEXT);
                }else{
                    String err = "Base service can choose one only.";
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR,ERROR_ATTR);
                }
                ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basechkslist);
            }
        }

    }


    public void chooseLicence(BaseProcessClass bpc){
        List<String> basechkslist = (List<String>)ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED);
        List<String> basechksNamelist = IaisCommonUtils.genNewArrayList();
        basechksNamelist = BaseIdToName(basechkslist);
        SearchResult searchResult = getLicense(bpc,basechksNamelist);
        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, "licence");
        ParamUtil.setRequestAttr(bpc.request, "licence", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "baseName", basechksNamelist.get(0));
    }

    public void licenseValidation(BaseProcessClass bpc){
        String licenceJudgeD=(String)bpc.request.getSession().getAttribute("licenceJudge");
        List<String> licenceD=(List<String>)bpc.request.getSession().getAttribute("licence");
        String action = ParamUtil.getString(bpc.request, "action");
        String licenceJudge = ParamUtil.getString(bpc.request, "licenceJudge");
        String[] licence = ParamUtil.getStrings(bpc.request, "licence");
        if(!StringUtil.isEmpty(licenceD)){
            licence=new String[licenceD.size()];
            for(int i=0;i<licenceD.size();i++){
                licence[i]=licenceD.get(i);
            }
        }
        if(licenceJudgeD!=null){
            licenceJudge=licenceJudgeD;
        }
        if(BACK_ATTR.equals(action)){
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, BACK_ATTR);
        }else {
            if(licenceJudge == null){
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                String err = "Please select at least one licence.";
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                ParamUtil.setRequestAttr(bpc.request, "action","err");
            }else if("1".equals(licenceJudge)) {
                if(licence == null){
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                    String err = "Please select at least one licence.";
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    ParamUtil.setRequestAttr(bpc.request, "action","err");
                }else{
                    List<String> licenceList = IaisCommonUtils.genNewArrayList();
                    for (String item : licence
                    ) {
                        licenceList.add(item);
                    }
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
                    ParamUtil.setSessionAttr(bpc.request, "licence", (Serializable) licenceList);
                }

            } else {
                ParamUtil.setSessionAttr(bpc.request, "licence", null);
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
            }
        }
        String  step =(String) bpc.request.getAttribute(VALIDATION_ATTR);
        if(NEXT.equals(step)){
            ParamUtil.setSessionAttr(bpc.request,"licenceJudge",licenceJudge);
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String crud_action_additional  =bpc.request.getParameter("crud_action_additional");
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(NewApplicationDelegator.SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, crud_action_additional);
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", crud_action_additional);
            }else if(attribute!=null){
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                return;
            }
        }

        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, step);
    }

    private void getDraft(BaseProcessClass bpc){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId;
        if(loginContext!=null){
             licenseeId = loginContext.getLicenseeId();
        }else {
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }

        List<String> licenceList =(List<String>) ParamUtil.getSessionAttr(bpc.request, "licence");
        List<String> baseServiceIds=IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(licenceList)){
            baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED);
        }
        List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED);
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(baseServiceIds)){
            serviceConfigIds.addAll(baseServiceIds);
        }
        if(!IaisCommonUtils.isEmpty(specifiedServiceIds)){
            serviceConfigIds.addAll(specifiedServiceIds);
        }
        if(!serviceConfigIds.isEmpty()) {
            List<HcsaServiceDto> hcsaServiceDtosById = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
            List<String> serviceCodeList = new ArrayList<>(hcsaServiceDtosById.size());
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtosById) {
                serviceCodeList.add(hcsaServiceDto.getSvcCode());
            }
            serviceCodeList.sort(String::compareTo);
            Map<String, Object> map = new HashMap<>();
            map.put("serviceCodesList", serviceCodeList);
            map.put("appType", ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            map.put("licenseeId",licenseeId);
            String entity = applicationClient.selectDarft(map).getEntity();
            bpc.request.setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, entity);
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

    private SearchResult getLicense(BaseProcessClass bpc,List<String> baselist){
        SearchParam searchParamGroup;
        searchParamGroup = new SearchParam(MenuLicenceDto.class.getName());
        searchParamGroup.setPageSize(200);
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
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        searchParamGroup.addFilter("licenseeId",loginContext.getLicenseeId(),true);
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySerName",searchParamGroup);
        SearchResult<MenuLicenceDto> searchResult = licenceViewService.getMenuLicence(searchParamGroup);
        return  searchResult;
    }

    private Map<String ,String> necessaryBase(List<String> basechkslist, List<String> sepcifiedchkslist){
        List<String> chkslistcopy = IaisCommonUtils.genNewArrayList();
        chkslistcopy.addAll(basechkslist);
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
            for (String basechk:chkslistcopy) {
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
