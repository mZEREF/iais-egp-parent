package sg.gov.moh.iais.egp.bsb.dto.register.approval;


import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.MemberOf;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JGlobalMap(excluded = {"currentIndex","spHandleAuthId","authorisedPerId"})
public class AuthorisedSelection implements Serializable {
    private String currentIndex;
    //this is the primary key of the table
    @JMap(value = "id")
    private String spHandleAuthId;
    @JMap(value = "${facilityAuthoriser.id}")
    private String authorisedPerId;
    //Work involving Second Schedule Biological Agent
    @NotNull(message = "GENERAL_ERR0006")
    @NotEmpty(message = "GENERAL_ERR0006")
    @CustomMsg(placeHolders = {"field","maxlength"}, replaceVals= {"Work involving Second Schedule Biological Agent","500"})
    @MaxLength(value = 500, message = "GENERAL_ERR0041")
    private String involvedWork;

    //Performing for Second Schedule Biological Agent
    @NotNull(message = "GENERAL_ERR0006")
    @MemberOf(message = "Invalid value", value = {"Y", "N"})
    @CustomMsg(placeHolders = "field", replaceVals = "Performing for Second Schedule Biological Agent")
    private String isPerformed;
}
