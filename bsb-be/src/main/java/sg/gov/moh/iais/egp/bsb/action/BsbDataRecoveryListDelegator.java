package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DataRecoveryClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.datarecovery.DataRecoverySearchDto;
import sg.gov.moh.iais.egp.bsb.dto.datarecovery.DataRecoverySearchResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PAGE_SIZE;


@Slf4j
@Delegator("bsbDataRecoveryListDelegator")
public class BsbDataRecoveryListDelegator {
    private static final String KEY_DATA_RECOVERY_SEARCH_DTO        = "dataRecoverySearchDto";
    private static final String KEY_DATA_RECOVERY_LIST_PAGE_INFO    = "pageInfo";
    private static final String KEY_DATA_RECOVERY_LIST_DATA_LIST    = "dataList";
    private static final String KEY_MASKED_DATA_RECOVERY_ID         = "maskedDataRecoveryId";
    private static final String KEY_RECOVER_RESULT                  = "recoverResult";

    private final DataRecoveryClient dataRecoveryClient;

    @Autowired
    public BsbDataRecoveryListDelegator(DataRecoveryClient dataRecoveryClient) {
        this.dataRecoveryClient = dataRecoveryClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_DATA_RECOVERY_SEARCH_DTO);
    }

    public void init(BaseProcessClass bpc) {
        // do nothing now
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DataRecoverySearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_DATA_RECOVERY_SEARCH_DTO, searchDto);

        ResponseDto<DataRecoverySearchResultDto> resultDto = dataRecoveryClient.searchDataRecoveryList(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_DATA_RECOVERY_LIST_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_DATA_RECOVERY_LIST_DATA_LIST, resultDto.getEntity().getDataRecoveryDisplayDtoList());
        } else {
            log.info("Search Data Recovery List fail");
            ParamUtil.setRequestAttr(request, KEY_DATA_RECOVERY_LIST_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_DATA_RECOVERY_LIST_DATA_LIST, new ArrayList<>());
        }
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DataRecoverySearchDto searchDto = getSearchDto(request);
        searchDto.reqObjMapping(request);
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_DATA_RECOVERY_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DataRecoverySearchDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_DATA_RECOVERY_SEARCH_DTO, searchDto);
    }

    public void recoverData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedDataRecoveryId = ParamUtil.getString(request, KEY_MASKED_DATA_RECOVERY_ID);
        String dataRecoveryId = MaskUtil.unMaskValue(KEY_MASKED_DATA_RECOVERY_ID, maskedDataRecoveryId);
        String response = dataRecoveryClient.recoverDataById(dataRecoveryId);
        ParamUtil.setRequestAttr(request, KEY_RECOVER_RESULT, response);
    }

    public void view(BaseProcessClass bpc) {
        // do nothing now, this feature is under consideration
    }

    private DataRecoverySearchDto getSearchDto(HttpServletRequest request) {
        DataRecoverySearchDto dto = (DataRecoverySearchDto) ParamUtil.getSessionAttr(request, KEY_DATA_RECOVERY_SEARCH_DTO);
        if (dto == null) {
            dto = new DataRecoverySearchDto();
            dto.defaultPaging();
        }
        return dto;
    }
}
