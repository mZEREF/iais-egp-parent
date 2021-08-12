package sg.gov.moh.iais.egp.bsb.dto.approval;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Getter
@Setter
public class BiologicalQueryDto implements Serializable {

    private String id;

    private String name;

}
