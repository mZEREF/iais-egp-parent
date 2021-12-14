package sg.gov.moh.iais.egp.bsb.dto.report;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2021/12/9 10:30
 **/
@Data
public class BiologicalInfo implements Serializable {
    private String bioId;
    private String bioName;
}
