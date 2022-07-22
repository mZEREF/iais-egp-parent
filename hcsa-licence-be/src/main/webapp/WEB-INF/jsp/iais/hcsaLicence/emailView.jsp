<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet-view"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class=" col-lg-10 col-xs-10 " style="left: 8%;" >
        <div style="padding: 30px 30px 30px 30px;">
            <h2>
                <span>${appPremisesUpdateEmailDto.subject}</span>
            </h2>
            <div >
                <span>${appPremisesUpdateEmailDto.mailContent}</span>
            </div>

        </div>
    </div>
</form>
