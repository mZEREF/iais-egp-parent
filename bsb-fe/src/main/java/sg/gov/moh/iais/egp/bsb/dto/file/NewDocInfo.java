package sg.gov.moh.iais.egp.bsb.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
public class NewDocInfo implements Serializable {
    private String tmpId;
    private String docType;
    private String filename;
    private long size;
    private Date submitDate;
    private String submitBy;
    private ByteArrayMultipartFile multipartFile;


    /** Convert this new doc to meta info object
     * @see DocMeta#DocMeta(String, String, long)  */
    public DocMeta toDocMeta() {
        return new DocMeta(docType, filename, size);
    }

    /** Convert this new doc to meta info object with a specific module name
     * @see DocMeta#DocMeta(String, String, String, long, String)  */
    public DocMeta toDocMeta(String module) {
        return new DocMeta(tmpId, docType, filename, size, module);
    }
}
