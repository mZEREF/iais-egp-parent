package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2021/12/30 15:39
 **/
@Data
public class MohInfoDto implements Serializable {
    private String remark;
    private String decision;
}
