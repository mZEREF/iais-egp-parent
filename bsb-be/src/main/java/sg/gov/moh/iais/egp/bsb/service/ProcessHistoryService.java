package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.RoutingHistoryClient;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;

/**
 * @author : LiRan
 * @date : 2022/1/21
 */
@Service
@Slf4j
public class ProcessHistoryService {
    private final RoutingHistoryClient routingHistoryClient;

    public ProcessHistoryService(RoutingHistoryClient routingHistoryClient) {
        this.routingHistoryClient = routingHistoryClient;
    }

    public void getAndSetHistoryInRequest(String applicationNo, HttpServletRequest request){
        List<ProcessHistoryDto> processHistoryDtoList = routingHistoryClient.getRoutingHistoryListByAppNo(applicationNo).getEntity();
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, processHistoryDtoList);
    }

    public void getAndSetHistoryInSession(String applicationNo, HttpServletRequest request){
        List<ProcessHistoryDto> processHistoryDtoList = routingHistoryClient.getRoutingHistoryListByAppNo(applicationNo).getEntity();
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, (Serializable) processHistoryDtoList);
    }
}
