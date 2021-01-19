package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceStepDto.class})
public class ServiceStepDtoTest {

    @Spy
    ServiceStepDto entity = new ServiceStepDto();

    @Test
    public void testGetterSetter() {
        entity.getHcsaServiceStepSchemeDtos();
        entity.setHcsaServiceStepSchemeDtos(null);
        entity.getPreviousStep();
        entity.setPreviousStep(null);
        entity.getCurrentStep();
        entity.setCurrentStep(null);
        entity.getNextStep();
        entity.setNextStep(null);
        entity.getCurrentNumber();
        entity.setCurrentNumber(1);
        entity.getServiceNumber();
        entity.setServiceNumber(1);
        entity.isServiceFirst();
        entity.setServiceFirst(false);
        entity.isServiceEnd();
        entity.setServiceEnd(false);
        entity.isStepFirst();
        entity.setStepFirst(false);
        entity.isStepEnd();
        entity.setStepEnd(false);
        assertNotNull(entity);
    }
}