package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelSheetDto;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/change-tcu-date")
public class ChangeTCUDateController {
    @Autowired
    private LicenceService licenceService;

    @GetMapping(value = "/search-result-download")
    public @ResponseBody
    void fileLdtHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PARAM_CHANGE_TUC_DATE);
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);
        // complete remarks
        searchParam.addFilter("down_flag", "1", true);

        QueryHelp.setMainSql("changeTCUDate", "listLicenceInfo", searchParam);

        SearchResult<LicPremisesQueryDto> results = licenceService.searchLicencesInChangeTCUDate(searchParam);

        if (!Objects.isNull(results)) {
            List<LicPremisesQueryDto> queryList = results.getRows();
            int count = 1;
            for (LicPremisesQueryDto queryDto : queryList) {
                queryDto.setTcuDateStr(Formatter.formatDateTime(queryDto.getTcuDate(), AppConsts.DEFAULT_DATE_FORMAT));
                queryDto.setSn(count++);
            }

            ExcelSheetDto excelSheetDto = new ExcelSheetDto();
            excelSheetDto.setSheetAt(0);
            excelSheetDto.setSheetName("sheet");
            excelSheetDto.setBlock(false);
            excelSheetDto.setPwd(Formatter.formatDateTime(new Date(), "yyyyMMdd"));
            excelSheetDto.setStartRowIndex(1);
            excelSheetDto.setSource(queryList);
            excelSheetDto.setSourceClass(LicPremisesQueryDto.class);
            excelSheetDto.setDefaultRowHeight((short) 600);
            excelSheetDto.setChangeHeight(true);
            excelSheetDto.setNeedFiled(true);
            Map<Integer, Integer> widthMap = IaisCommonUtils.genNewHashMap(1);
            widthMap.put(7, 50);
            excelSheetDto.setWidthMap(widthMap);

            try {
                file = ExcelWriter.writerToExcel(excelSheetDto, null, "ChangeTCUDate_SearchResult_Download");
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