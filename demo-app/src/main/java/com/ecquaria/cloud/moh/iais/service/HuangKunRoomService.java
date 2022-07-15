package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.HuangKunPersonDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;

import java.util.List;

/**
 * @ClassName: HuangKunRoomService
 * @author: haungkun
 * @date: 2022/7/8 16:38
 */
public interface HuangKunRoomService {
    SearchResult<HuangKunRoomDto> doQuery();

    SearchResult<HuangKunRoomDto> doQuery(SearchParam param);

    SearchResult<HuangKunRoomDto> queryRoomByType(String param);

    List<String> listRoomTypes();

    HuangKunRoomDto getRoomById(String Id);

    void updateRoom(HuangKunRoomDto roomEditDto);

    SearchResult<HuangKunPersonDto> queryPersonByRoomId(String roomId);

    void addRoom(HuangKunRoomDto huangKunRoomDto);

    void savePerson(HuangKunPersonDto huangKunPersonDto);
}
