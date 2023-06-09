package sg.gov.moh.iais.egp.bsb.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocMeta implements Serializable {
    private String id;
    private String docType;
    private String docSubType;
    private String filename;
    private long size;

    public DocMeta(String docType, String filename, long size) {
        this.docType = docType;
        this.filename = filename;
        this.size = size;
    }

    public DocMeta(String id, String docType, String filename, long size) {
        this.id = id;
        this.docType = docType;
        this.filename = filename;
        this.size = size;
    }

    public DocMeta(String id, String docType, String docSubType, String filename, long size) {
        this.id = id;
        this.docType = docType;
        this.docSubType = docSubType;
        this.filename = filename;
        this.size = size;
    }
}