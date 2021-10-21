<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>


<%@ include file="arHeader.jsp" %>
<%-- current page: stage--%>
<input type="hidden" name="ar_page" value="stage"/>
<%--preview/ack--%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Preview and Submit</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/previewEfoDetail.jsp" %>
                </div>
            </div>
        </div>
    </div>
</form>
