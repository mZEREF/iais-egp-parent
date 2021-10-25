package sg.gov.moh.iais.egp.bsb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocSetting {
    private String type;
    private String typeDisplay;
    private boolean mandatory;
}
