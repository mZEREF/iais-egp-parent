package sg.gov.moh.iais.egp.bsb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.GlobalConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Support multi-level sorting
 * @see PagingAndSortingDto
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageableDto implements Serializable {
    private int page;
    private int size;
    private List<String> sort;


    public void defaultPaging(String... sortStrings) {
        page = 0;
        size = GlobalConstants.DEFAULT_PAGE_SIZE;
        sort = new ArrayList<>(Arrays.asList(sortStrings));
    }

    /**
     * @param sortType should be ASC or DESC (no matter upper-case or lower-case)
     * @see PagingAndSortingDto#computeSortString(String, String)
     */
    public String computeSortString(String sortColumn, String sortType) {
        return sortColumn + "," + sortType;
    }


    public void changeSort(String sortColumn, String sortType) {
        sort = new ArrayList<>(Collections.singletonList(computeSortString(sortColumn, sortType)));
    }

    public void changeSort(String[] sortStrings) {
        sort = new ArrayList<>(Arrays.asList(sortStrings));
    }

    public void changeSort(List<String> sortList) {
        sort = new ArrayList<>(sortList);
    }

    public void addSort(String sortString) {
        sort.add(sortString);
    }

    public void addSort(String sortColumn, String sortType) {
        sort.add(sortColumn + "," + sortType);
    }
}
