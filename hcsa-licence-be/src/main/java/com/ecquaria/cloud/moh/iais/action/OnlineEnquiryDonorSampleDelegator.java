package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * OnlineEnquiryDonorSampleDelegator
 *
 * @author junyu
 * @date 2021/12/20
 */
@Delegator(value = "onlineEnquiryDonorSampleDelegator")
@Slf4j
public class OnlineEnquiryDonorSampleDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter donorSampleParameter = new FilterParameter.Builder()
            .clz(ArEnquiryDonorSampleDto.class)
            .searchAttr("donorSampleParam")
            .resultAttr("donorSampleResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;

    @Autowired
    private AssistedReproductionClient assistedReproductionClient;
    
    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_DS);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        String dspc_004 = MasterCodeUtil.getCodeDesc("DSPC_004");
        String donorMessageTip = MessageUtil.replaceMessage("DS_ERR053", dspc_004,"1");
        pageSize= Integer.valueOf(defaultValue);
        donorSampleParameter.setPageSize(pageSize);
        donorSampleParameter.setPageNo(1);
        donorSampleParameter.setSortField("ID");
        donorSampleParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"arEnquiryDonorSampleFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "donorSampleParam",null);
        ParamUtil.setSessionAttr(bpc.request, "donorResultSize", dspc_004);
        ParamUtil.setSessionAttr(bpc.request,"donorMessageTip", donorMessageTip);

    }
    private ArEnquiryDonorSampleFilterDto setArEnquiryDonorSampleFilterDto(HttpServletRequest request)  {
        ArEnquiryDonorSampleFilterDto dsFilterDto=new ArEnquiryDonorSampleFilterDto();
        String arCentre=ParamUtil.getString(request,"arCentre");
        dsFilterDto.setArCentre(arCentre);
        String donorSampleCode=ParamUtil.getString(request,"donorSampleCode");
        dsFilterDto.setDonorSampleCode(donorSampleCode);
        String sampleType=ParamUtil.getString(request,"sampleType");
        dsFilterDto.setSampleType(sampleType);
        String sampleHciCode=ParamUtil.getString(request,"sampleHciCode");
        dsFilterDto.setSampleHciCode(sampleHciCode);
        String othersSampleHciCode=ParamUtil.getString(request,"othersSampleHciCode");
        dsFilterDto.setOthersSampleHciCode(othersSampleHciCode);
        String donorIdType=ParamUtil.getString(request,"donorIdType");
        dsFilterDto.setDonorIdType(donorIdType);
        String donorIdNumber=ParamUtil.getString(request,"donorIdNumber");
        dsFilterDto.setDonorIdNumber(donorIdNumber);
        String birthEventsTotal0=ParamUtil.getString(request,"birthEventsTotal0");
        dsFilterDto.setBirthEventsTotal0(birthEventsTotal0);
        String birthEventsTotal1=ParamUtil.getString(request,"birthEventsTotal1");
        dsFilterDto.setBirthEventsTotal1(birthEventsTotal1);
        String birthEventsTotal2=ParamUtil.getString(request,"birthEventsTotal2");
        dsFilterDto.setBirthEventsTotal2(birthEventsTotal2);
        String birthEventsTotal3=ParamUtil.getString(request,"birthEventsTotal3");
        dsFilterDto.setBirthEventsTotal3(birthEventsTotal3);
        String birthEventsTotalMax=ParamUtil.getString(request,"birthEventsTotalMax");
        dsFilterDto.setBirthEventsTotalMax(birthEventsTotalMax);
        ParamUtil.setSessionAttr(request,"arEnquiryDonorSampleFilterDto",dsFilterDto);

        return dsFilterDto;

    }
    public void preSearch(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String back = (String) ParamUtil.getRequestAttr(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "donorSampleParam");
        List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions(DataSubmissionConsts.DS_AR,"null");
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);
        List<SelectOption> arCentreSelectOptionFrom  = assistedReproductionService.genPremisesOptions(DataSubmissionConsts.DS_AR,"null");
        SelectOption otherSelectOption=new SelectOption();
        otherSelectOption.setText("Others");
        otherSelectOption.setValue("AR_SC_001");
        arCentreSelectOptionFrom.add(otherSelectOption);
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOptionFrom",arCentreSelectOptionFrom);

        if(!"back".equals(back)||searchParam==null){
            ArEnquiryDonorSampleFilterDto arDto=setArEnquiryDonorSampleFilterDto(request);

            Map<String,Object> filter= IaisCommonUtils.genNewHashMap();

            if(arDto.getArCentre()!=null) {
                filter.put("arCentre", arDto.getArCentre());
            }
            if(arDto.getDonorSampleCode()!=null){
                filter.put("donorSampleCode", arDto.getDonorSampleCode());
            }
            if(arDto.getSampleType()!=null){
                filter.put("sampleType", getDonorSampleType(arDto.getSampleType()));
                ParamUtil.setRequestAttr(request,"sampleType",arDto.getSampleType());
            }
            if(arDto.getSampleHciCode()!=null){
                if("AR_SC_001".equals(arDto.getSampleHciCode())&&arDto.getOthersSampleHciCode()!=null){
                    filter.put("othersDonorSampleCode", arDto.getOthersSampleHciCode());
                }else {
                    filter.put("sampleHciCode", arDto.getSampleHciCode());
                }
            }
            String idType = arDto.getDonorIdType();
            if(idType != null){
                filter.put("donorIdType", idType);
            }
            if (isFreshSearch(idType,arDto.getSampleType())){
                filter.remove("donorIdType");
                filter.put("mDonorIdType", idType);
            }
            String idNumber = arDto.getDonorIdNumber();
            if(idNumber != null){
                List<DonorSampleDto> donorSampleDtos = assistedReproductionClient.getDonorSampleListByIdNumber(idNumber).getEntity();
                getIbNumber(donorSampleDtos,filter,idNumber, idType);
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

            donorSampleParameter.setFilters(filter);

            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                donorSampleParameter.setSortType(sortType);
                donorSampleParameter.setSortField(sortFieldName);
            }

            SearchParam donorSampleParam = SearchResultHelper.getSearchParam(request, donorSampleParameter,true);
            if(donorSampleParam.getSortMap().containsKey("SAMPLE_TYPE_DESC")){
                HalpSearchResultHelper.setMasterCodeForSearchParam(donorSampleParam,"SAMPLE_TYPE", "SAMPLE_TYPE_DESC",MasterCodeUtil.AR_DONOR_SAMPLE_TYPE);
            }
            if(searchParam!=null){
                donorSampleParam.setPageNo(searchParam.getPageNo());
                donorSampleParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(donorSampleParam,bpc.request);

            QueryHelp.setMainSql("onlineEnquiry","searchDonorSampleByAssistedReproduction",donorSampleParam);
            SearchResult<ArEnquiryDonorSampleDto> donorSampleResult = assistedReproductionService.searchDonorSampleByParam(donorSampleParam);
            ParamUtil.setRequestAttr(request,"donorSampleResult",donorSampleResult);
            ParamUtil.setSessionAttr(request,"donorSampleParam",donorSampleParam);
        }else {
            SearchResult<ArEnquiryDonorSampleDto> donorSampleResult = assistedReproductionService.searchDonorSampleByParam(searchParam);
            ParamUtil.setRequestAttr(request,"donorSampleResult",donorSampleResult);
            ParamUtil.setSessionAttr(request,"donorSampleParam",searchParam);
        }


    }

    /**
     *  fresh search add mType
     * @param idType
     * @param sampType
     * @return
     */
    private static Boolean isFreshSearch(String idType,String sampType){
        return StringUtil.isNotEmpty(idType) && StringUtil.isNotEmpty(sampType) && DataSubmissionConsts.DONOR_SAMPLE_TYPE_SPERM.equals(sampType);
    }

    /**
     *  get IbNumber type: fresh/oocyte
     */
    private static void getIbNumber(List<DonorSampleDto> donorSampleDtos, Map<String,Object> filter, String idNumber, String idType){
        if (IaisCommonUtils.isNotEmpty(donorSampleDtos)){
            filter.put("donorIdNumber", idNumber);
        }else {
            if (idType != null){
                filter.remove("donorIdType");
                filter.put("mDonorIdType", idType);
            }
            filter.put("mDonorIdNumber", idNumber);
        }
    }

    /**
     *  set selection SampleType
     * @param type
     * @return
     */
    private static List<String> getDonorSampleType(String type){
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (DataSubmissionConsts.DONOR_SAMPLE_TYPE_OOCYTE.equals(type)){
            result.add(DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE);
            result.add(DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE);
        } else if (DataSubmissionConsts.DONOR_SAMPLE_TYPE_EMBRYO.equals(type)){
            result.add(DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO);
        } else if (DataSubmissionConsts.DONOR_SAMPLE_TYPE_SPERM.equals(type)){
            result.add(DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM);
            result.add(DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM);
        }
        return result;
    }


    public void nextStep(BaseProcessClass bpc){

    }

    public void perDonorInfo(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String dspc_004 = MasterCodeUtil.getCodeDesc("DSPC_004");
        Integer donorResultSize = Integer.valueOf(dspc_004);
        String donorMessageTip = MessageUtil.replaceMessage("DS_ERR053", dspc_004,"1");
        ParamUtil.setSessionAttr(bpc.request, "donorResultSize",donorResultSize);
        ParamUtil.setSessionAttr(bpc.request,"donorMessageTip", donorMessageTip);

        String submissionNo = ParamUtil.getString(request, InboxConst.CRUD_ACTION_VALUE);
        String sampleHciCode = ParamUtil.getString(request,InboxConst.CRUD_ACTION_ADDITIONAL);
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(request,
                AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        if(dto.getFunctionName().equals(AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_AR)){
            submissionNo= (String) ParamUtil.getSessionAttr(request,"submissionIdNo");

        }
        ArSuperDataSubmissionDto donorInfo = assistedReproductionService.getArSuperDataSubmissionDto(submissionNo);
        if(StringUtil.isNotEmpty(sampleHciCode)){
            donorInfo.getDonorSampleDto().setSampleFromHciCode(sampleHciCode);
        }else {
            List<PremisesDto> premisesDtos=assistedReproductionClient.getAllCenterPremisesDtoByPatientCode(DataSubmissionConsts.DS_AR,"null","null").getEntity();
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
            if(DataSubmissionConsts.AR_SOURCE_OTHER.equals(donorInfo.getDonorSampleDto().getSampleFromHciCode())){
                donorInfo.getDonorSampleDto().setSampleFromHciCode("Others - "+donorInfo.getDonorSampleDto().getSampleFromOthers());
            }else if(StringUtil.isNotEmpty(donorInfo.getDonorSampleDto().getSampleFromHciCode())&&map.containsKey(donorInfo.getDonorSampleDto().getSampleFromHciCode())){
                donorInfo.getDonorSampleDto().setSampleFromHciCode(map.get(donorInfo.getDonorSampleDto().getSampleFromHciCode()));
            }
        }
        ParamUtil.setRequestAttr(request,"donorInfoDataSubmissionDto",donorInfo);
    }

    public void backStep(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request,"back","back");
    }
}
