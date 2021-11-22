package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InventoryClient;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryAgentResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryDtResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inventory.InventoryDto;
import sg.gov.moh.iais.egp.bsb.entity.DataSubmissionBat;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/15 18:24
 **/
@Delegator(value = "inventoryDelegator")
@Slf4j
public class InventoryDelegator {
    private static final String KEY_ENQUIRY_SEARCH_DTO = "inventoryDto";
    private static final String PARAM_INVENTORY_RESULT = "inventoryResult";
    private static final String PARAM_INVENTORY_PARAM = "inventoryParam";
    private static final String PARAM_BIO_ID_MAP = "bioIdMap";
    private static final String PARAM_SEARCH_CHK = "searchChk";
    private static final String PARAM_COUNT  = "count";
    private static final String KEY_PAGE_INFO = "pageInfo";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";
    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    //--------------------KEY-----------------------
    private static final String KEY_SCHEDULE_TYPE = "scheduleType";
    private static final String KEY_BIOLOGICAL_NAME = "bioName";
    private static final String KEY_TRANSACTION_TYPE = "transactionType";
    private static final String KEY_TRANSACTION_DATE_FROM = "transactionDateFrom";
    private static final String KEY_TRANSACTION_DATE_TO = "transactionDateTo";
    private static final String KEY_FACILITY_NAME       = "facilityName";
    private static final String KEY_SEND_FACILITY       = "sendFacility";
    private static final String KEY_RECEIVE_FACILITY    = "recFacility";

    @Autowired
    private InventoryClient inventoryClient;

    /**
     * AutoStep: prepareData
     */
    public void prepareData(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String count = ParamUtil.getString(request, PARAM_SEARCH_CHK);
        ParamUtil.setRequestAttr(request, PARAM_COUNT, count);
        preSelectOption(request);
        InventoryDto searchDto = getSearchDto(request);
        getFilterAndSearch(request,searchDto,count);
        ParamUtil.setSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO, searchDto);
    }

    /**
     * AutoStep: changePage
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
        String scheduleType = ParamUtil.getString(request,KEY_SCHEDULE_TYPE);
        String biologicalAgent = ParamUtil.getString(request,KEY_BIOLOGICAL_NAME);
        String transactionType = ParamUtil.getString(request,KEY_TRANSACTION_TYPE);
        String transactionDateFrom = ParamUtil.getString(request,KEY_TRANSACTION_DATE_FROM);
        String transactionDateTo = ParamUtil.getString(request,KEY_TRANSACTION_DATE_TO);
        String[] facilityName = ParamUtil.getStrings(request,KEY_FACILITY_NAME);
        String sendFacility = ParamUtil.getString(request,KEY_SEND_FACILITY);
        String recFacility = ParamUtil.getString(request,KEY_RECEIVE_FACILITY);

        inventoryDto.setScheduleType(scheduleType);

        inventoryDto.setBioName(biologicalAgent);

        inventoryDto.setTransactionType(transactionType);

        inventoryDto.setRecFacility(sendFacility);

        inventoryDto.setRecFacility(recFacility);

        inventoryDto.setTransactionDtFrom(transactionDateFrom);

        inventoryDto.setTransactionDtTo(transactionDateTo);

        if(facilityName != null){
            inventoryDto.setFacilityName(Arrays.asList(facilityName));
        }


     if("agent".equals(count)){
         InventoryAgentResultDto dto =  inventoryClient.findInventoryByAgentInfo(inventoryDto).getEntity();
         List<DataSubmissionBat> dataSubmissionBats = dto.getBsbAgent();
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_RESULT,dataSubmissionBats);
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_PARAM,inventoryDto);
         ParamUtil.setRequestAttr(request,PARAM_BIO_ID_MAP,dto.getBioIdMap());
         ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, dto.getPageInfo());
     }else if("date".equals(count)){
         InventoryDtResultDto dto = inventoryClient.findInventoryByDt(inventoryDto).getEntity();
         List<DataSubmissionBat> dataSubmissionBats = dto.getBsbDt();
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_RESULT,dataSubmissionBats);
         ParamUtil.setRequestAttr(request,PARAM_INVENTORY_PARAM,inventoryDto);
         ParamUtil.setRequestAttr(request,PARAM_BIO_ID_MAP,dto.getBioIdMap());
         ParamUtil.setRequestAttr(request, KEY_PAGE_INFO, dto.getPageInfo());
     }

    }

    /**
     * AutoStep: preHistoryData
     */
    public void preHistoryData(BaseProcessClass bpc) {
        String count = ParamUtil.getString(bpc.request, "searchChk");
        ParamUtil.setSessionAttr(bpc.request, "count", count);
        preSelectOption(bpc.request);
    }

    /**
     * AutoStep: doAdjustment
     */
    public void doAdjustment(BaseProcessClass bpc) {
        //do nothing now
    }

    /**
     * AutoStep: preBasicData
     */
    public void preBasicData(BaseProcessClass bpc) {
        String count = ParamUtil.getString(bpc.request, "searchChk");
        if (StringUtils.hasLength(count)) {
            ParamUtil.setRequestAttr(bpc.request, "count", count);
        }
    }

    /**
     * AutoStep: preBasicList
     */
    public void preBasicList(BaseProcessClass bpc) {
        String count = ParamUtil.getString(bpc.request, "searchChk");
        ParamUtil.setSessionAttr(bpc.request, "count", count);
        preSelectOption(bpc.request);
    }

    /**
     * AutoStep: doBasicAdjustment
     */
    public void doBasicAdjustment(BaseProcessClass bpc) {
        //do nothing now
    }

    public void selectOption(HttpServletRequest request, String name, List<String> strings) {
        List<SelectOption> selectModel = new ArrayList<>(strings.size());
        for (String string : strings) {
            if (!"biologicalAgent".equals(name)) {
                selectModel.add(new SelectOption(string, string));
            } else {
                selectModel.add(new SelectOption(inventoryClient.getBiologicalByName(string).getEntity().getId(), string));
            }
        }
        ParamUtil.setRequestAttr(request, name, selectModel);
    }

    public void preSelectOption(HttpServletRequest request) {
        List<String> facNames = inventoryClient.queryDistinctFN().getEntity();
        selectOption(request, KEY_FACILITY_NAME, facNames);
        List<String> bioNames = inventoryClient.queryDistinctFA().getEntity();
        selectOption(request, KEY_BIOLOGICAL_NAME, bioNames);
    }

    private InventoryDto getSearchDto(HttpServletRequest request) {
        InventoryDto searchDto = (InventoryDto) ParamUtil.getSessionAttr(request, KEY_ENQUIRY_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private InventoryDto getDefaultSearchDto() {
        return new InventoryDto();
    }

}
