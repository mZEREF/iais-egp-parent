package sg.gov.moh.iais.egp.bsb.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/** This DTO add additional description fields for document type and submit person.
 * This DTO does not contain four audit fields.
 * Attention! Your entity may not have the same field name with this class,
 * be safe to add config to handle this or change your field name. */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocDisplayDto implements Serializable {
    @JMap
    private String id;

    @JMap
    private String docName;

    @JMap
    private Long docSize;

    @JMap
    private String docType;

    private String docTypeDesc;

    @JMap
    private String fileRepoId;

    @JMap
    private Date submitDt;

    @JMap
    private String submitBy;

    private String submitByName;
}
