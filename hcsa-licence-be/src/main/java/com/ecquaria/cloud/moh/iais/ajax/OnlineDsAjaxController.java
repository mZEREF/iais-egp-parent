package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCoFundingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionAdvEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryAjaxPatientResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryAjaxResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsVssEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * OnlinArAjaxController
 *
 * @author junyu
 * @date 2021/11/30
 */
@Slf4j
@Controller
@RequestMapping("/hcsa/enquiry/ar")
public class OnlineDsAjaxController {
    @Autowired
    private AssistedReproductionService assistedReproductionService;

    @GetMapping(value = "ar-quick-view")
    public @ResponseBody
    String viewArQuick(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        String patientCode = ParamUtil.getString(request,"patientCode");

        if(patientCode==null){
            return "";
        }
        String sql = SqlMap.INSTANCE.getSql("onlineEnquiry", "ar-quick-view").getSqlStr();

        PatientInfoDto patientInfoDto=assistedReproductionService.patientInfoDtoByPatientCode(patientCode);
        int currentFrozenOocytes=0;
        int currentFreshOocytes=0;
        int currentFrozenEmbryos=0;
        int currentFreshEmbryos=0;
        int currentFrozenSperms=0;
        ArEnquiryCoFundingHistoryDto arCoFundingDto =new ArEnquiryCoFundingHistoryDto();
        try {
            arCoFundingDto= assistedReproductionService.patientCoFundingHistoryByCode(patientCode);
            List<ArCurrentInventoryDto> arCurrentInventoryDtos=assistedReproductionService.arCurrentInventoryDtosByPatientCode(patientInfoDto.getPatient().getPatientCode());
            for (ArCurrentInventoryDto aci:arCurrentInventoryDtos
            ) {
                currentFrozenOocytes+=aci.getFrozenOocyteNum();
                currentFreshOocytes+=aci.getFreshOocyteNum();
                currentFrozenEmbryos+=aci.getFrozenEmbryoNum();
                currentFreshEmbryos+=aci.getFreshEmbryoNum();
                currentFrozenSperms+=aci.getFrozenEmbryoNum();
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        sql=sql.replaceAll("IUICyclesNumber", String.valueOf(arCoFundingDto.getIuiCoFundedTotal()));
        sql=sql.replaceAll("ARTFreshCyclesNumber",String.valueOf(arCoFundingDto.getArtFreshCoFundedTotal()));
        sql=sql.replaceAll("ARTFrozenCyclesNumber",String.valueOf(arCoFundingDto.getArtFrozenCoFundedTotal()));
        sql=sql.replaceAll("PGTCyclesNumber",String.valueOf(arCoFundingDto.getPgtCoFundedTotal()));

        sql=sql.replaceAll("FreshOocytesNumber",String.valueOf(currentFreshOocytes));
        sql=sql.replaceAll("FrozenOocytesNumber",String.valueOf(currentFrozenOocytes));
        sql=sql.replaceAll("FreshEmbryosNumber",String.valueOf(currentFreshEmbryos));
        sql=sql.replaceAll("FrozenEmbryosNumber",String.valueOf(currentFrozenEmbryos));
        sql=sql.replaceAll("FrozenSpermsNumber",String.valueOf(currentFrozenSperms));

        sql=sql.replaceAll("patientCode",patientCode);

        return sql;
    }


    @RequestMapping(value = "patientDetail.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> patientDetailAjax(HttpServletRequest request, HttpServletResponse response) {

        String patientCode = request.getParameter("patientCode");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(patientCode)){
            SearchParam searchParam = new SearchParam(AssistedReproductionEnquiryAjaxPatientResultsDto.class.getName());
            searchParam.setPageSize(Integer.MAX_VALUE);
            searchParam.setPageNo(0);
            searchParam.setSort("CREATED_DT", SearchParam.DESCENDING);
            //set filter
            SearchParam searchParamFather = (SearchParam) ParamUtil.getSessionAttr(request, "patientParam");
            searchParam.setFilters(searchParamFather.getFilters());
            searchParam.setParams(searchParamFather.getParams());
            searchParam.addFilter("patientCode", patientCode, true);
            //search
            QueryHelp.setMainSql("onlineEnquiry", "searchPatientAjaxByAssistedReproduction", searchParam);
            SearchResult<AssistedReproductionEnquiryAjaxPatientResultsDto> searchResult = assistedReproductionService.searchPatientAjaxByParam(searchParam);
            List<AssistedReproductionEnquiryAjaxPatientResultsDto> arAjaxList=searchResult.getRows();
            for (AssistedReproductionEnquiryAjaxPatientResultsDto ajax:arAjaxList
                 ) {
                String coFunding="";
                String arTreatment="";

                if(ajax.getTreatmentFreshNatural()!=null&&ajax.getTreatmentFreshNatural()){
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_NATURAL);
                }
                if(ajax.getTreatmentFreshStimulated()!=null&&ajax.getTreatmentFreshStimulated()){
                    if(!"".equals(arTreatment)){
                        arTreatment=arTreatment+", ";
                    }
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_STIMULATED);
                }
                if(ajax.getTreatmentFrozenEmbryo()!=null&&ajax.getTreatmentFrozenEmbryo()){
                    if(!"".equals(arTreatment)){
                        arTreatment=arTreatment+", ";
                    }
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FROZEN_EMBRYO_CYCLE);

                }
                if(ajax.getTreatmentFrozenOocyte()!=null&&ajax.getTreatmentFrozenOocyte()){
                    if(!"".equals(arTreatment)){
                        arTreatment=arTreatment+", ";
                    }
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FROZEN_OOCYTE_CYCLE);
                }
                if(StringUtil.isNotEmpty(ajax.getArCoFunding())){
                    coFunding=coFunding+MasterCodeUtil.getCodeDesc(ajax.getArCoFunding());
                }
                if(StringUtil.isNotEmpty(ajax.getIuiCoFunding())){
                    if(!"".equals(coFunding)){
                        coFunding=coFunding+", ";
                    }
                    coFunding=coFunding+MasterCodeUtil.getCodeDesc(ajax.getIuiCoFunding());

                }
                if(StringUtil.isNotEmpty(ajax.getPgtCoFunding())){
                    if(!"".equals(coFunding)){
                        coFunding=coFunding+", ";
                    }
                    coFunding=coFunding+ajax.getPgtCoFunding();
                }
                if("".equals(arTreatment)){
                    arTreatment="-";
                }
                if("".equals(coFunding)){
                    coFunding="-";
                }
                ajax.setArTreatment(arTreatment);
                ajax.setCoFunding(coFunding);
                ajax.setStatus(MasterCodeUtil.getCodeDesc(ajax.getStatus()));
            }
            if(searchResult.getRowCount()>0){
                map.put("result", "Success");
            }else {
                map.put("result", "Fail");
            }
            map.put("ajaxResult", searchResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }


    @RequestMapping(value = "cycleStageDetail.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> cycleStageDetailAjax(HttpServletRequest request, HttpServletResponse response) {

        String cycleId = request.getParameter("cycleIder");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(cycleId)){

            List<DataSubmissionDto> cycleStageAjaxList=assistedReproductionService.allDataSubmissionByCycleId(cycleId);
            for (DataSubmissionDto ajax:cycleStageAjaxList
            ) {
                ajax.setCycleStageStr(MasterCodeUtil.getCodeDesc(ajax.getCycleStage()));
                ajax.setSubmitDtStr(Formatter.formatDateTime(ajax.getSubmitDt(),Formatter.DATE));
            }
            if(cycleStageAjaxList.size()>0){
                map.put("result", "Success");
            }else {
                map.put("result", "Fail");
            }
            map.put("ajaxResult", cycleStageAjaxList);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    @RequestMapping(value = "topDetail.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> topDetailAjax(HttpServletRequest request, HttpServletResponse response) {

        String patientIdNo = request.getParameter("patientIdNo");
        String patientIdType = request.getParameter("patientIdType");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(patientIdNo)&&!StringUtil.isEmpty(patientIdType)){
            FilterParameter topParameter = new FilterParameter.Builder()
                    .clz(DsTopEnquiryResultsDto.class)
                    .sortField("SUBMIT_DT").sortType(SearchParam.DESCENDING).pageNo(0).pageSize(Integer.MAX_VALUE).build();


            Map<String,Object> filter=IaisCommonUtils.genNewHashMap();

            filter.put("patientIdType", patientIdType);
            filter.put("patientIdNo",patientIdNo);
            topParameter.setFilters(filter);
            SearchParam searchParam = SearchResultHelper.getSearchParam(request, topParameter,true);
            QueryHelp.setMainSql("onlineEnquiry", "searchTopAjax",searchParam);

            SearchResult<DsTopEnquiryResultsDto> results = assistedReproductionService.searchDsTopByParam(searchParam);
            List<DsTopEnquiryResultsDto> queryList = null;
            if (!Objects.isNull(results)){
                 queryList = results.getRows();
                queryList.forEach(i -> i.setSubmitDtStr(Formatter.formatDateTime(i.getSubmitDt(), AppConsts.DEFAULT_DATE_FORMAT)));
                queryList.forEach(i -> i.setPatientBirthdayStr(Formatter.formatDateTime(i.getPatientBirthday(), AppConsts.DEFAULT_DATE_FORMAT)));
                queryList.forEach(i -> i.setPatientIdType(MasterCodeUtil.getCodeDesc(i.getPatientIdType())));
                queryList.forEach(i -> i.setDoctorName(i.getDoctorName()==null?"":i.getDoctorName()));
                queryList.forEach(i -> i.setDoctorRegnNo(i.getDoctorRegnNo()==null?"":i.getDoctorRegnNo()));
                queryList.forEach(i -> i.setCenterName(i.getCenterName()==null?"":i.getCenterName()));
                map.put("result", "Success");
            }else {
                map.put("result", "Fail");
            }
            map.put("ajaxResult", results);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    @RequestMapping(value = "vssDetail.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> vssDetailAjax(HttpServletRequest request, HttpServletResponse response) {

        String patientIdNo = request.getParameter("patientIdNo");
        String patientIdType = request.getParameter("patientIdType");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(patientIdNo)&&!StringUtil.isEmpty(patientIdType)){
            FilterParameter vssParameter = new FilterParameter.Builder()
                    .clz(DsVssEnquiryResultsDto.class)
                    .sortField("SUBMIT_DT").sortType(SearchParam.DESCENDING).pageNo(0).pageSize(Integer.MAX_VALUE).build();


            Map<String,Object> filter=IaisCommonUtils.genNewHashMap();

            filter.put("patientIdType", patientIdType);
            filter.put("patientIdNo",patientIdNo);
            vssParameter.setFilters(filter);
            SearchParam searchParam = SearchResultHelper.getSearchParam(request, vssParameter,true);
            QueryHelp.setMainSql("onlineEnquiry", "searchVssByAjax",searchParam);

            SearchResult<DsVssEnquiryResultsDto> results = assistedReproductionService.searchDsVssByParam(searchParam);
            List<DsVssEnquiryResultsDto> queryList = null;
            if (!Objects.isNull(results)){
                queryList = results.getRows();
                queryList.forEach(i -> i.setSubmitDtStr(Formatter.formatDateTime(i.getSubmitDt(), AppConsts.DEFAULT_DATE_FORMAT)));
                queryList.forEach(i -> i.setPatientBirthdayStr(Formatter.formatDateTime(i.getPatientBirthday(), AppConsts.DEFAULT_DATE_FORMAT)));
                queryList.forEach(i -> i.setPatientIdType(MasterCodeUtil.getCodeDesc(i.getPatientIdType())));
                queryList.forEach(i -> i.setMaritalStatus(MasterCodeUtil.getCodeDesc(i.getMaritalStatus())));
                queryList.forEach(i -> i.setSterilisationReason(MasterCodeUtil.getCodeDesc(i.getSterilisationReason())));
                queryList.forEach(i -> i.setCenterName(i.getCenterName()==null?"":i.getCenterName()));
                map.put("result", "Success");
            }else {
                map.put("result", "Fail");
            }
            map.put("ajaxResult", results);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    @RequestMapping(value = "drpDetail.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> drpDetailAjax(HttpServletRequest request, HttpServletResponse response) {

        String patientCode = request.getParameter("patientCode");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(patientCode)){
            SearchParam searchParam = new SearchParam(DsDrpEnquiryAjaxResultsDto.class.getName());
            searchParam.setPageSize(Integer.MAX_VALUE);
            searchParam.setPageNo(0);
            searchParam.setSort("SUBMIT_DT", SearchParam.DESCENDING);
            searchParam.addFilter("patientCode", patientCode, true);
            //search
            QueryHelp.setMainSql("onlineEnquiry", "searchByDrpAjax", searchParam);
            SearchResult<DsDrpEnquiryAjaxResultsDto> searchResult = assistedReproductionService.searchDrpAjaxByParam(searchParam);
            List<DsDrpEnquiryAjaxResultsDto> queryList = null;
            if (!Objects.isNull(searchResult)){
                queryList = searchResult.getRows();
                queryList.forEach(i -> i.setSubmitDtStr(Formatter.formatDateTime(i.getSubmitDt(), AppConsts.DEFAULT_DATE_FORMAT)));
                queryList.forEach(i -> i.setMedicationType(MasterCodeUtil.getCodeDesc(i.getMedicationType())));
                queryList.forEach(i -> i.setDrugType(MasterCodeUtil.getCodeDesc(i.getDrugType())));
                queryList.forEach(i -> i.setDoctorName(i.getDoctorName()==null?"":i.getDoctorName()));

                map.put("result", "Success");
            }else {
                map.put("result", "Fail");
            }
            map.put("ajaxResult", searchResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    @GetMapping(value = "PatientInfo-SearchResults-DownloadS")
    public @ResponseBody
    void filePatientInfoHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "patientParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);
        if(searchParam.getSortMap().containsKey("ID_TYPE_DESC")){
            HalpSearchResultHelper.setMasterCodeForSearchParam(searchParam,"dpi.ID_TYPE","ID_TYPE_DESC",MasterCodeUtil.CATE_ID_DS_ID_TYPE);
        }else if(searchParam.getSortMap().containsKey("NATIONALITY_DESC")){
            HalpSearchResultHelper.setMasterCodeForSearchParam(searchParam,"dpi.NATIONALITY","NATIONALITY_DESC",MasterCodeUtil.CATE_ID_NATIONALITY);
        }
        searchParam.addSort("ID",SearchParam.DESCENDING);
        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "advancedSearchPatientByAssistedReproduction",searchParam);

        SearchResult<AssistedReproductionEnquiryResultsDto> results = assistedReproductionService.searchPatientByParam(searchParam);

        if (!Objects.isNull(results)){
            List<AssistedReproductionEnquiryResultsDto> queryList = results.getRows();
            queryList.forEach(i -> i.setPatientIdType(MasterCodeUtil.getCodeDesc(i.getPatientIdType())));
            queryList.forEach(i -> i.setPatientNationality(MasterCodeUtil.getCodeDesc(i.getPatientNationality())));
            queryList.forEach(i -> i.setPatientDateBirthStr(Formatter.formatDateTime(i.getPatientDateBirth(), AppConsts.DEFAULT_DATE_FORMAT)));

            try {
                file = ExcelWriter.writerToExcel(queryList, AssistedReproductionEnquiryResultsDto.class, "PatientInfo_SearchResults_DownloadS");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "SubmissionID-SearchResults-Download")
    public @ResponseBody
    void fileSubmissionHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "submissionParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "searchSubmissionByAssistedReproduction",searchParam);

        SearchResult<AssistedReproductionEnquirySubResultsDto> results = assistedReproductionService.searchSubmissionByParam(searchParam);

        if (!Objects.isNull(results)){
            List<AssistedReproductionEnquirySubResultsDto> queryList = results.getRows();
            for (AssistedReproductionEnquirySubResultsDto subResultsDto:results.getRows()
            ) {
                switch (subResultsDto.getSubmissionType()){
                    case "AR_TP001":subResultsDto.setSubmissionType("Patient Information");break;
                    case "AR_TP002":subResultsDto.setSubmissionType("Cycle Stage");break;
                    case "AR_TP003":subResultsDto.setSubmissionType("Donor Sample");break;
                    default:subResultsDto.setSubmissionType(MasterCodeUtil.getCodeDesc(subResultsDto.getSubmissionType()));
                }
            }
            for (AssistedReproductionEnquirySubResultsDto subResultsDto:results.getRows()
            ) {
                if(!"-".equals(subResultsDto.getSubmissionSubtype())){
                    subResultsDto.setSubmissionSubtype(MasterCodeUtil.getCodeDesc(subResultsDto.getSubmissionSubtype()));
                }
            }
            queryList.forEach(i -> i.setSubmissionDateStr(Formatter.formatDateTime(i.getSubmissionDate(), AppConsts.DEFAULT_DATE_FORMAT)));

            try {
                file = ExcelWriter.writerToExcel(queryList, AssistedReproductionEnquirySubResultsDto.class, "SubmissionID_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "PatientInfo-Adv-SearchResults-DownloadS")
    public @ResponseBody
    void filePatientInfoAdvHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "patientAdvParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "advancedSearchPatientByAssistedReproduction",searchParam);

        SearchResult<AssistedReproductionAdvEnquiryResultsDto> results = assistedReproductionService.searchPatientAdvByParam(searchParam);

        if (!Objects.isNull(results)){
            List<AssistedReproductionAdvEnquiryResultsDto> queryList = results.getRows();
            queryList.forEach(i -> i.setPatientDateBirthStr(Formatter.formatDateTime(i.getPatientDateBirth(), AppConsts.DEFAULT_DATE_FORMAT)));
            queryList.forEach(i -> i.setPatientIdType(MasterCodeUtil.getCodeDesc(i.getPatientIdType())));
            queryList.forEach(i -> i.setPatientNationality(MasterCodeUtil.getCodeDesc(i.getPatientNationality())));
            queryList.forEach(i -> i.setCycleStartDateStr(Formatter.formatDateTime(i.getCycleStartDate(), AppConsts.DEFAULT_DATE_FORMAT)));

            try {
                file = ExcelWriter.writerToExcel(queryList, AssistedReproductionAdvEnquiryResultsDto.class, "PatientInfo_SearchResults_DownloadS");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "DonorSample-SearchResults-DownloadS")
    public @ResponseBody
    void fileDonorSampleHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "donorSampleParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "searchDonorSampleByAssistedReproduction",searchParam);

        SearchResult<ArEnquiryDonorSampleDto> results = assistedReproductionService.searchDonorSampleByParam(searchParam);

        if (!Objects.isNull(results)){
            List<ArEnquiryDonorSampleDto> queryList = results.getRows();

            queryList.forEach(i -> i.setSampleType(MasterCodeUtil.getCodeDesc(i.getSampleType())));

            try {
                file = ExcelWriter.writerToExcel(queryList, ArEnquiryDonorSampleDto.class, "DonorSample_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }


    @GetMapping(value = "LDT-SearchResults-DownloadS")
    public @ResponseBody
    void fileLdtHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "ldtParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "searchLaboratoryDevelopTest",searchParam);

        SearchResult<DsLaboratoryDevelopTestEnquiryResultsDto> results = assistedReproductionService.searchDsLdtByParam(searchParam);

        if (!Objects.isNull(results)){
            List<DsLaboratoryDevelopTestEnquiryResultsDto> queryList = results.getRows();
            queryList.forEach(i -> i.setLdtDateStr(Formatter.formatDateTime(i.getLdtDate(), AppConsts.DEFAULT_DATE_FORMAT)));

            try {
                file = ExcelWriter.writerToExcel(queryList, DsLaboratoryDevelopTestEnquiryResultsDto.class, "LaboratoryDevelopTest_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "TOP-SearchResults-DownloadS")
    public @ResponseBody
    void fileTopHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "topParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "searchByTop",searchParam);

        SearchResult<DsTopEnquiryResultsDto> results = assistedReproductionService.searchDsTopByParam(searchParam);

        if (!Objects.isNull(results)){
            List<DsTopEnquiryResultsDto> queryList = results.getRows();
            queryList.forEach(i -> i.setSubmitDtStr(Formatter.formatDateTime(i.getSubmitDt(), AppConsts.DEFAULT_DATE_FORMAT)));
            queryList.forEach(i -> i.setPatientBirthdayStr(Formatter.formatDateTime(i.getPatientBirthday(), AppConsts.DEFAULT_DATE_FORMAT)));
            queryList.forEach(i -> i.setPatientIdType(MasterCodeUtil.getCodeDesc(i.getPatientIdType())));

            try {
                file = ExcelWriter.writerToExcel(queryList, DsTopEnquiryResultsDto.class, "TerminationOfPregnancy_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "VSS-SearchResults-DownloadS")
    public @ResponseBody
    void fileVssHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "vssParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "searchByVss",searchParam);

        SearchResult<DsVssEnquiryResultsDto> results = assistedReproductionService.searchDsVssByParam(searchParam);

        if (!Objects.isNull(results)){
            List<DsVssEnquiryResultsDto> queryList = results.getRows();
            queryList.forEach(i -> i.setSubmitDtStr(Formatter.formatDateTime(i.getSubmitDt(), AppConsts.DEFAULT_DATE_FORMAT)));
            queryList.forEach(i -> i.setPatientBirthdayStr(Formatter.formatDateTime(i.getPatientBirthday(), AppConsts.DEFAULT_DATE_FORMAT)));
            queryList.forEach(i -> i.setPatientIdType(MasterCodeUtil.getCodeDesc(i.getPatientIdType())));
            queryList.forEach(i -> i.setMaritalStatus(MasterCodeUtil.getCodeDesc(i.getMaritalStatus())));
            queryList.forEach(i -> i.setSterilisationReason(MasterCodeUtil.getCodeDesc(i.getSterilisationReason())));

            try {
                file = ExcelWriter.writerToExcel(queryList, DsVssEnquiryResultsDto.class, "TerminationOfPregnancy_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "DRP-SearchResults-DownloadS")
    public @ResponseBody
    void fileDrpHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "drpParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("onlineEnquiry", "searchByDrpPatient",searchParam);

        SearchResult<DsDrpEnquiryResultsDto> results = assistedReproductionService.searchDrpByParam(searchParam);

        if (!Objects.isNull(results)){
            List<DsDrpEnquiryResultsDto> queryList = results.getRows();
            queryList.forEach(i -> i.setSubmitDtStr(Formatter.formatDateTime(i.getSubmitDt(), AppConsts.DEFAULT_DATE_FORMAT)));
            queryList.forEach(i -> i.setPatientBirthdayStr(Formatter.formatDateTime(i.getPatientBirthday(), AppConsts.DEFAULT_DATE_FORMAT)));
            queryList.forEach(i -> i.setPatientIdType(MasterCodeUtil.getCodeDesc(i.getPatientIdType())));


            try {
                file = ExcelWriter.writerToExcel(queryList, DsDrpEnquiryResultsDto.class, "Drug_Practices_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }

        try {
            FileUtils.writeFileResponseContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }
}