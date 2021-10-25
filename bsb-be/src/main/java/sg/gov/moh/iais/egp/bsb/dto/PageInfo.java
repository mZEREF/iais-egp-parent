package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.GlobalConstants;


/**
 * This class is intend to hold the pagination info returned from the API part.
 * We use data here for the pagination in the jsp.
 * If we don't get pagination data from the API, we can use the {@link #emptyPageInfo} method for fallback.
 */
@Data
public class PageInfo {
    private boolean first;

    private boolean last;

    private int totalPages;

    private long totalElements;

    private int pageNo;

    private int size;

    private int numberOfElements;

    /**
     *  Empty page info with default page size
     */
    public static PageInfo emptyPageInfo() {
        return emptyPageInfo(GlobalConstants.DEFAULT_PAGE_SIZE);
    }

    public static PageInfo emptyPageInfo(int pageSize) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.first = true;
        pageInfo.last = true;
        pageInfo.totalPages = 0;
        pageInfo.totalElements = 0;
        pageInfo.pageNo = 0;
        pageInfo.size = pageSize;
        pageInfo.numberOfElements = 0;
        return pageInfo;
    }

    /**
     *  Empty page info but keep the current page size
     */
    public static PageInfo emptyPageInfo(PagingAndSortingDto dto) {
        return emptyPageInfo(dto.getSize());
    }
}
