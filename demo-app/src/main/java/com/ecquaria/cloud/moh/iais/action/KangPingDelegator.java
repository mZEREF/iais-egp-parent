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
import com.ecquaria.cloud.moh.iais.dto.KPGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.KPGameDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.KangPingGameService;
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




@Delegator("kangPingDelegator")
public class KangPingDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(KPGameCategoryDto.class)
            .searchAttr("categorySearchParam")
            .resultAttr("categorySearchResult")
            .sortFieldToMap("category_Name", SearchParam.ASCENDING).build();

    @Autowired
    private KangPingGameService kangPingGameService;

    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        System.out.println("into start step!");
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        ParamUtil.setSessionAttr(request,"categorySearchParam", null);
        ParamUtil.setSessionAttr(request, "categorySearchResult", null);
    }


    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String flag = ParamUtil.getString(request,"flag");
        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        System.out.println(flag);
        if("flag".equals(flag)){
            param.setFilters(new HashMap<>());
            param.setMainSql(null);
            param.setParams(new HashMap<>());
        }
        QueryHelp.setMainSql("categorySample", "queryCategory", param);
        SearchResult<KPGameCategoryDto> result = kangPingGameService.doQuery(param);
        ParamUtil.setSessionAttr(request, "categorySearchParam", param);
        ParamUtil.setSessionAttr(request, "categorySearchResult", result);
    }

    public void doSorting(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> categoryNames =  IaisCommonUtils.genNewArrayList();
        List<String> option = kangPingGameService.getCategoryNames();
        for (String i : option){
            categoryNames.add(new SelectOption(i, i));
        }
        ParamUtil.setSessionAttr(request, "categoryNameSelect", (Serializable) categoryNames);
    }

    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String categoryId = ParamUtil.getMaskedString(request, "categoryId");
        KPGameCategoryDto category = kangPingGameService.getCategoryById(categoryId);
        ParamUtil.setSessionAttr(request,"categoryRequestDto", category);
        SearchResult<KPGameDto> result = kangPingGameService.getGamesByCategoryId(categoryId);
        ParamUtil.setRequestAttr(request, "gameResult", result);
    }

    public void deleteGame(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String categoryId = ParamUtil.getString(request, "categoryId");
        String gameId = ParamUtil.getString(request, "gameId");
        System.out.println("----------------------");
        System.out.println(categoryId);
        System.out.println(gameId);
        kangPingGameService.deleteGameById(gameId);
        SearchResult<KPGameDto> searchResult = kangPingGameService.getGamesByCategoryId(categoryId);
        ParamUtil.setRequestAttr(request, "gameResult", searchResult);
    }

    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String categoryName = request.getParameter("categoryName");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(categoryName)){
            searchParam.addFilter("categoryName", categoryName, true);
            ParamUtil.setRequestAttr(request, "categoryName", categoryName);
        }
    }

    public void doBack(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"categorySearchParam", null);
    }

    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String categoryName = ParamUtil.getString(request, "categoryName");
        String categoryNO = ParamUtil.getString(request, "categoryNO");
        String categoryDescription = ParamUtil.getString(request,"categoryDescription");
        KPGameCategoryDto dto = (KPGameCategoryDto) ParamUtil.getSessionAttr(request, "categoryRequestDto");
        dto.setCategoryNo(categoryNO);
        dto.setCategoryName(categoryName);
        dto.setCategoryDescription(categoryDescription);

        ValidationResult vResult = WebValidationHelper.validateProperty(dto, "edit");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            kangPingGameService.updateGameCategory(dto);
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
        String categoryName = ParamUtil.getString(request, "categoryName");
        String categoryNo = ParamUtil.getString(request, "categoryNo");
        String categoryDescription = ParamUtil.getString(request, "categoryDescription");
        KPGameCategoryDto dto = new KPGameCategoryDto();

        dto.setCategoryNo(categoryNo);
        dto.setCategoryName(categoryName);
        dto.setCategoryDescription(categoryDescription);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(categoryNo)){
            searchParam.addFilter("categoryNo", categoryNo, true);
        }
        QueryHelp.setMainSql("categorySample", "queryCategory", searchParam);
        SearchResult<KPGameCategoryDto> result = kangPingGameService.doQuery(searchParam);

        ValidationResult vResult = WebValidationHelper.validateProperty(dto, "add");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            if (result.getRowCount()!=0){
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("categoryNO", "This categoryNo already existed");
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
            }else {
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
                kangPingGameService.saveGameCategory(dto);
            }
        }

    }

    public void prepareAddGame(BaseProcessClass bpc){
    }

    public void addGame(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String categoryId = ParamUtil.getMaskedString(request, "categoryId");
        String gameName = ParamUtil.getString(request, "gameName");
        String gameDescription = ParamUtil.getString(request, "gameDescription");
        BigDecimal price = BigDecimal.valueOf(ParamUtil.getDouble(request, "price"));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Date issueDate =  sf.parse(ParamUtil.getDate(request,"issueDate"));

        KPGameDto game = new KPGameDto();
        game.setGameName(gameName);
        game.setCategoryId(categoryId);
        game.setGameDescription(gameDescription);
        game.setIssueDate(issueDate);
        game.setPrice(price);

        kangPingGameService.saveGame(game);
        SearchResult<KPGameDto> searchResult = kangPingGameService.getGamesByCategoryId(categoryId);
        ParamUtil.setRequestAttr(request, "gameResult", searchResult);
    }
}
