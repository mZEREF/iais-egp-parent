<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%--@elvariable id="emailSubject" type="java.lang.String"--%>
<%--@elvariable id="emailContent" type="java.lang.String"--%>

<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <div class="intranet-content">
            <div style="padding: 30px 30px 30px 30px;">
                <h2>
                    <span><c:out value="${emailSubject}"/></span>
                </h2>
                <div>
                    <span>${emailContent}</span><%--Do not escape HTML--%>
                </div>

                <div style="text-align: left"><span><a href="#" onclick="showWaiting();$('#mainForm').submit();"><em class="fa fa-angle-left"> </em> Back</a></span></div>
            </div>
        </div>
    </form>
</div>
