<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <br/>
                    <input type="hidden" name="draftNo" value="<iais:mask name="draftNo" value="${AppSubmissionDto.draftNo}"/>" />
                    <p><strong style="font-size:20px;"><iais:message key="GENERAL_ERR0037" escape="false"/></strong></p>
                    <p>You can click <a id="reSubmit" href="#">here</a> to re-submit</p>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    $('#reSubmit').click(function () {
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    });
</script>



