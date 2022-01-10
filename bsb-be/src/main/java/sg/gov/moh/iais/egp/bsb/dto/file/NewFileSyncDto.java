package sg.gov.moh.iais.egp.bsb.dto.file;

import lombok.Data;


/**
 * When we sync a file, we need to sync the repoId and file data.
 * @author chenwei
 */
@Data
public class NewFileSyncDto {
    private String id;
    private byte[] data;
}
