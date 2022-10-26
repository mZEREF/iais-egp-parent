package sg.gov.moh.iais.egp.bsb.dto.chklst;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * BsbAdCheckListShowDto
 *
 * @author Jinhua
 * @date 2022/5/13 16:34
 */
@Getter
@Setter
public class BsbAdCheckListShowDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<BsbAdhocNcCheckItemDto> adItemList;
    private boolean moreOneDraft;

}
