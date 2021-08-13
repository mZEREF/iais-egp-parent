package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;

/**
 * @author Zhu Tangtang
 * @date 2021/8/13 14:46
 */
@Data
public class AODecisionDto {
    public String facId;
    public RevocationDetailsDto revocationDetailsDto;
    public BsbRoutingHistoryDto historyDto;
}
