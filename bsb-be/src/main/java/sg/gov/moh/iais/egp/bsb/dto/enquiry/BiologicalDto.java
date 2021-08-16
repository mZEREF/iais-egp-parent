package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/6 13:44
 * DESCRIPTION: TODO
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
