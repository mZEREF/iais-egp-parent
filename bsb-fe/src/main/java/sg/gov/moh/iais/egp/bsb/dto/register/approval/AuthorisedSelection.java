package sg.gov.moh.iais.egp.bsb.dto.register.approval;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthorisedSelection implements Serializable {
    private String currentIndex;
    //this is the primary key of the table
    private String spHandleAuthId;
    private String authorisedPerId;
    //Work involving Second Schedule Biological Agent
    private String involvedWork;

    //Performing for Second Schedule Biological Agent
    private String isPerformed;
}
