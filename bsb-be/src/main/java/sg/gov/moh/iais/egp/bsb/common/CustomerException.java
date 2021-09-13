package sg.gov.moh.iais.egp.bsb.common;

/**
 * @author YiMing
 * DATE:2021/9/10 9:35
 * DESCRIPTION: TODO
 **/
public class CustomerException extends Exception {

    private String retCd ;
    private String msgDes;

    public CustomerException() {
        super();
    }

    public CustomerException(String message) {
        super(message);
        msgDes = message;
    }

    public CustomerException(String retCd, String msgDes) {
        super();
        this.retCd = retCd;
        this.msgDes = msgDes;
    }

    public String getRetCd() {
        return retCd;
    }

    public String getMsgDes() {
        return msgDes;
    }
}
