package com.ecquaria.cloud.moh.iais.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({ValidationResult.class})
public class ValidationResultTest {
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final boolean HASERROR =true;
    @InjectMocks
    private ValidationResult validationResult;

    // method addMessage and retrieveAll
    @Test
    public void testMessage() {
        validationResult.addMessage(KEY,VALUE);
        Map<String, String> result = validationResult.retrieveAll();
        assertTrue(result.get(KEY).equals(VALUE));
    }
    // method set get
    @Test
    public void testSetGet(){
        validationResult.setHasErrors(HASERROR);
        assertTrue(validationResult.isHasErrors());
    }
}
