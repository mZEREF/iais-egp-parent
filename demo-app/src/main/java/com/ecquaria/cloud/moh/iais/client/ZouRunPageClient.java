package com.ecquaria.cloud.moh.iais.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.sample.ShuFeiCreateSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.sample.ShuFeiPersonSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.sample.ShuFeiRoomSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.sample.ShuFeiSampleQueryDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.ZrSampleGameDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author zourun
 */
@FeignClient(name = "iais-sample", configuration = FeignConfiguration.class,
        fallback = ZouRunPageClientFallback.class)
public interface ZouRunPageClient {
    @DeleteMapping(value = "/zourun/category/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteCategory(@PathVariable("id") String id);

    @PostMapping(value = "/zourun/save", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveCategory(@RequestBody ZrSampleGameCategoryDto zrSampleGameCategoryDto);

    @GetMapping(value = "/zourun/category/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ZrSampleGameCategoryDto> getCategoryById(@PathVariable("id") String id);

    @GetMapping(value = "/zourun/category", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ZrSampleGameCategoryDto>> getAllCategory();

    @GetMapping(value = "/zourun/category/categoryName", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> listGameCategoryName();

    @GetMapping(value = "/zourun/game", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ZrSampleGameDto>> getGame();

    @GetMapping(value = "/zourun/game/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ZrSampleGameDto> getGameById(@PathVariable("id") String id);

    @GetMapping(value = "/zourun/game/category/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ZrSampleGameDto>> getGameByGameCategoryId(@PathVariable("id") String id);

    @PostMapping(path = "/zourun/game", consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveGame(@RequestBody ZrSampleGameDto zrSampleGameDto);

    @PutMapping(value = "/zourun/game", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateGame(@RequestBody ZrSampleGameDto zrSampleGameDto);

    @PutMapping(value = "/zourun/game/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteGame(@PathVariable("id") String id);

    @PostMapping(path = "/zourun/category/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ZrSampleGameCategoryDto>> doQuery(@RequestBody SearchParam searchParam);


}
