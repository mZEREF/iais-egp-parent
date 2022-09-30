package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.client.KangPingGameClient;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.KPGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.KPGameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KangPingGameService {

    @Autowired
    private KangPingGameClient kangPingGameClient;

    public List<KPGameCategoryDto> getAllCategories(){
        return kangPingGameClient.getCategories().getEntity();
    }

    public KPGameCategoryDto getCategoryById(String id){
        return kangPingGameClient.getGameCategoryById(id).getEntity();
    }

    public SearchResult<KPGameDto> getGamesByCategoryId(String categoryId) {
        return kangPingGameClient.getGameByCategoryId(categoryId).getEntity();
    }

    public List<String> getCategoryNames(){
        return kangPingGameClient.getCategoryName().getEntity();
    }

    public void saveGameCategory(KPGameCategoryDto dto){
        kangPingGameClient.saveCategory(dto);
    }

    public void updateGameCategory(KPGameCategoryDto dto){
        kangPingGameClient.saveCategory(dto);
    }

    public List<KPGameDto> getAllGames(){
        return kangPingGameClient.getGames().getEntity();
    }

    public KPGameDto getGameById(String id){
        return kangPingGameClient.getGameById(id).getEntity();
    }

    public void saveGame(KPGameDto dto){
        kangPingGameClient.saveGame(dto);
    }

    public SearchResult<KPGameCategoryDto> doQuery(SearchParam param) {
        return kangPingGameClient.queryCategory(param).getEntity();
    }

    public void deleteGameById(String id){
        kangPingGameClient.deleteGame(id);
    }
}
