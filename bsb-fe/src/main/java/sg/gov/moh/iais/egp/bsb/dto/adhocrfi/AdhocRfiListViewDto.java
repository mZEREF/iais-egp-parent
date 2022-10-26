package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdhocRfiListViewDto {
    private String id;
    //adhocRfiAppNo
    private String requestNo;
    private String facilityName;
    private String facilityNo;
    private LocalDate startDate;
    private LocalDate dueDate;
}
