package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author: yichen
 * @date time:1/20/2020 12:36 PM
 * @description:
 */

@Slf4j
@Getter
@Setter
public class HcsaServiceTag extends DivTagSupport{

	private String value;
	private Boolean maskValue;
	private HttpServletRequest request;

	public HcsaServiceTag() {
		super();
		init();
	}

	@Override
	protected void init() {
		super.init();
		value = null;
		maskValue = false;
	}

	@Override
	public void release() {
		super.release();
		init();
	}

	@Override
	public int doStartTag() throws JspException {
		if (StringUtils.isEmpty(value)){
			return SKIP_BODY;
		}

		StringBuilder html = new StringBuilder();

		boolean isMask = this.maskValue;
		String value = this.value;
		if (isMask){
			value = ParamUtil.getMaskedString(request, value);
		}

		String serviceName = HcsaServiceCacheHelper.getServiceNameById(value);
		String serviceType = HcsaServiceCacheHelper.getServiceTypeById(value);

		html.append(serviceName);

		//<li><span>${baseItem}</span> (Base Service)</li>
		/*if (isBaseService(serviceType)){
			html.append("<li><span>" + serviceName +"</span> (Base Service)</li>");
		}else if(isSpecifiedService(serviceType)){
			html.append("<li><span>" + serviceName +"</span> (Specified Service)</li>");
		}else if(isSubsumed(serviceType)){
			html.append("<li><span>" + serviceName +"</span> (Subsumed Service)</li>");
		}*/

		try {
			pageContext.getOut().print(StringUtil.escapeSecurityScript(html.toString().trim()));
		} catch (Exception ex) {
			throw new JspTagException("HcsaServiceTag: " + ex.getMessage());
		}

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() {
		init();
		return EVAL_PAGE;
	}

	private Boolean isBaseService(String serviceType){
		return ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(serviceType) ? true : false;
	}

	private Boolean isSpecifiedService(String serviceType){
		return ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED.equals(serviceType) ? true : false;
	}

	private Boolean isSubsumed(String serviceType){
		return ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(serviceType) ? true : false;
	}
}
