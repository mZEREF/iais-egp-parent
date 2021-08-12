package sg.gov.moh.iais.egp.bsb.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;


/**
 * This class is intend to hold the pagination info which comes from the JPA Page
 * We return this object instead of the JPA page, because it contains too many fields we don't need
 */
@ApiModel("Page data for search result")
@Data
public class PageInfo {
    @ApiModelProperty("if this is the first page")
    private boolean first;

    @ApiModelProperty("if this is the last page")
    private boolean last;

    @ApiModelProperty("total page amount")
    private int totalPages;

    @ApiModelProperty("total elements in Database")
    private long totalElements;

    @ApiModelProperty("current page number, starts with 0")
    private int pageNo;

    @ApiModelProperty("page size")
    private int size;

    @ApiModelProperty("number of elements in this page")
    private int numberOfElements;


    /**
     * Construct this class instance by this method
     */
    public static PageInfo of(Page<?> pageData) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.first = pageData.isFirst();
        pageInfo.last = pageData.isLast();
        pageInfo.totalPages = pageData.getTotalPages();
        pageInfo.totalElements = pageData.getTotalElements();
        pageInfo.pageNo = pageData.getNumber();
        pageInfo.size = pageData.getSize();
        pageInfo.numberOfElements = pageData.getNumberOfElements();
        return pageInfo;
    }
}
