package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskHistoryDto.class})
public class TaskHistoryDtoTest {

    @Spy
    TaskHistoryDto entity = new TaskHistoryDto();

    @Test
    public void testGetterSetter() {
        entity.getTaskDtoList();
        entity.setTaskDtoList(null);
        entity.getAppPremisesRoutingHistoryDtos();
        entity.setAppPremisesRoutingHistoryDtos(null);
        entity.getApplicationDtos();
        entity.setApplicationDtos(null);
        entity.getRollBackApplicationDtos();
        entity.setRollBackApplicationDtos(null);
        assertNotNull(entity);
    }
}