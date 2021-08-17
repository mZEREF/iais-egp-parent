package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.GlobalConstants;

import java.io.Serializable;


/**
 * This class is intended to be parsed by {@code PageableHandlerMethodArgumentResolver} in spring data web.
 * At the API part, these three fields will be parsed into a {@code Pageable} instance, so at the API part,
 * we don't need the search dto extends this class.
 * But at the egp part, we have to do so, because the Pageable encoder doesn't work properly.
 */
@Data
public class PagingAndSortingDto implements Serializable {
    private int page;
    private int size;
    private String sort;


    /** Suitable for some list pages, it would better to use methods with arguments */
    public void defaultPaging() {
        defaultPaging("createdAt,desc");
    }

    /**
     * Reset to the first page and default page size, restore the default sort also
     * This method is intended for initialization of a new page
     */
    public void defaultPaging(String sortString) {
        page = 0;
        size = GlobalConstants.DEFAULT_PAGE_SIZE;
        sort = sortString;
    }

    public void defaultPaging(String sortColumn, boolean desc) {
        defaultPaging(computeSortString(sortColumn, desc));
    }



    /**
     * changeSort methods are used to make behavior more comprehensive
     */
    public void changeSort(String sortColumn) {
        sort = computeSortString(sortColumn, false);
    }

    public void changeSort(String sortColumn, boolean desc) {
        sort = computeSortString(sortColumn, desc);
    }

    public void changeSort(String sortColumn, String sortType) {
        sort = computeSortString(sortColumn, sortType);
    }


    /**
     * This method generate a string can be parsed by {@code SortHandlerMethodArgumentResolver} in the spring data web.
     * Current design only support sorting by one column.
     * @param sortColumn entity field name for the column
     * @param desc true if use descending, false if use ascending
     * @return a string can be used by {@code SortHandlerMethodArgumentResolver}
     */
    public String computeSortString(String sortColumn, boolean desc) {
        return desc ? sortColumn + "," + "desc" : sortColumn;
    }

    /**
     * @param sortType should be ASC or DESC (no matter upper-case or lower-case)
     */
    public String computeSortString(String sortColumn, String sortType) {
        return sortColumn + "," + sortType;
    }
}
