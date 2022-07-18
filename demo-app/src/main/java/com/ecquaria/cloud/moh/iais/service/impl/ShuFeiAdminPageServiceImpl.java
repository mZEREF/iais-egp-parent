package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.*;
import com.ecquaria.cloud.moh.iais.client.*;
import com.ecquaria.cloud.moh.iais.common.dto.*;
import com.ecquaria.cloud.moh.iais.common.dto.sample.*;
import com.ecquaria.cloud.moh.iais.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ShuFeiAdminPageServiceImpl implements ShuFeiAdminPageService {

    @Autowired
    private ShuFeiAdminPageClient shuFeiAdminPageClient;

    @Override
    public List<ShuFeiRoomSampleDto> getAllRoom() {
        return shuFeiAdminPageClient.selectRoom().getEntity();
    }

    @Override
    public List<ShuFeiPersonSampleDto> getAllPerson() {
        return shuFeiAdminPageClient.selectPerson().getEntity();
    }

    @Override
    public List<String> getRoomType() {
        return shuFeiAdminPageClient.selectRoomType().getEntity();
    }

    @Override
    public List<String> getRoomNo() {
        return shuFeiAdminPageClient.selectRoomNo().getEntity();
    }

    @SearchTrack(catalog = "ShuFeiSampleDemo", key = "searchShuFeiSampleDemo")
    @Override
    public SearchResult<ShuFeiSampleQueryDto> doQuery(SearchParam param) {
        return shuFeiAdminPageClient.doQuery(param).getEntity();
    }

    @Override
    public ShuFeiCreateSampleDto getByPersonId(String id) {
        return shuFeiAdminPageClient.selectPersonId(id).getEntity();
    }

    @Override
    public void saveCreatePerson(ShuFeiCreateSampleDto shuFeiCreateSampleDto) {
        shuFeiAdminPageClient.saveByRoomCreatePerson(shuFeiCreateSampleDto);
    }

    @Override
    public String selectRoomId(String roomType, String roomNo) {
        return shuFeiAdminPageClient.selectId(roomType,roomNo).getEntity();
    }

    @Override
    public void saveRoom(ShuFeiRoomSampleDto roomSampleDto) {
        shuFeiAdminPageClient.createRoom(roomSampleDto);
    }

    @Override
    public List<ShuFeiPersonSampleDto> getByRoomId(String roomId) {
        return shuFeiAdminPageClient.selectByRoomId(roomId).getEntity();
    }

    @Override
    public void savePerson(ShuFeiPersonSampleDto shuFeiPersonSampleDto) {
        shuFeiAdminPageClient.savePerson(shuFeiPersonSampleDto);
    }

}
