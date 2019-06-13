package com.ecquaria.cloud.moh.iais.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({ExceptionsHandler.class})
public class ExceptionsHandlerTest {
    private static final String OBJECTNAME = "moh";
    private static final String FIELD = "unit";
    private static final String MESSAGE = "unit test message";
    @InjectMocks
    private ExceptionsHandler exceptionsHandler;

    @Mock
    private MethodParameter methodParameter;

    //method customGenericValidationHnadler
    @Test
    public void testcustomGenericValidationHnadler(){
        List<FieldError> list = new ArrayList<>();
        FieldError fe = new FieldError(OBJECTNAME,FIELD,MESSAGE);
        list.add(fe);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        doReturn(list).when(bindingResult).getFieldErrors();

        ResponseEntity<Map<String, String>> responseEntity= exceptionsHandler.customGenericValidationHnadler(
                new MethodArgumentNotValidException(methodParameter, bindingResult));

        assertTrue(responseEntity.getBody().get(FIELD).equals(MESSAGE));
    }

}
