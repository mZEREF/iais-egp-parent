package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *File Name: MsgServiceImpl
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.dao.MsgDao;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.Message;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.service.MsgService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MsgServiceImpl implements MsgService {

    @Autowired
    private QueryDao<Message> queryDao;

    @Autowired
    private MsgDao msgDao;

    public SearchResult<Message> doSearch(SearchParam param, String catalog, String key) {
        return queryDao.doQuery(param, catalog, key);
    }

    public void deleteMessageById(Integer id) {
        msgDao.delete(id);
    }

    public void saveMessage(Message msg) {
        msgDao.save(msg);
    }

    @Override
    public Message getMessageByMsgId(Integer id) {
        return msgDao.findOne(id);
    }
}
