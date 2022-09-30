package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.BCGameCategoryDto;
import com.ecquaria.cloud.moh.iais.dto.BCGameDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "iais-sample", configuration = FeignConfiguration.class,
        fallback = SampleClientFallback.class)
public interface BaiChuanGameClient {

    @GetMapping(value = "/bc/category",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BCGameCategoryDto>> getCategories();

    @GetMapping(value = "/bc/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BCGameCategoryDto> getGameCategoryById(@PathVariable("id") String id);

    @GetMapping(path = "/bc/category/game/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<BCGameDto>> getGameByCategoryId(@PathVariable("categoryId") String id);

    @GetMapping(value = "/bc/category/names",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getCategoryName();

    @PostMapping(value = "/bc/category",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveCategory(@RequestBody BCGameCategoryDto dto);

    @PutMapping(value = "/bc/category",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateCategory(@RequestBody BCGameCategoryDto dto);

    @DeleteMapping(value = "/bc/category/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteCategory(@PathVariable("id") String id);

    @GetMapping(value = "/bc/game",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BCGameDto>> getGames();

    @GetMapping(value = "/bc/game/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BCGameDto> getGameById(@PathVariable("id") String id);

    @PostMapping(value = "/bc/game",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveGame(@RequestBody BCGameDto dto);

    @PutMapping(value = "/bc/game",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateGame(@RequestBody BCGameDto dto);

    @DeleteMapping(value = "/bc/game/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteGame(@PathVariable("id") String id);

    @PostMapping(path = "/bc/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<BCGameCategoryDto>> queryCategory(@RequestBody SearchParam searchParam);
}
