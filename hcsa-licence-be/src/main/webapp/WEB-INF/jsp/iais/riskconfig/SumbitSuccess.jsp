<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
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
                            <c:if test="${empty RSMVerisonChanged}">
                                The data have been submitted.
                            </c:if>
                            <c:if test="${not empty RSMVerisonChanged}">
                                <iais:message key="RSM_ACK001" escape="true"></iais:message>
                            </c:if>
                           </h2>
                    </div>
                </div>
               <c:if test="${backButtonNeed == 'Y'}">
                <div align="left"><span><a href="/hcsa-licence-web/eservice/INTRANET/MohRiskConigMenu"><em class="fa fa-angle-left"></em> Back</a></span></div>
               </c:if>
            </div>
        </div>
    </div>
</div>
