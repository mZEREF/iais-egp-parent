<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    /*
      You can customize this default file:
      /D:/tools/eclipse/plugins/com.ecquaria.eclipse.sit_6.1.1/WebPage.jsp.default
    */

//handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setAttribute name="header-ext">
    <%
        /* You can add additional content (SCRIPT, STYLE elements)
         * which need to be placed inside HEAD element here.
         */
    %>
</webui:setAttribute>

<webui:setAttribute name="title">
    <%
        /* You can set your page title here. */
    %>

    <%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<webui:setLayout name="iais-intranet"/>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot=IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="col-sm-5">
    <p><textarea name="messageContent" cols="80" rows="25" class="wenbenkuang" id="htmlEditroArea" title="content" readonly >${content}</textarea></p>
</div>
<form method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="submit" class="back" value="Back">
</form>
