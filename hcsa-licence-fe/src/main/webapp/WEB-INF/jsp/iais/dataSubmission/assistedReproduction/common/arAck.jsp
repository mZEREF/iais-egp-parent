<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%-- current page: ack --%>

<%@ include file="arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="formHidden.jsp" %>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Successful Submission</h3>
                <div>Thank you for your submission.</div>
                <div class="application-tab-footer">
                    <div class="col-xs-12 col-sm-12 col-md-12">
                        <div class="button-group">
                            <a class="btn btn-secondary premiseSaveDraft" id="startSbt" >Start Another Submission</a>
                            <a class="btn btn-primary next premiseId" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to DashBoard</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
