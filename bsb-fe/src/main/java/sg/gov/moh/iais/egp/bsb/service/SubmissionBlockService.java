package sg.gov.moh.iais.egp.bsb.service;


import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Service
public class SubmissionBlockService {
    private final boolean notBlock;

    public SubmissionBlockService(@Value("${iais.bsb.block.submission.disable:false}") String notBlock) {
        this.notBlock = Boolean.parseBoolean(notBlock);
    }


    /**
     * Checks if it's a double submission, if so block and throw an exception; if not, set the block flag to prevent
     * double submission.
     * <p>
     * Will do nothing, if the 'not block' flag is on.
     */
    public void blockSubmission(HttpServletRequest request, String blockFlagAttributeName) {
        if (!notBlock) {
            String blockSubmit = (String) ParamUtil.getSessionAttr(request, blockFlagAttributeName);
            if (blockSubmit != null) {
                throw new IllegalStateException("Double submission!");
            } else {
                ParamUtil.setSessionAttr(request, blockFlagAttributeName, MasterCodeConstants.YES);
            }
        }
    }
}
