package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferRequestDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/11/1 17:27
 **/

@Slf4j
@Delegator(value = "requestTransferDelegator")
public class BsbRequestForTransferDelegator {

    /**
     * start
     * This module is used to initialize data
     * */
    public void start(BaseProcessClass bpc){
        if(log.isInfoEnabled()){
            log.info("In the future this module will be used to initialize some data");
        }
    }

    /**
     * prepareData
     * this module is used to prepare facility info and biological agent/toxin
     * */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"doSettings",getDocSettingMap());
        ParamUtil.setRequestAttr(request,"transferReq",new TransferRequestDto());
    }


    public Map<String,DocSetting> getDocSettingMap(){
        Map<String,DocSetting> settingMap = new HashMap<>();
        settingMap.put("ityBat",new DocSetting(DocConstants.DOC_TYPE_INVENTORY_AGENT,"Inventory: Biological Agents",true));
        settingMap.put("ityToxin",new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TOXIN,"Inventory: Toxins",true));
        settingMap.put("others",new DocSetting(DocConstants.DOC_TYPE_OTHERS,"others",true));
        return settingMap;
    }

}
