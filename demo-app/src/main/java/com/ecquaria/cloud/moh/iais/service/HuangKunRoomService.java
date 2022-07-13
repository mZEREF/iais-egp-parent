package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.Dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;

import java.util.List;

/**
 * @ClassName: HuangKunRoomService
 * @author: haungkun
 * @date: 2022/7/8 16:38
 */
public interface HuangKunRoomService {
    SearchResult<HuangKunRoomDto> doQuery();

    SearchResult<HuangKunRoomDto> queryRoomByType(String param);

    List<String> listRoomTypes();

    HuangKunRoomDto getRoomById(String Id);
}
