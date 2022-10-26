<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%--@elvariable id="instructionInfo" type="sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityInstructionDto"--%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <br/><br/>
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                        <p>You are submitting an application to renew the following facility:</p>
                        <br/>
                        <div class="table-responsive">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col">Active Facility Number</th>
                                    <th scope="col">Facility Name</th>
                                    <th scope="col">Facility Classification</th>
                                    <th scope="col">Facility Address</th>
                                    <th scope="col">Valid From</th>
                                            <th scope="col">Valid Until</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td><c:out value="${instructionInfo.facilityNo}"/></td>
                                            <td><c:out value="${instructionInfo.facilityName}"/></td>
                                            <td><iais:code code="${instructionInfo.classification}"/></td>
                                            <td><c:out value="${instructionInfo.facilityAddress}"/></td>
                                            <td><c:out value="${instructionInfo.validFrom}"/></td>
                                            <td><c:out value="${instructionInfo.validUntil}"/></td>
                                        </tr>
                                        </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="application-tab-footer" style="margin-top: 200px">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 ">
                                <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxFac"><em
                                        class="fa fa-angle-left"></em> Previous</a>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <div class="button-group">
                                    <a class="btn btn-primary next" id="next">Start Application</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
