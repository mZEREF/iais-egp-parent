<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <div class="row">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="bg-title">
                        <c:if test="${'Y' == rfiSuccessInfo}">
                            <h2><span><c:out value="${successInfo}"></c:out></span></h2>
                        </c:if>

                        <c:if test="${'Y' != rfiSuccessInfo}">
                            <h2><iais:message key="${successInfo}" escape="true"></iais:message></h2>
                        </c:if>
                        <h2><span><c:out value="You have successfully dealt with it"></c:out></span></h2>
                    </div>
                </div>
                <div align="left"><span><a href="/system-admin-web/eservice/INTRANET/MohProcessingRevocation"><em class="fa fa-angle-left"></em> Back To Process</a></span></div>
            </div>
        </div>
    </div>
</div>
