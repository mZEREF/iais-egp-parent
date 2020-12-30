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
                            <iais:message key="LOLEV_ACK035" escape="true"></iais:message>
                        </h2>
                    </div>
                </div>
                <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohBackendInbox?fromOther=1"><em class="fa fa-angle-left"></em> Back</a>
            </div>
        </div>
    </div>
</div>

