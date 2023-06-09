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

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import java.io.IOException;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
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
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * PaginationTagTest
 *
 * @author Jinhua
 * @date 2019/8/6 9:23
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({PaginationTag.class})
public class PaginationTagTest {
    @Spy
    private PaginationTag tag = new PaginationTag();
    @Mock
    private JspWriter jw;

    @Before
    public void setup() throws IOException {
        PageContext pageContext = PowerMockito.mock(PageContext.class);
        Whitebox.setInternalState(tag, "pageContext", pageContext);
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(pageContext.getRequest()).thenReturn(request);
        when(pageContext.getOut()).thenReturn(jw);
        doNothing().when(jw).print(anyString());
        tag.setId("idd");
        tag.setStyle("ssss");
        tag.setParam(null);
        tag.setResult(null);
        tag.setJsFunc(null);
        tag.setNeedRowNum(true);
        SearchParam param = new SearchParam(String.class.getName());
        param.setPageNo(2);
        param.setPageSize(1);
        SearchResult<String> sr = new SearchResult<String>();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        sr.setRows(list);
        sr.setRowCount(list.size());
        request.setAttribute("searchParam", param);
        request.setAttribute("searchResult", sr);
    }

    @Test
    public void testDoStartTag() throws JspException {
        int ret = tag.doStartTag();
        assertEquals(ActionTag.SKIP_BODY, ret);
    }

    @Test(expected = JspTagException.class)
    public void testDoStartTagExp() throws JspException, IOException {
        doThrow(new RuntimeException()).when(jw).print(anyString());
        int ret = tag.doStartTag();
    }

    @Test
    public void testDoEndTag() throws JspException {
        int ret = tag.doEndTag();
        assertEquals(ActionTag.EVAL_PAGE, ret);
        tag.release();
    }

}
