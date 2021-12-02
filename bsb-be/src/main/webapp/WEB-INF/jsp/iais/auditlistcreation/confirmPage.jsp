<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<div class="main-content">
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Audit Information</h2>
                        </div>
                        <div id="sectionGroup">
                            <c:forEach var="item" items="${saveAuditDto.saveAudits}" varStatus="status">
                                <section id="conSection--v--${status.index}">
                                    <c:if test="${saveAuditDto.saveAudits.size() > 1}">
                                        <div class="form-group">
                                            <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Facility ${status.index + 1}</h3>
                                        </div>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Facility Name" width="15" required="false"/>
                                        <iais:value width="10">
                                            <p>${item.facName}<p>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Facility Classification" width="15" required="false"/>
                                        <iais:value width="10">
                                            <p><iais:code code="${item.facClassification}"/><p>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Activity Type" width="15" required="false"/>
                                        <iais:value width="10">
                                            <p><iais:code code="${item.activityType}"/><p>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Audit Type" width="15" required="true"/>
                                        <iais:value width="10">
                                            <p><iais:code code="${item.auditType}"/></p>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Remarks" width="15" required="false"/>
                                        <iais:value width="10">
                                            <p><c:out value="${item.remarks}"/></p>
                                        </iais:value>
                                    </iais:row>
                                </section>
                            </c:forEach>
                        </div>

                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em>Back</a>
                            </div>
                            <div align="right">
                                <button name="submitBtn" id="submitAudit" type="button" class="btn btn-primary">Submit</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>