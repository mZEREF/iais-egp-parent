package sg.gov.moh.iais.egp.bsb.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/11/4 10:18
 **/
@Data
public class FacListDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacList{
        private String facId;
        private String facName;
        private String facAddress;
        private String isProtected;
        private Map<String, List<Biological>> bioMap;
    }

    private List<FacList> facLists;

    public FacListDto() {
//        facLists = new ArrayList<>();
//        facLists.add(new FacList());
    }

    public List<FacList> getFacLists() {
        return facLists;
    }

    public void addFacLists(FacList facList){
        this.facLists.add(facList);
    }


}
