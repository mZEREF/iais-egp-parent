package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.dto.TestValidationUtilsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({ValidationUtil.class})
public class ValidationUtilsTest {

    //test the method of the validateEntity
    @Test
  public void testvalidateEntity(){
        TestValidationUtilsDto dto = new TestValidationUtilsDto();
        ValidationResult vr = ValidationUtil.validateEntity(dto);
        assertTrue(vr.isHasErrors());
  }
    //test the method of the validateProperty
    @Test
    public void testvalidateProperty(){
        TestValidationUtilsDto dto = new TestValidationUtilsDto();
        ValidationResult vr = ValidationUtil.validateProperty(dto,"operation");
        assertTrue(vr.isHasErrors());
    }


}
