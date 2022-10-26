package sg.gov.moh.iais.egp.bsb.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocRecordInfo implements Serializable {
    private String docEntityId;
    private String docType;
    private String docSubType;
    private String filename;
    private long size;
    private String repoId;
    private Date submitDate;
    private String submitBy;



    /** Convert this new doc to meta info object with a specific module name
     * @see DocMeta#DocMeta(String, String, String, long)  */
    public DocMeta toDocMeta() {
        return new DocMeta(repoId, docType, filename, size);
    }
}