package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Application;

import java.util.List;

/**
 * @author Zhu Tangtang
 **/

@Data
public class AOQueryResultDto {
    private PageInfo pageInfo;
    private List<Application> tasks;
}
