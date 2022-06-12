package sg.gov.moh.iais.egp.bsb.dto.withdrawn;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;

import java.io.Serializable;
import java.util.List;

/**
 * @author tangtang
 * @date 2021/12/10 10:41
 */
@Data
public class AppSubmitWithdrawnDto implements Serializable {
    private String appId;
    private String initialAppNo;
    private String processType;
    private String reason;
    private String remarks;
    private List<String> withdrawnAppNos;
    //
    private List<DocRecordInfo> savedInfos;
    //
    private List<DocMeta> docMetas;
}
