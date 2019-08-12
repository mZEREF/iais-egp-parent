/*
 *   This file is generated by ECQ project skeleton automatically.
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
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * SelectTagTest
 *
 * @author suocheng
 * @date 8/6/2019
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({SelectTag.class})
public class SelectTagTest {
    @Spy
    private SelectTag tag = new SelectTag();
    MockHttpServletRequest request = new MockHttpServletRequest();
    private SelectOption selectOption = new SelectOption("key","value");
    @Mock
    private JspWriter jw;

    @Before
    public void setup() throws IOException, JspException {
        PageContext pageContext = PowerMockito.mock(PageContext.class);
        Whitebox.setInternalState(tag, "pageContext", pageContext);
        when(pageContext.getRequest()).thenReturn(request);
        when(pageContext.getOut()).thenReturn(jw);
        doNothing().when(jw).print(anyString());
        tag.setId("idd");
        tag.setName("name");
        tag.setOptions("options");
        tag.setFirstOption("firstOption");
        tag.setOnchange("onChange");
        tag.setValue("value");
        tag.setCodeCategory(null);
        tag.setOtherOption("otherOption");
        tag.setStyle("ssss");
        tag.setCssClass("css");
        tag.setNeedErrorSpan(true);
        tag.setFilterCode("filterCode");

        List<SelectOption> options =  new ArrayList<>() ;
        options.add(selectOption);
        request.setAttribute("options",options);
    }

    @Test
    public void testdoStartTag() throws JspException {
        int ret = tag.doStartTag();
        assertEquals(SelectTag.SKIP_BODY, ret);
    }
    @Test
    public void testdoStartNullTag() throws JspException {
        tag.setId(null);
        tag.setCssClass(null);
        tag.setStyle(null);
        int ret = tag.doStartTag();
        assertEquals(SelectTag.SKIP_BODY, ret);
    }
    @Test(expected = JspTagException.class)
    public void testDoStartTagExp() throws JspException, IOException {
        doThrow(new RuntimeException()).when(jw).print(anyString());
        int ret = tag.doStartTag();
    }
    @Test
    public void testDoEndTag() throws JspException {
        int ret = tag.doEndTag();
        assertEquals(SelectTag.EVAL_PAGE, ret);
        tag.release();
    }


}
