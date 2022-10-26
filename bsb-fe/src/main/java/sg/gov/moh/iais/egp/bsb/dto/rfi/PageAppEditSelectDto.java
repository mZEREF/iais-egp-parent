package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageAppEditSelectDto implements Serializable {
    // Facility Information
    private boolean facSelect;
    // Biological Agents & Toxins (for UCF and BMF)
    private boolean batSelect;
    // Supporting Documents
    private boolean docSelect;
    // Approved Facility Certifier (facility which required certification only)
    private boolean afcSelect;
    // Application Information
    private boolean appSelect;

    // to save RFI selected
    private List<String> selectedList;
}
