<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" id="perViewEmail" name="perViewEmail" value="Y">

        <div class="intranet-content">
            <div style="padding: 30px 30px 30px 30px;">
                <h2>
                    <span>${appPremisesUpdateEmailDto.subject}</span>
                </h2>
                <div >
                    <span id="viewMessageContent">${appPremisesUpdateEmailDto.mailContent}</span>
                </div>

                <div align="left"><span><a href="#" onclick="$('#mainForm').submit();"><em class="fa fa-angle-left"> </em> Back</a></span></div>
            </div>
        </div>
    </form>
</div>

<script>
    $(document).ready(function () {
        $('#viewMessageContent a').removeAttr('href')
    })
</script>