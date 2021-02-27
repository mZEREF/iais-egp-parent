package com.ecquaria.cloud.moh.iais.tags;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import lombok.extern.slf4j.Slf4j;

/**
 * DownloadFileTag
 *
 * @author Jinhua
 * @date 2021/2/26 13:28
 */
@Slf4j
public class DownloadFileTag extends TagSupport {
    private static final long serialVersionUID = 6107270399082706212L;

    private String fileRepoIdName;
    private String fileRepoId;
    private String docName;

    public DownloadFileTag() throws JspException {
        super();
        init();
    }

    // resets local state
    private void init() throws JspException {
        setFileRepoIdName(null);
    }

    // Releases any resources we may have (or inherit)
    @Override
    public void release() {
        super.release();
        try {
            init();
        } catch (JspException e) {
            log.info(e.getMessage(),e);
        }
    }

    @Override
    public int doStartTag() {
        StringBuilder html = new StringBuilder();
        //html
        html.append("<a href=\"");
        html.append(((HttpServletRequest)pageContext.getRequest()).getContextPath());
        html.append("/file-repo?filerepo=").append(fileRepoIdName).append('&').append(fileRepoIdName);
        html.append('=').append(MaskUtil.maskValue(fileRepoIdName, fileRepoId));
        try {
            html.append("&fileRepoName=").append(URLEncoder.encode(docName, StandardCharsets.UTF_8.toString()));
            html.append(" title=\"Download\" class=\"downloadFile\">").append(docName).append("</a>");
            pageContext.getOut().print(html.toString());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new IaisRuntimeException("LangSelectTag: " + ex.getMessage(),ex);
        }
        release();

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setFileRepoIdName(String fileRepoIdName) {
        this.fileRepoIdName = fileRepoIdName;
    }

    public void setFileRepoId(String fileRepoId) {
        this.fileRepoId = fileRepoId;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
