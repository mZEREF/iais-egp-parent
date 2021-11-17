package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.dto.submission.AckTransferReceiptDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/11/16 15:46
 **/

@Slf4j
@Controller
@RequestMapping("/sub-info")
public class FacilitySubAjaxController {
    @Autowired
    private  final TransferClient transferClient;
    private static final String ACK_TRANSFER_RECEIPT = "receiptSavedMap";

    public FacilitySubAjaxController(TransferClient transferClient) {
        this.transferClient = transferClient;
    }

    /**
     * this ajax method is used to get biological info by schedule from FacListDto
     * queryBiologicalBySchedule
     * @return Map<String, Object>
     * */
    @PostMapping(value = "sub.do")
    public @ResponseBody
    Map<String, Object> querySubmissionByFacId(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        String maskFacId = ParamUtil.getString(request,"maskFacId");
        String facId  = MaskUtil.unMaskValue("id",maskFacId);
        if(StringUtils.hasLength(facId) && !maskFacId.equals(facId)){
            HashMap<String, AckTransferReceiptDto.AckTransferReceiptSaved> receiptSavedMap
                    = new HashMap<>(transferClient.getReceiptDataSubNoMap(facId).getEntity().getReceiptSavedMap());
            ParamUtil.setSessionAttr(request,ACK_TRANSFER_RECEIPT, receiptSavedMap);
            List<String> strings = new ArrayList<>(receiptSavedMap.keySet());
            if(!CollectionUtils.isEmpty(strings)) {
            jsonMap.put("result", "success");
            jsonMap.put("queryResult",strings);
            }
        } else {
            jsonMap.put("result", "Fail");
        }
        return jsonMap;
    }
}
