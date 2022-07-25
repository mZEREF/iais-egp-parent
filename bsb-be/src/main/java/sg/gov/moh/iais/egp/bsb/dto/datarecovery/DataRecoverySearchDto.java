package sg.gov.moh.iais.egp.bsb.dto.datarecovery;

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
public class DataRecoverySearchDto extends PagingAndSortingDto {
    private String searchModuleName;
    private String searchFunctionName;
    private String searchCreateDateFrom;
    private String searchCreateDateTo;

    private static final String KEY_SEARCH_MODULE_NAME          = "searchModuleName";
    private static final String KEY_SEARCH_FUNCTION_NAME        = "searchFunctionName";
    private static final String KEY_SEARCH_CREATE_DATE_FROM     = "searchCreateDateFrom";
    private static final String KEY_SEARCH_CREATE_DATE_TO       = "searchCreateDateTo";

    public void reqObjMapping(HttpServletRequest request) {
        this.setSearchModuleName(request.getParameter(KEY_SEARCH_MODULE_NAME));
        this.setSearchFunctionName(request.getParameter(KEY_SEARCH_FUNCTION_NAME));
        this.setSearchCreateDateFrom(request.getParameter(KEY_SEARCH_CREATE_DATE_FROM));
        this.setSearchCreateDateTo(request.getParameter(KEY_SEARCH_CREATE_DATE_TO));
    }
}
