package sg.gov.moh.iais.egp.bsb.dto.approval;

import sg.gov.moh.iais.egp.bsb.entity.Facility;

import java.util.Date;

/**
 * @author : LiRan
 * @date : 2021/9/17
 */
public class FacilityDocDto {
    private String id;

    private Facility facility;

    private String name;

    private long size;

    private String fileRepoId;

    private Date submitAt;

    private String submitBy;
}
