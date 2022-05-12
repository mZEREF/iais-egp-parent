package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicationRfiIndicatorDto implements Serializable {
    // display for applicant what rfi you need to do
    private String moduleName;
    // bsb fe different process module url
    private String internetProcessUrl;
    // completed status
    private Boolean status;
}
