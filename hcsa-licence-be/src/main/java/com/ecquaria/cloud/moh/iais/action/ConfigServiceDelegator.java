package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2020/2/11 16:19
 */
@Delegator("congfigSeriviceDelegator")
@Log4j
public class ConfigServiceDelegator {
    @Autowired
    private ConfigService configService;
    @Autowired
    private OrganizationClient organizationClient;

    private static Map<String, Integer> SEQ_MAP = new HashMap<>();
    private static Map<String, String> NAME_MAP = new HashMap<>();

    static {
        SEQ_MAP.put(HcsaConsts.STEP_BUSINESS_NAME, 1);
        SEQ_MAP.put(HcsaConsts.STEP_VEHICLES, 2);
        SEQ_MAP.put(HcsaConsts.STEP_CLINICAL_DIRECTOR, 3);
        SEQ_MAP.put(HcsaConsts.STEP_LABORATORY_DISCIPLINES, 4);
        SEQ_MAP.put(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS, 5);
        SEQ_MAP.put(HcsaConsts.STEP_SECTION_LEADER, 6);
        SEQ_MAP.put(HcsaConsts.STEP_DISCIPLINE_ALLOCATION, 7);
        SEQ_MAP.put(HcsaConsts.STEP_CHARGES, 8);
        SEQ_MAP.put(HcsaConsts.STEP_SERVICE_PERSONNEL, 9);
        SEQ_MAP.put(HcsaConsts.STEP_PRINCIPAL_OFFICERS, 10);
        SEQ_MAP.put(HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER, 11);
        SEQ_MAP.put(HcsaConsts.STEP_MEDALERT_PERSON, 12);
        SEQ_MAP.put(HcsaConsts.STEP_DOCUMENTS, 13);
        SEQ_MAP = Collections.unmodifiableMap(SEQ_MAP);

        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, "principalOfficer");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, "DeputyPrincipalOfficer");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "ClinicalGovernanceOfficer");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, "ServicePersonnel");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, "MedalertPerson");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, "clinical_director");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_VEHICLES, "vehicles");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CHARGES, "charges");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CHARGES_OTHER, "other-charges");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, "SectionLeader");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_KAH, "KAH");
        NAME_MAP = Collections.unmodifiableMap(NAME_MAP);
    }

    public void start(BaseProcessClass bpc){
        log.info("*********start***********");
        removeSession(bpc);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_SERVICE_CONFIGURATOR);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        OrgUserDto entity = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        bpc.request.getSession().setAttribute("orgUserDto",entity);
    }
    private void removeSession(BaseProcessClass bpc){
        bpc.request.getSession().removeAttribute("hcsaServiceCategoryDtos");
        bpc.request.getSession().removeAttribute("categoryDtos");
        bpc.request.getSession().removeAttribute("maskHcsaServiceCategory");
    }
    public void switchOr(BaseProcessClass bpc){
    log.info("*********switchOr  start***********");

    log.info("*********switchOr  end***********");
    }
    public void prepare(BaseProcessClass bpc){
        log.info("*********prepare  start***********");

    }
    /*
    * list all service oder by service name (First ranking) , version (Second ranking)
    * */
    public void list(BaseProcessClass bpc){
        log.info("*********list  start***********");
        List<HcsaServiceDto> allHcsaServices = configService.getAllHcsaServices();
        bpc.request.setAttribute("hcsaServiceDtos", allHcsaServices);
    }

    /*
    * add new service
    *
    * */
    public void addNewService(BaseProcessClass bpc){
        log.info("*********addNewService  start***********");
        bpc.request.getSession().removeAttribute("routingStage");
        configService.addNewService(bpc.request);
        Object individualPremises = bpc.request.getAttribute("individualPremises");
        if(individualPremises==null){
            bpc.request.setAttribute("individualPremises","0");
        }
    }
    /*
    * update service to new version
    * */
    public void saveOrUpdate(BaseProcessClass bpc) throws Exception{
        log.info("*********saveOrUpdate  start***********");
        HcsaServiceConfigDto dateOfHcsaService = getDateOfHcsaService(bpc.request);
        configService.saveOrUpdate(bpc.request,bpc.response,dateOfHcsaService);
    }
    public void saveDate(BaseProcessClass bpc){
        log.info("*********saveDate  start***********");
        configService.saData(bpc.request);
    }
    public void editOrDelete(BaseProcessClass bpc){
        log.info("*********editOrDelete  start***********");

    }
    /*
    * edit or choose other version service to edit
    *
    * */
    public void edit(BaseProcessClass bpc){
        log.info("*********edit  start***********");
        configService.viewPageInfo(bpc.request);
    }

    public void editOrSave(BaseProcessClass bpc){
        log.info("*********editOrSave  start***********");
        String crud_action_value = bpc.request.getParameter("crud_action_type");
        if("save".equals(crud_action_value)){
            bpc.request.setAttribute("crud_action_type","save");
        }
        if("edit".equals(crud_action_value)){
            bpc.request.setAttribute("crud_action_type","edit");
        }


    }

    /*
    * view page
    * */
    public void editView(BaseProcessClass bpc){
        log.info("*********editView  start***********");

        configService.viewPageInfo(bpc.request);
    }
    /*
    * delete service if service never used
    * */
    public void delete(BaseProcessClass bpc){

        log.info("*********delete  start***********");

        configService.delete(bpc.request);

    }

    public  void selectVersionAsNewTem(BaseProcessClass bpc){

        log.info("*********selectVersionAsNewTem  start***********");

    }
    public void deleteOrCancel(BaseProcessClass bpc){

        log.info("*********deleteOrCancel  start***********");
        configService.deleteOrCancel(bpc.request,bpc.response);

    }

    public void update(BaseProcessClass bpc) throws  Exception{

        log.info("*********update  start***********");
        HcsaServiceConfigDto dateOfHcsaService = getDateOfHcsaService(bpc.request);
        configService.update(bpc.request,bpc.response,dateOfHcsaService);

    }
    /*
    * get page all data
    * -----------------------
    * service name ,service code ...
    * --------------------------
    * ( NEW APPLICATION ) ; ( REQUEST FOR CHANGE ) ...BUTTON
    * these info in session
     *
    * */
    private HcsaServiceConfigDto getDateOfHcsaService(HttpServletRequest request)  {
        HcsaServiceConfigDto hcsaServiceConfigDto = new HcsaServiceConfigDto();
        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = configService.getHcsaSvcRoutingStageDtos();
        //if service type is sub must to chose
        String[] subsumption = request.getParameterValues("Subsumption");
        String selectSubsumption = ParamUtil.getStringsToString(request, "Subsumption");
        // if service type is pre must be Choice
        String[] preRequisite = request.getParameterValues("Pre-requisite");
        String selectPreRequisite = ParamUtil.getStringsToString(request, "Pre-requisite");
        request.setAttribute("selectSubsumption",selectSubsumption);
        request.setAttribute("selectPreRequisite",selectPreRequisite);
        String serviceId = request.getParameter("serviceId");
        String selectCategoryId = request.getParameter("selectCategoryId");
        Map<String, String> maskHcsaServiceCategory = configService.getMaskHcsaServiceCategory();
        selectCategoryId= maskHcsaServiceCategory.get(selectCategoryId);
        String serviceName = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String displayDescription = request.getParameter("displayDescription");
        String serviceCode = request.getParameter("serviceCode");
        String selectAsNewVersion = request.getParameter("selectAsNewVersion");
        String serviceType = request.getParameter("ServiceType");
        String maxVersionEffectiveDate = request.getParameter("maxVersionEffectiveDate");
        String maxVersionEndDate = request.getParameter("maxVersionEndDate");
        String serviceIsUse = request.getParameter("serviceIsUse");
        String[] premisesTypes = request.getParameterValues("PremisesType");
        String version = request.getParameter("version");
        String[] subTypes = request.getParameterValues("subType");
        String[] levels = request.getParameterValues("level");
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= IaisCommonUtils.genNewArrayList();
        Map<Integer ,String> level1=IaisCommonUtils.genNewHashMap();
        if(levels!=null){
            for(int i=0;i<levels.length;i++ ){
                level1.put(i,levels[i]);
            }
            for(int i=0;i<levels.length;i++){
                String s = level1.get(i);
                if("0".equals(s)){
                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto1=new HcsaSvcSubtypeOrSubsumedDto();
                    hcsaSvcSubtypeOrSubsumedDto1.setName(subTypes[i]);
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos2=IaisCommonUtils.genNewArrayList();
                    hcsaSvcSubtypeOrSubsumedDtos.add(hcsaSvcSubtypeOrSubsumedDto1);
                    for(int j=i+1;j<levels.length;j++){
                        String s1 = level1.get(j);
                        if("1".equals(s1)){
                            HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto2=new HcsaSvcSubtypeOrSubsumedDto();
                            hcsaSvcSubtypeOrSubsumedDto2.setName(subTypes[j]);
                            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos3=IaisCommonUtils.genNewArrayList();
                            hcsaSvcSubtypeOrSubsumedDtos2.add(hcsaSvcSubtypeOrSubsumedDto2);
                            hcsaSvcSubtypeOrSubsumedDto1.setList(hcsaSvcSubtypeOrSubsumedDtos2);
                            for(int k=j+1;k<levels.length;k++){
                                String s2 = level1.get(k);
                                if("2".equals(s2)){
                                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto3=new HcsaSvcSubtypeOrSubsumedDto();
                                    hcsaSvcSubtypeOrSubsumedDto3.setName(subTypes[k]);
                                    hcsaSvcSubtypeOrSubsumedDtos3.add(hcsaSvcSubtypeOrSubsumedDto3);
                                    hcsaSvcSubtypeOrSubsumedDto2.setList(hcsaSvcSubtypeOrSubsumedDtos3);
                                }else if(!"2".equals(s2)){

                                    break;
                                }
                            }
                        }else if(!"1".equals(s1)){
                            break;
                        }
                    }
                }
            }
        }
        hcsaServiceConfigDto.setHcsaSvcSubtypeOrSubsumedDtos(hcsaSvcSubtypeOrSubsumedDtos);

        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceSubTypeDto> list=IaisCommonUtils.genNewArrayList();
        if(ApplicationConsts.SERVICE_TYPE_SUBSUMED.equals(serviceType)){
            if(subsumption!=null){
                for(String str:subsumption){
                    if(!"".equals(str)){
                        HcsaServiceSubTypeDto hcsaServiceSubTypeDto=new HcsaServiceSubTypeDto();
                        hcsaServiceSubTypeDto.setServiceId(str);
                        list.add(hcsaServiceSubTypeDto);
                    }

                }
            }

        } else if (preRequisite != null && ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)) {
            for(String str : preRequisite){
                if(!"".equals(str)){
                    HcsaServiceSubTypeDto hcsaServiceSubTypeDto=new HcsaServiceSubTypeDto();
                    hcsaServiceSubTypeDto.setServiceId(str);
                    list.add(hcsaServiceSubTypeDto);
                }
            }
        }
        hcsaServiceDto.setServiceSubTypeDtos(list);
        if (premisesTypes != null) {
            for (String str : premisesTypes) {
                HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto = new HcsaSvcSpePremisesTypeDto();
                hcsaSvcSpePremisesTypeDto.setPremisesType(str);
                hcsaSvcSpePremisesTypeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaSvcSpePremisesTypeDtos.add(hcsaSvcSpePremisesTypeDto);
            }
        }
        addSvcStepConfigsFromPage(hcsaServiceConfigDto, request);

        addDocumentConfigsFromPage(hcsaServiceConfigDto, request);

        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtoList = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
        List<String> type = configService.getType();
        List<HcsaSvcCateWrkgrpCorrelationDto> hcsaSvcCateWrkgrpCorrelationDto = configService.getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(selectCategoryId);
        Map<String ,HcsaSvcCateWrkgrpCorrelationDto> hashMap= Maps.newHashMapWithExpectedSize(hcsaSvcCateWrkgrpCorrelationDto.size());
        for(HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto : hcsaSvcCateWrkgrpCorrelationDto){
            hashMap.put(svcCateWrkgrpCorrelationDto.getStageId()+svcCateWrkgrpCorrelationDto.getSubOrder(),svcCateWrkgrpCorrelationDto);
        }
        Map<String,List<HcsaSvcSpecificStageWorkloadDto>> hcsaSvcSpecificStageWorkloadDtoMap=IaisCommonUtils.genNewHashMap();
        Map<String,List<HcsaSvcStageWorkingGroupDto>> hcsaSvcStageWorkingGroupDtoMap=IaisCommonUtils.genNewHashMap();
        Map<String, List<HcsaSvcSpeRoutingSchemeDto>> hcsaSvcSpeRoutingSchemeDtoMap=IaisCommonUtils.genNewHashMap();
        Map<String,List<HcsaSvcSpeRoutingSchemeDto>> newHashMap=IaisCommonUtils.genNewHashMap();
        for(String every:type){
            List<HcsaSvcSpecificStageWorkloadDto> workloadDtos=IaisCommonUtils.genNewArrayList();
            List<HcsaSvcStageWorkingGroupDto> workingGroupDtos=IaisCommonUtils.genNewArrayList();
            List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos=IaisCommonUtils.genNewArrayList();
            List<HcsaSvcSpeRoutingSchemeDto> svcSpeRoutingSchemeDtoList=new ArrayList<>(2);
            boolean flag=false;
            if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(every) ||
                    ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(every)){
                flag=true;
            }
            for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
                HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
                HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto = new HcsaSvcSpecificStageWorkloadDto();
                HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto = new HcsaSvcSpeRoutingSchemeDto();
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto1=new HcsaSvcSpeRoutingSchemeDto();
                HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto2=new HcsaSvcSpeRoutingSchemeDto();
                String stageCode = hcsaSvcRoutingStageDto.getStageCode();
                String id = hcsaSvcRoutingStageDto.getId();
                String routingScheme = request.getParameter("RoutingScheme" + stageCode+every);
                String workloadManhours = request.getParameter("WorkloadManhours" + stageCode+every);
                String workloadId = request.getParameter("workloadId" + stageCode+every);
                String stageId = request.getParameter("stageId" + stageCode+every);
                String workstageId = request.getParameter("workstageId" + stageCode+every);
                String isMandatory=  request.getParameter("isMandatory"+ stageCode+every);
                //todo can approve ao1 ao2
                String canApprove = request.getParameter("canApprove" + stageCode + every);
                //for audi ins and post ins
                if(flag&&"INS".equals(stageCode)){
                    routingScheme="round";
                    workloadManhours="1";
                    isMandatory="mandatory";
                }else if(flag&&"AO3".equals(stageCode)){
                    routingScheme="round";
                    workloadManhours="1";
                    isMandatory="mandatory";
                }else if(flag&&"AO2".equals(stageCode)){
                    routingScheme="round";
                    workloadManhours="1";
                    isMandatory="mandatory";
                }else if(flag&&!"AO2".equals(stageCode)&&!"INS".equals(stageCode)&&!"AO3".equals(stageCode)){
                    isMandatory="optional";
                }
                if (!StringUtil.isEmpty(workloadManhours)) {
                    hcsaSvcSpecificStageWorkloadDto.setStringManhourCount(workloadManhours);
                    hcsaConfigPageDto.setManhours(workloadManhours);
                    if(workloadManhours.matches("^[0-9]+$")){
                        hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.valueOf(workloadManhours));
                    }
                }
                hcsaConfigPageDto.setCanApprove(canApprove);
                hcsaSvcSpecificStageWorkloadDto.setCanApprove(canApprove);
                if(canApprove==null){
                    hcsaSvcSpecificStageWorkloadDto.setCanApprove(String.valueOf(0));
                }
                hcsaConfigPageDto.setWorkloadId(workloadId);
                if (!StringUtil.isEmpty(stageId)) {
                    //todo delete
                    /*   hcsaSvcSpeRoutingSchemeDto.setId(stageId);*/
                    hcsaConfigPageDto.setRoutingSchemeId(stageId);
                }
                hcsaSvcSpeRoutingSchemeDto.setStageId(id);
                hcsaSvcStageWorkingGroupDto.setOrder(1);
                HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto = hashMap.get(id+1);
                if(svcCateWrkgrpCorrelationDto!=null){
                    hcsaSvcStageWorkingGroupDto.setStageWorkGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                    hcsaConfigPageDto.setWorkingGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                }
                hcsaSvcStageWorkingGroupDto.setStageId(id);

                if(!StringUtil.isEmpty(workstageId)){
                    /*hcsaSvcStageWorkingGroupDto.setId(workstageId);*/
                    //todo delete
                    /*  hcsaSvcSpecificStageWorkloadDto.setId(workloadId);*/
                }
                if("INS".equals(stageCode)){
                    String parameter0 = request.getParameter("RoutingScheme" + stageCode + every + "0");
                    String parameter1 = request.getParameter("RoutingScheme" + stageCode + every + "1");
                    hcsaSvcSpeRoutingSchemeDto1.setSchemeType(parameter0);
                    hcsaSvcSpeRoutingSchemeDto2.setSchemeType(parameter1);
                    hcsaSvcSpeRoutingSchemeDto1.setInsOder(String.valueOf(0));
                    hcsaSvcSpeRoutingSchemeDto2.setInsOder(String.valueOf(1));
                }
                if ("optional".equals(isMandatory)||ApplicationConsts.SERVICE_TYPE_SUBSUMED.equals(serviceType)) {
                    hcsaConfigPageDto.setIsMandatory(String.valueOf(false));
                    hcsaSvcSpecificStageWorkloadDto.setIsMandatory(String.valueOf(false));
                    hcsaSvcStageWorkingGroupDto.setIsMandatory(String.valueOf(false));
                    hcsaSvcSpeRoutingSchemeDto.setIsMandatory(String.valueOf(false));
                    workingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDto.setStageId(id);
                    hcsaSvcSpeRoutingSchemeDto.setSchemeType(routingScheme);
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    hcsaSvcSpecificStageWorkloadDto.setStringManhourCount(workloadManhours);
                    hcsaSvcSpecificStageWorkloadDto.setStageId(id);
                    workloadDtos.add(hcsaSvcSpecificStageWorkloadDto);
                    if("INS".equals(stageCode)){
                        hcsaSvcSpeRoutingSchemeDto1.setIsMandatory(String.valueOf(false));
                        hcsaSvcSpeRoutingSchemeDto2.setIsMandatory(String.valueOf(false));
                        svcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto1);
                        svcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto2);
                        hcsaConfigPageDto.setHcsaSvcSpeRoutingSchemeDtos(svcSpeRoutingSchemeDtoList);
                    }
                }else if("mandatory".equals(isMandatory)){
                    hcsaSvcStageWorkingGroupDto.setIsMandatory(String.valueOf(true));
                    workingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDto.setSchemeType(routingScheme);
                    hcsaSvcSpeRoutingSchemeDto.setAppType(every);
                    hcsaSvcSpeRoutingSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcSpeRoutingSchemeDto.setIsMandatory(String.valueOf(true));
                    hcsaSvcSpecificStageWorkloadDto.setStageId(id);
                    hcsaSvcSpecificStageWorkloadDto.setAppType(every);
                    hcsaSvcSpecificStageWorkloadDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcSpecificStageWorkloadDto.setIsMandatory(String.valueOf(true).intern());
                    workloadDtos.add(hcsaSvcSpecificStageWorkloadDto);
                    hcsaSvcSpecificStageWorkloadDtoList.add(hcsaSvcSpecificStageWorkloadDto);
                    hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto);
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    if("INS".equals(stageCode)){
                        hcsaSvcSpeRoutingSchemeDto1.setStageId(id);
                        hcsaSvcSpeRoutingSchemeDto1.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        hcsaSvcSpeRoutingSchemeDto1.setAppType(every);
                        hcsaSvcSpeRoutingSchemeDto1.setIsMandatory(String.valueOf(true));
                        hcsaSvcSpeRoutingSchemeDto2.setIsMandatory(String.valueOf(true));
                        hcsaSvcSpeRoutingSchemeDto2.setStageId(id);
                        if(flag){
                            hcsaSvcSpeRoutingSchemeDto1.setSchemeType(routingScheme);
                            hcsaSvcSpeRoutingSchemeDto2.setSchemeType(routingScheme);
                        }
                        hcsaSvcSpeRoutingSchemeDto2.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        hcsaSvcSpeRoutingSchemeDto2.setAppType(every);
                        HcsaSvcStageWorkingGroupDto order3=new HcsaSvcStageWorkingGroupDto();
                        HcsaSvcStageWorkingGroupDto order2=new HcsaSvcStageWorkingGroupDto();
                        order2.setOrder(2);
                        order2.setStageId(id);
                        //toto change
                        HcsaSvcCateWrkgrpCorrelationDto svcCateWrkgrpCorrelationDto1 = hashMap.get(id + 2);
                        if(svcCateWrkgrpCorrelationDto1!=null){
                            order2.setStageWorkGroupId(svcCateWrkgrpCorrelationDto1.getWrkGrpId());
                        }
                        if(svcCateWrkgrpCorrelationDto!=null){
                            order3.setStageWorkGroupId(svcCateWrkgrpCorrelationDto.getWrkGrpId());
                        }
                        order3.setOrder(3);
                        order3.setStageId(id);
                        hcsaSvcStageWorkingGroupDtos.add(order2);
                        hcsaSvcStageWorkingGroupDtos.add(order3);
                        hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto1);
                        hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto2);
                        svcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto1);
                        svcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto2);
                        hcsaConfigPageDto.setHcsaSvcSpeRoutingSchemeDtos(svcSpeRoutingSchemeDtoList);
                    }
                    hcsaConfigPageDto.setIsMandatory(String.valueOf(true));
                }else if("".equals(isMandatory)) {
                    workingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    workloadDtos.add(hcsaSvcSpecificStageWorkloadDto);
                    if("INS".equals(stageCode)){
                        svcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto1);
                        svcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto2);
                        hcsaConfigPageDto.setHcsaSvcSpeRoutingSchemeDtos(svcSpeRoutingSchemeDtoList);
                    }

                    hcsaSvcStageWorkingGroupDto.setIsMandatory("");
                    hcsaSvcSpeRoutingSchemeDto.setIsMandatory("");
                    hcsaSvcSpecificStageWorkloadDto.setIsMandatory("");
                }
                hcsaConfigPageDto.setWorkStageId(workstageId);
                hcsaConfigPageDto.setRoutingSchemeName(routingScheme);
                hcsaConfigPageDto.setAppType(every);
                hcsaConfigPageDtos.add(hcsaConfigPageDto);
            }
            hcsaSvcSpecificStageWorkloadDtoMap.put(every,workloadDtos);
            hcsaSvcSpeRoutingSchemeDtoMap.put(every,hcsaSvcSpeRoutingSchemeDtos);
            hcsaSvcStageWorkingGroupDtoMap.put(every,workingGroupDtos);
            newHashMap.put(every,svcSpeRoutingSchemeDtoList);
        }

        String startDate = request.getParameter("StartDate");
        String endDate = request.getParameter("EndDate");

        //todo delete
        if (!StringUtil.isEmpty(serviceId)) {
            hcsaServiceDto.setId(serviceId);
        }
        if(!StringUtil.isEmpty(selectCategoryId)){
            hcsaServiceDto.setCategoryId(selectCategoryId);
        }

        hcsaServiceDto.setSvcName(serviceName);
        hcsaServiceDto.setSvcCode(serviceCode);
        hcsaServiceDto.setMaxVersionEffectiveDate(maxVersionEffectiveDate);
        hcsaServiceDto.setSelectAsNewVersion(String.valueOf(true).equals(selectAsNewVersion));
        if(!StringUtil.isEmpty(maxVersionEndDate)){
            try {
                Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(maxVersionEndDate);
                hcsaServiceDto.setMaxVersionEndDate(parse);
            }catch (Exception e){

            }
        }
        try {
            Date parse = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(startDate);
            String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(parse);
            hcsaServiceDto.setEffectiveDate(format);
            if(parse.before(new Date())){
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            }else {
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            }

        } catch (Exception e) {
          /*  Date parse = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            hcsaServiceDto.setEffectiveDate(startDate);
            if(parse.after(new Date())){
                hcsaServiceDto.setStatus("CMSTAT003");
            }else {
                hcsaServiceDto.setStatus("CMSTAT001");
            }*/
        }


        if (!StringUtil.isEmpty(endDate)) {
            try {
                hcsaServiceDto.setEndDate(new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(endDate));
            } catch (ParseException e) {
                hcsaServiceDto.setEndDate(new Date(99,1,1));
            }
        }

        hcsaServiceDto.setSvcDisplayDesc(displayDescription);
        hcsaServiceDto.setSvcDesc(description);
        hcsaServiceDto.setSvcType(serviceType);
        if (StringUtil.isEmpty(version)) {
            hcsaServiceDto.setVersion(String.valueOf(1));
        } else {
            hcsaServiceDto.setVersion(version);
        }
        hcsaServiceDto.setServiceIsUsed(String.valueOf(true).equals(serviceIsUse));
        hcsaServiceConfigDto.setHcsaSvcSpePremisesTypeDtos(hcsaSvcSpePremisesTypeDtos);
        hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
        hcsaServiceConfigDto.setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtoList);
        hcsaServiceConfigDto.setHcsaSvcSpecificStageWorkloadDtos(hcsaSvcSpecificStageWorkloadDtoList);
        hcsaServiceConfigDto.setHcsaSvcStageWorkingGroupDtos(hcsaSvcStageWorkingGroupDtos);
        request.setAttribute("hcsaConfigPageDtos", hcsaConfigPageDtos);
        request.setAttribute("insRoutingStage",newHashMap);
        hcsaServiceConfigDto.setHcsaSvcSpecificStageWorkloadDtoMap(hcsaSvcSpecificStageWorkloadDtoMap);
        hcsaServiceConfigDto.setHcsaSvcStageWorkingGroupDtoMap(hcsaSvcStageWorkingGroupDtoMap);
        hcsaServiceConfigDto.setHcsaSvcSpeRoutingSchemeDtoMap(hcsaSvcSpeRoutingSchemeDtoMap);
        return hcsaServiceConfigDto;
    }

    private void addDocumentConfigsFromPage(HcsaServiceConfigDto hcsaServiceConfigDto, HttpServletRequest request) {
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = hcsaServiceConfigDto.getHcsaServiceStepSchemeDtos();
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfig = IaisCommonUtils.genNewArrayList();
        String numberDocument = request.getParameter("NumberDocument");
        String[] descriptionServiceDocs = request.getParameterValues("descriptionServiceDoc");
        String[] parameterValues = request.getParameterValues("selectDocPerson");
        String[] serviceDocMandatories = request.getParameterValues("serviceDocMandatory");
        String[] serviceDocPremises = request.getParameterValues("serviceDocPremises");
        String numberfields = request.getParameter("Numberfields");

        request.setAttribute("serviceDocSize", numberDocument);
        if (descriptionServiceDocs != null) {
            for (int i = 0; i < descriptionServiceDocs.length; i++) {
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = new HcsaSvcDocConfigDto();
                hcsaSvcDocConfigDto.setDocDesc(descriptionServiceDocs[i]);
                hcsaSvcDocConfigDto.setDocTitle(descriptionServiceDocs[i]);
                hcsaSvcDocConfigDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaSvcDocConfigDto.setDispOrder(i);
                hcsaSvcDocConfigDto.setServiceId("");
                hcsaSvcDocConfigDto.setDupForPrem(String.valueOf(0));
                if (String.valueOf(0).equals(serviceDocMandatories[i])) {
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.FALSE);
                } else if (String.valueOf(1).equals(serviceDocMandatories[i])) {
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.TRUE);
                }
                if (!"".equals(parameterValues[i])) {
                    hcsaSvcDocConfigDto.setDupForPerson(parameterValues[i]);
                }
                if (String.valueOf(0).equals(serviceDocPremises[i])) {
                    hcsaSvcDocConfigDto.setDupForPrem(String.valueOf(0));
                } else if (String.valueOf(1).equals(serviceDocPremises[i])) {
                    hcsaSvcDocConfigDto.setDupForPrem(String.valueOf(1));
                }
                hcsaSvcDocConfigDtos.add(hcsaSvcDocConfigDto);
                hcsaSvcDocConfig.add(hcsaSvcDocConfigDto);
            }

            addStepSchemeDto(true, HcsaConsts.STEP_DOCUMENTS, HcsaConsts.DOCUMENTS, hcsaServiceStepSchemeDtos);
        }
        request.setAttribute("serviceDoc", hcsaSvcDocConfigDtos);
        request.setAttribute("comDocSize", numberfields);
        String[] descriptionCommDocs = request.getParameterValues("descriptionCommDoc");
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtoList = IaisCommonUtils.genNewArrayList();
        String[] commDocMandatory = request.getParameterValues("commDocMandatory");
        String[] commDocPremises = request.getParameterValues("commDocPremises");
        if (descriptionCommDocs != null) {
            for (int i = 0; i < descriptionCommDocs.length; i++) {
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = new HcsaSvcDocConfigDto();
                hcsaSvcDocConfigDto.setDocTitle(descriptionCommDocs[i]);
                hcsaSvcDocConfigDto.setDocDesc(descriptionCommDocs[i]);
                hcsaSvcDocConfigDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaSvcDocConfigDto.setDispOrder(i);
                if (String.valueOf(0).equals(commDocMandatory[i])) {
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.FALSE);
                } else if (String.valueOf(1).equals(commDocMandatory[i])) {
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.TRUE);
                }
                if (String.valueOf(0).equals(commDocPremises[i])) {
                    hcsaSvcDocConfigDto.setDupForPrem(String.valueOf(0));
                } else if (String.valueOf(1).equals(commDocPremises[i])) {
                    hcsaSvcDocConfigDto.setDupForPrem(String.valueOf(1));
                }
               /* if(!StringUtil.isEmpty(commDocIds[i])){
                    hcsaSvcDocConfigDto.setId(commDocIds[i]);
                }*/
                hcsaSvcDocConfig.add(hcsaSvcDocConfigDto);
                hcsaSvcDocConfigDtoList.add(hcsaSvcDocConfigDto);
            }
        }
        request.setAttribute("comDoc", hcsaSvcDocConfigDtoList);
        hcsaServiceConfigDto.setHcsaSvcDocConfigDtos(hcsaSvcDocConfig);
        hcsaServiceConfigDto.setComDocSize(numberDocument);
        hcsaServiceConfigDto.setServiceDocSize(numberfields);
    }

    private void addSvcStepConfigsFromPage(HcsaServiceConfigDto hcsaServiceConfigDto, HttpServletRequest request) {
        // personnel
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
        HcsaSvcPersonnelDto poDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, request);
        HcsaSvcPersonnelDto dpoDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, request);
        HcsaSvcPersonnelDto cgoDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, request);
        HcsaSvcPersonnelDto svcPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, request);
        HcsaSvcPersonnelDto mapPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, request);
        HcsaSvcPersonnelDto director = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, request);
        HcsaSvcPersonnelDto vehicles = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_VEHICLES, request);
        HcsaSvcPersonnelDto charges = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_CHARGES, request);
        HcsaSvcPersonnelDto otherCharges = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_CHARGES_OTHER, request);
        HcsaSvcPersonnelDto slPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, request);
        HcsaSvcPersonnelDto kahPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_KAH, request);
        hcsaSvcPersonnelDtos.add(poDto);
        hcsaSvcPersonnelDtos.add(dpoDto);
        hcsaSvcPersonnelDtos.add(cgoDto);
        hcsaSvcPersonnelDtos.add(svcPersonnelDto);
        hcsaSvcPersonnelDtos.add(mapPersonnelDto);//4
        hcsaSvcPersonnelDtos.add(director);//5
        hcsaSvcPersonnelDtos.add(vehicles);//6
        hcsaSvcPersonnelDtos.add(charges);//7
        hcsaSvcPersonnelDtos.add(otherCharges);//8
        hcsaSvcPersonnelDtos.add(slPersonnelDto);//9
        hcsaSvcPersonnelDtos.add(kahPersonnelDto);//10

        String businessName = request.getParameter("business-name");
        request.setAttribute("businessName", businessName);
        String pageName = request.getParameter("pageName");
        request.setAttribute("pageName", pageName);
        // step
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = hcsaServiceConfigDto.getHcsaSvcSubtypeOrSubsumedDtos();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = IaisCommonUtils.genNewArrayList();
        addStepSchemeDto(isNeed(vehicles), HcsaConsts.STEP_VEHICLES, HcsaConsts.VEHICLES, hcsaServiceStepSchemeDtos);
        addStepSchemeDto(!hcsaSvcSubtypeOrSubsumedDtos.isEmpty(), HcsaConsts.STEP_LABORATORY_DISCIPLINES,
                pageName, hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(director), HcsaConsts.STEP_CLINICAL_DIRECTOR, HcsaConsts.CLINICAL_DIRECTOR, hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(charges), HcsaConsts.STEP_CHARGES,
                HcsaConsts.CHARGES, hcsaServiceStepSchemeDtos);
        addStepSchemeDto(businessName != null && String.valueOf(1).equals(businessName), HcsaConsts.STEP_BUSINESS_NAME,
                HcsaConsts.BUSINESS_NAME, hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(cgoDto), HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS, HcsaConsts.CLINICAL_GOVERNANCE_OFFICERS,
                hcsaServiceStepSchemeDtos);
        addStepSchemeDto(!hcsaSvcSubtypeOrSubsumedDtos.isEmpty() && isNeed(cgoDto), HcsaConsts.STEP_DISCIPLINE_ALLOCATION,
                pageName + " Allocation", hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(svcPersonnelDto), HcsaConsts.STEP_SERVICE_PERSONNEL, HcsaConsts.SERVICE_PERSONNEL,
                hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(poDto), HcsaConsts.STEP_PRINCIPAL_OFFICERS, HcsaConsts.PRINCIPAL_OFFICERS, hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(mapPersonnelDto), HcsaConsts.STEP_MEDALERT_PERSON, HcsaConsts.MEDALERT_PERSON,
                hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(slPersonnelDto), HcsaConsts.STEP_SECTION_LEADER, HcsaConsts.SECTION_LEADER, hcsaServiceStepSchemeDtos);
        addStepSchemeDto(isNeed(kahPersonnelDto), HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER, HcsaConsts.KEY_APPOINTMENT_HOLDER,
                hcsaServiceStepSchemeDtos);

        hcsaServiceConfigDto.setHcsaSvcPersonnelDtos(hcsaSvcPersonnelDtos);
        hcsaServiceConfigDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        request.setAttribute("hcsaServiceStepSchemeDtos", hcsaServiceStepSchemeDtos);
    }

    private HcsaSvcPersonnelDto getHcsaSvcPersonnelDto(String psnType, HttpServletRequest request) {
        String manMedalertPerson = request.getParameter("man-" + NAME_MAP.get(psnType));
        String mixMedalertPerson = request.getParameter("mix-" + NAME_MAP.get(psnType));
        return configService.getHcsaSvcPersonnelDto(manMedalertPerson, mixMedalertPerson, psnType);
    }

    private void addStepSchemeDto(boolean isNeed, String stepCode, String stepName,
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos) {
        if (!isNeed) {
            return;
        }
        HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
        hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        hcsaServiceStepSchemeDto.setStepCode(stepCode);
        hcsaServiceStepSchemeDto.setSeqNum(SEQ_MAP.get(stepCode));
        hcsaServiceStepSchemeDto.setStepName(stepName);
        hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
    }

    private boolean isNeed(HcsaSvcPersonnelDto dto) {
        return isPositive(dto.getMandatoryCount()) && isPositive(dto.getMaximumCount());
    }

    private boolean isPositive(Integer i) {
        return i != null && i > 0;
    }
}
