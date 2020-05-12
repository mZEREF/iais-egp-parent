package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HcsaConfigPageDto.class})
public class HcsaConfigPageDtoTest {

    @Spy
    HcsaConfigPageDto entity = new HcsaConfigPageDto();

    @Test
    public void testGetterSetter() {
        entity.getRoutingSchemeId();
        entity.setRoutingSchemeId(null);
        entity.getRoutingSchemeName();
        entity.setRoutingSchemeName(null);
        entity.getWorkloadId();
        entity.setWorkloadId(null);
        entity.getAppType();
        entity.setAppType(null);
        entity.getStage();
        entity.setStage(null);
        entity.getWorkingGroupId();
        entity.setWorkingGroupId(null);
        entity.getWorkingGroupName();
        entity.setWorkingGroupName(null);
        entity.getManhours();
        entity.setManhours(null);
        entity.getWorkingGroup();
        entity.setWorkingGroup(null);
        entity.getStageCode();
        entity.setStageCode(null);
        entity.getWorkStageId();
        entity.setWorkStageId(null);
        entity.getStageName();
        entity.setStageName(null);
        entity.getStageId();
        entity.setStageId(null);
        entity.getAppTypeName();
        entity.setAppTypeName(null);
        entity.getIsMandatory();
        entity.setIsMandatory(null);
        assertNotNull(entity);
    }
}