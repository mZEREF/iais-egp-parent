package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCoFundingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryTransactionHistoryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryTransactionHistoryResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDefectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeBabyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.AssistedReproductionService;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter patientAdvParameter = new FilterParameter.Builder()
            .clz(AssistedReproductionEnquiryResultsDto.class)
            .searchAttr("patientParam")
            .resultAttr("patientResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter submissionParameter = new FilterParameter.Builder()
            .clz(AssistedReproductionEnquirySubResultsDto.class)
            .searchAttr("submissionParam")
            .resultAttr("submissionResult")
            .sortField("SUBMIT_DT").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();
    FilterParameter transactionParameter = new FilterParameter.Builder()
            .clz(ArEnquiryTransactionHistoryResultDto.class)
            .searchAttr("transactionParam")
            .resultAttr("transactionResult")
            .sortField("SUBMIT_DT").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    FilterParameter cycleStageParameter = new FilterParameter.Builder()
            .clz(ArEnquiryCycleStageDto.class)
            .searchAttr("cycleStageParam")
            .resultAttr("cycleStageResult")
            .sortField("CREATED_DT").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    private static final Set<String> patientSortFieldNames = ImmutableSet.of(
            "NAME", "ID_NUMBER","DATE_OF_BIRTH","ID_TYPE_DESC","NATIONALITY_DESC"
    );
    private static final Set<String> submissionSortFieldNames = ImmutableSet.of(
            "BUSINESS_NAME", "SUBMISSION_NO", "SUBMIT_DT","CYCLE_STAGE_DESC"
    );
    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;

    @Autowired
    private AssistedReproductionClient assistedReproductionClient;

    @Autowired
    private MohDsActionDelegator mohDsActionDelegator;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;


    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_AR);

        AssistedReproductionEnquiryFilterDto assistedReproductionEnquiryFilterDto=new AssistedReproductionEnquiryFilterDto();
        assistedReproductionEnquiryFilterDto.setSearchBy("1");
        ParamUtil.setSessionAttr(bpc.request,"assistedReproductionEnquiryFilterDto",assistedReproductionEnquiryFilterDto);


        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        submissionParameter.setPageSize(pageSize);
        submissionParameter.setPageNo(1);
        submissionParameter.setSortField("SUBMIT_DT");
        submissionParameter.setSortType(SearchParam.DESCENDING);
        patientParameter.setPageNo(1);
        patientParameter.setPageSize(pageSize);
        patientParameter.setSortField("ID");
        patientParameter.setSortType(SearchParam.DESCENDING);
        patientAdvParameter.setPageNo(1);
        patientAdvParameter.setPageSize(pageSize);
        patientAdvParameter.setSortField("ID");
        patientAdvParameter.setSortType(SearchParam.DESCENDING);
        transactionParameter.setPageNo(1);
        transactionParameter.setPageSize(pageSize);
        transactionParameter.setSortField("SUBMIT_DT");
        transactionParameter.setSortType(SearchParam.DESCENDING);
        cycleStageParameter.setPageNo(1);
        cycleStageParameter.setPageSize(pageSize);
        cycleStageParameter.setSortField("CREATED_DT");
        cycleStageParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"DashboardTitle","Assisted Reproduction Enquiry");
        ParamUtil.setSessionAttr(bpc.request, "patientParam",null);
        ParamUtil.setSessionAttr(bpc.request, "submissionParam",null);
        ParamUtil.setSessionAttr(bpc.request, "patientAdvParam",null);
        ParamUtil.setSessionAttr(bpc.request, "transactionParam",null);
        ParamUtil.setSessionAttr(bpc.request, "cycleStageParam",null);
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
        String[] patientIdTypeList=ParamUtil.getStrings(request,"patientIdTypeList");
        if(patientIdTypeList != null){
            List<String> selectValList = Arrays.asList(patientIdTypeList);
            arFilterDto.setPatientIdTypeList(selectValList);
        }else {
            arFilterDto.setPatientIdTypeList(null);
        }
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
//more
        String[] indicationArCycle=ParamUtil.getStrings(request,"indicationArCycle");
        if(indicationArCycle != null){
            List<String> selectValList = Arrays.asList(indicationArCycle);
            arFilterDto.setIndicationArCycleList(selectValList);
        }else {
            arFilterDto.setIndicationArCycleList(null);
        }
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
        String embryosTransferredNum0=ParamUtil.getString(request,"embryosTransferredNum0");
        arFilterDto.setEmbryosTransferredNum0(embryosTransferredNum0);
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


    private ArEnquiryTransactionHistoryFilterDto setArEnquiryTransactionHistoryFilterDto(HttpServletRequest request) throws ParseException{
        ArEnquiryTransactionHistoryFilterDto arFilterDto=new ArEnquiryTransactionHistoryFilterDto();
        String arCentre=ParamUtil.getString(request,"arCentre");
        arFilterDto.setArCentre(arCentre);
        Date submissionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "submissionDateFrom"));
        arFilterDto.setSubmissionDateFrom(submissionDateFrom);
        Date submissionDateTo= Formatter.parseDate(ParamUtil.getString(request, "submissionDateTo"));
        arFilterDto.setSubmissionDateTo(submissionDateTo);
        String cycleNumber=ParamUtil.getString(request,"cycleNumber");
        arFilterDto.setCycleNumber(cycleNumber);
        String includeTransfers=ParamUtil.getString(request,"includeTransfers");
        arFilterDto.setIncludeTransfers(includeTransfers);
        ParamUtil.setSessionAttr(request,"arTransactionHistoryFilterDto",arFilterDto);

        return arFilterDto;

    }

    private void setQueryFilter(AssistedReproductionEnquiryFilterDto arDto,FilterParameter filterParameter,int sqf){
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        if(arDto.getArCentre()!=null) {
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
        if(sqf==1&&arDto.getSubmissionType()!=null){
            filter.put("submissionType",arDto.getSubmissionType());
            if(arDto.getSubmissionType().equals(DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE)){
                if(arDto.getCycleStage()!=null){
                    filter.put("cycleStage",arDto.getCycleStage());
                }
            }
        }
        if(sqf==1||sqf==2){
            if(arDto.getSubmissionId()!=null){
                filter.put("submissionId",arDto.getSubmissionId());
            }


            if(arDto.getSubmissionDateFrom()!=null){
                String submissionDateFrom = Formatter.formatDateTime(arDto.getSubmissionDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("submission_start_date", submissionDateFrom);
            }

            if(arDto.getSubmissionDateTo()!=null){
                String submissionDateTo = Formatter.formatDateTime(arDto.getSubmissionDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
                filter.put("submission_to_date", submissionDateTo);
            }
        }
        if(sqf==2){


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

            if(arDto.getHusbandIdNumber()!=null){
                filter.put("husbandIdNumber",arDto.getHusbandIdNumber());
            }

            if(arDto.getEmbryologist()!=null){
                filter.put("embryologist",arDto.getEmbryologist());
            }
            if(arDto.getArPractitioner()!=null){
                filter.put("arPractitioner",arDto.getArPractitioner());
            }

            if(arDto.getCycleStagesStatus()!=null){
                if(arDto.getCycleStagesStatus().equals(DataSubmissionConsts.DS_STATUS_COMPLETED)){
                    filter.put("cycleStagesFinalStatus",arDto.getCycleStagesStatus());

                }else {
                    filter.put("cycleStagesStatus",arDto.getCycleStagesStatus());
                }
            }
            if(arDto.getCycleStagesDateFrom()!=null){
                String cycleStagesDateFrom = Formatter.formatDateTime(arDto.getCycleStagesDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("cycleStagesDateFrom", cycleStagesDateFrom);
            }
            if(arDto.getCycleStagesDateTo()!=null){
                String cycleStagesDateTo = Formatter.formatDateTime(arDto.getCycleStagesDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
                filter.put("cycleStagesDateTo", cycleStagesDateTo);
            }
            if(arDto.getArOrIuiCycle()!=null){
                filter.put("arOrIui",arDto.getArOrIuiCycle());
            }
            if(arDto.getIVM()!=null){//bit
                filter.put("ivm", Integer.valueOf(arDto.getIVM()));
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

            if (arDto.getAbandonedCycle() != null) {//bit
                filter.put("abandonedCycle", Integer.valueOf(arDto.getAbandonedCycle()));
            }

            if (arDto.getDonorGameteUsed() != null) {
                if (Integer.parseInt(arDto.getDonorGameteUsed()) == 1) {
                    filter.put("donorUsedYes", Integer.valueOf(arDto.getDonorGameteUsed()));
                } else {
                    filter.put("donorUsedNo", Integer.valueOf(arDto.getDonorGameteUsed()));
                }
            }
            if(arDto.getDonorName()!=null){
                filter.put("donorName", arDto.getDonorName());
            }
            if(arDto.getDonorIdNumber()!=null){
                filter.put("donorIdNumber",arDto.getDonorIdNumber());
            }
            //todo not found
//            if(arDto.getRemovedFromStorage()!=null){
//                filter.put("removedFromStorage", arDto.getRemovedFromStorage());
//            }
            //todo not found
//            if(arDto.getEmbryosStoredBeyond()!=null){
//                filter.put("embryosStoredBeyond",Integer.parseInt(arDto.getEmbryosStoredBeyond()));
//            }
            if(arDto.getSourceSemen()!=null){
                if("Donor".equals(arDto.getSourceSemen())){
                    filter.put("FROM_DONOR",1);
                }
                if("Husband".equals(arDto.getSourceSemen())){
                    filter.put("FROM_HUSBAND",1);
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
            if(arDto.getEmbryosTransferredNum0()!=null&& "on".equals(arDto.getEmbryosTransferredNum0())){
                embryosTransferredNums.add(0);
            }
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
            if(arDto.getBirthEventsTotalMax()!=null&& "on".equals(arDto.getBirthEventsTotalMax())){
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
                        SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
                filter.put("deliveryDateTo", deliveryDateTo);
            }

            if(arDto.getPatientART()!=null){
                if("0".equals(arDto.getPatientART())){
                    filter.put("patientArtYes",1);
                }
                if("1".equals(arDto.getPatientART())){
                    filter.put("patientArtNo",1);
                }
            }
            if(arDto.getPatientIUI()!=null){
                if("0".equals(arDto.getPatientIUI())){
                    filter.put("patientIuiYes",1);

                }
                if("1".equals(arDto.getPatientIUI())){
                    filter.put("patientIuiNo",1);
                }
            }
            if(arDto.getPatientPGT()!=null){
                if("0".equals(arDto.getPatientPGT())){
                    filter.put("patientPgtYes",1);

                }
                if("1".equals(arDto.getPatientPGT())){
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
                        SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
                filter.put("disposalDateTo", disposalDateTo);
            }
            if(arDto.getTransferInOrOut()!=null){
                if("1".equals(arDto.getTransferInOrOut())){
                    filter.put("transferInOrOut", "in");
                }
                if("0".equals(arDto.getTransferInOrOut())){
                    filter.put("transferInOrOut", "out");
                }
            }

            if(arDto.getTransferredOocyte()!=null&& "on".equals(arDto.getTransferredOocyte())){
                filter.put("transferredOocyte",0);
            }
            if(arDto.getTransferredEmbryo()!=null&& "on".equals(arDto.getTransferredEmbryo())){
                filter.put("transferredEmbryo",0);
            }
            if(arDto.getTransferredSperm()!=null&& "on".equals(arDto.getTransferredSperm())){
                filter.put("transferredSperm",0);
            }
            if(StringUtil.isNotEmpty(arDto.getTransferredInFrom())) {
                filter.put("transferredInFrom", arDto.getTransferredInFrom());
            }
            if(StringUtil.isNotEmpty(arDto.getTransferOutTo())) {
                filter.put("transferOutTo", arDto.getTransferOutTo());
            }

            if(arDto.getTransferDateFrom()!=null){
                String transferDateFrom = Formatter.formatDateTime(arDto.getTransferDateFrom(),
                        SystemAdminBaseConstants.DATE_FORMAT);
                filter.put("transferDateFrom", transferDateFrom);
            }

            if(arDto.getTransferDateTo()!=null){
                String transferDateTo = Formatter.formatDateTime(arDto.getTransferDateTo(),
                        SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
                filter.put("transferDateTo", transferDateTo);
            }
            if(arDto.getPGT()!=null){
                if("1".equals(arDto.getPGT())){
                    filter.put("pgtYes", 1);
                }
                if("0".equals(arDto.getPGT())){
                    filter.put("pgtNo", 1);
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
                if("1".equals(arDto.getPgtDisease())){
                    filter.put("PGT_M_DSLD", 1);

                }
                if("0".equals(arDto.getPgtDisease())){
                    filter.put("PGT_M_DSLD", 0);

                }
            }
        }

        filterParameter.setFilters(filter);

    }

    public void baseSearch(BaseProcessClass bpc)throws ParseException{
        HttpServletRequest request = bpc.request;
        LoginContext loginContext=(LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<SelectOption> submissionTypeOptions= IaisCommonUtils.genNewArrayList();
        submissionTypeOptions.add(new SelectOption("AR_TP001","Patient Information"));
        submissionTypeOptions.add(new SelectOption("AR_TP002","Cycle Stage"));
        submissionTypeOptions.add(new SelectOption("AR_TP003","Donor Sample"));
        ParamUtil.setRequestAttr(bpc.request,"submissionTypeOptions",submissionTypeOptions);
        List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions("null",loginContext.getOrgId());
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);
        List<SelectOption> stageTypeSelectOption= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DS_STAGE_TYPE);
        stageTypeSelectOption.add(new SelectOption(DataSubmissionConsts.AR_CYCLE_AR,MasterCodeUtil.getCodeDesc(DataSubmissionConsts.AR_CYCLE_AR)));
        stageTypeSelectOption.add(new SelectOption(DataSubmissionConsts.AR_CYCLE_IUI,MasterCodeUtil.getCodeDesc(DataSubmissionConsts.AR_CYCLE_IUI)));
        stageTypeSelectOption.add(new SelectOption(DataSubmissionConsts.AR_CYCLE_EFO,MasterCodeUtil.getCodeDesc(DataSubmissionConsts.AR_CYCLE_EFO)));
        ParamUtil.setRequestAttr(bpc.request,"stageTypeSelectOption",stageTypeSelectOption);


        ParamUtil.setSessionAttr(request,"arViewFull",null);
        String action = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        SearchParam searchParamForPat = (SearchParam) ParamUtil.getSessionAttr(request, "patientParam");
        SearchParam searchParamForSub = (SearchParam) ParamUtil.getSessionAttr(request, "submissionParam");
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");

        if(!"backBase".equals(action)){
            AssistedReproductionEnquiryFilterDto arFilterDtoOld= (AssistedReproductionEnquiryFilterDto) ParamUtil.getSessionAttr(request,"assistedReproductionEnquiryFilterDto");
            String searchBy=arFilterDtoOld.getSearchBy();
            AssistedReproductionEnquiryFilterDto arFilterDto= setAssistedReproductionEnquiryFilterDto(request);
            if("1".equals(arFilterDto.getSearchBy())){
                if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                    if(patientSortFieldNames.contains(sortFieldName)){
                        patientParameter.setSortType(sortType);
                        patientParameter.setSortField(sortFieldName);
                    }
                }

                setQueryFilter(arFilterDto,patientParameter,0);
                SearchParam patientParam = SearchResultHelper.getSearchParam(request, patientParameter,true);
                if(patientParam.getSortMap().containsKey("ID_TYPE_DESC")){
                    HalpSearchResultHelper.setMasterCodeForSearchParam(patientParam,"dpi.ID_TYPE","ID_TYPE_DESC",MasterCodeUtil.CATE_ID_DS_ID_TYPE);
                }else if(patientParam.getSortMap().containsKey("NATIONALITY_DESC")){
                    HalpSearchResultHelper.setMasterCodeForSearchParam(patientParam,"dpi.NATIONALITY","NATIONALITY_DESC",MasterCodeUtil.CATE_ID_NATIONALITY);
                }
                patientParam.addFilter("dc_licenseeId",loginContext.getLicenseeId(),true);
                if(IaisCommonUtils.isNotEmpty(arFilterDto.getPatientIdTypeList())){
                    String patientIdTypeListStr = SqlHelper.constructInCondition("dpi.ID_TYPE", arFilterDto.getPatientIdTypeList().size());
                    patientParam.addParam("patient_id_types", patientIdTypeListStr);
                    for(int i = 0; i < arFilterDto.getPatientIdTypeList().size(); i++){
                        patientParam.addFilter("dpi.ID_TYPE" + i, arFilterDto.getPatientIdTypeList().get(i));
                    }
                }
                if(searchParamForPat!=null){
                    patientParam.setPageNo(searchParamForPat.getPageNo());
                    patientParam.setPageSize(searchParamForPat.getPageSize());
                }
                if(searchBy.equals(arFilterDto.getSearchBy())){
                    CrudHelper.doPaging(patientParam,bpc.request);
                }
                patientParam.addSort("ID",SearchParam.DESCENDING);
                QueryHelp.setMainSql("onlineEnquiry","searchPatientByAssistedReproduction",patientParam);

                SearchResult<AssistedReproductionEnquiryResultsDto> patientResult = assistedReproductionService.searchPatientByParam(patientParam);
                ParamUtil.setRequestAttr(request,"patientResult",patientResult);
                ParamUtil.setSessionAttr(request,"patientParam",patientParam);
            }else if("0".equals(arFilterDto.getSearchBy())){
                if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                    if(submissionSortFieldNames.contains(sortFieldName)){
                        submissionParameter.setSortType(sortType);
                        submissionParameter.setSortField(sortFieldName);
                    }
                }
                setQueryFilter(arFilterDto,submissionParameter,1);
                SearchParam submissionParam = SearchResultHelper.getSearchParam(request, submissionParameter,true);
                if(submissionParam.getSortMap().containsKey("CYCLE_STAGE_DESC")){
                    HalpSearchResultHelper.setMasterCodeForSearchParam(submissionParam,"CYCLE_STAGE","CYCLE_STAGE_DESC",MasterCodeUtil.CATE_ID_DS_STAGE_TYPE);
                }
                submissionParam.addFilter("dc_licenseeId",loginContext.getLicenseeId(),true);
                if(searchParamForSub!=null){
                    submissionParam.setPageNo(searchParamForSub.getPageNo());
                    submissionParam.setPageSize(searchParamForSub.getPageSize());
                }
                if(searchBy.equals(arFilterDto.getSearchBy())){
                    CrudHelper.doPaging(submissionParam,bpc.request);
                }
                QueryHelp.setMainSql("onlineEnquiry","searchSubmissionByAssistedReproduction",submissionParam);
                SearchResult<AssistedReproductionEnquirySubResultsDto> submissionResult = assistedReproductionService.searchSubmissionByParam(submissionParam);
                if(IaisCommonUtils.isNotEmpty(submissionResult.getRows())){
                    for (AssistedReproductionEnquirySubResultsDto subResultsDto:submissionResult.getRows()
                    ) {
                        switch (subResultsDto.getSubmissionType()){
                            case "AR_TP001":subResultsDto.setSubmissionType("Patient Information");break;
                            case "AR_TP002":subResultsDto.setSubmissionType("Cycle Stage");break;
                            case "AR_TP003":subResultsDto.setSubmissionType("Donor Sample");break;
                            default:subResultsDto.setSubmissionType(MasterCodeUtil.getCodeDesc(subResultsDto.getSubmissionType()));
                        }
                    }
                }
                ParamUtil.setSessionAttr(request,"submissionParam",submissionParam);
                ParamUtil.setRequestAttr(request,"submissionResult",submissionResult);
            }
        }else {
            if(searchParamForPat != null){
                SearchResult<AssistedReproductionEnquiryResultsDto> patientResult = assistedReproductionService.searchPatientByParam(searchParamForPat);
                ParamUtil.setRequestAttr(request,"patientResult",patientResult);
                ParamUtil.setSessionAttr(request,"patientParam",searchParamForPat);
            }
            if(searchParamForSub != null){
                SearchResult<AssistedReproductionEnquirySubResultsDto> submissionResult = assistedReproductionService.searchSubmissionByParam(searchParamForSub);
                if(IaisCommonUtils.isNotEmpty(submissionResult.getRows())){
                    for (AssistedReproductionEnquirySubResultsDto subResultsDto:submissionResult.getRows()
                    ) {
                        switch (subResultsDto.getSubmissionType()){
                            case "AR_TP001":subResultsDto.setSubmissionType("Patient Information");break;
                            case "AR_TP002":subResultsDto.setSubmissionType("Cycle Stage");break;
                            case "AR_TP003":subResultsDto.setSubmissionType("Donor Sample");break;
                            default:subResultsDto.setSubmissionType(MasterCodeUtil.getCodeDesc(subResultsDto.getSubmissionType()));
                        }
                    }
                }
                ParamUtil.setSessionAttr(request,"submissionParam",searchParamForSub);
                ParamUtil.setRequestAttr(request,"submissionResult",submissionResult);
            }
        }



        ParamUtil.setSessionAttr(request,"arBase",1);
        ParamUtil.setSessionAttr(request,"arAdv",0);


    }

    public void changePagination(BaseProcessClass bpc){

    }

    public void advNextStep(BaseProcessClass bpc){

    }

    public void perAdvancedSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"arViewFull",null);
        List<SelectOption> aRorIUICycleOptions= IaisCommonUtils.genNewArrayList();
        aRorIUICycleOptions.add(new SelectOption(DataSubmissionConsts.DS_CYCLE_AR,"AR"));
        aRorIUICycleOptions.add(new SelectOption(DataSubmissionConsts.DS_CYCLE_IUI,"IUI"));
        ParamUtil.setRequestAttr(bpc.request,"aRorIUICycleOptions",aRorIUICycleOptions);
        List<SelectOption> cycleStagesStatusOptions= IaisCommonUtils.genNewArrayList();
        cycleStagesStatusOptions.add(new SelectOption(DataSubmissionConsts.DS_STATUS_ACTIVE,MasterCodeUtil.getCodeDesc(DataSubmissionConsts.DS_STATUS_ACTIVE)));
        cycleStagesStatusOptions.add(new SelectOption(DataSubmissionConsts.DS_STATUS_COMPLETED,MasterCodeUtil.getCodeDesc(DataSubmissionConsts.DS_STATUS_COMPLETED)));
        cycleStagesStatusOptions.add(new SelectOption(DataSubmissionConsts.DS_STATUS_ONGOING,MasterCodeUtil.getCodeDesc(DataSubmissionConsts.DS_STATUS_ONGOING)));
        cycleStagesStatusOptions.add(new SelectOption(DataSubmissionConsts.DS_STATUS_WITHDRAW,MasterCodeUtil.getCodeDesc(DataSubmissionConsts.DS_STATUS_WITHDRAW)));
        ParamUtil.setRequestAttr(bpc.request,"cycleStagesStatusOptions",cycleStagesStatusOptions);
        List<SelectOption> sourceSemenOptions= IaisCommonUtils.genNewArrayList();
        sourceSemenOptions.add(new SelectOption("Donor","Donor"));
        sourceSemenOptions.add(new SelectOption("Husband","Husband"));
        ParamUtil.setRequestAttr(bpc.request,"sourceSemenOptions",sourceSemenOptions);
        List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions("null","null");
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);
        String action = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "patientParam");

        if(!"backAdv".equals(action)||searchParam==null){
            AssistedReproductionEnquiryFilterDto arFilterDto= setAssistedReproductionEnquiryFilterDto(request);

            setQueryFilter(arFilterDto,patientAdvParameter,2);
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                patientAdvParameter.setSortType(sortType);
                patientAdvParameter.setSortField(sortFieldName);
            }

            SearchParam patientParam = SearchResultHelper.getSearchParam(request, patientAdvParameter,true);

            if(IaisCommonUtils.isNotEmpty(arFilterDto.getIndicationArCycleList())){
                String mainIndicationListStr = SqlHelper.constructInCondition("dacs.MAIN_INDICATION", arFilterDto.getIndicationArCycleList().size());
                patientParam.addParam("indicationArCycleList", mainIndicationListStr);
                for(int i = 0; i < arFilterDto.getIndicationArCycleList().size(); i++){
                    patientParam.addFilter("dacs.MAIN_INDICATION" + i, arFilterDto.getIndicationArCycleList().get(i));
                }
            }
            if(IaisCommonUtils.isNotEmpty(arFilterDto.getPatientIdTypeList())){
                String patientIdTypeListStr = SqlHelper.constructInCondition("dpi.ID_TYPE", arFilterDto.getPatientIdTypeList().size());
                patientParam.addParam("patient_id_types", patientIdTypeListStr);
                for(int i = 0; i < arFilterDto.getPatientIdTypeList().size(); i++){
                    patientParam.addFilter("dpi.ID_TYPE" + i, arFilterDto.getPatientIdTypeList().get(i));
                }
            }
            if(IaisCommonUtils.isNotEmpty(arFilterDto.getHusbandIdTypeList())){
                String husbandIdTypeListStr = SqlHelper.constructInCondition("dh.ID_TYPE", arFilterDto.getHusbandIdTypeList().size());
                patientParam.addParam("husband_id_types", husbandIdTypeListStr);
                for(int i = 0; i < arFilterDto.getHusbandIdTypeList().size(); i++){
                    patientParam.addFilter("dh.ID_TYPE" + i, arFilterDto.getHusbandIdTypeList().get(i));
                }
            }
            if(IaisCommonUtils.isNotEmpty(arFilterDto.getDonorIdTypeList())){
                String donorIdTypeListStrAr = SqlHelper.constructInCondition("dad_donor.ID_TYPE", arFilterDto.getDonorIdTypeList().size());
                patientParam.addParam("donor_id_types_ar", donorIdTypeListStrAr);
                for(int i = 0; i < arFilterDto.getDonorIdTypeList().size(); i++){
                    patientParam.addFilter("dad_donor.ID_TYPE" + i, arFilterDto.getDonorIdTypeList().get(i));
                }

                String donorIdTypeListStrIui = SqlHelper.constructInCondition("did_donor.ID_TYPE", arFilterDto.getDonorIdTypeList().size());
                patientParam.addParam("donor_id_types_iui", donorIdTypeListStrIui);
                for(int i = 0; i < arFilterDto.getDonorIdTypeList().size(); i++){
                    patientParam.addFilter("did_donor.ID_TYPE" + i, arFilterDto.getDonorIdTypeList().get(i));
                }
            }
            if(patientParam.getSortMap().containsKey("ID_TYPE_DESC")){
                HalpSearchResultHelper.setMasterCodeForSearchParam(patientParam,"dpi.ID_TYPE","ID_TYPE_DESC",MasterCodeUtil.CATE_ID_DS_ID_TYPE);
            }else if(patientParam.getSortMap().containsKey("NATIONALITY_DESC")){
                HalpSearchResultHelper.setMasterCodeForSearchParam(patientParam,"dpi.NATIONALITY","NATIONALITY_DESC",MasterCodeUtil.CATE_ID_NATIONALITY);
            }
            if(searchParam!=null){
                patientParam.setPageNo(searchParam.getPageNo());
                patientParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(patientParam,bpc.request);
            patientParam.addSort("ID",SearchParam.DESCENDING);
            QueryHelp.setMainSql("onlineEnquiry","advancedSearchPatientByAssistedReproduction",patientParam);
            SearchResult<AssistedReproductionEnquiryResultsDto> patientResult = assistedReproductionService.searchPatientByParam(patientParam);
            ParamUtil.setRequestAttr(request,"patientResult",patientResult);
            ParamUtil.setSessionAttr(request,"patientParam",patientParam);
        }else {
            SearchResult<AssistedReproductionEnquiryResultsDto> patientResult = assistedReproductionService.searchPatientByParam(searchParam);
            ParamUtil.setRequestAttr(request,"patientResult",patientResult);
            ParamUtil.setSessionAttr(request,"patientParam",searchParam);
        }


        ParamUtil.setSessionAttr(request,"arBase",0);
        ParamUtil.setSessionAttr(request,"arAdv",1);
    }
    public void preViewFullDetails(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request=bpc.request;
        String additional=ParamUtil.getRequestString(request, InboxConst.CRUD_ACTION_ADDITIONAL);
        String key=ParamUtil.getRequestString(request, InboxConst.CRUD_ACTION_VALUE);
        PatientInfoDto patientInfoDto=null;
        if(StringUtil.isNotEmpty(additional)&&StringUtil.isNotEmpty(key)){
            request.getSession().removeAttribute("arTransactionHistoryFilterDto");
            if("patient".equals(additional)){
                patientInfoDto=assistedReproductionService.patientInfoDtoByPatientCode(key);

            }
            if("submission".equals(additional)){
                AssistedReproductionEnquiryFilterDto arFilterDto= (AssistedReproductionEnquiryFilterDto) ParamUtil.getSessionAttr(request,"assistedReproductionEnquiryFilterDto");
                arFilterDto.setSearchBy("0");
                ParamUtil.setSessionAttr(request,"assistedReproductionEnquiryFilterDto",arFilterDto);
                String submissionType=ParamUtil.getRequestString(request, InboxConst.SWITCH_ACTION);
                if(StringUtil.isNotEmpty(submissionType)&& "Donor Sample".equals(submissionType)){
                    StringBuilder url = new StringBuilder();
                    url.append(InboxConst.URL_HTTPS)
                            .append(bpc.request.getServerName())
                            .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohOnlineEnquiryDonorSample/1/perDonorInfo");
                    String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                    String submissionIdNo=ParamUtil.getRequestString(request, "crud_type");
                    ParamUtil.setSessionAttr(request,"submissionIdNo",submissionIdNo);

                    IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
                    return;
                }
                patientInfoDto= assistedReproductionService.patientInfoDtoBySubmissionId(key);

            }
            if(patientInfoDto!=null){
                DsRfcHelper.handle(patientInfoDto);
                ParamUtil.setSessionAttr(request,"patientInfoDto",patientInfoDto);
                ParamUtil.setSessionAttr(request,"arViewFull",null);

                LinkedHashMap<String, ArCurrentInventoryDto> arCurrentInventoryDtoLinkedHashMap=new LinkedHashMap<>(patientInfoDto.getPatient().getArCentres().size()+1);
                ArCurrentInventoryDto patientInventoryDtoTotal=new ArCurrentInventoryDto();
                int currentFrozenOocytes=0;
                int currentThawedOocytes=0;
                int currentFreshOocytes=0;
                int currentFrozenEmbryos=0;
                int currentThawedEmbryos=0;
                int currentFreshEmbryos=0;
                int currentFrozenSperms=0;
                List<ArCurrentInventoryDto> arCurrentInventoryDtos=assistedReproductionService.arCurrentInventoryDtosByPatientCode(patientInfoDto.getPatient().getPatientCode());

                List<PremisesDto> premisesDtos=assistedReproductionClient.getAllArCenterPremisesDtoByPatientCode(patientInfoDto.getPatient().getPatientCode(),"null").getEntity();
                Map<String, PremisesDto> premisesMap = IaisCommonUtils.genNewHashMap();
                if(IaisCommonUtils.isNotEmpty(premisesDtos)){
                    for (PremisesDto premisesDto : premisesDtos) {
                        if(premisesDto!=null){
                            premisesMap.put(premisesDto.getHciCode(), premisesDto);
                        }
                    }
                }
                for (ArCurrentInventoryDto aci:arCurrentInventoryDtos
                ) {
                    if(aci!=null){
                        currentFrozenOocytes+=aci.getFrozenOocyteNum();
                        currentThawedOocytes+=aci.getThawedOocyteNum();
                        currentFreshOocytes+=aci.getFreshOocyteNum();
                        currentFrozenEmbryos+=aci.getFrozenEmbryoNum();
                        currentThawedEmbryos+=aci.getThawedEmbryoNum();
                        currentFreshEmbryos+=aci.getFreshEmbryoNum();
                        currentFrozenSperms+=aci.getFrozenSpermNum();
                        if(premisesMap.containsKey(aci.getHciCode())){
                            arCurrentInventoryDtoLinkedHashMap.put(premisesMap.get(aci.getHciCode()).getPremiseLabel(),aci);
                        }
                    }
                }
                patientInventoryDtoTotal.setThawedEmbryoNum(currentThawedEmbryos);
                patientInventoryDtoTotal.setThawedOocyteNum(currentThawedOocytes);
                patientInventoryDtoTotal.setFreshOocyteNum(currentFreshOocytes);
                patientInventoryDtoTotal.setFreshEmbryoNum(currentFreshEmbryos);
                patientInventoryDtoTotal.setFrozenEmbryoNum(currentFrozenEmbryos);
                patientInventoryDtoTotal.setFrozenOocyteNum(currentFrozenOocytes);
                patientInventoryDtoTotal.setFrozenSpermNum(currentFrozenSperms);
                arCurrentInventoryDtoLinkedHashMap.put("Total",patientInventoryDtoTotal);
                ParamUtil.setSessionAttr(request,"patientInventoryDtos",  arCurrentInventoryDtoLinkedHashMap);

                ArEnquiryCoFundingHistoryDto arCoFundingDto= assistedReproductionService.patientCoFundingHistoryByCode(patientInfoDto.getPatient().getPatientCode());
                ParamUtil.setSessionAttr(request,"arCoFundingDto", arCoFundingDto);

            }else if("patient".equals(additional)||"submission".equals(additional)){
                ParamUtil.setSessionAttr(request,"patientInfoDto",null);
                ParamUtil.setSessionAttr(request,"patientInventoryDtos", null);
                ParamUtil.setSessionAttr(request,"arCoFundingDto", null);

            }
        }





    }

    public void searchInventory(BaseProcessClass bpc) throws ParseException{
        ParamUtil.setRequestAttr(bpc.request, "preActive", "1");
        HttpServletRequest request=bpc.request;
        PatientInfoDto patientInfoDto= (PatientInfoDto) ParamUtil.getSessionAttr(request,"patientInfoDto");
        if(patientInfoDto!=null){
            List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions(patientInfoDto.getPatient().getPatientCode(),"null");
            ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);
        }
        ParamUtil.setSessionAttr(request,"arViewFull",1);
        String action = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("searchInv".equals(action)){
            ArEnquiryTransactionHistoryFilterDto arDto=setArEnquiryTransactionHistoryFilterDto(request);

            if(patientInfoDto!=null){

                Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
                filter.put("patientCode", patientInfoDto.getPatient().getPatientCode());

                if(arDto.getArCentre()!=null) {
                    filter.put("arCentre", arDto.getArCentre());
                }
                if(arDto.getSubmissionDateFrom()!=null){
                    String submissionDateFrom = Formatter.formatDateTime(arDto.getSubmissionDateFrom(),
                            SystemAdminBaseConstants.DATE_FORMAT);
                    filter.put("submission_start_date", submissionDateFrom);
                }

                if(arDto.getSubmissionDateTo()!=null){
                    String submissionDateTo = Formatter.formatDateTime(arDto.getSubmissionDateTo(),
                            SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
                    filter.put("submission_to_date", submissionDateTo);
                }
                if(arDto.getSubmissionDateFrom()==null&&arDto.getSubmissionDateTo()==null){
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.YEAR, -1);
                    Date y = c.getTime();
                    String submissionDateFrom = Formatter.formatDateTime(y,
                            SystemAdminBaseConstants.DATE_FORMAT);
                    filter.put("submission_start_date", submissionDateFrom);
                }
                if(arDto.getIncludeTransfers()!=null) {
                    filter.put("transfers", 1);
                }else {
                    filter.put("transfersNotIn", 1);
                }
                if(arDto.getCycleNumber()!=null) {
                    filter.put("cycleNo", Integer.valueOf(arDto.getCycleNumber()));
                }
                transactionParameter.setFilters(filter);

                String sortFieldName = ParamUtil.getString(request,"crud_action_value");
                String sortType = ParamUtil.getString(request,"crud_action_additional");
                if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                    transactionParameter.setSortType(sortType);
                    transactionParameter.setSortField(sortFieldName);
                }

                SearchParam transactionParam = SearchResultHelper.getSearchParam(request, transactionParameter,true);
                if(transactionParam.getSortMap().containsKey("CYCLE_STAGE_DESC")){
                    HalpSearchResultHelper.setMasterCodeForSearchParam(transactionParam,"CYCLE_STAGE","CYCLE_STAGE_DESC",MasterCodeUtil.CATE_ID_DS_STAGE_TYPE);
                }
                SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request,"transactionParam");
                if(searchParam!=null){
                    transactionParam.setPageNo(searchParam.getPageNo());
                    transactionParam.setPageSize(searchParam.getPageSize());
                }
                CrudHelper.doPaging(transactionParam,bpc.request);

                QueryHelp.setMainSql("onlineEnquiry","searchTransactionHistoryByAssistedReproduction",transactionParam);
                SearchResult<ArEnquiryTransactionHistoryResultDto> transactionResult = assistedReproductionService.searchTransactionHistoryByParam(transactionParam);
                ParamUtil.setRequestAttr(request,"transactionResult",transactionResult);
                ParamUtil.setSessionAttr(request,"transactionParam",transactionParam);
            }


        }else {
            SearchParam transactionParam = (SearchParam) ParamUtil.getSessionAttr(request,"transactionParam");
            SearchResult<ArEnquiryTransactionHistoryResultDto> transactionResult = assistedReproductionService.searchTransactionHistoryByParam(transactionParam);
            ParamUtil.setRequestAttr(request,"transactionResult",transactionResult);
            ParamUtil.setSessionAttr(request,"transactionParam",transactionParam);
        }



    }

    public void searchCycle(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request, "preActive", "2");
        HttpServletRequest request=bpc.request;
        ParamUtil.setSessionAttr(request,"arViewFull",2);
        String action = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("searchCyc".equals(action)){
            PatientInfoDto patientInfoDto= (PatientInfoDto) ParamUtil.getSessionAttr(request,"patientInfoDto");
            Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
            if(patientInfoDto!=null){
                filter.put("patientCode", patientInfoDto.getPatient().getPatientCode());
                cycleStageParameter.setFilters(filter);
                String sortFieldName = ParamUtil.getString(request,"crud_action_value");
                String sortType = ParamUtil.getString(request,"crud_action_additional");
                if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                    cycleStageParameter.setSortType(sortType);
                    cycleStageParameter.setSortField(sortFieldName);
                }

                SearchParam cycleStageParam = SearchResultHelper.getSearchParam(request, cycleStageParameter,true);
                SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request,"cycleStageParam");
                if(searchParam!=null){
                    cycleStageParam.setPageNo(searchParam.getPageNo());
                    cycleStageParam.setPageSize(searchParam.getPageSize());
                }
                CrudHelper.doPaging(cycleStageParam,bpc.request);
                QueryHelp.setMainSql("onlineEnquiry","searchCycleStageByPatientCode",cycleStageParam);
                SearchResult<ArEnquiryCycleStageDto> cycleStageResult = assistedReproductionService.searchCycleStageByParam(cycleStageParam);

                SearchParam nonCycleStageParam = SearchResultHelper.getSearchParam(request, cycleStageParameter,true);
                if(searchParam!=null){
                    nonCycleStageParam.setPageNo(searchParam.getPageNo());
                    nonCycleStageParam.setPageSize(searchParam.getPageSize());
                }
                CrudHelper.doPaging(nonCycleStageParam,bpc.request);
                QueryHelp.setMainSql("onlineEnquiry","searchNonCycleStageByPatientCode",nonCycleStageParam);
                SearchResult<ArEnquiryCycleStageDto> nonCycleStageResult = assistedReproductionService.searchCycleStageByParam(nonCycleStageParam);

                if(nonCycleStageResult.getRowCount()>cycleStageResult.getRowCount()){
                    cycleStageResult.setRowCount(nonCycleStageResult.getRowCount());
                }
                ParamUtil.setRequestAttr(request,"noCycleResult",nonCycleStageResult);
                ParamUtil.setRequestAttr(request,"cycleStageResult",cycleStageResult);
                ParamUtil.setSessionAttr(request,"cycleStageParam",cycleStageParam);
                ParamUtil.setSessionAttr(request,"nonCycleStageParam",nonCycleStageParam);
            }
        }else {
            SearchParam cycleStageParam = (SearchParam) ParamUtil.getSessionAttr(request,"cycleStageParam");
            SearchResult<ArEnquiryCycleStageDto> cycleStageResult = assistedReproductionService.searchCycleStageByParam(cycleStageParam);
            SearchParam nonCycleStageParam = (SearchParam) ParamUtil.getSessionAttr(request,"nonCycleStageParam");
            SearchResult<ArEnquiryCycleStageDto> nonCycleStageResult = assistedReproductionService.searchCycleStageByParam(nonCycleStageParam);
            if(nonCycleStageResult.getRowCount()>cycleStageResult.getRowCount()){
                cycleStageResult.setRowCount(nonCycleStageResult.getRowCount());
            }
            ParamUtil.setRequestAttr(request,"noCycleResult",nonCycleStageResult);
            ParamUtil.setRequestAttr(request,"cycleStageResult",cycleStageResult);
            ParamUtil.setSessionAttr(request,"cycleStageParam",cycleStageParam);
            ParamUtil.setSessionAttr(request,"nonCycleStageParam",nonCycleStageParam);
        }


    }

    public void perStep(BaseProcessClass bpc){


    }


    public void perStageInfo(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String cycleId = ParamUtil.getString(request,"crud_action_value");
        String submissionNo = ParamUtil.getString(request,"crud_action_additional");
        String oldId = ParamUtil.getString(request,"crud_type");
        String arSuperVisSubmissionNo = ParamUtil.getString(request,"arSuperVisSubmissionNo");
        if("${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}".equals(submissionNo)){
            submissionNo=arSuperVisSubmissionNo;
        }

        if(StringUtil.isNotEmpty(cycleId)){
            List<DataSubmissionDto> cycleStageList=assistedReproductionService.allDataSubmissionByCycleId(cycleId);
            cycleStageList.sort(Comparator.comparing(DataSubmissionDto::getSubmissionNo));
            ParamUtil.setSessionAttr(request,"cycleStageList", (Serializable) cycleStageList);
        }
        List<DataSubmissionDto> dataSubmissionDtoList= (List<DataSubmissionDto>) ParamUtil.getSessionAttr(request,"cycleStageList");
        if(IaisCommonUtils.isNotEmpty(dataSubmissionDtoList)){
            ArSuperDataSubmissionDto arSuper = assistedReproductionService.getArSuperDataSubmissionDto(
                    dataSubmissionDtoList.get(0).getSubmissionNo());
            if(StringUtil.isNotEmpty(submissionNo)){
                for (DataSubmissionDto dataSubmissionDto:dataSubmissionDtoList
                ) {
                    if(dataSubmissionDto.getSubmissionNo().equals(submissionNo)){
                        arSuper = assistedReproductionService.getArSuperDataSubmissionDto(
                                dataSubmissionDto.getSubmissionNo());
                        break;
                    }
                }
            }
            initDataForView(arSuper, bpc.request);
            arSuper.setDonorSampleDto(setflagMsg(arSuper.getDonorSampleDto()));
            if(IaisCommonUtils.isNotEmpty(arSuper.getOldArSuperDataSubmissionDto())){
                ArSuperDataSubmissionDto arSuperOld=arSuper.getOldArSuperDataSubmissionDto().get(0);
                List<SelectOption> versionOptions= IaisCommonUtils.genNewArrayList();
                for (ArSuperDataSubmissionDto arSdOld:arSuper.getOldArSuperDataSubmissionDto()
                ) {
                    versionOptions.add(new SelectOption(arSdOld.getDataSubmissionDto().getId(),"V "+arSdOld.getDataSubmissionDto().getVersion()));
                    if(StringUtil.isNotEmpty(oldId)&&(oldId.equals(arSdOld.getDataSubmissionDto().getId()))){
                        initDataForView(arSdOld, bpc.request);
                        arSdOld.setDonorSampleDto(setflagMsg(arSdOld.getDonorSampleDto()));
                        arSuperOld=arSdOld;
                    }
                }
                if(StringUtil.isEmpty(oldId)){
                    initDataForView(arSuperOld, bpc.request);
                    arSuperOld.setDonorSampleDto(setflagMsg(arSuperOld.getDonorSampleDto()));
                }
                ParamUtil.setRequestAttr(bpc.request,"versionOptions",versionOptions);
                ParamUtil.setRequestAttr(request,"arSuperDataSubmissionDtoVersion",arSuperOld);
            }
            ParamUtil.setRequestAttr(request,"arSuperDataSubmissionDto",arSuper);
        }

    }

    private DonorSampleDto setflagMsg(DonorSampleDto donorSampleDto){
        if(donorSampleDto != null){
            List<DonorSampleAgeDto> donorSampleAgeDtos = donorSampleDto.getDonorSampleAgeDtos();
            if(IaisCommonUtils.isNotEmpty(donorSampleAgeDtos) && !donorSampleDto.isDirectedDonation()){
                for(DonorSampleAgeDto donorSampleAgeDto : donorSampleAgeDtos){
                    int age = donorSampleAgeDto.getAge();
                    if(DataSubmissionConsts.DONOR_SAMPLE_TYPE_SPERM.equals(donorSampleDto.getSampleType())){
                        if(age <21 || age >40){
                            donorSampleDto.setAgeErrorMsg(StringUtil.viewNonNullHtml(MessageUtil.getMessageDesc("DS_ERR044")));
                            break;
                        }
                    }else if(DataSubmissionConsts.DONOR_SAMPLE_TYPE_EMBRYO.equals(donorSampleDto.getSampleType())
                            ||DataSubmissionConsts.DONOR_SAMPLE_TYPE_OOCYTE.equals(donorSampleDto.getSampleType())){
                        if(age <21 || age >35){
                            donorSampleDto.setAgeErrorMsg(StringUtil.viewNonNullHtml(MessageUtil.getMessageDesc("DS_ERR045")));
                            break;
                        }
                    }
                }
            }
        }

        return donorSampleDto;
    }

    public void initDataForView(ArSuperDataSubmissionDto arSuper, HttpServletRequest request) {
        String cycelType = Optional.ofNullable(arSuper)
                .map(ArSuperDataSubmissionDto::getCycleDto)
                .map(CycleDto::getCycleType)
                .orElse(null);
        if (DataSubmissionConsts.DS_CYCLE_PATIENT_ART.equals(cycelType)) {
            PatientInfoDto patientInfoDto=arSuper.getPatientInfoDto();
            DsRfcHelper.handle(patientInfoDto);
        }

        if (arSuper != null) {
            if(arSuper.getDataSubmissionDto().getCycleStage().equals(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY)){
                List<List<String>> defectTypesArray = IaisCommonUtils.genNewArrayList();
                List<String> otherDefectTypes = IaisCommonUtils.genNewArrayList();
                List<PregnancyOutcomeBabyDto> pregnancyOutcomeBabyDtos = arSuper.getPregnancyOutcomeStageDto().getPregnancyOutcomeBabyDtos();
                if (IaisCommonUtils.isNotEmpty(pregnancyOutcomeBabyDtos)) {
                    for (int i = 0; i < pregnancyOutcomeBabyDtos.size(); i++) {
                        PregnancyOutcomeBabyDto pregnancyOutcomeBabyDto = pregnancyOutcomeBabyDtos.get(i);
                        List<String> defectTypes = IaisCommonUtils.genNewArrayList();
                        otherDefectTypes.add("");
                        for (PregnancyOutcomeBabyDefectDto pregnancyOutcomeBabyDefectDto : pregnancyOutcomeBabyDto.getPregnancyOutcomeBabyDefectDtos()) {
                            defectTypes.add(pregnancyOutcomeBabyDefectDto.getDefectType());
                            if ("POSBDT008".equals(pregnancyOutcomeBabyDefectDto.getDefectType())) {
                                otherDefectTypes.set(i, pregnancyOutcomeBabyDefectDto.getOtherDefectType());
                            }
                        }
                        defectTypesArray.add(defectTypes);
                    }
                }
                ParamUtil.setRequestAttr(request, "defectTypesArray", defectTypesArray);
                ParamUtil.setRequestAttr(request, "otherDefectTypes", otherDefectTypes);

            }

            if(arSuper.getDataSubmissionDto().getCycleStage().equals(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING)){
                List<PgtStageDto> oldPgtList=assistedReproductionService.listPgtStageByPatientCode(arSuper.getPatientInfoDto().getPatient().getPatientCode());
                int count =0;
                if(oldPgtList!=null){
                    for (PgtStageDto pgt:oldPgtList
                    ) {
                        if(pgt.getIsPgtMEbt()+pgt.getIsPgtMCom()+pgt.getIsPgtMRare()>0 && pgt.getCreatedAt().before(arSuper.getDataSubmissionDto().getSubmitDt())){
                            count+=pgt.getIsPgtCoFunding();
                        }
                        if(pgt.getIsPgtSr()>0 && pgt.getCreatedAt().before(arSuper.getDataSubmissionDto().getSubmitDt())){
                            count+=pgt.getIsPgtCoFunding();
                        }
                    }
                }
                if(count>=6 && arSuper.getPgtStageDto().getIsPgtMRare()+arSuper.getPgtStageDto().getIsPgtMEbt()+arSuper.getPgtStageDto().getIsPgtMCom()+arSuper.getPgtStageDto().getIsPgtSr()>0 &&arSuper.getPgtStageDto().getIsPgtCoFunding()==1){
                    ParamUtil.setRequestAttr(request, "appealDisplayShow",true);
                }
            }
            List<PremisesDto> premisesDtos=assistedReproductionClient.getAllArCenterPremisesDtoByPatientCode("null","null").getEntity();
            Map<String, PremisesDto> premisesMap = IaisCommonUtils.genNewHashMap();
            if(IaisCommonUtils.isNotEmpty(premisesDtos)){
                for (PremisesDto premisesDto : premisesDtos) {
                    if(premisesDto!=null){
                        premisesMap.put(premisesDto.getHciCode(), premisesDto);
                    }
                }
            }

            Map<String, String> map = IaisCommonUtils.genNewLinkedHashMap();
            if (!premisesMap.isEmpty()) {
                for (Map.Entry<String, PremisesDto> entry : premisesMap.entrySet()) {
                    map.put(entry.getKey(), entry.getValue().getPremiseLabel());
                }
            }
            if (arSuper.getArCycleStageDto() != null) {

                if(arSuper.getPatientInfoDto() != null && arSuper.getPatientInfoDto().getPatient() !=null){
                    PatientDto patientDto = arSuper.getPatientInfoDto().getPatient();
                    List<Integer> integers = Formatter.getYearsAndDays(patientDto.getBirthDate());
                    if(IaisCommonUtils.isNotEmpty(integers)){
                        int year = integers.get(0);
                        int month = integers.get(integers.size()-1);
                        arSuper.getArCycleStageDto().setCycleAgeYear(year);
                        arSuper.getArCycleStageDto().setCycleAgeMonth(month);
                        arSuper.getArCycleStageDto().setCycleAge(IaisCommonUtils.getYearsAndMonths(year,month));
                    }

                }
                setEnhancedCounsellingTipShow(request, arSuper.getArCycleStageDto(), true);
                if(IaisCommonUtils.isNotEmpty(arSuper.getArCycleStageDto().getDonorDtos())){
                    for (DonorDto donor:arSuper.getArCycleStageDto().getDonorDtos()
                    ) {
                        if(!DataSubmissionConsts.AR_SOURCE_OTHER.equals(donor.getSource())&&map.containsKey(donor.getSource())){
                            donor.setSource(map.get(donor.getSource()));
                        }
                    }
                }
            } else if (arSuper.getIuiCycleStageDto() != null) {
                setIuiCycleStageDtoDefaultVal(arSuper);
                if(IaisCommonUtils.isNotEmpty(arSuper.getIuiCycleStageDto().getDonorDtos())){
                    for (DonorDto donor:arSuper.getIuiCycleStageDto().getDonorDtos()
                    ) {
                        if(!DataSubmissionConsts.AR_SOURCE_OTHER.equals(donor.getSource())&&map.containsKey(donor.getSource())){
                            donor.setSource(map.get(donor.getSource()));
                        }
                    }
                }
            } else if (arSuper.getTransferInOutStageDto() != null) {
                if(StringUtil.isNotEmpty(arSuper.getTransferInOutStageDto().getTransOutToHciCode())&&map.containsKey(arSuper.getTransferInOutStageDto().getTransOutToHciCode())){
                    arSuper.getTransferInOutStageDto().setTransOutToHciCode(map.get(arSuper.getTransferInOutStageDto().getTransOutToHciCode()));
                }
                if(StringUtil.isNotEmpty(arSuper.getTransferInOutStageDto().getTransInFromHciCode())&&map.containsKey(arSuper.getTransferInOutStageDto().getTransInFromHciCode())){
                    arSuper.getTransferInOutStageDto().setTransInFromHciCode(map.get(arSuper.getTransferInOutStageDto().getTransInFromHciCode()));
                }
                TransferInOutStageDto transferInOutStageDto = arSuper.getTransferInOutStageDto();
                if (StringUtil.isNotEmpty(transferInOutStageDto.getBindSubmissionId())) {
                    ArSuperDataSubmissionDto bindStageArSuperDto = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionId(transferInOutStageDto.getBindSubmissionId());
                    if (bindStageArSuperDto != null) {
                        TransferInOutDelegator.flagInAndOutDiscrepancy(request, transferInOutStageDto, bindStageArSuperDto);
                    }
                }
            }
        }

    }

    public void perNext(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String actionType = ParamUtil.getString(request,"crud_action_type");
        if("backBase".equals(actionType)||"backAdv".equals(actionType)){
            ParamUtil.setSessionAttr(request,"arViewFull",null);
        }

    }




    public void setEnhancedCounsellingTipShow(HttpServletRequest request, ArCycleStageDto arCycleStageDto, boolean needTip){
        if((arCycleStageDto.getCycleAgeYear() > 45 || arCycleStageDto.getCycleAgeYear() == 45 && arCycleStageDto.getCycleAgeMonth() > 0)
                || arCycleStageDto.getCountForEnhancedCounselling() >10){
            if(arCycleStageDto.getEnhancedCounselling() == null || !arCycleStageDto.getEnhancedCounselling()){
                if(AppConsts.YES.equalsIgnoreCase(ParamUtil.getRequestString(request,"INIT_IN_ARCYCLE_STAGE"))){
                    ParamUtil.setSessionAttr(request,"enhancedCounsellingNoShow",AppConsts.YES);
                    ParamUtil.setRequestAttr(request,"enhancedCounsellingTipShow", AppConsts.YES);
                }
                if(needTip){
                    ParamUtil.setRequestAttr(request, "DS_ERR018Tip","<p>"+MessageUtil.getMessageDesc("DS_ERR018")+"</p>");
                }
            }
        }
    }
    public ArSuperDataSubmissionDto setIuiCycleStageDtoDefaultVal(ArSuperDataSubmissionDto arSuperDataSubmission) {
        if (arSuperDataSubmission != null) {
            IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
            if (iuiCycleStageDto == null) {
                iuiCycleStageDto = new IuiCycleStageDto();
                iuiCycleStageDto.setOwnPremises(true);
                iuiCycleStageDto.setDonorDtos(IaisCommonUtils.genNewArrayList());
            }
            //set patient age show
            PatientInfoDto patientInfoDto = arSuperDataSubmission.getPatientInfoDto();
            if (patientInfoDto != null) {
                PatientDto patientDto = patientInfoDto.getPatient();
                if (patientDto != null) {
                    List<Integer> integers = Formatter.getYearsAndDays(patientDto.getBirthDate());
                    if (IaisCommonUtils.isNotEmpty(integers)) {
                        int year = integers.get(0);
                        int month = integers.get(integers.size() - 1);
                        iuiCycleStageDto.setUserAgeShow(IaisCommonUtils.getYearsAndMonths(year, month));
                    }
                }
            }
            arSuperDataSubmission.setIuiCycleStageDto(iuiCycleStageDto);
        }
        return arSuperDataSubmission;
    }

}
