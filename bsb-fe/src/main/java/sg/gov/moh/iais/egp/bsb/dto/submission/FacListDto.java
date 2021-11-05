package sg.gov.moh.iais.egp.bsb.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/11/4 10:18
 **/
@Data
public class FacListDto implements Serializable{

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacList implements Serializable {
        private String facId;
        private String facName;
        private String facAddress;
        private String isProtected;
        private Map<String, List<Biological>> bioMap;
        private FacOfficer facOfficer;
        private List<FacAdmin> facAdminList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacOfficer implements Serializable{
        private String name;
        private String nationality;
        private String idNumber;
        private String designation;
        private String contactNo;
        private String email;
        private Date employmentStartDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacAdmin implements Serializable{
        private String name;
        private String nationality;
        private String idNumber;
        private String designation;
        private String contactNo;
        private String email;
        private Date employmentStartDate;
    }

    private List<FacList> facLists;
    private List<FacAdmin> facAdminList;

    public FacListDto() {
        facLists = new ArrayList<>();
        facLists.add(new FacList());
    }

    public List<FacList> getFacLists() {
        return facLists;
    }

    public void addFacLists(FacList facList){
        this.facLists.add(facList);
    }

    public List<FacAdmin> getFacAdminList() {
        return facAdminList;
    }

    public void addFacAdminList(FacAdmin facAdmin){
        this.facAdminList.add(facAdmin);
    }
}
