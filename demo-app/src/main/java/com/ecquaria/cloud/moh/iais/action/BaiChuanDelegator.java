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
import com.ecquaria.cloud.moh.iais.dto.BCGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.BCGameDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.impl.BaiChuanGameService;
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

@Delegator("baiChuanDelegator")
public class BaiChuanDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(BCGameCategoryDto.class)
            .searchAttr("categorySearchParam")
            .resultAttr("categorySearchResult")
            .sortFieldToMap("category_Name", SearchParam.ASCENDING).build();

    @Autowired
    private BaiChuanGameService baiChuanGameService;

    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        ParamUtil.setSessionAttr(request,"categorySearchParam", null);
        ParamUtil.setSessionAttr(request, "categorySearchResult", null);
    }

    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        String clean = request.getParameter("clean");
        if(null != clean && clean.equals("clean")){
            param.setFilters(new HashMap<>());
            param.setMainSql(null);
            param.setParams(new HashMap<>());
        }
        QueryHelp.setMainSql("categorySample", "queryCategory", param);
        SearchResult<BCGameCategoryDto> result = baiChuanGameService.doQuery(param);
        ParamUtil.setSessionAttr(request, "categorySearchParam", param);
        ParamUtil.setRequestAttr(request,"categoryName",request.getParameter("categoryName"));
        ParamUtil.setSessionAttr(request, "categorySearchResult", result);
    }

    public void doSorting(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> categoryNames =  IaisCommonUtils.genNewArrayList();
        List<String> option = baiChuanGameService.getCategoryNames();
        for (String i : option){
            categoryNames.add(new SelectOption(i, i));
        }
        ParamUtil.setSessionAttr(request, "categoryNameSelect", (Serializable) categoryNames);
    }

    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String categoryId = ParamUtil.getMaskedString(request, "categoryId");
        BCGameCategoryDto category = baiChuanGameService.getCategoryById(categoryId);
        ParamUtil.setSessionAttr(request,"categoryRequestDto", category);
        SearchResult<BCGameDto> result = baiChuanGameService.getGamesByCategoryId(categoryId);
        ParamUtil.setRequestAttr(request, "gameResult", result);
    }

    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String categoryName = request.getParameter("categoryName");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(categoryName)){
            searchParam.addFilter("categoryName", categoryName, true);
        }
    }

    public void doBack(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"categorySearchParam", null);
    }

    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String categoryName = ParamUtil.getString(request, "categoryName");
        String categoryNo = ParamUtil.getString(request, "categoryNo");
        String categoryDescription = ParamUtil.getString(request, "categoryDescription");
        BCGameCategoryDto dto = (BCGameCategoryDto) ParamUtil.getSessionAttr(request, "categoryRequestDto");
        dto.setCategoryNo(categoryNo);
        dto.setCategoryName(categoryName);
        dto.setCategoryDescription(categoryDescription);
        ValidationResult vResult = WebValidationHelper.validateProperty(dto, "edit");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            ParamUtil.setSessionAttr(request,"categorySearchParam", null);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            baiChuanGameService.updateGameCategory(dto);
        }
    }

    public void doPaging(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    public void prepareAddCategory(BaseProcessClass bpc){

    }

    public void addCategory(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String categoryNo = ParamUtil.getString(request, "categoryNo");
        String categoryName = ParamUtil.getString(request, "categoryName");
        String categoryDescription = ParamUtil.getString(request, "categoryDescription");
        BCGameCategoryDto dto = new BCGameCategoryDto();
        dto.setCategoryNo(categoryNo);
        dto.setCategoryName(categoryName);
        dto.setCategoryDescription(categoryDescription);
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(categoryNo)){
            searchParam.addFilter("categoryNo", categoryNo, true);
        }
        QueryHelp.setMainSql("categorySample", "queryCategory", searchParam);
        SearchResult<BCGameCategoryDto> result = baiChuanGameService.doQuery(searchParam);

        ValidationResult vResult = WebValidationHelper.validateProperty(dto, "add");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            if (result.getRowCount()!=0){
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("categoryNo", "This categoryNo already existed");
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
            }else {
                ParamUtil.setSessionAttr(request,"categorySearchParam", null);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
                baiChuanGameService.saveGameCategory(dto);
            }
        }

    }

    public void prepareAddGame(BaseProcessClass bpc){
    }

    public void addGame(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;

        String categoryId = ParamUtil.getMaskedString(request, "categoryId");
        String gameName = ParamUtil.getString(request, "gameName");
        String issueDate = ParamUtil.getDate(request, "issueDate");
        String price = ParamUtil.getString(request, "price");
        String description = ParamUtil.getString(request, "description");
        //设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        BCGameDto game = new BCGameDto();
        game.setGameName(gameName);
        game.setCategoryId(categoryId);
        game.setGameDescription(description);
        Date date = null;
        if(StringUtil.isNotEmpty(issueDate)){
             date = sdf.parse(issueDate);
        }
        Double dtoPrice = null;
        if(StringUtil.isNotEmpty(price)){
            dtoPrice = Double.parseDouble(price);
        }
        game.setPrice(dtoPrice);
        game.setIssueDate(date);
        //验证game
        ValidationResult vResult = WebValidationHelper.validateProperty(game, "add");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
            return;
        }
        //判断是否有重复
        SearchResult<BCGameDto> searchResult = baiChuanGameService.getGamesByCategoryId(categoryId);
        if(searchResult.getRows().stream().anyMatch((g)->g.equals(game))){
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            errorMap.put("categoryNO", "This categoryNO already existed");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            baiChuanGameService.saveGame(game);
        }
    }
}
