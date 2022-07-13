package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.Dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.service.Client.HuangKunRoomClient;
import com.ecquaria.cloud.moh.iais.service.HuangKunRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: HuangKunRoomServiceImpl
 * @author: haungkun
 * @date: 2022/7/8 16:38
 */
@Service
@Slf4j
public class HuangKunRoomServiceImpl implements HuangKunRoomService {

    @Autowired
    private HuangKunRoomClient huangKunRoomClient;

    @Override
    public SearchResult<HuangKunRoomDto> doQuery() {
        return huangKunRoomClient.queryRooms().getEntity();
    }

    @Override
    public SearchResult<HuangKunRoomDto> queryRoomByType(String param) {
        return huangKunRoomClient.queryRoomsByType(param).getEntity();
    }

    @Override
    public List<String> listRoomTypes() {
        return huangKunRoomClient.queryRoomType().getEntity();
    }

    @Override
    public HuangKunRoomDto getRoomById(String Id) {
        return huangKunRoomClient.queryRoomByID(Id).getEntity();
    }
}
