package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.client.BaiChuanGameClient;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.BCGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.BCGameDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunPersonDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaiChuanGameService {

    @Autowired
    private BaiChuanGameClient baiChuanGameClient;

    public List<BCGameCategoryDto> getAllCategories(){
        return baiChuanGameClient.getCategories().getEntity();
    }

    public BCGameCategoryDto getCategoryById(String id){
        return baiChuanGameClient.getGameCategoryById(id).getEntity();
    }

    public SearchResult<BCGameDto> getGamesByCategoryId(String categoryId) {
        return baiChuanGameClient.getGameByCategoryId(categoryId).getEntity();
    }

    public List<String> getCategoryNames(){
        return baiChuanGameClient.getCategoryName().getEntity();
    }

    public void saveGameCategory(BCGameCategoryDto dto){
        baiChuanGameClient.saveCategory(dto);
    }

    public void updateGameCategory(BCGameCategoryDto dto){
        baiChuanGameClient.saveCategory(dto);
    }

    public List<BCGameDto> getAllGames(){
        return baiChuanGameClient.getGames().getEntity();
    }

    public BCGameDto getGameById(String id){
        return baiChuanGameClient.getGameById(id).getEntity();
    }

    public void saveGame(BCGameDto dto){
        baiChuanGameClient.saveGame(dto);
    }

    public SearchResult<BCGameCategoryDto> doQuery(SearchParam param) {
        return baiChuanGameClient.queryCategory(param).getEntity();
    }
}
