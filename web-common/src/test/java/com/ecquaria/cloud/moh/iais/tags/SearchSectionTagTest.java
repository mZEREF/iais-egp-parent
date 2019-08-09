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

import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import org.junit.Assert;
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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * SearchSectionTagTest
 *
 * @author suocheng
 * @date 8/6/2019
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({SearchSectionTag.class,AccessUtil.class})
public class SearchSectionTagTest {
    @Spy
    private SearchSectionTag tag = new SearchSectionTag();
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
        tag.setTitle("title");
        tag.setOnclick("onclick");
        tag.setCollapse(true);
        tag.setReload(true);
        tag.setRemove(true);
        tag.setHide(true);
        tag.setPlaceholder("placeHolder");
        tag.setValue("value");
        tag.setFieldName("FieldName");
        PowerMockito.mockStatic(AccessUtil.class);
    }
    @Test
    public void testGet(){
        Assert.assertTrue(tag.isCollapse());
        Assert.assertTrue(tag.isReload());
        Assert.assertTrue(tag.isRemove());
    }

    @Test
    public void testdoStartTagBE() throws JspException {
        PowerMockito.when(AccessUtil.isBackend()).thenReturn(true);
        int ret = tag.doStartTag();
        assertEquals(SearchSectionTag.EVAL_BODY_INCLUDE, ret);
    }
    @Test
    public void testdoStartTagFE() throws JspException {
        PowerMockito.when(AccessUtil.isBackend()).thenReturn(false);
        int ret = tag.doStartTag();
        assertEquals(SearchSectionTag.EVAL_BODY_INCLUDE, ret);
    }

    @Test(expected = JspTagException.class)
    public void testDoStartTagExp() throws JspException, IOException {
        doThrow(new RuntimeException()).when(jw).print(anyString());
        int ret = tag.doStartTag();
    }
    @Test
    public void testDoEndTag() throws JspException {
        int ret = tag.doEndTag();
        assertEquals(SearchSectionTag.EVAL_PAGE, ret);
        tag.release();
    }
    @Test(expected = JspTagException.class)
    public void testDoEndTagExp() throws JspException, IOException {
        doThrow(new RuntimeException()).when(jw).print(anyString());
        int ret = tag.doEndTag();
    }

}
