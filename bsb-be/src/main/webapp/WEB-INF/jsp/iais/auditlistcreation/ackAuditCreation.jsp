<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<div>
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="bg-title">
                                <%--                        <h2>${submit_message_success}</h2>--%>
                                <h2>You have successfully created the audit tasks.</h2>
                            </div>
                        </div>
                        <div align="left">
                            <span>
                                <a href="/bsb-web/eservice/INTRANET/AuditListCreationList"><em class="fa fa-angle-left"></em> Back</a>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>