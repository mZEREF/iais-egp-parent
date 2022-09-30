package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.HuangKunPersonDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName: ZouRunAllService
 * @author: zourun
 */
public interface ZouRunAllService {
    ZrSampleGameCategoryDto  doQueryCategory(String id);

    SearchResult<ZrSampleGameCategoryDto> doQueryAllCategory();

    String deleteCategoryById(String id);

    String saveCategory(ZrSampleGameCategoryDto zrSampleGameCategoryDto);

    List<ZrSampleGameDto> getGame();

    ZrSampleGameDto getGameById(String id);

    String saveGame(ZrSampleGameDto zrSampleGameDto);

    String updateGame(ZrSampleGameDto zrSampleGameDto);

    String deleteGame(String id);

    List<String> doQueryAllCategoryName();

    SearchResult<ZrSampleGameDto> getGameByCategoryId(String categoryId);

    SearchResult<ZrSampleGameCategoryDto> doQuery(@RequestBody SearchParam searchParam);

}
