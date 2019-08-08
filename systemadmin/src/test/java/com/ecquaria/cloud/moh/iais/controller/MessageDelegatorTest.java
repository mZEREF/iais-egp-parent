package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:2019/8/8 13:32
 *description:
 */

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.dao.MsgDao;
import com.ecquaria.cloud.moh.iais.entity.Message;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.service.MsgService;
import com.ecquaria.cloud.moh.iais.service.impl.MsgServiceImpl;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.webflow.rt.api.BaseProcessClass;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MessageDelegator.class, CrudHelper.class, MsgServiceImpl.class,
        SpringContextHelper.class, MiscUtil.class, AuditTrailDto.class, ParamUtil.class})
public class MessageDelegatorTest {
    @InjectMocks
    private MessageDelegator messageDelegator;

    @Spy
    private MsgService msgService = new MsgServiceImpl();

    @Mock
    private BaseProcessClass bpc;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Mock
    private QueryDao<Message> queryDao;

    @Mock
    private MsgDao msgDao;

    private Message message = new Message();
}
