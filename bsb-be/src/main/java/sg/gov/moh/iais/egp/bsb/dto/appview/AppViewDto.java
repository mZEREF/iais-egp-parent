package sg.gov.moh.iais.egp.bsb.dto.appview;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2022/1/6
 */
@Data
public class AppViewDto implements Serializable {
    private String applicationId;
    private String moduleType;
}
