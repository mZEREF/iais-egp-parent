package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.List;

/**
 * AdhocRfiViewDto
 *
 * @author junyu
 * @date 2022/4/20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdhocRfiViewDto implements Serializable {
    private AdhocRfiDto adhocRfiDto;
    private List<DocRecordInfo> applicationDocDtos;


}
