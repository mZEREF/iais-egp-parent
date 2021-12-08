package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionAdvEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OnlineEnquiryAssistedReproductionDelegator
 *
 * @author junyu
 * @date 2021/11/17
 */
@Delegator(value = "onlineEnquiryAssistedReproductionDelegator")
@Slf4j
public class OnlineEnquiryAssistedReproductionDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter patientParameter = new FilterParameter.Builder()
            .clz(AssistedReproductionEnquiryResultsDto.class)
            .searchAttr("patientParam")
            .resultAttr("patientResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter patientAdvParameter = new FilterParameter.Builder()
            .clz(AssistedReproductionAdvEnquiryResultsDto.class)
            .searchAttr("patientAdvParam")
            .resultAttr("patientAdvResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter submissionParameter = new FilterParameter.Builder()
            .clz(AssistedReproductionEnquirySubResultsDto.class)
            .searchAttr("submissionParam")
            .resultAttr("submissionResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

    private static final Set<String> patientSortFieldNames = ImmutableSet.of(
            "NAME", "ID_NUMBER","DATE_OF_BIRTH"
    );
    private static final Set<String> submissionSortFieldNames = ImmutableSet.of(
            "BUSINESS_NAME", "SUBMISSION_NO", "SUBMIT_DT"
    );
    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;

    public void start(BaseProcessClass bpc){
        AssistedReproductionEnquiryFilterDto assistedReproductionEnquiryFilterDto=new AssistedReproductionEnquiryFilterDto();
        assistedReproductionEnquiryFilterDto.setSearchBy("1");
        ParamUtil.setSessionAttr(bpc.request,"assistedReproductionEnquiryFilterDto",assistedReproductionEnquiryFilterDto);


        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        submissionParameter.setPageSize(pageSize);
        submissionParameter.setPageNo(1);
        patientParameter.setPageNo(1);
        patientParameter.setPageSize(pageSize);
        patientAdvParameter.setPageNo(1);
        patientAdvParameter.setPageSize(pageSize);

    }

    private AssistedReproductionEnquiryFilterDto setAssistedReproductionEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        AssistedReproductionEnquiryFilterDto arFilterDto= (AssistedReproductionEnquiryFilterDto) ParamUtil.getSessionAttr(request,"assistedReproductionEnquiryFilterDto");
        String searchBy=ParamUtil.getString(request,"searchBy");
        if(StringUtil.isNotEmpty(searchBy)){
            arFilterDto.setSearchBy(searchBy);
        }

        String arCentre=ParamUtil.getString(request,"arCentre");
        arFilterDto.setArCentre(arCentre);
        String patientName=ParamUtil.getString(request,"patientName");
        arFilterDto.setPatientName(patientName);
        String patientIdType=ParamUtil.getString(request,"patientIdType");
        arFilterDto.setPatientIdType(patientIdType);
        String[] patientIdTypeList=ParamUtil.getStrings(request,"patientIdTypeList");
        if(patientIdTypeList != null){
            List<String> selectValList = Arrays.asList(patientIdTypeList);
            arFilterDto.setPatientIdTypeList(selectValList);
        }
        arFilterDto.setPatientIdType(patientIdType);
        String patientIdNumber=ParamUtil.getString(request,"patientIdNumber");
        arFilterDto.setPatientIdNumber(patientIdNumber);
        String submissionId=ParamUtil.getString(request,"submissionId");
        arFilterDto.setSubmissionId(submissionId);
        String submissionType=ParamUtil.getString(request,"submissionType");
        arFilterDto.setSubmissionType(submissionType);
        if(submissionType!=null&&submissionType.equals(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE)){
            String cycleStage=ParamUtil.getString(request,"cycleStage");
            arFilterDto.setCycleStage(cycleStage);
        }
        Date submissionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "submissionDateFrom"));
        arFilterDto.setSubmissionDateFrom(submissionDateFrom);
        Date submissionDateTo= Formatter.parseDate(ParamUtil.getString(request, "submissionDateTo"));
        arFilterDto.setSubmissionDateTo(submissionDateTo);

        String patientAgeNumberFrom=ParamUtil.getString(request,"patientAgeNumberFrom");
        arFilterDto.setPatientAgeNumberFrom(patientAgeNumberFrom);
        String patientAgeNumberTo=ParamUtil.getString(request,"patientAgeNumberTo");
        arFilterDto.setPatientAgeNumberTo(patientAgeNumberTo);
        String husbandName=ParamUtil.getString(request,"husbandName");
        arFilterDto.setHusbandName(husbandName);
        String husbandIdType=ParamUtil.getString(request,"husbandIdType");
        arFilterDto.setHusbandIdType(husbandIdType);
        String husbandIdNumber=ParamUtil.getString(request,"husbandIdNumber");
        arFilterDto.setHusbandIdNumber(husbandIdNumber);
        String embryologist=ParamUtil.getString(request,"embryologist");
        arFilterDto.setEmbryologist(embryologist);
        String arPractitioner=ParamUtil.getString(request,"arPractitioner");
        arFilterDto.setArPractitioner(arPractitioner);
        String cycleStagesStatus=ParamUtil.getString(request,"cycleStagesStatus");
        arFilterDto.setCycleStagesStatus(cycleStagesStatus);
        Date cycleStagesDateFrom= Formatter.parseDate(ParamUtil.getString(request, "cycleStagesDateFrom"));
        arFilterDto.setCycleStagesDateFrom(cycleStagesDateFrom);
        Date cycleStagesDateTo= Formatter.parseDate(ParamUtil.getString(request, "cycleStagesDateTo"));
        arFilterDto.setCycleStagesDateTo(cycleStagesDateTo);
        String arOrIuiCycle=ParamUtil.getString(request,"arOrIuiCycle");
        arFilterDto.setArOrIuiCycle(arOrIuiCycle);

        String IVM=ParamUtil.getString(request,"IVM");
        arFilterDto.setIVM(IVM);
        String freshCycleNatural=ParamUtil.getString(request,"freshCycleNatural");
        arFilterDto.setFreshCycleNatural(freshCycleNatural);
        String freshCycleSimulated=ParamUtil.getString(request,"freshCycleSimulated");
        arFilterDto.setFreshCycleSimulated(freshCycleSimulated);
        String frozenOocyteCycle=ParamUtil.getString(request,"frozenOocyteCycle");
        arFilterDto.setFrozenOocyteCycle(frozenOocyteCycle);
        String frozenEmbryoCycle=ParamUtil.getString(request,"frozenEmbryoCycle");
        arFilterDto.setFrozenEmbryoCycle(frozenEmbryoCycle);
        String freshCycleNumFrom=ParamUtil.getString(request,"freshCycleNumFrom");
        arFilterDto.setFreshCycleNumFrom(freshCycleNumFrom);
        String freshCycleNumTo=ParamUtil.getString(request,"freshCycleNumTo");
        arFilterDto.setFreshCycleNumTo(freshCycleNumTo);
        String abandonedCycle=ParamUtil.getString(request,"abandonedCycle");
        arFilterDto.setAbandonedCycle(abandonedCycle);
        String donorGameteUsed=ParamUtil.getString(request,"donorGameteUsed");
        arFilterDto.setDonorGameteUsed(donorGameteUsed);
        String donorName=ParamUtil.getString(request,"donorName");
        arFilterDto.setDonorName(donorName);

        String donorIdNumber=ParamUtil.getString(request,"donorIdNumber");
        arFilterDto.setDonorIdNumber(donorIdNumber);
        String removedFromStorage=ParamUtil.getString(request,"removedFromStorage");
        arFilterDto.setRemovedFromStorage(removedFromStorage);
        String embryosStoredBeyond=ParamUtil.getString(request,"embryosStoredBeyond");
        arFilterDto.setEmbryosStoredBeyond(embryosStoredBeyond);
        String sourceSemen=ParamUtil.getString(request,"sourceSemen");
        arFilterDto.setSourceSemen(sourceSemen);
        String GIFT=ParamUtil.getString(request,"GIFT");
        arFilterDto.setGIFT(GIFT);
        String ICSI=ParamUtil.getString(request,"ICSI");
        arFilterDto.setICSI(ICSI);
        String ZIFT=ParamUtil.getString(request,"ZIFT");
        arFilterDto.setZIFT(ZIFT);
        String IVF=ParamUtil.getString(request,"IVF");
        arFilterDto.setIVF(IVF);

        String embryosTransferredNum1=ParamUtil.getString(request,"embryosTransferredNum1");
        arFilterDto.setEmbryosTransferredNum1(embryosTransferredNum1);
        String embryosTransferredNum2=ParamUtil.getString(request,"embryosTransferredNum2");
        arFilterDto.setEmbryosTransferredNum2(embryosTransferredNum2);
        String embryosTransferredNum3=ParamUtil.getString(request,"embryosTransferredNum3");
        arFilterDto.setEmbryosTransferredNum3(embryosTransferredNum3);
        String embryosTransferredNumMax=ParamUtil.getString(request,"embryosTransferredNumMax");
        arFilterDto.setEmbryosTransferredNumMax(embryosTransferredNumMax);
        String ageEmbryosNum1=ParamUtil.getString(request,"ageEmbryosNum1");
        arFilterDto.setAgeEmbryosNum1(ageEmbryosNum1);
        String ageEmbryosNum4=ParamUtil.getString(request,"ageEmbryosNum4");
        arFilterDto.setAgeEmbryosNum4(ageEmbryosNum4);
        String ageEmbryosNum2=ParamUtil.getString(request,"ageEmbryosNum2");
        arFilterDto.setAgeEmbryosNum2(ageEmbryosNum2);
        String ageEmbryosNum5=ParamUtil.getString(request,"ageEmbryosNum5");
        arFilterDto.setAgeEmbryosNum5(ageEmbryosNum5);
        String ageEmbryosNum3=ParamUtil.getString(request,"ageEmbryosNum3");
        arFilterDto.setAgeEmbryosNum3(ageEmbryosNum3);
        String ageEmbryosNum6=ParamUtil.getString(request,"ageEmbryosNum6");
        arFilterDto.setAgeEmbryosNum6(ageEmbryosNum6);
        String clinicalPregnancy=ParamUtil.getString(request,"clinicalPregnancy");
        arFilterDto.setClinicalPregnancy(clinicalPregnancy);
        String ectopicPregnancy=ParamUtil.getString(request,"ectopicPregnancy");
        arFilterDto.setEctopicPregnancy(ectopicPregnancy);
        String implantationDocumented=ParamUtil.getString(request,"implantationDocumented");
        arFilterDto.setImplantationDocumented(implantationDocumented);
        String noPregnancy=ParamUtil.getString(request,"noPregnancy");
        arFilterDto.setNoPregnancy(noPregnancy);
        String unknown=ParamUtil.getString(request,"unknown");
        arFilterDto.setUnknown(unknown);
        String birthEventsTotal0=ParamUtil.getString(request,"birthEventsTotal0");
        arFilterDto.setBirthEventsTotal0(birthEventsTotal0);
        String birthEventsTotal1=ParamUtil.getString(request,"birthEventsTotal1");
        arFilterDto.setBirthEventsTotal1(birthEventsTotal1);
        String birthEventsTotal2=ParamUtil.getString(request,"birthEventsTotal2");
        arFilterDto.setBirthEventsTotal2(birthEventsTotal2);
        String birthEventsTotal3=ParamUtil.getString(request,"birthEventsTotal3");
        arFilterDto.setBirthEventsTotal3(birthEventsTotal3);
        String birthEventsTotalMax=ParamUtil.getString(request,"birthEventsTotalMax");
        arFilterDto.setBirthEventsTotalMax(birthEventsTotalMax);
        Date deliveryDateFrom= Formatter.parseDate(ParamUtil.getString(request, "deliveryDateFrom"));
        arFilterDto.setDeliveryDateFrom(deliveryDateFrom);
        Date deliveryDateTo= Formatter.parseDate(ParamUtil.getString(request, "deliveryDateTo"));
        arFilterDto.setDeliveryDateTo(deliveryDateTo);

        String patientART=ParamUtil.getString(request,"patientART");
        arFilterDto.setPatientART(patientART);
        String patientIUI=ParamUtil.getString(request,"patientIUI");
        arFilterDto.setPatientIUI(patientIUI);
        String patientPGT=ParamUtil.getString(request,"patientPGT");
        arFilterDto.setPatientPGT(patientPGT);
        String disposalTypeFreshOocyte=ParamUtil.getString(request,"disposalTypeFreshOocyte");
        arFilterDto.setDisposalTypeFreshOocyte(disposalTypeFreshOocyte);
        String disposalTypeFrozenOocyte=ParamUtil.getString(request,"disposalTypeFrozenOocyte");
        arFilterDto.setDisposalTypeFrozenOocyte(disposalTypeFrozenOocyte);
        String disposalTypeThawedOocyte=ParamUtil.getString(request,"disposalTypeThawedOocyte");
        arFilterDto.setDisposalTypeThawedOocyte(disposalTypeThawedOocyte);
        String disposalTypeFreshEmbryo=ParamUtil.getString(request,"disposalTypeFreshEmbryo");
        arFilterDto.setDisposalTypeFreshEmbryo(disposalTypeFreshEmbryo);
        String disposalTypeFrozenEmbryo=ParamUtil.getString(request,"disposalTypeFrozenEmbryo");
        arFilterDto.setDisposalTypeFrozenEmbryo(disposalTypeFrozenEmbryo);
        String disposalTypeThawedEmbryo=ParamUtil.getString(request,"disposalTypeThawedEmbryo");
        arFilterDto.setDisposalTypeThawedEmbryo(disposalTypeThawedEmbryo);
        String disposalTypeFrozenSperm=ParamUtil.getString(request,"disposalTypeFrozenSperm");
        arFilterDto.setDisposalTypeFrozenSperm(disposalTypeFrozenSperm);
        String disposedTotalNumber=ParamUtil.getString(request,"disposedTotalNumber");
        arFilterDto.setDisposedTotalNumber(disposedTotalNumber);
        Date disposalDateFrom= Formatter.parseDate(ParamUtil.getString(request, "disposalDateFrom"));
        Date disposalDateTo= Formatter.parseDate(ParamUtil.getString(request, "disposalDateTo"));
        arFilterDto.setDisposalDateFrom(disposalDateFrom);
        arFilterDto.setDisposalDateTo(disposalDateTo);

        String transferInOrOut=ParamUtil.getString(request,"transferInOrOut");
        arFilterDto.setTransferInOrOut(transferInOrOut);
        String transferredOocyte=ParamUtil.getString(request,"transferredOocyte");
        arFilterDto.setTransferredOocyte(transferredOocyte);
        String transferredEmbryo=ParamUtil.getString(request,"transferredEmbryo");
        arFilterDto.setTransferredEmbryo(transferredEmbryo);
        String transferredSperm=ParamUtil.getString(request,"transferredSperm");
        arFilterDto.setTransferredSperm(transferredSperm);
        String transferredInFrom=ParamUtil.getString(request,"transferredInFrom");
        arFilterDto.setTransferredInFrom(transferredInFrom);
        String transferOutTo=ParamUtil.getString(request,"transferOutTo");
        arFilterDto.setTransferOutTo(transferOutTo);
        Date transferDateFrom= Formatter.parseDate(ParamUtil.getString(request, "transferDateFrom"));
        Date transferDateTo= Formatter.parseDate(ParamUtil.getString(request, "transferDateTo"));
        arFilterDto.setTransferDateFrom(transferDateFrom);
        arFilterDto.setTransferDateTo(transferDateTo);

        String PGT=ParamUtil.getString(request,"PGT");
        arFilterDto.setPGT(PGT);
        String pgtMCom=ParamUtil.getString(request,"pgtMCom");
        arFilterDto.setPgtMCom(pgtMCom);
        String pgtMRare=ParamUtil.getString(request,"pgtMRare");
        arFilterDto.setPgtMRare(pgtMRare);
        String pgtMEbt=ParamUtil.getString(request,"pgtMEbt");
        arFilterDto.setPgtMEbt(pgtMEbt);
        String pgtSr=ParamUtil.getString(request,"pgtSr");
        arFilterDto.setPgtSr(pgtSr);
        String pgtA=ParamUtil.getString(request,"pgtA");
        arFilterDto.setPgtA(pgtA);
        String ptt=ParamUtil.getString(request,"ptt");
        arFilterDto.setPtt(ptt);
        String pgtOthers=ParamUtil.getString(request,"pgtOthers");
        arFilterDto.setPgtOthers(pgtOthers);
        String pgtDisease=ParamUtil.getString(request,"pgtDisease");
        arFilterDto.setPgtDisease(pgtDisease);

        ParamUtil.setSessionAttr(request,"assistedReproductionEnquiryFilterDto",arFilterDto);

        return arFilterDto;
    }

    private void setQueryFilter(AssistedReproductionEnquiryFilterDto arDto,FilterParameter filterParameter,int sqf){
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        if(arDto.getArCentre()!=null) {//todo xml sql
            filter.put("arCentre", arDto.getArCentre());
        }
        if(sqf==0||sqf==2){
            if(arDto.getPatientName()!=null){
                filter.put("patientName", arDto.getPatientName());
            }

            if(arDto.getPatientIdNumber()!=null){
                filter.put("patientIdNumber",arDto.getPatientIdNumber());
            }
        }
        if(sqf==1||sqf==2){
            if(arDto.getSubmissionId()!=null){
                filter.put("submissionId",arDto.getSubmissionId());
            }

            if(arDto.getSubmissionType()!=null){
                filter.put("submissionType",arDto.getSubmissionType());
                if(arDto.getSubmissionType().equals(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE)){
                    if(sqf==1&&arDto.getCycleStage()!=null){
                        filter.put("cycleStage",arDto.getCycleStage());
                    }
                }
            }
            if(arDto.getSubmissionDateFrom()!=null){
                String submissionDateFrom = Formatter.formatDateTime(arDto.getSubmissionDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("submission_start_date", submissionDateFrom);
            }

            if(arDto.getSubmissionDateTo()!=null){
                String submissionDateTo = Formatter.formatDateTime(arDto.getSubmissionDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("submission_to_date", submissionDateTo);
            }
        }
        if(sqf==2){
            if(arDto.getPatientIdType()!=null){
                filter.put("patientIdType", arDto.getPatientIdType());
            }

            if(arDto.getPatientAgeNumberFrom()!=null){
                try {
                    int ageFrom=Integer.parseInt(arDto.getPatientAgeNumberFrom());
                    Calendar calendar=Calendar.getInstance();
                    calendar.add(Calendar.YEAR, -ageFrom);
                    String ageDateFrom = Formatter.formatDateTime(calendar.getTime(),
                            SystemAdminBaseConstants.DATE_FORMAT);
                    filter.put("patientAgeDateFrom", ageDateFrom);
                }catch (Exception e){
                    log.error("Patient age not int");
                }
            }

            if(arDto.getPatientAgeNumberTo()!=null){
                try {
                    int ageTo=Integer.parseInt(arDto.getPatientAgeNumberTo());
                    Calendar calendar=Calendar.getInstance();
                    calendar.add(Calendar.YEAR, -ageTo);
                    String ageDateTo = Formatter.formatDateTime(calendar.getTime(),
                            SystemAdminBaseConstants.DATE_FORMAT);
                    filter.put("patientAgeDateTo", ageDateTo);
                }catch (Exception e){
                    log.error("Patient age not int");
                }
            }


            if(arDto.getHusbandName()!=null){
                filter.put("husbandName", arDto.getHusbandName());
            }
            if(arDto.getHusbandIdType()!=null){
                filter.put("husbandIdType", arDto.getHusbandIdType());
            }
            if(arDto.getHusbandIdNumber()!=null){
                filter.put("husbandIdNumber",arDto.getHusbandIdNumber());
            }

            if(arDto.getEmbryologist()!=null){
                filter.put("embryologist",arDto.getEmbryologist());
            }
            if(arDto.getEmbryologist()!=null){
                filter.put("arPractitioner",arDto.getArPractitioner());
            }

            if(arDto.getCycleStagesStatus()!=null){
                filter.put("cycleStagesStatus",arDto.getCycleStagesStatus());
            }
            if(arDto.getCycleStagesDateFrom()!=null){
                String cycleStagesDateFrom = Formatter.formatDateTime(arDto.getCycleStagesDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("cycleStagesDateFrom", cycleStagesDateFrom);
            }
            if(arDto.getCycleStagesDateTo()!=null){
                String cycleStagesDateTo = Formatter.formatDateTime(arDto.getCycleStagesDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("cycleStagesDateTo", cycleStagesDateTo);
            }
            if(arDto.getArOrIuiCycle()!=null){
                filter.put("arOrIui",arDto.getArOrIuiCycle());
            }
            if(arDto.getIVM()!=null){//bit
                filter.put("ivm",Integer.parseInt(arDto.getIVM()));
            }
            if(arDto.getFreshCycleNatural()!=null&& "on".equals(arDto.getFreshCycleNatural())){
                filter.put("cart_fcn",1);
            }
            if(arDto.getFreshCycleSimulated()!=null&& "on".equals(arDto.getFreshCycleSimulated())){
                filter.put("cart_fcs",1);
            }
            if(arDto.getFrozenOocyteCycle()!=null&& "on".equals(arDto.getFrozenOocyteCycle())){
                filter.put("cart_foc",1);
            }
            if(arDto.getFrozenEmbryoCycle()!=null&& "on".equals(arDto.getFrozenEmbryoCycle())){
                filter.put("cart_foe",1);
            }
//TOTAL_PREVIOUSLY_PREVIOUSLY freshCycleNumFrom
            if(arDto.getFreshCycleNumFrom()!=null){
                try {
                    int totPreFreFrom=Integer.parseInt(arDto.getFreshCycleNumFrom());
                    filter.put("freshCycleNumFrom", totPreFreFrom);
                }catch (Exception e){
                    log.error("Total No. of AR cycles previously undergone by patient not int");
                }
            }

            if(arDto.getFreshCycleNumTo()!=null){
                try {
                    int totPreFreTo=Integer.parseInt(arDto.getFreshCycleNumTo());
                    filter.put("freshCycleNumTo", totPreFreTo);
                }catch (Exception e){
                    log.error("Total No. of AR cycles previously undergone by patient not int");
                }
            }

            if(arDto.getAbandonedCycle()!=null){//bit
                filter.put("abandonedCycle",Integer.parseInt(arDto.getAbandonedCycle()));
            }

            if(arDto.getDonorGameteUsed()!=null){//not found
                filter.put("donorGameteUsed",Integer.parseInt(arDto.getDonorGameteUsed()));
            }
            if(arDto.getDonorName()!=null){
                filter.put("donorName", arDto.getDonorName());
            }
            if(arDto.getDonorIdNumber()!=null){
                filter.put("donorIdNumber",arDto.getDonorIdNumber());
            }
            if(arDto.getRemovedFromStorage()!=null){//not found
                filter.put("removedFromStorage", arDto.getRemovedFromStorage());
            }
            if(arDto.getEmbryosStoredBeyond()!=null){//not found
                filter.put("embryosStoredBeyond",Integer.parseInt(arDto.getEmbryosStoredBeyond()));
            }
            if(arDto.getSourceSemen()!=null){
                if("Donor".equals(arDto.getSourceSemen())){
                    filter.put("FROM_DONOR",1);
                    filter.put("FROM_DONOR_TISSUE",1);
                }
                if("Husband".equals(arDto.getSourceSemen())){
                    filter.put("FROM_HUSBAND",1);
                    filter.put("FROM_HUSBAND_TISSUE",1);
                }
            }
            if(arDto.getGIFT()!=null&& "on".equals(arDto.getGIFT())){
                filter.put("gift",1);
            }
            if(arDto.getICSI()!=null&& "on".equals(arDto.getICSI())){
                filter.put("icsi",1);
            }
            if(arDto.getZIFT()!=null&& "on".equals(arDto.getZIFT())){
                filter.put("zift",1);
            }
            if(arDto.getIVF()!=null&& "on".equals(arDto.getIVF())){
                filter.put("ivf",1);
            }
            List<Integer> embryosTransferredNums=IaisCommonUtils.genNewArrayList();

            if(arDto.getEmbryosTransferredNum1()!=null&& "on".equals(arDto.getEmbryosTransferredNum1())){
                embryosTransferredNums.add(1);
            }
            if(arDto.getEmbryosTransferredNum2()!=null&& "on".equals(arDto.getEmbryosTransferredNum2())){
                embryosTransferredNums.add(2);
            }
            if(arDto.getEmbryosTransferredNum3()!=null&& "on".equals(arDto.getEmbryosTransferredNum3())){
                embryosTransferredNums.add(3);
            }
            if(arDto.getEmbryosTransferredNumMax()!=null&& "on".equals(arDto.getEmbryosTransferredNumMax())){
                for (int i=4;i<30;i++){
                    embryosTransferredNums.add(i);
                }
            }
            if(IaisCommonUtils.isNotEmpty(embryosTransferredNums)){
                filter.put("embTransNums",embryosTransferredNums);
            }


            List<String> ageEmbryosNums=IaisCommonUtils.genNewArrayList();

            if(arDto.getAgeEmbryosNum1()!=null&& "on".equals(arDto.getAgeEmbryosNum1())){
                ageEmbryosNums.add("AOFET001");
            }
            if(arDto.getAgeEmbryosNum2()!=null&& "on".equals(arDto.getAgeEmbryosNum2())){
                ageEmbryosNums.add("AOFET002");
            }
            if(arDto.getAgeEmbryosNum3()!=null&& "on".equals(arDto.getAgeEmbryosNum3())){
                ageEmbryosNums.add("AOFET003");
            }
            if(arDto.getAgeEmbryosNum4()!=null&& "on".equals(arDto.getAgeEmbryosNum4())){
                ageEmbryosNums.add("AOFET004");
            }
            if(arDto.getAgeEmbryosNum5()!=null&& "on".equals(arDto.getAgeEmbryosNum5())){
                ageEmbryosNums.add("AOFET005");
            }
            if(arDto.getAgeEmbryosNum6()!=null&& "on".equals(arDto.getAgeEmbryosNum6())){
                ageEmbryosNums.add("AOFET006");

            }
            if(IaisCommonUtils.isNotEmpty(ageEmbryosNums)){
                filter.put("ageEmbryosNums",ageEmbryosNums);
            }

            List<String> outcomeEmbryoTransferreds=IaisCommonUtils.genNewArrayList();

            if(arDto.getClinicalPregnancy()!=null&& "on".equals(arDto.getClinicalPregnancy())){
                outcomeEmbryoTransferreds.add(DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_CLINICAL_PREGNANCY);
            }
            if(arDto.getEctopicPregnancy()!=null&& "on".equals(arDto.getEctopicPregnancy())){
                outcomeEmbryoTransferreds.add(DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_ECTOPIC_PREGNANCY);
            }
            if(arDto.getImplantationDocumented()!=null&& "on".equals(arDto.getImplantationDocumented())){
                outcomeEmbryoTransferreds.add(DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_IMPLANTATION_DETECTED);
            }
            if(arDto.getNoPregnancy()!=null&& "on".equals(arDto.getNoPregnancy())){
                outcomeEmbryoTransferreds.add(DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_NO_PREGNANCY_DETECTED);
            }
            if(arDto.getUnknown()!=null&& "on".equals(arDto.getUnknown())){
                outcomeEmbryoTransferreds.add(DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_UNKNOWN);
            }

            if(IaisCommonUtils.isNotEmpty(outcomeEmbryoTransferreds)){
                filter.put("outcomeEmbryoTransferreds",outcomeEmbryoTransferreds);
            }

            List<Integer> birthEventsTotalList=IaisCommonUtils.genNewArrayList();

            if(arDto.getBirthEventsTotal0()!=null&& "on".equals(arDto.getBirthEventsTotal0())){
                birthEventsTotalList.add(0);
            }
            if(arDto.getBirthEventsTotal1()!=null&& "on".equals(arDto.getBirthEventsTotal1())){
                birthEventsTotalList.add(1);
            }
            if(arDto.getBirthEventsTotal2()!=null&& "on".equals(arDto.getBirthEventsTotal2())){
                birthEventsTotalList.add(2);
            }
            if(arDto.getBirthEventsTotal3()!=null&& "on".equals(arDto.getBirthEventsTotal3())){
                birthEventsTotalList.add(3);
            }
            if(arDto.getBirthEventsTotalMax()!=null&& "on".equals(arDto.getEmbryosTransferredNumMax())){
                for (int i=4;i<30;i++){
                    birthEventsTotalList.add(i);
                }
            }
            if(IaisCommonUtils.isNotEmpty(birthEventsTotalList)){
                filter.put("birthEventsTotalList",birthEventsTotalList);
            }

            if(arDto.getDeliveryDateFrom()!=null){
                String deliveryDateFrom = Formatter.formatDateTime(arDto.getDeliveryDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("deliveryDateFrom", deliveryDateFrom);
            }

            if(arDto.getDeliveryDateTo()!=null){
                String deliveryDateTo = Formatter.formatDateTime(arDto.getDeliveryDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("deliveryDateTo", deliveryDateTo);
            }

            if(arDto.getPatientART()!=null){
                if("1".equals(arDto.getPatientART())){
                    filter.put("patientArtYes",1);
                }
                if("0".equals(arDto.getPatientART())){
                    filter.put("patientArtNo",1);
                }
            }
            if(arDto.getPatientIUI()!=null){
                if("1".equals(arDto.getPatientIUI())){
                    filter.put("patientIuiYes",1);

                }
                if("0".equals(arDto.getPatientIUI())){
                    filter.put("patientIuiNo",1);
                }
            }
            if(arDto.getPatientPGT()!=null){
                if("1".equals(arDto.getPatientPGT())){
                    filter.put("patientPgtYes",1);

                }
                if("0".equals(arDto.getPatientPGT())){
                    filter.put("patientPgtNo",1);
                }
            }

            List<String> disposalTypeList=IaisCommonUtils.genNewArrayList();

            if(arDto.getDisposalTypeFreshOocyte()!=null&& "on".equals(arDto.getDisposalTypeFreshOocyte())){
                disposalTypeList.add(DataSubmissionConsts.DISPOSAL_TYPE_FRESH_OOCYTE);
            }
            if(arDto.getDisposalTypeFrozenOocyte()!=null&& "on".equals(arDto.getDisposalTypeFrozenOocyte())){
                disposalTypeList.add(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_OOCYTE);
            }
            if(arDto.getDisposalTypeThawedOocyte()!=null&& "on".equals(arDto.getDisposalTypeThawedOocyte())){
                disposalTypeList.add(DataSubmissionConsts.DISPOSAL_TYPE_THAWED_OOCYTE);
            }
            if(arDto.getDisposalTypeFreshEmbryo()!=null&& "on".equals(arDto.getDisposalTypeFreshEmbryo())){
                disposalTypeList.add(DataSubmissionConsts.DISPOSAL_TYPE_FRESH_EMBRYO);
            }
            if(arDto.getDisposalTypeFrozenEmbryo()!=null&& "on".equals(arDto.getDisposalTypeFrozenEmbryo())){
                disposalTypeList.add(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_EMBRYO);
            }
            if(arDto.getDisposalTypeThawedEmbryo()!=null&& "on".equals(arDto.getDisposalTypeThawedEmbryo())){
                disposalTypeList.add(DataSubmissionConsts.DISPOSAL_TYPE_THAWED_EMBRYO);
            }
            if(arDto.getDisposalTypeFrozenSperm()!=null&& "on".equals(arDto.getDisposalTypeFrozenSperm())){
                disposalTypeList.add(DataSubmissionConsts.DISPOSAL_TYPE_FROZEN_SPERM);
            }
            if(IaisCommonUtils.isNotEmpty(disposalTypeList)){
                filter.put("disposalTypeList",disposalTypeList);
            }
            if(arDto.getDisposedTotalNumber()!=null){
                try {
                    int disposedTotalNumber=Integer.parseInt(arDto.getDisposedTotalNumber());
                    filter.put("disposedTotalNumber", disposedTotalNumber);
                }catch (Exception e){
                    log.error("Total No. Disposed Of not int");
                }
            }

            if(arDto.getDisposalDateFrom()!=null){
                String disposalDateFrom = Formatter.formatDateTime(arDto.getDisposalDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("disposalDateFrom", disposalDateFrom);
            }

            if(arDto.getDisposalDateTo()!=null){
                String disposalDateTo = Formatter.formatDateTime(arDto.getDisposalDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("disposalDateTo", disposalDateTo);
            }
            if(arDto.getTransferInOrOut()!=null){
                if("0".equals(arDto.getTransferInOrOut())){
                    filter.put("transferInOrOut", "in");
                }
                if("1".equals(arDto.getTransferInOrOut())){
                    filter.put("transferInOrOut", "out");
                }
            }
            if(arDto.getTransferredOocyte()!=null&& "on".equals(arDto.getTransferredOocyte())
                    ||arDto.getTransferredEmbryo()!=null&& "on".equals(arDto.getTransferredEmbryo())
                    ||arDto.getTransferredSperm()!=null&& "on".equals(arDto.getTransferredSperm())){
                filter.put("transferredOocyte",0);
                filter.put("transferredEmbryo",0);
                filter.put("transferredSperm",0);
                filter.put("transferredList",0);
            }
            if(arDto.getTransferredOocyte()!=null&& "on".equals(arDto.getTransferredOocyte())){
                filter.put("transferredOocyte",1);
            }
            if(arDto.getTransferredEmbryo()!=null&& "on".equals(arDto.getTransferredEmbryo())){
                filter.put("transferredEmbryo",1);
            }
            if(arDto.getTransferredSperm()!=null&& "on".equals(arDto.getTransferredSperm())){
                filter.put("transferredSperm",1);
            }
            if(StringUtil.isNotEmpty(arDto.getTransferredInFrom())) {//todo xml sql
                filter.put("transferredInFrom", arDto.getTransferredInFrom());
            }
            if(StringUtil.isNotEmpty(arDto.getTransferOutTo())) {//todo xml sql
                filter.put("transferOutTo", arDto.getTransferOutTo());
            }

            if(arDto.getTransferDateFrom()!=null){
                String transferDateFrom = Formatter.formatDateTime(arDto.getTransferDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("transferDateFrom", transferDateFrom);
            }

            if(arDto.getTransferDateTo()!=null){
                String transferDateTo = Formatter.formatDateTime(arDto.getTransferDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("transferDateTo", transferDateTo);
            }
            if(arDto.getPGT()!=null){
                if(arDto.getPGT().equals("1")){
                    filter.put("pgtNo", 1);

                }
                if(arDto.getPGT().equals("0")){
                    filter.put("pgtYes", 1);

                }
            }

            if(arDto.getPgtMCom()!=null&& "on".equals(arDto.getPgtMCom())){
                filter.put("pgtMCom", 1);
            }
            if(arDto.getPgtMRare()!=null&& "on".equals(arDto.getPgtMRare())){
                filter.put("pgtMRare", 1);
            }
            if(arDto.getPgtMEbt()!=null&& "on".equals(arDto.getPgtMEbt())){
                filter.put("pgtMEbt", 1);
            }
            if(arDto.getPgtSr()!=null&& "on".equals(arDto.getPgtSr())){
                filter.put("pgtSr", 1);
            }
            if(arDto.getPgtA()!=null&& "on".equals(arDto.getPgtA())){
                filter.put("pgtA", 1);
            }
            if(arDto.getPtt()!=null&& "on".equals(arDto.getPtt())){
                filter.put("ptt", 1);
            }
            if(arDto.getPgtOthers()!=null&& "on".equals(arDto.getPgtOthers())){
                filter.put("pgtOthers", 1);
            }

            if(arDto.getPgtDisease()!=null){
                if(arDto.getPgtDisease().equals("1")){
                    filter.put("PGT_M_DSLD", 1);

                }
                if(arDto.getPgtDisease().equals("0")){
                    filter.put("PGT_M_DSLD", 0);

                }
            }
        }

        filterParameter.setFilters(filter);

    }

    public void baseSearch(BaseProcessClass bpc)throws ParseException{
        List<SelectOption> submissionTypeOptions= IaisCommonUtils.genNewArrayList();
        submissionTypeOptions.add(new SelectOption("AR_TP001","Patient Information"));
        submissionTypeOptions.add(new SelectOption("AR_TP002","Cycle Stages"));
        submissionTypeOptions.add(new SelectOption("AR_TP003","Donor Samples"));
        ParamUtil.setRequestAttr(bpc.request,"submissionTypeOptions",submissionTypeOptions);
        List<SelectOption> arCentreSelectOption  = IaisCommonUtils.genNewArrayList();
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);
        HttpServletRequest request = bpc.request;

        AssistedReproductionEnquiryFilterDto arFilterDto= setAssistedReproductionEnquiryFilterDto(request);
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            if(patientSortFieldNames.contains(sortFieldName)){
                patientParameter.setSortType(sortType);
                patientParameter.setSortField(sortFieldName);
            }
        }

        setQueryFilter(arFilterDto,patientParameter,0);
        SearchParam patientParam = SearchResultHelper.getSearchParam(request, patientParameter,true);

        if(IaisCommonUtils.isNotEmpty(arFilterDto.getPatientIdTypeList())){
            String patientIdTypeListStr = SqlHelper.constructInCondition("dpi.ID_TYPE", arFilterDto.getPatientIdTypeList().size());
            patientParam.addParam("patient_id_types", patientIdTypeListStr);
            for(int i = 0; i < arFilterDto.getPatientIdTypeList().size(); i++){
                patientParam.addFilter("dpi.ID_TYPE" + i, arFilterDto.getPatientIdTypeList().get(i));
            }
        }
        CrudHelper.doPaging(patientParam,bpc.request);

        QueryHelp.setMainSql("onlineEnquiry","searchPatientByAssistedReproduction",patientParam);

        SearchResult<AssistedReproductionEnquiryResultsDto> patientResult = assistedReproductionService.searchPatientByParam(patientParam);
        ParamUtil.setRequestAttr(request,"patientResult",patientResult);
        ParamUtil.setSessionAttr(request,"patientParam",patientParam);
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            if(submissionSortFieldNames.contains(sortFieldName)){
                submissionParameter.setSortType(sortType);
                submissionParameter.setSortField(sortFieldName);
            }
        }
        setQueryFilter(arFilterDto,submissionParameter,1);
        SearchParam submissionParam = SearchResultHelper.getSearchParam(request, submissionParameter,true);
        CrudHelper.doPaging(submissionParam,bpc.request);
        QueryHelp.setMainSql("onlineEnquiry","searchSubmissionByAssistedReproduction",submissionParam);
        SearchResult<AssistedReproductionEnquirySubResultsDto> submissionResult = assistedReproductionService.searchSubmissionByParam(submissionParam);
        if(IaisCommonUtils.isNotEmpty(submissionResult.getRows())){
            for (AssistedReproductionEnquirySubResultsDto subResultsDto:submissionResult.getRows()
            ) {
                switch (subResultsDto.getSubmissionType()){
                    case "AR_TP001":subResultsDto.setSubmissionType("Patient Information");break;
                    case "AR_TP002":subResultsDto.setSubmissionType("Cycle Stages");break;
                    case "AR_TP003":subResultsDto.setSubmissionType("Donor Samples");break;
                    default:subResultsDto.setSubmissionType(MasterCodeUtil.getCodeDesc(subResultsDto.getSubmissionType()));
                }
            }
        }
        ParamUtil.setSessionAttr(request,"submissionParam",submissionParam);
        ParamUtil.setRequestAttr(request,"submissionResult",submissionResult);


    }

    public void changePagination(BaseProcessClass bpc){

    }

    public void advNextStep(BaseProcessClass bpc){

    }

    public void perAdvancedSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;

        List<SelectOption> aRorIUICycleOptions= IaisCommonUtils.genNewArrayList();
        aRorIUICycleOptions.add(new SelectOption("AR","AR"));
        aRorIUICycleOptions.add(new SelectOption("IUI","IUI"));
        ParamUtil.setRequestAttr(bpc.request,"aRorIUICycleOptions",aRorIUICycleOptions);
        List<SelectOption> sourceSemenOptions= IaisCommonUtils.genNewArrayList();
        sourceSemenOptions.add(new SelectOption("Donor","Donor"));
        sourceSemenOptions.add(new SelectOption("Husband","Husband"));
        ParamUtil.setRequestAttr(bpc.request,"sourceSemenOptions",sourceSemenOptions);
        List<SelectOption> arCentreSelectOption  = IaisCommonUtils.genNewArrayList();
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);

        AssistedReproductionEnquiryFilterDto arFilterDto= setAssistedReproductionEnquiryFilterDto(request);

        setQueryFilter(arFilterDto,patientAdvParameter,2);
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            patientAdvParameter.setSortType(sortType);
            patientAdvParameter.setSortField(sortFieldName);
        }

        SearchParam patientParam = SearchResultHelper.getSearchParam(request, patientAdvParameter,true);
        CrudHelper.doPaging(patientParam,bpc.request);

        QueryHelp.setMainSql("onlineEnquiry","advancedSearchPatientByAssistedReproduction",patientParam);
        SearchResult<AssistedReproductionAdvEnquiryResultsDto> patientResult = assistedReproductionService.searchPatientAdvByParam(patientParam);
        ParamUtil.setRequestAttr(request,"patientAdvResult",patientResult);
        ParamUtil.setSessionAttr(request,"patientAdvParam",patientParam);
    }
    public void preViewFullDetails(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String additional=ParamUtil.getRequestString(request, InboxConst.CRUD_ACTION_ADDITIONAL);
        String key=ParamUtil.getRequestString(request, InboxConst.CRUD_ACTION_VALUE);
        PatientInfoDto patientInfoDto=null;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto=null;
        if(StringUtil.isNotEmpty(additional)){
            if("patient".equals(additional)){
                patientInfoDto=assistedReproductionService.patientInfoDtoByPatientCode(key);
                arSuperDataSubmissionDto= assistedReproductionService.getArSuperDataSubmissionDto(patientInfoDto.getPatient().getSubmissionId());

            }
            if("submission".equals(additional)){
                arSuperDataSubmissionDto= assistedReproductionService.getArSuperDataSubmissionDto(key);
                patientInfoDto=assistedReproductionService.patientInfoDtoByPatientCode(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getPatientCode());

            }
        }
        if(patientInfoDto!=null){
            ParamUtil.setSessionAttr(request,"patientInfoDto",patientInfoDto);

            Map<String,PatientInventoryDto> patientInventoryDtos=IaisCommonUtils.genNewHashMap();
            for (PremisesDto premisesDto:patientInfoDto.getPatient().getArCentres()
                 ) {
                try {
                    PatientInventoryDto patientInventoryDto=assistedReproductionService.patientInventoryByCode(patientInfoDto.getPatient().getPatientCode(),premisesDto.getHciCode());
                    patientInventoryDtos.put(premisesDto.getAddress(),patientInventoryDto);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }

            }
            ParamUtil.setSessionAttr(request,"patientInventoryDtos", (Serializable) patientInventoryDtos);

        }
        if(arSuperDataSubmissionDto!=null){
            ParamUtil.setSessionAttr(request,"arSuperDataSubmissionDto",arSuperDataSubmissionDto);
        }



    }
    public void searchCycle(BaseProcessClass bpc){

    }
    public void perStep(BaseProcessClass bpc){

    }

    public void perStageInfo(BaseProcessClass bpc){

    }

    public void searchInventory(BaseProcessClass bpc){

    }


    @GetMapping(value = "/ar-quick-view")
    public @ResponseBody
    String viewArQuick(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        String submissionIdNo = ParamUtil.getString(request,"submissionIdNo");

        if(submissionIdNo==null){
            return "";
        }
        String sql = SqlMap.INSTANCE.getSql("onlineEnquiry", "ar-quick-view").getSqlStr();


        sql=sql.replaceAll("IUICyclesNumber","1");
        sql=sql.replaceAll("FreshCyclesNumber","1");
        sql=sql.replaceAll("FrozenCyclesNumber","1");
        sql=sql.replaceAll("PGTCyclesNumber","1");

        sql=sql.replaceAll("FreshOocytesNumber","1");
        sql=sql.replaceAll("FrozenOocytesNumber","1");
        sql=sql.replaceAll("FreshEmbryosNumber","1");
        sql=sql.replaceAll("FrozenEmbryosNumber","1");
        sql=sql.replaceAll("FrozenSpermsNumber","1");

        sql=sql.replaceAll("patientId","1");

        return sql;
    }
}
