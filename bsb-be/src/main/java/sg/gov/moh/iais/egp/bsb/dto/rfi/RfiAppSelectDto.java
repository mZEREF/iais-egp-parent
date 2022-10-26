package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfiAppSelectDto implements Serializable {
    private boolean applicationSelect;
    private boolean preInspectionChecklistSelect;
}
