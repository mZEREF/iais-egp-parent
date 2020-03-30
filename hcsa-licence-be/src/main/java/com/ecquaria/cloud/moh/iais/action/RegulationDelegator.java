package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaRegulationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.RegulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/

@Delegator(value = "hcsaRegulationDelegator")
@Slf4j
public class RegulationDelegator {

    @Autowired
    private RegulationService regulationService;

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(RegulationQueryDto.class)
            .searchAttr(HcsaRegulationConstants.PARAM_SEARCH)
            .resultAttr(HcsaRegulationConstants.PARAM_RESULT)
            .sortField("id").build();

    /**
     * @AutoStep: startStep
     * @param:
     * @return:
     * @author: yichen
     */
    public void startStep(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("Checklist Regulation", "Regulation");
        HttpServletRequest request = bpc.request;
    }

    /**
     * @AutoStep: preLoad
     * @param:
     * @return:
     * @author: yichen
     */
    public void preLoad(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

        QueryHelp.setMainSql("hcsaconfig", "regulationQuery", searchParam);

        SearchResult searchResult =  regulationService.searchRegulation(searchParam);

        ParamUtil.setSessionAttr(request, HcsaRegulationConstants.PARAM_SEARCH, searchParam);
        ParamUtil.setRequestAttr(request, HcsaRegulationConstants.PARAM_RESULT, searchResult);
    }

    /**
     * @AutoStep: doDelete
     * @param:
     * @return:
     * @author: yichen
     */
    public void doDelete(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: doPage
     * @param:
     * @return:
     * @author: yichen
     */
    public void doPage(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: doSort
     * @param:
     * @return:
     * @author: yichen
     */
    public void doSort(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: preUpload
     * @param:
     * @return:
     * @author: yichen
     */
    public void preUpload(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: preUpdate
     * @param:
     * @return:
     * @author: yichen
     */
    public void preUpdate(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: preCreate
     * @param:
     * @return:
     * @author: yichen
     */
    public void preCreate(BaseProcessClass bpc){

    }

    /**
     * @AutoStep: preCreate
     * @param:
     * @return:
     * @author: yichen
     */
    public void doCreateOrUpdate(BaseProcessClass bpc){

    }

    @GetMapping(value = "regulation-result-file")
    public @ResponseBody
    void fileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));

        File file = null;

        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, HcsaRegulationConstants.PARAM_SEARCH);
        SearchResult searchResult =  regulationService.searchRegulation(searchParam);
        if (searchResult != null){
            List<RegulationQueryDto> regulationResult = searchResult.getRows();
            file = ExcelWriter.exportExcel(regulationResult, RegulationQueryDto.class, "Checklist_Regulations_Upload_Template");
        }

        if(file != null){
            try {
                FileUtils.writeFileResponseContent(response, file);
                FileUtils.deleteTempFile(file);
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        }

        log.debug(StringUtil.changeForLog("fileHandler end ...."));

    }

}
