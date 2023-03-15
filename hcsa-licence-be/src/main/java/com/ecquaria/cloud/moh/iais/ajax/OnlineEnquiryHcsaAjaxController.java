package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.action.LoginAccessCheck;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicAppMainQueryResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenseeLicTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenseeQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.PaymentQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewPrintService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * OnlinArAjaxController
 *
 * @author junyu
 * @date 2021/11/30
 */
@Slf4j
@Controller
@RequestMapping("/hcsa/enquiry/hcsa")
public class OnlineEnquiryHcsaAjaxController implements LoginAccessCheck {
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private LicenceViewPrintService licenceViewPrintService;

    @Autowired
    private LicCommService licCommService;

    @GetMapping(value = "Licence-Print")
    public @ResponseBody
    void fileLicencePrintHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        List<String> licIds=IaisCommonUtils.genNewArrayList();
        String licencId = (String) ParamUtil.getSessionAttr(request, "licenceId");
        licIds.add(licencId);
        licenceViewPrintService.downloadLicencsToPdf(licIds,response);
        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @GetMapping(value = "Main-SearchResults-Download")
    public @ResponseBody
    void fileLicAppMainHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "mainParam");
        if(searchParam==null||searchParam.getFilters().isEmpty()){
            List<LicAppMainQueryResultDto> queryList = IaisCommonUtils.genNewArrayList();
            try {
                file = ExcelWriter.writerToExcel(queryList, LicAppMainQueryResultDto.class, "Main_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }else {
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            log.debug("indicates that a record has been selected ");

            QueryHelp.setMainSql("hcsaOnlineEnquiry", "mainOnlineEnquiry",searchParam);
            List<SelectOption> appTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_TYPE);
            List<SelectOption> appStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_STATUS);
            List<SelectOption> licStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_LICENCE_STATUS);
            MasterCodePair mcp = new MasterCodePair("mainView.app_type", "app_type_desc", appTypes);
            MasterCodePair mcp_status = new MasterCodePair("mainView.appStatus", "app_status_desc", appStatus);
            MasterCodePair mcp_lic_status = new MasterCodePair("mainView.LicSTATUS", "lic_status_desc", licStatus);
            searchParam.addMasterCode(mcp);
            searchParam.addMasterCode(mcp_status);
            searchParam.addMasterCode(mcp_lic_status);
            SearchResult<LicAppMainQueryResultDto> results = onlineEnquiriesService.searchMainQueryResult(searchParam);

            if (!Objects.isNull(results)){
                List<LicAppMainQueryResultDto> queryList = results.getRows();

                for (LicAppMainQueryResultDto subResultsDto:results.getRows()
                ) {
                    subResultsDto.setLicenceStatus(StringUtils.isEmpty(MasterCodeUtil.getCodeDesc(subResultsDto.getLicenceStatus()))? "-":MasterCodeUtil.getCodeDesc(subResultsDto.getLicenceStatus()));
                    subResultsDto.setApplicationType(StringUtils.isEmpty(MasterCodeUtil.getCodeDesc(subResultsDto.getApplicationType())) ? "-":MasterCodeUtil.getCodeDesc(subResultsDto.getApplicationType()));
                    subResultsDto.setApplicationStatus(StringUtils.isEmpty(MasterCodeUtil.getCodeDesc(subResultsDto.getApplicationStatus()))? "-":MasterCodeUtil.getCodeDesc(subResultsDto.getApplicationStatus())) ;
                }

                try {
                    file = ExcelWriter.writerToExcel(queryList, LicAppMainQueryResultDto.class, "Main_SearchResults_Download");
                } catch (Exception e) {
                    log.error("=======>fileHandler error >>>>>", e);
                }
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

    @GetMapping(value = "Licence-SearchResults-Download")
    public @ResponseBody
    void fileLicenceHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "licParam");
        if(searchParam==null||searchParam.getFilters().isEmpty()){
            List<LicenceQueryResultsDto> queryList = IaisCommonUtils.genNewArrayList();
            try {
                file = ExcelWriter.writerToExcel(queryList, LicenceQueryResultsDto.class, "Licence_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }else {
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            log.debug("indicates that a record has been selected ");

            QueryHelp.setMainSql("hcsaOnlineEnquiry", "licenceOnlineEnquiry",searchParam);

            SearchResult<LicenceQueryResultsDto> results = onlineEnquiriesService.searchLicenceQueryResult(searchParam);

            if (!Objects.isNull(results)){
                List<LicenceQueryResultsDto> queryList = results.getRows();
                for (LicenceQueryResultsDto subResultsDto:queryList) {
                    subResultsDto.setLicenceStatus(MasterCodeUtil.getCodeDesc(subResultsDto.getLicenceStatus()));
                }

                try {
                    file = ExcelWriter.writerToExcel(queryList, LicenceQueryResultsDto.class, "Licence_SearchResults_Download");
                } catch (Exception e) {
                    log.error("=======>fileHandler error >>>>>", e);
                }
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

    @GetMapping(value = "Inspection-SearchResults-Download")
    public @ResponseBody
    void fileInspectionHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "inspectionParam");
        InspectionEnquiryFilterDto filterDto= (InspectionEnquiryFilterDto) ParamUtil.getSessionAttr(request, "inspectionEnquiryFilterDto");
        if(searchParam==null||searchParam.getFilters().isEmpty()&&StringUtil.isEmpty(filterDto.getInspectionType())){
            List<InspectionQueryResultsDto> queryList = IaisCommonUtils.genNewArrayList();
            try {
                file = ExcelWriter.writerToExcel(queryList, InspectionQueryResultsDto.class, "Inspection_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }else {
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            log.debug("indicates that a record has been selected ");

            QueryHelp.setMainSql("hcsaOnlineEnquiry", "inspectionOnlineEnquiry",searchParam);

            SearchResult<InspectionQueryResultsDto> results = onlineEnquiriesService.searchInspectionQueryResult(searchParam);

            if (!Objects.isNull(results)){
                List<InspectionQueryResultsDto> queryList = results.getRows();
                try {
                    file = ExcelWriter.writerToExcel(queryList, InspectionQueryResultsDto.class, "Inspection_SearchResults_Download");
                } catch (Exception e) {
                    log.error("=======>fileHandler error >>>>>", e);
                }
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


    @GetMapping(value = "Licensee-SearchResults-Download")
    public @ResponseBody
    void fileLicenseeHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "lisParam");
        if(searchParam==null||searchParam.getFilters().isEmpty()){
            List<LicenseeQueryResultsDto> queryList = IaisCommonUtils.genNewArrayList();
            try {
                file = ExcelWriter.writerToExcel(queryList, LicenseeQueryResultsDto.class, "Licensee_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }else {
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            log.debug("indicates that a record has been selected ");

            QueryHelp.setMainSql("hcsaOnlineEnquiry", "licenseeOnlineEnquiry",searchParam);

            SearchResult<LicenseeQueryResultsDto> results = onlineEnquiriesService.searchLicenseeQueryResult(searchParam);

            if (!Objects.isNull(results)){
                List<LicenseeQueryResultsDto> queryList = results.getRows();
                try {
                    file = ExcelWriter.writerToExcel(queryList, LicenseeQueryResultsDto.class, "Licensee_SearchResults_Download");
                } catch (Exception e) {
                    log.error("=======>fileHandler error >>>>>", e);
                }
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

    @GetMapping(value = "Application-SearchResults-Download")
    public @ResponseBody
    void fileApplicationHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "appParam");
        if(searchParam==null||searchParam.getFilters().isEmpty()){
            List<ApplicationQueryResultsDto> queryList = IaisCommonUtils.genNewArrayList();
            try {
                file = ExcelWriter.writerToExcel(queryList, ApplicationQueryResultsDto.class, "Application_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }else {
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            log.debug("indicates that a record has been selected ");

            QueryHelp.setMainSql("hcsaOnlineEnquiry", "applicationOnlineEnquiry", searchParam);

            SearchResult<ApplicationQueryResultsDto> results = onlineEnquiriesService.searchApplicationQueryResult(searchParam);

            if (!Objects.isNull(results)) {
                List<ApplicationQueryResultsDto> queryList = results.getRows();

                try {
                    file = ExcelWriter.writerToExcel(queryList, ApplicationQueryResultsDto.class, "Application_SearchResults_Download");
                } catch (Exception e) {
                    log.error("=======>fileHandler error >>>>>", e);
                }
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

    @GetMapping(value = "Payment-SearchResults-Download")
    public @ResponseBody
    void filePaymentHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "paymentParam");
        if(searchParam==null||searchParam.getFilters().isEmpty()){
            List<PaymentQueryResultsDto> queryList = IaisCommonUtils.genNewArrayList();
            try {
                file = ExcelWriter.writerToExcel(queryList, PaymentQueryResultsDto.class, "Payment_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }else {
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            log.debug("indicates that a record has been selected ");

            QueryHelp.setMainSql("hcsaOnlineEnquiry", "paymentOnlineEnquiry", searchParam);

            SearchResult<PaymentQueryResultsDto> results = onlineEnquiriesService.searchPaymentQueryResult(searchParam);

            if (!Objects.isNull(results)) {
                List<PaymentQueryResultsDto> queryList = results.getRows();
                for (PaymentQueryResultsDto subResultsDto:results.getRows()
                ) {
                    subResultsDto.setFeesAmount("$"+Formatter.formatNumber(Double.valueOf(subResultsDto.getFeesAmount())));
                }
                try {
                    file = ExcelWriter.writerToExcel(queryList, PaymentQueryResultsDto.class, "Payment_SearchResults_Download");
                } catch (Exception e) {
                    log.error("=======>fileHandler error >>>>>", e);
                }
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

    @GetMapping(value = "Licensee-LicTab-SearchResults-Download")
    public @ResponseBody
    void fileLicenseeLicTabHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "licTabParam");
        if(searchParam==null||searchParam.getFilters().isEmpty()){
            List<LicenseeLicTabQueryResultsDto> queryList = IaisCommonUtils.genNewArrayList();
            try {
                file = ExcelWriter.writerToExcel(queryList, LicenseeLicTabQueryResultsDto.class, "Licensee_LicTab_SearchResults_Download");
            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }else {
            searchParam.setPageNo(0);
            searchParam.setPageSize(Integer.MAX_VALUE);

            log.debug("indicates that a record has been selected ");

            QueryHelp.setMainSql("hcsaOnlineEnquiry", "licenceOnlineEnquiry",searchParam);

            SearchResult<LicenceQueryResultsDto> results = onlineEnquiriesService.searchLicenceQueryResult(searchParam);

            if (!Objects.isNull(results)){
                List<LicenseeLicTabQueryResultsDto> queryListDld = IaisCommonUtils.genNewArrayList();
                for (LicenceQueryResultsDto subResultsDto:results.getRows()
                ) {
                    subResultsDto.setLicenceStatus(MasterCodeUtil.getCodeDesc(subResultsDto.getLicenceStatus()));
                    LicenseeLicTabQueryResultsDto dto= MiscUtil.transferEntityDto(subResultsDto,LicenseeLicTabQueryResultsDto.class);
                    queryListDld.add(dto);
                }
                try {
                    file = ExcelWriter.writerToExcel(queryListDld, LicenseeLicTabQueryResultsDto.class, "Licensee_LicTab_SearchResults_Download");
                } catch (Exception e) {
                    log.error("=======>fileHandler error >>>>>", e);
                }
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

    @GetMapping(value = "Licence-AppTab-SearchResults-Download")
    public @ResponseBody
    void fileLicenceAppTabHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "appTabParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("hcsaOnlineEnquiry", "applicationTabOnlineEnquiry",searchParam);

        SearchResult<ApplicationTabQueryResultsDto> results = onlineEnquiriesService.searchLicenceAppTabQueryResult(searchParam);

        if (!Objects.isNull(results)){
            List<ApplicationTabQueryResultsDto> queryList = results.getRows();

            for (ApplicationTabQueryResultsDto subResultsDto:results.getRows()
            ) {
                subResultsDto.setAppStatus(MasterCodeUtil.getCodeDesc(subResultsDto.getAppStatus()));
                subResultsDto.setAppType(MasterCodeUtil.getCodeDesc(subResultsDto.getAppType()));
                subResultsDto.setPmtStatus(MasterCodeUtil.getCodeDesc(subResultsDto.getPmtStatus()));
            }

            try {
                file = ExcelWriter.writerToExcel(queryList, ApplicationTabQueryResultsDto.class, "Application_Tab_SearchResults_Download");
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

    @GetMapping(value = "Licence-RfiTab-SearchResults-Download")
    public @ResponseBody
    void fileLicenceRfiTabHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "rfiTabParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("hcsaOnlineEnquiry", "adHocRfiTabOnlineEnquiry",searchParam);

        SearchResult<RfiTabQueryResultsDto> results = onlineEnquiriesService.searchLicenceRfiTabQueryResult(searchParam);

        if (!Objects.isNull(results)){
            List<RfiTabQueryResultsDto> queryList = results.getRows();

            try {
                file = ExcelWriter.writerToExcel(queryList, RfiTabQueryResultsDto.class, "Adhoc_Rfi_Tab_SearchResults_Download");
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

    @GetMapping(value = "Licence-InsTab-SearchResults-Download")
    public @ResponseBody
    void fileLicenceInsTabHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "insTabParam");
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);

        log.debug("indicates that a record has been selected ");

        QueryHelp.setMainSql("hcsaOnlineEnquiry", "inspectionsTabOnlineEnquiry",searchParam);

        SearchResult<InspectionTabQueryResultsDto> results = onlineEnquiriesService.searchLicenceInsTabQueryResult(searchParam);

        if (!Objects.isNull(results)){
            List<InspectionTabQueryResultsDto> queryList = results.getRows();

            for (InspectionTabQueryResultsDto subResultsDto:results.getRows()
            ) {
                subResultsDto.setAppStatus(MasterCodeUtil.getCodeDesc(subResultsDto.getAppStatus()));
                subResultsDto.setAppType(MasterCodeUtil.getCodeDesc(subResultsDto.getAppType()));
                subResultsDto.setAuditType(StringUtil.getNonNull(MasterCodeUtil.getCodeDesc(subResultsDto.getAuditType()), "-"));
                subResultsDto.setRisk(MasterCodeUtil.getCodeDesc(subResultsDto.getRisk()));
            }

            try {
                file = ExcelWriter.writerToExcel(queryList, InspectionTabQueryResultsDto.class, "Inspection_Tab_SearchResults_Download");
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
