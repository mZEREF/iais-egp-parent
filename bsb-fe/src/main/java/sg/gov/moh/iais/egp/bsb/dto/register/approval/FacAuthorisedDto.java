package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacAuthorisedDto extends ValidatableNodeValue {

    public void reqObjMapping(HttpServletRequest request) {

    }
}
