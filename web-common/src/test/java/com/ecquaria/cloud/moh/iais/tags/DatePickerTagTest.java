/*
 * This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import java.io.IOException;
import java.util.Date;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * DatePickerTagTest
 *
 * @author Jinhua
 * @date 2019/8/5 16:51
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({DatePickerTag.class})
public class DatePickerTagTest {
    @Spy
    private DatePickerTag tag = new DatePickerTag();
    @Mock
    private JspWriter jw;

    @Before
    public void setup() throws IOException {
        PageContext pageContext = PowerMockito.mock(PageContext.class);
        Whitebox.setInternalState(tag, "pageContext", pageContext);
        when(pageContext.getOut()).thenReturn(jw);
        doNothing().when(jw).print(anyString());
        tag.setId("idd");
        tag.setStyle("ssss");
        tag.setName("name");
        tag.setValue("value");
        tag.setOnchange("onchange");
        tag.setOnblur("someaction");
        tag.setOnclick("someAction");
        tag.setFromNow(true);
        tag.setToNow(true);
        tag.setWorkingDay(true);
        tag.setTitle("title");
        tag.setDisableWeekDay("6");
        tag.setStartDate("01/01/2010");
        tag.setEndDate("01/01/2020");
        tag.setCssClass("sss");
        tag.setDateVal(new Date());
    }

    @Test
    public void testDoStartTag() throws JspException {
        int ret = tag.doStartTag();
        assertEquals(ActionTag.SKIP_BODY, ret);
        tag.setId(null);
        tag.setFromNow(false);
        tag.setToNow(false);
        tag.setWorkingDay(false);
        tag.doStartTag();
    }

    @Test(expected = IaisRuntimeException.class)
    public void testDoStartTagExp() throws JspException, IOException {
        doThrow(new RuntimeException()).when(jw).print(anyString());
        int ret = tag.doStartTag();
    }

    @Test
    public void testDoEndTag() throws JspException {
        int ret = tag.doEndTag();
        tag.getTitle();
        assertEquals(ActionTag.EVAL_PAGE, ret);
        tag.release();
    }

}
