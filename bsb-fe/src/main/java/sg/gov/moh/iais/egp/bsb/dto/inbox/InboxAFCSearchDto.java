package sg.gov.moh.iais.egp.bsb.dto.inbox;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import javax.servlet.http.HttpServletRequest;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InboxAFCSearchDto extends PagingAndSortingDto {
    private String searchAppNo;
    private String searchProcessType;
    private String searchCertificationStatus;
    private String searchFacilityName;
    private String searchUpdatedDateFrom;
    private String searchUpdatedDateTo;

    public static final String KEY_APPLICATION_NO = "searchAppNo";
    public static final String KEY_PROCESS_TYPE = "searchProcessType";
    public static final String KEY_CERTIFICATION_STATUS = "searchCertificationStatus";
    public static final String KEY_FACILITY_NAME = "searchFacilityName";
    public static final String KEY_UPDATE_DATE_FROM = "searchUpdatedDateFrom";
    public static final String KEY_UPDATE_DATE_TO = "searchUpdatedDateTo";

    public void reqObjMapping(HttpServletRequest request){
        this.searchAppNo = ParamUtil.getString(request,KEY_APPLICATION_NO);
        this.searchProcessType = ParamUtil.getString(request,KEY_PROCESS_TYPE);
        this.searchCertificationStatus = ParamUtil.getString(request,KEY_CERTIFICATION_STATUS);
        this.searchFacilityName = ParamUtil.getString(request,KEY_FACILITY_NAME);
        this.searchUpdatedDateFrom = ParamUtil.getString(request,KEY_UPDATE_DATE_FROM);
        this.searchUpdatedDateTo = ParamUtil.getString(request,KEY_UPDATE_DATE_TO);
    }
}
