package sg.gov.moh.iais.egp.common.tags;

import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;


public class AddressTag extends SimpleTagSupport {
    private String block;
    private String street;
    private String floor;
    private String unitNo;
    private String postalCode;

    @Override
    public void doTag() throws JspException, IOException {
        String address = TableDisplayUtil.getOneLineAddress(block, street, floor, unitNo, postalCode);
        getJspContext().getOut().print(address);
    }


    public void setBlock(String block) {
        this.block = block;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
