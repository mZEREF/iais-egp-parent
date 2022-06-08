package sg.gov.moh.iais.egp.bsb.common.tags;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BatClient;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;


public class BatCodeTag extends SimpleTagSupport {
    private String code;


    @Override
    public void doTag() throws JspException, IOException {
        RedisCacheHelper redisCacheHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
        String name = redisCacheHelper.get(RedisNameSpaceConstant.CACHE_NAME_BSB_BAT, code);
        if (!StringUtils.hasLength(name)) {
            /* Try to find the code from DB, if found, put it into cache */
            BatClient batClient = SpringContextHelper.getContext().getBean("batInfoFeignClient", BatClient.class);
            name = batClient.queryBatName(code);
            if (StringUtils.hasLength(name)) {
                redisCacheHelper.set(RedisNameSpaceConstant.CACHE_NAME_BSB_BAT, code, name);
            } else {
                name = "";
            }
        }
        getJspContext().getOut().print(name);
    }

    public void setCode(String code) {
        this.code = code;
    }
}
