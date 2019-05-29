/*
package sg.gov.moh.iais.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import MessageDao;
import SearchConditionDTO;
import Message;
import MessageService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Slf4j
public class MessageServiceImpl implements MessageService {

    private static MessageDao messageDao;

    private static List<Message> cacheMsgList;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao){
        try {
            this.messageDao = messageDao;
        }catch (Exception e){
          e.printStackTrace();
        }

        cacheMsgList = getCacheMsgList();


        log.info("cacheMsgList ========>>> " + cacheMsgList.size());
    }


    public List<Message> listMessageBySearchCondition(SearchConditionDTO searchCondition) {
        List<Message> collectList = cacheMsgList;

        if(searchCondition == null){
            log.info("searchMessageByConditions error");
        }

        if(!searchCondition.getType().equals("")){
            collectList = collectList.stream().filter(i -> i.getType().equals(searchCondition.getType())).collect(Collectors.toList());
        }

        if(!searchCondition.getMessageType().equals("")){
            collectList = collectList.stream().filter(i -> i.getMessage_type().equals(searchCondition.getMessageType())).collect(Collectors.toList());
        }

        if(!searchCondition.getModule().equals("")){
            collectList = collectList.stream().filter(i -> i.getModule().equals(searchCondition.getModule())).collect(Collectors.toList());
        }

        return collectList;
    }

    @Transactional
    public void updateMessageByCodeId(String codeId, String desctipion) {
        try {
            messageDao.updateDescriptionByCodeId(Integer.valueOf(codeId), desctipion);
            cacheMsgList = messageDao.findAll();
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }


    public static List<Message> getCacheMsgList(){
        if(cacheMsgList == null){
            cacheMsgList = messageDao.findAll();
        }
        return cacheMsgList;
    }
}
*/
