package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/15 18:24
 * DESCRIPTION: TODO
 **/
@Delegator(value = "inventoryManagementDelegator")
@Slf4j
public class InventoryManagementDelegator {

    /**
     *  AutoStep: prepareData
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc){
            String count = ParamUtil.getString(bpc.request,"searchChk");
            if(StringUtil.isEmpty(count)){
                count = "0";
            }
            ParamUtil.setRequestAttr(bpc.request, "count",count);
            preSelectOption(bpc.request,Integer.valueOf(count));
    }

    /**
     *  AutoStep: preHistoryData
     * @param bpc
     */
    public void preHistoryData(BaseProcessClass bpc){
        String count = ParamUtil.getString(bpc.request,"searchChk");
        ParamUtil.setSessionAttr(bpc.request, "count",count);
        preSelectOption(bpc.request,3);
    }

    /**
     *  AutoStep: doAdjustment
     * @param bpc
     */
    public void doAdjustment(BaseProcessClass bpc){

    }

    /**
     * AutoStep: preBasicData
     * @param bpc
     */
    public void preBasicData(BaseProcessClass bpc){
        String count = ParamUtil.getString(bpc.request,"searchChk");
        if(StringUtil.isEmpty(count) || count == null){
            count = "0";
        }
        ParamUtil.setRequestAttr(bpc.request, "count",count);
    }

    /**
     * AutoStep: preBasicList
     * @param bpc
     */
    public void preBasicList(BaseProcessClass bpc){
        String count = ParamUtil.getString(bpc.request,"searchChk");
        ParamUtil.setSessionAttr(bpc.request, "count",count);
        preSelectOption(bpc.request,3);
    }

    /**
     * AutoStep: doBasicAdjustment
     * @param bpc
     */
    public void doBasicAdjustment(BaseProcessClass bpc){

    }

    public void selectOption(HttpServletRequest request, String name, String[] args){
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        for(int i=0;i<args.length;i++){
            selectModel.add(new SelectOption(String.valueOf(i),args[i]));
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    public void preSelectOption(HttpServletRequest request,Integer num){
        switch(num){
            case 1:
                String[] tot = {"Export","Import","Local Transfer In",
                        "Local Transfer Out","Disposal","Adjustment","Consume"};
                selectOption(request,"tot",tot);
                String[] facilityName = {"fnc","top","bbc"};
                selectOption(request,"facilityName",facilityName);
                String[] scheduleType = {"First  Schedule Part I","First Schedule Part II","Second  Schedule","Third  Schedule","Fourth  Schedule","Fifth Schedule"};
                selectOption(request,"scheduleType",scheduleType);
                String[] biologicalAgent = {"List of BA/T"};
                selectOption(request,"biologicalAgent",biologicalAgent);
                break;

            case 2:
                // Transaction Date
                String[] sendingFacility = {"Yuanmei Art","Tairu Technology","Sino Insurance"};
                selectOption(request,"sendingFacility",sendingFacility);
                String[] receivingFacility = {"Yuanmei Art","Tairu Technology","Sino Insurance"};
                selectOption(request,"receivingFacility",receivingFacility);
                String[] noba = {"Abacavir","Abamectin","Abanoquil","Abunidazole","Acamylophenine"};
                selectOption(request,"noba",noba);
                break;

            case 3:
                //Transaction History
                String[] adjustmentType = {"Import","Export","Disposal","Local Transfer In","Local Transfer Out","Adjustment History"};
                selectOption(request,"adjustmentType",adjustmentType);
                String[] typeOfTransfer = {"Complete","Incomplete"};
                selectOption(request,"typeOfTransfer",typeOfTransfer);
                break;

        }




    }
}
