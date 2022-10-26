package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;

import java.io.Serializable;


@Data
public class DualDocSortingDto implements Serializable {
    // sorting param (for Spring Sort) for supporting document searching
    private String supportingDocSort;
    // sorting param (for Spring Sort) for internal document searching
    private String internalDocSort;



    public static DualDocSortingDto newInstance() {
        DualDocSortingDto dto = new DualDocSortingDto();
        dto.setSupportingDocSort(null);
        dto.setInternalDocSort(null);
        return dto;
    }
}
