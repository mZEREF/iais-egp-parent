<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-display-or-not.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-renewal-facility-register.js"></script>

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
                    <div class="tab-gp steps-tab">
                        <%@ include file="renewalInnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="col-xs-12 col-sm-11 col-md-10 col-lg-8">
                                <p>Your approval no. to renewal are listed below. Please proceed to review your facility details.</p>
                                <br/>
                                <div class="table-responsive">
                                    <table aria-describedby="" class="table">
                                        <thead>
                                        <tr>
                                            <th scope="col">Approval No.</th>
                                            <th scope="col">Type</th>
                                            <th scope="col">Facility Address</th>
                                            <th scope="col">Start Date</th>
                                            <th scope="col">Expires On</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td><c:out value="${instructionInfo.approvalNo}"/></td>
                                            <td><iais:code code="${instructionInfo.type}"/></td>
                                            <td><c:out value="${instructionInfo.facilityAddress}"/></td>
                                            <td><c:out value="${instructionInfo.startDate}"/></td>
                                            <td><c:out value="${instructionInfo.expiresOn}"/></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="application-tab-footer" style="margin-top: 200px">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 ">
                                        <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="button-group">
                                            <a class="btn btn-primary next" id="next" >Next</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
