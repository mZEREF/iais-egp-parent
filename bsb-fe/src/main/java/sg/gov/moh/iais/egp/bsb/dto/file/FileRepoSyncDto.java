package sg.gov.moh.iais.egp.bsb.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * This DTO is used to synchronize file from one zone to another.
 * This DTO contains new files to be created (contains Id and file data),
 * and contains IDs of files to be deleted.
 * @author chenwei
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileRepoSyncDto implements Serializable {
    private List<NewFileSyncDto> newFiles;
    private List<String> toDeleteIds;
}
