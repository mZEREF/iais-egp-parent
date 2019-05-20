<%-- <%@ taglib uri="/workdesk.tld" prefix="workdesk"%> --%>
<%@page import="sop.config.ConfigUtil"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@ page isELIgnored="true"%>
<%@ page import="sop.webflow.rt.api.BaseProcessClass"%>
<%@ page import="sop.webflow.rt.engine5.SOPEngineResponse"%>
<%@ page import="java.io.*"%>
<%@ page import="org.slf4j.*" %>
<%@ page import="ecq.commons.helper.StringHelper"%>
<%-- <%@ page import="sop.webflow.rt.engine.*"%> --%>
<%@ page import="sop.iwe.SessionManager"%>
<%-- <%@ page import="sop.config.web.tags.SmcWebConfig"%> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%!
    private static final Logger logger = LoggerFactory.getLogger("jsp");
%>
<%
    //boolean showError = SmcWebConfig.getShowExceptionStackTrace();
    boolean showError = ConfigUtil.getBoolean(ConfigUtil.SHOW_EXCEPTION_STACK_TRACE,
            ConfigUtil.DEFAULT_SHOW_EXCEPTION_STACK_TRACE);
    pageContext.setAttribute("isShowError", String.valueOf(showError));
    String webContext = ConfigUtil.getWebBathPath(request);
%>

<link rel="stylesheet" type="text/css" href="<%=webContext%>/_statics/css/error-page.css" />

<script language="JavaScript" type="text/javascript">

    function searchTableAction(displayStatus) {
        var searchTr = $("#divError");
        if (displayStatus == "none") {
            $("#showSearchTableCtrl_none").hide();
            $("#showSearchTableCtrl_").show();
            searchTr.slideUp(200);
        } else {
            $("#showSearchTableCtrl_").hide();
            $("#showSearchTableCtrl_none").show();
            searchTr.slideDown(200);
        }
    }

    $(function() {
        var isShowErr = "<c:out value='${isShowError}' />";
        if ('true' == isShowErr){
            $('#divShowHidden').show();
        }
    });

</script>

<div style="padding-left: 30px;">
    <div class="fb-rounded-box-top-error-Appexception">
        <div class="fb-rounded-box-top-left-error-Appexception"></div>
        <div class="fb-rounded-box-top-right-error-Appexception"></div>
    </div>
    <div class="fb-rounded-box-content-error-Appexception fb-summary-info-error-Appexception">
        <div>
            <strong>
                <egov-smc:message key="errorEncountered">An unexpected error has been encountered</egov-smc:message>
            </strong>
            <br />

            <%
                if (showError) {
                    Object obj = request.getAttribute("process");
                    if (obj!=null){
                        BaseProcessClass bpc=(BaseProcessClass)obj;
                        if (bpc.sopRes!=null && bpc.sopRes.hasError()){
                            Throwable t = bpc.sopRes.getError();
                            StringWriter sw = new StringWriter();
                            t.printStackTrace(new PrintWriter(sw));
                            String stack = StringHelper.escapeHtmlChars(sw.toString());
            %>
            <%=StringHelper.escapeHtmlChars(t.getMessage())%><br/><br/>

            <div id="divShowHidden" style="display: none;">
							<span id="showSearchTableCtrl_none"  style="display:none;">
								(<a href="javascript:void(0);" onclick="searchTableAction('none')"><egov-smc:commonLabel>Hide exception stack trace</egov-smc:commonLabel><img width="11" height="11" src="<%=webContext %>/_statics/images/empty.gif" class="icon_toggle_up"></a>)
							</span>
                <span id="showSearchTableCtrl_"  style="display:">
								(<a href="javascript:void(0);" onclick="searchTableAction('')"><egov-smc:commonLabel>Show exception stack trace</egov-smc:commonLabel><img width="11" height="11" src="<%=webContext %>/_statics/images/empty.gif" class="icon_toggle_down"></a>)
							</span>
            </div>
            <div id="divError" style="display: none;">
                    <%=stack%><br/><br/>
            </div>
            <%
                        }
                    }
                }
            %>
        </div>
        <br />
        <c:if test="${NO_PRIVILEGE}">
            <% if (!SessionManager.getInstance(request).isAuthenticated()) {%>
            <div style="color: black;">
                <egov-smc:commonLabel>Click</egov-smc:commonLabel> <a style="color: blue; text-decoration: underline;" href="<c:url value="/" />"><egov-smc:commonLabel>here</egov-smc:commonLabel></a> <egov-smc:commonLabel>to login.</egov-smc:commonLabel>
            </div>
            <% } %>
        </c:if>
        <hr />

        <div class="fb-summary-info-error-Appexception-action">
            <egov-smc:message key="sorryForError">We are sorry for the error you have encountered.</egov-smc:message><br/>
            <egov-smc:message key="PlsReport">Please report it to your administrator and we will investigate it.</egov-smc:message></div>
    </div>
    <div class="fb-rounded-box-bottom-error-Appexception">
        <div class="fb-rounded-box-bottom-left-error-Appexception"></div>
        <div class="fb-rounded-box-bottom-right-error-Appexception"></div>
    </div>
</div>
