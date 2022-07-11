package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.Dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;

/**
 * @ClassName: HuangKunRoomService
 * @author: haungkun
 * @date: 2022/7/8 16:38
 */
public interface HuangKunRoomService {
    SearchResult<HuangKunRoomDto> doQuery();
}
