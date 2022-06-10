package sg.gov.moh.iais.egp.bsb.dto.withdrawn;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WithdrawnAckDto {
    private List<String> applicationNos;
    private Date withdrawnDate;
}
