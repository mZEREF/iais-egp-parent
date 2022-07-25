package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;


@Delegator("bsbInboxAFCDelegator")
@Slf4j
public class BsbInboxAFCDelegator {
    private final BsbInboxClient inboxClient;

    public BsbInboxAFCDelegator(BsbInboxClient inboxClient) {
        this.inboxClient = inboxClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void bindAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

}
