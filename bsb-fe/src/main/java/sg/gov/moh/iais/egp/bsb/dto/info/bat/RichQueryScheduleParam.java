package sg.gov.moh.iais.egp.bsb.dto.info.bat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RichQueryScheduleParam {
    private String activity;
    private boolean protectedPlace;
}
