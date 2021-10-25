package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InventoryClient;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryAgentResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryDtResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryDto;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/15 18:24
 * DESCRIPTION: TODO
 **/
@Delegator(value = "inventoryDelegator")
@Slf4j
public class InventoryDelegator {
    private static final String KEY_ENQUIRY_SEARCH_DTO = "inventoryDto";
    private static final String PARAM_INVENTORY_RESULT = "inventoryResult";
    private static final String PARAM_INVENTORY_PARAM = "inventoryParam";
    private static final String KEY_PAGE_INFO = "pageInfo";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";
    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";
//    private static final String PACKAGE_NAME_BIO_AGENT = "sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent";
    @Autowired
    private InventoryClient inventoryClient;

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, "searchChk");
        ParamUtil.setRequestAttr(request, "count", count);
        preSelectOption(request,count);
        InventoryDto searchDto = getSearchDto(request);
        getFilterAndSearch(request,searchDto,count);
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, searchDto);
    }

    /**
     * AutoStep: changePage
     * @param bpc
     */
    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InventoryDto inventoryDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                inventoryDto.setPage(0);
                inventoryDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                inventoryDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, inventoryDto);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InventoryDto inventoryDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDT);
        inventoryDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, inventoryDto);
    }

    public void getFilterAndSearch(HttpServletRequest request,InventoryDto inventoryDto,String count) throws ParseException {
        inventoryDto.clearAllFields();
        String scheduleType = ParamUtil.getString(request,"scheduleType");
        String biologicalAgent = ParamUtil.getString(request,"biologicalAgent");
        String transactionType = ParamUtil.getString(request,"transactionType");
        Date transactionDateFrom = Formatter.parseDate(ParamUtil.getString(request,ParamUtil.getString(request,"transactionDateFrom")));
        Date transactionDateTo = Formatter.parseDate(ParamUtil.getString(request,ParamUtil.getString(request,"transactionDateTo")));
        String[] facilityName = ParamUtil.getStrings(request,"facilityName");
        String sendFacility = ParamUtil.getString(request,"sendFacility");
        String recFacility = ParamUtil.getString(request,"recFacility");
        if(StringUtil.isNotEmpty(scheduleType)){
         inventoryDto.setScheduleType(scheduleType);
     }
     if(StringUtil.isNotEmpty(biologicalAgent)){
         inventoryDto.setBiologicalAgent(biologicalAgent);
     }
     if(StringUtil.isNotEmpty(transactionType)){
         inventoryDto.setTransactionType(transactionType);
     }
     if(StringUtil.isNotEmpty(sendFacility)){
         inventoryDto.setRecFacility(sendFacility);
     }
     if(StringUtil.isNotEmpty(recFacility)){
         inventoryDto.setRecFacility(recFacility);
     }
     if(transactionDateFrom != null){
         inventoryDto.setTransactionDtFrom(transactionDateFrom);
     }
     if(transactionDateTo != null){
         inventoryDto.setTransactionDtTo(transactionDateTo);
     }
     if(facilityName != null && facilityName.length >0){
         inventoryDto.setFacilityName(Arrays.asList(facilityName));
     }
     if("agent".equals(count)){
         InventoryAgentResultDto inventoryAgentResultDto =  inventoryClient.findInventoryByAgentInfo(inventoryDto).getEntity();
         List<FacilityBiologicalAgent> facilityBiologicalAgents = inventoryAgentResultDto.getBsbAgent();
//         for (FacilityBiologicalAgent agent : facilityBiologicalAgents) {
//             agent.setBioName(inventoryClient.getBiologicalById(agent.getBiologicalId()).getEntity().getName());
//         }
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_RESULT,facilityBiologicalAgents);
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_PARAM,inventoryDto);
         ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, inventoryAgentResultDto.getPageInfo());
     }else if("date".equals(count)){
         InventoryDtResultDto inventoryDtResultDto = inventoryClient.findInventoryByDt(inventoryDto).getEntity();
         List<FacilityBiologicalAgent> facilityBiologicalAgents = inventoryDtResultDto.getBsbDt();
//         for (FacilityBiologicalAgent agent : facilityBiologicalAgents) {
//             agent.setBioName(inventoryClient.getBiologicalById(agent.getBiologicalId()).getEntity().getName());
//         }
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_RESULT,facilityBiologicalAgents);
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_PARAM,inventoryDto);
         ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, inventoryDtResultDto.getPageInfo());
     }

    }



    /**
     * AutoStep: preHistoryData
     *
     * @param bpc
     */
    public void preHistoryData(BaseProcessClass bpc) {
        String count = ParamUtil.getString(bpc.request, "searchChk");
        ParamUtil.setSessionAttr(bpc.request, "count", count);
        preSelectOption(bpc.request, "3");
    }

    /**
     * AutoStep: doAdjustment
     *
     * @param bpc
     */
    public void doAdjustment(BaseProcessClass bpc) {

    }

    /**
     * AutoStep: preBasicData
     *
     * @param bpc
     */
    public void preBasicData(BaseProcessClass bpc) {
        String count = ParamUtil.getString(bpc.request, "searchChk");
        if (StringUtil.isEmpty(count) || count == null) {
            count = "0";
        }
        ParamUtil.setRequestAttr(bpc.request, "count", count);
    }

    /**
     * AutoStep: preBasicList
     *
     * @param bpc
     */
    public void preBasicList(BaseProcessClass bpc) {
        String count = ParamUtil.getString(bpc.request, "searchChk");
        ParamUtil.setSessionAttr(bpc.request, "count", count);
        preSelectOption(bpc.request, "3");
    }

    /**
     * AutoStep: doBasicAdjustment
     *
     * @param bpc
     */
    public void doBasicAdjustment(BaseProcessClass bpc) {

    }

    public void selectOption(HttpServletRequest request, String name, List<String> strings) {
        List<SelectOption> selectModel = IaisCommonUtils.genNewArrayList();
        log.info(StringUtil.changeForLog("strings value" + strings.toString()));
        for (String string : strings) {
            if (!"biologicalAgent".equals(name)) {
                selectModel.add(new SelectOption(string, string));
            } else {
                selectModel.add(new SelectOption(inventoryClient.getBiologicalByName(string).getEntity().getId(), string));
            }
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    public void preSelectOption(HttpServletRequest request, String num) {
        List<String> facNames = inventoryClient.queryDistinctFN().getEntity();
        selectOption(request, "facilityName", facNames);
        List<String> bioNames = inventoryClient.queryDistinctFA().getEntity();
        selectOption(request, "biologicalAgent", bioNames);
    }

    private InventoryDto getSearchDto(HttpServletRequest request) {
        InventoryDto searchDto = (InventoryDto) ParamUtil.getSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InventoryDto getDefaultSearchDto() {
        InventoryDto dto = new InventoryDto();
        return dto;
    }


}
