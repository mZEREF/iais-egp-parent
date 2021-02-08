<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
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
                        <h2>
                            <c:if test="${COMPLETED}" >
                                <iais:message key="LOLEV_ACK039" escape="true" />
                            </c:if>
                            <c:if test="${!COMPLETED}">
                                <iais:message key="LOLEV_ACK035" escape="true"/>
                            </c:if>
                        </h2>
                    </div>
                </div>
                <div align="left"><span><a  href="/main-web/eservice/INTRANET/MohBackendInbox"><em class="fa fa-angle-left"> </em> Back</a></span></div>
            </div>
        </div>
    </div>
</div>

