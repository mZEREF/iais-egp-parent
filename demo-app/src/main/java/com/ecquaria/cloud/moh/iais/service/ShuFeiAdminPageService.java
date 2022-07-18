package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.*;
import com.ecquaria.cloud.moh.iais.common.dto.sample.*;

import java.util.*;

public interface ShuFeiAdminPageService {
    List<ShuFeiRoomSampleDto> getAllRoom();

    List<ShuFeiPersonSampleDto> getAllPerson();

    List<String> getRoomType();

    List<String> getRoomNo();

    SearchResult<ShuFeiSampleQueryDto> doQuery(SearchParam param);

    ShuFeiCreateSampleDto getByPersonId(String id);

    void savePerson(ShuFeiPersonSampleDto shuFeiPersonSampleDto);

    void saveCreatePerson(ShuFeiCreateSampleDto shuFeiCreateSampleDto);

    String selectRoomId(String roomType,String roomNo);

    void saveRoom(ShuFeiRoomSampleDto roomSampleDto);

    List<ShuFeiPersonSampleDto> getByRoomId(String roomId);

    ShuFeiPersonSampleDto getByDisPlayName(String displayName);

}
