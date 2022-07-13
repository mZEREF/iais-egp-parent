package com.ecquaria.cloud.moh.iais.service.Client;


import com.ecquaria.cloud.moh.iais.Dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping(path = "/huangkun/room/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HuangKunRoomDto> queryRoomByID(@PathVariable("id") String id);

    @GetMapping(path = "/huangkun/room/type/{type}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HuangKunRoomDto>> queryRoomsByType(@PathVariable("type") String type);

    @GetMapping(path = "/huangkun/room/roomType", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> queryRoomType();

}
