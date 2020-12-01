<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="iasi" uri="ecquaria/sop/egov-mc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<webui:setLayout name="iais-internet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<form id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2><iais:code code="UC_INSP_ACK005"/></h2>
                        </div>
                    </div>
                    <div align="left"><span><a href="/main-web/eservice/INTRANET/MohBackendInbox"><em class="fa fa-angle-left"></em> Back</a></span></div>
                </div>
            </div>
        </div>
    </div>
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>