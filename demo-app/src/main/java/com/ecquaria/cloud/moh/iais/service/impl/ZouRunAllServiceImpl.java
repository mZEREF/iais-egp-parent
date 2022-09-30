package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.client.ZouRunPageClient;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.HuangKunPersonDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameDto;
import com.ecquaria.cloud.moh.iais.service.Client.HuangKunRoomClient;
import com.ecquaria.cloud.moh.iais.service.HuangKunRoomService;
import com.ecquaria.cloud.moh.iais.service.ZouRunAllService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: ZouRunAllServiceImpl
 * @author: zourun
 */
@Service
@Slf4j
public class ZouRunAllServiceImpl implements ZouRunAllService {

    @Autowired
    private ZouRunPageClient zouRunPageClient;

    @Override
    public ZrSampleGameCategoryDto doQueryCategory(String id) {
        return zouRunPageClient.getCategoryById(id).getEntity();
    }

    @Override
    public SearchResult<ZrSampleGameCategoryDto> doQueryAllCategory() {
        return zouRunPageClient.getAllCategory().getEntity();
    }

    @Override
    public String deleteCategoryById(String id) {
        return zouRunPageClient.deleteCategory(id).getEntity();
    }

    @Override
    public String saveCategory(ZrSampleGameCategoryDto zrSampleGameCategoryDto) {
        return zouRunPageClient.saveCategory(zrSampleGameCategoryDto).getEntity();
    }

    @Override
    public List<ZrSampleGameDto> getGame() {
        return zouRunPageClient.getGame().getEntity();
    }

    @Override
    public ZrSampleGameDto getGameById(String id) {
        return zouRunPageClient.getGameById(id).getEntity();
    }

    @Override
    public String saveGame(ZrSampleGameDto zrSampleGameDto) {
        return zouRunPageClient.saveGame(zrSampleGameDto).getEntity();
    }

    @Override
    public String updateGame(ZrSampleGameDto zrSampleGameDto) {
        return zouRunPageClient.updateGame(zrSampleGameDto).getEntity();
    }

    @Override
    public String deleteGame(String id) {
        return zouRunPageClient.deleteGame(id).getEntity();
    }

    @Override
    public List<String> doQueryAllCategoryName() {
        return zouRunPageClient.listGameCategoryName().getEntity();
    }

    @Override
    public SearchResult<ZrSampleGameDto> getGameByCategoryId(String categoryId) {
        return zouRunPageClient.getGameByGameCategoryId(categoryId).getEntity();
    }

    @Override
    public SearchResult<ZrSampleGameCategoryDto> doQuery(SearchParam searchParam) {
        return zouRunPageClient.doQuery(searchParam).getEntity();
    }

}
