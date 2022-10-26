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
public class InboxFacSearchDto extends PagingAndSortingDto {
    private String facilityName;
    private String facilityStatus;
    private String role;


    public static final String KEY_FACILITY_NAME = "facilityName";
    public static final String KEY_FACILITY_STATUS = "facilityStatus";
    public static final String KEY_ROLE_IN_FACILITY = "role";
    public void reqObjMapping(HttpServletRequest request){
       this.facilityName = ParamUtil.getString(request,KEY_FACILITY_NAME);
       this.facilityStatus = ParamUtil.getString(request,KEY_FACILITY_STATUS);
       this.role = ParamUtil.getString(request,KEY_ROLE_IN_FACILITY);
    }
}
