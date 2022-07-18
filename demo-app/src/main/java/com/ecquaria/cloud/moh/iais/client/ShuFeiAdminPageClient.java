package com.ecquaria.cloud.moh.iais.client;


import com.ecquaria.cloud.moh.iais.common.dto.*;
import com.ecquaria.cloud.moh.iais.common.dto.sample.*;
import com.ecquaria.cloudfeign.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@FeignClient(name = "iais-sample", configuration = FeignConfiguration.class,
        fallback = ShuFeiAdminPageClientFallback.class)
public interface ShuFeiAdminPageClient {
    @PostMapping(value = "sf_room/create_room",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ShuFeiRoomSampleDto> createRoom(@RequestBody ShuFeiRoomSampleDto room);

    @GetMapping(value = "sf_room/select_room",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ShuFeiRoomSampleDto>> selectRoom();

    @GetMapping(value = "sf_room/select_roomType",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> selectRoomType();

    @GetMapping(value = "sf_room/select_roomNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> selectRoomNo();

    @GetMapping(value = "sf_room/select_id/{roomType}/{roomNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> selectId(@PathVariable("roomType") String roomType,@PathVariable("roomNo") String roomNo);

    @GetMapping(value = "sf_person/select_person",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ShuFeiPersonSampleDto>> selectPerson();

    @PostMapping(path = "sf_person/results",  produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ShuFeiSampleQueryDto>> doQuery(@RequestBody SearchParam searchParam);

    @GetMapping(value = "sf_person/select_personId/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ShuFeiCreateSampleDto> selectPersonId(@PathVariable("id") String id);

    @PutMapping(value = "sf_person/update_personal",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> savePerson(@RequestBody ShuFeiPersonSampleDto person);

    @PostMapping(value = "sf_person/create_by_room_person",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveByRoomCreatePerson(@RequestBody ShuFeiCreateSampleDto shuFeiCreateSampleDto);

    @GetMapping(value = "sf_person/select_by_roomId/{roomId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ShuFeiPersonSampleDto>> selectByRoomId(@PathVariable("roomId") String roomId);

    @GetMapping(value = "sf_person/select_displayName/{displayName}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ShuFeiPersonSampleDto> selectByDisPlayName(@PathVariable("displayName") String displayName);
}
