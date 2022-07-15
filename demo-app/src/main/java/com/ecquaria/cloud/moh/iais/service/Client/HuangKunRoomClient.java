package com.ecquaria.cloud.moh.iais.service.Client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.HuangKunPersonDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: Client
 * @author: haungkun
 * @date: 2022/7/8 17:06
 */
@FeignClient(name = "iais-sample", configuration = FeignConfiguration.class,
        fallback = HuangKunRoomClientFallback.class)
public interface HuangKunRoomClient {

    @GetMapping(path = "/huangkun/room", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HuangKunRoomDto>> queryRooms();

    @PostMapping(path = "/huangkun/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HuangKunRoomDto>> queryRoomByParam(@RequestBody SearchParam searchParam);

    @GetMapping(path = "/huangkun/room/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HuangKunRoomDto> queryRoomByID(@PathVariable("id") String id);

    @GetMapping(path = "/huangkun/room/type/{type}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HuangKunRoomDto>> queryRoomsByType(@PathVariable("type") String type);

    @GetMapping(path = "/huangkun/room/roomType", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> queryRoomType();

    @PutMapping(path = "/huangkun/room", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateRoom(@RequestBody HuangKunRoomDto roomEditDto);

    @PostMapping(path = "/huangkun/room", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveRoom(@RequestBody HuangKunRoomDto huangKunRoomDto);

    @PostMapping(path = "/huangkun/person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> savePerson(@RequestBody HuangKunPersonDto huangKunPersonDto);

    @GetMapping(path = "/huangkun/room/person/{roomid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HuangKunPersonDto>> queryPersonByRoomID(@PathVariable("roomid") String id);
}
