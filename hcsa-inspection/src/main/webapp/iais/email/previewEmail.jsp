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

<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot=IaisEGPConstant.FE_CSS_ROOT;
%>
<form method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--5" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-5">
                        <p><textarea name="messageContent" cols="80" rows="25" class="wenbenkuang" id="cotent" title="content" readonly >${context}</textarea></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>


</form>
