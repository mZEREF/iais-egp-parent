package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.KPGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.KPGameDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "iais-sample", configuration = FeignConfiguration.class,
        fallback = SampleClientFallback.class)
public interface KangPingGameClient {

    @GetMapping(value = "/kp/category",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<KPGameCategoryDto>> getCategories();

    @GetMapping(value = "/kp/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<KPGameCategoryDto> getGameCategoryById(@PathVariable("id") String id);

    @GetMapping(path = "/kp/category/game/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<KPGameDto>> getGameByCategoryId(@PathVariable("categoryId") String id);

    @GetMapping(value = "/kp/category/names",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getCategoryName();

    @PostMapping(value = "/kp/category",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveCategory(@RequestBody KPGameCategoryDto dto);

    @PutMapping(value = "/kp/category",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateCategory(@RequestBody KPGameCategoryDto dto);

    @DeleteMapping(value = "/kp/category/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteCategory(@PathVariable("id") String id);

    @GetMapping(value = "/kp/game",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<KPGameDto>> getGames();

    @GetMapping(value = "/kp/game/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<KPGameDto> getGameById(@PathVariable("id") String id);

    @PostMapping(value = "/kp/game",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveGame(@RequestBody KPGameDto dto);

    @PutMapping(value = "/kp/game",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateGame(@RequestBody KPGameDto dto);

    @DeleteMapping(value = "/kp/game/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteGame(@PathVariable("id") String id);

    @PostMapping(path = "/kp/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<KPGameCategoryDto>> queryCategory(@RequestBody SearchParam searchParam);
}
