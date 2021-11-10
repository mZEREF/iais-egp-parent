package sg.gov.moh.iais.egp.bsb.dto.rfc;

import lombok.Data;

import java.io.Serializable;

/**
 * This is used to hold information for the different column in use RFC.
 * @author : LiRan
 * @date : 2021/11/9
 */
@Data
public class DiffContent implements Serializable {
    private String modifyField;
    private String oldValue;
    private String newValue;
}
