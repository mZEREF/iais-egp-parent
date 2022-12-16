package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.action.LoginAccessCheck;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import lombok.extern.slf4j.Slf4j;
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

                for (LicenceQueryResultsDto subResultsDto:results.getRows()
                ) {
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

            for (RfiTabQueryResultsDto subResultsDto:results.getRows()
            ) {
                subResultsDto.setStatus(MasterCodeUtil.getCodeDesc(subResultsDto.getStatus()));
            }

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
                subResultsDto.setAuditType(MasterCodeUtil.getCodeDesc(subResultsDto.getAuditType()));
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
