package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
public class FacAuthorisedDto implements Serializable {
    private List<AuthorisedSelection> facAuthorisedSelections;

    private Set<String> spHandleAuthDeleteIdSet;

    public List<String> getAuthorisedPersonIdList(){
        return facAuthorisedSelections.stream().map(AuthorisedSelection::getAuthorisedPerId).collect(Collectors.toList());
    }
}
