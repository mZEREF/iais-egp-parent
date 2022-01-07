package sg.gov.moh.iais.egp.bsb.dto.process;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/8/23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MohProcessDto implements Serializable {
    private String applicationId;
    private String taskId;
    private SubmitDetailsDto submitDetailsDto;
    private DOScreeningDto doScreeningDto;
    private DOProcessingDto doProcessingDto;
    private AOScreeningDto aoScreeningDto;
    private AOProcessingDto aoProcessingDto;
    private HMScreeningDto hmScreeningDto;
}
