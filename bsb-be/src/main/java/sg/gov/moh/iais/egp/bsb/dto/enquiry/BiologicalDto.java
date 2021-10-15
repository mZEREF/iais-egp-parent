package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/


@Data
public class BiologicalDto extends BaseEntity{
    private static final long serialVersionUID = 1L;

    private String id;

    private String biologicalType;

    private String name;

    private String riskLevel;

    private String schedule;
}
