<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<%@include file="dashboard.jsp" %>
<form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <%--                <div class="center-content">--%>
                <div class="internet-content" id="clearSelect">
                    <div class="bg-title">
                        <h2>Audit Information</h2>
                    </div>
                    <iais:row>
                        <iais:field value="Audit Date indicated previously" width="15" required="false"/>
                        <iais:value width="10">
                            <p><fmt:formatDate value='${auditDto.lastAuditDate}' pattern='dd/MM/yyyy'/></p>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="Audit Date" width="15" required="true"/>
                        <iais:value width="10">
                            <p><fmt:formatDate value='${auditDto.auditDate}' pattern='dd/MM/yyyy'/></p>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="Reasons for change" width="15" required="true"/>
                        <iais:value width="10">
                            <p><c:out value="${auditDto.remarks}"/></p>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field value="Remarks" width="15" required="false"/>
                        <iais:value width="10">
                            <p><c:out value="${auditDto.remarks}"/></p>
                        </iais:value>
                    </iais:row>

                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div align="right">
                            <button name="submitBtn" id="submitBtn" type="button" class="btn btn-primary">Submit</button>
                        </div>
                    </div>
                </div>
                <%--                </div>--%>
            </div>
        </div>
    </div>
</form>