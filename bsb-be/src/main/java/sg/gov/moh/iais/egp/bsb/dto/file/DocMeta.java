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
    private String filename;
    private long size;
    private String module;

    public DocMeta(String docType, String filename, long size) {
        this.docType = docType;
        this.filename = filename;
        this.size = size;
    }

    public DocMeta(String id, String docType, String filename, long size, String module) {
        this.id = id;
        this.docType = docType;
        this.filename = filename;
        this.size = size;
        this.module = module;
    }
}