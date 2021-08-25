<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
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
<%--                        <h2>${submit_message_success}</h2>--%>
                        <h2>Audit list has been successfully created and an e-mail notification has been sent to the assigned inspector.</h2>
                    </div>
                </div>
                <div align="left"><span><a href="/system-admin-web/eservice/INTRANET/MohAuditFunctional"><em class="fa fa-angle-left"></em> Back</a></span></div>
            </div>
        </div>
    </div>
</div>
