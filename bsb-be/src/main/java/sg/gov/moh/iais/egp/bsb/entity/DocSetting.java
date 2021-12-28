package sg.gov.moh.iais.egp.bsb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocSetting implements Serializable {
    private String type;
    private String typeDisplay;
    private boolean mandatory;
}
