package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.HuangKunPersonDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.ZouRunAllService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ZouRunDelegator
 * @author: zourun
 */
@Delegator("zouRunDelegator")
@Slf4j
public class ZouRunDelegator {

    @Autowired
    ZouRunAllService zouRunAllService;

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(ZrSampleGameCategoryDto.class)
            .searchAttr("categorySearchParam")
            .resultAttr("categorySearchResult")
            .sortFieldToMap("CATEGORY_NAME", SearchParam.ASCENDING).build();

    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        ParamUtil.setSessionAttr(request, "categorySearchParam", null);
        ParamUtil.setSessionAttr(request, "categorySearchResult", null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        String clean = request.getParameter("clean");
        if (null != clean && clean.equals("clean")) {
            param.setFilters(new HashMap<>());
            param.setMainSql(null);
            param.setParams(new HashMap<>());
        }
        QueryHelp.setMainSql("categorySample", "queryCategory", param);
        SearchResult<ZrSampleGameCategoryDto> result = zouRunAllService.doQuery(param);


        ParamUtil.setSessionAttr(request, "categorySearchParam", param);
        ParamUtil.setSessionAttr(request, "categorySearchResult", result);
    }

    public void doSorting(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    private void preSelectOption(HttpServletRequest request) {
        List<SelectOption> categoryNameSelect = IaisCommonUtils.genNewArrayList();
        List<String> option = zouRunAllService.doQueryAllCategoryName();
        for (String i : option) {
            categoryNameSelect.add(new SelectOption(i, i));
        }
        ParamUtil.setSessionAttr(request, "categoryNameSelect", (Serializable) categoryNameSelect);
    }

    public void prepareEdit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String categoryId = ParamUtil.getMaskedString(request, "categoryId");
        ZrSampleGameCategoryDto sampleGameCategoryDto = zouRunAllService.doQueryCategory(categoryId);
        ParamUtil.setSessionAttr(request, "sampleGameCategoryDto", sampleGameCategoryDto);
        SearchResult<ZrSampleGameDto> result = zouRunAllService.getGameByCategoryId(categoryId);
        System.out.println(result.getRows() + "--------------------result");
        ParamUtil.setRequestAttr(request, "gameResult", result);
    }

    public void doSearch(BaseProcessClass bpc) {
        System.out.println("----------------------------------doSearch");
        HttpServletRequest request = bpc.request;
        String categoryName = request.getParameter("categoryName");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(categoryName)) {
            searchParam.addFilter("categoryName", categoryName, true);
            ParamUtil.setRequestAttr(request, "categoryName", categoryName);
        }

    }

    public void doBack(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "categoryName", null);
    }

    public void doEdit(BaseProcessClass bpc) {
        System.out.println("----------------------------------doEdit");
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String categoryName = ParamUtil.getString(request, "categoryName");
        String categoryNo = ParamUtil.getString(request, "categoryNo");
        String categoryDescription = ParamUtil.getString(request, "categoryDescription");

        ZrSampleGameCategoryDto zrSampleGameCategoryDto = (ZrSampleGameCategoryDto) ParamUtil.getSessionAttr(request, "sampleGameCategoryDto");
        zrSampleGameCategoryDto.setCategoryName(categoryName);
        zrSampleGameCategoryDto.setCategoryNo(categoryNo);
        zrSampleGameCategoryDto.setCategoryDescription(categoryDescription);

        ValidationResult vResult = WebValidationHelper.validateProperty(zrSampleGameCategoryDto, "edit");
        if (vResult != null && vResult.isHasErrors()) {
            Map<String, String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "Y");
            zouRunAllService.saveCategory(zrSampleGameCategoryDto);
        }

    }

    public void doPaging(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam, bpc.request);
    }

    public void prepareAddCategory(BaseProcessClass bpc) {


    }

    public void addCategory(BaseProcessClass bpc) {
        System.out.println("--------------------------------addCategory");
        HttpServletRequest request = bpc.request;
        String categoryName = ParamUtil.getString(request, "categoryName");
        String categoryNo = ParamUtil.getString(request, "categoryNo");
        String categoryDescription = ParamUtil.getString(request, "categoryDescription");
        ZrSampleGameCategoryDto gameCategoryDto = new ZrSampleGameCategoryDto();
        gameCategoryDto.setCategoryNo(categoryNo);
        gameCategoryDto.setCategoryName(categoryName);
        gameCategoryDto.setCategoryDescription(categoryDescription);


        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(categoryNo)) {
            searchParam.addFilter("categoryNo", categoryNo, true);
        }
        QueryHelp.setMainSql("categorySample", "queryCategory", searchParam);
        SearchResult<ZrSampleGameCategoryDto> result = zouRunAllService.doQuery(searchParam);

        ValidationResult vResult = WebValidationHelper.validateProperty(gameCategoryDto, "add");
        if (vResult != null && vResult.isHasErrors()) {

            Map<String, String> errorMap = vResult.retrieveAll();
//            errorMap.put()
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        } else {
            if (result.getRowCount() != 0) {
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("categoryNO", "This categoryNO already existed");
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
            } else {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "Y");
                zouRunAllService.saveCategory(gameCategoryDto);

            }
        }


    }

    public void prepareAddGame(BaseProcessClass bpc) {
    }

    public void addGame(BaseProcessClass bpc) throws ParseException {
        System.out.println("----------------------------------addGame");
        HttpServletRequest request = bpc.request;
        String categoryId = ParamUtil.getMaskedString(request, "categoryId");
        String gameName = ParamUtil.getString(request, "gameName");
        String priceStr = ParamUtil.getString(request, "price");
        String description = ParamUtil.getString(request, "gameDescription");
        String issueDate = ParamUtil.getDate(request, "issueDate");
        Date date = null;
        BigDecimal price = null;
        if (issueDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            date = sdf.parse(issueDate);
        }
        if (priceStr != null && priceStr.matches("^[0-9]+\\.{0,1}[0-9]{0,2}$")) {
            price = new BigDecimal(priceStr);
        }

        ZrSampleGameDto zrSampleGameDto = new ZrSampleGameDto();
        zrSampleGameDto.setCategoryId(categoryId);
        zrSampleGameDto.setGameName(gameName);
        zrSampleGameDto.setPrice(price);
        zrSampleGameDto.setIssueDate(date);
        zrSampleGameDto.setGameDescription(description);

        ValidationResult vResult = WebValidationHelper.validateProperty(zrSampleGameDto, "addGame");
        if (vResult != null && vResult.isHasErrors()) {

            Map<String, String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        } else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "Y");
            zouRunAllService.saveGame(zrSampleGameDto);
        }

//        zouRunAllService.saveGame(zrSampleGameDto);
        SearchResult<ZrSampleGameDto> result = zouRunAllService.getGameByCategoryId(categoryId);
        ParamUtil.setRequestAttr(request, "gameResult", null);
        ParamUtil.setRequestAttr(request, "gameResult", result);


    }
}
