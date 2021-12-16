<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-withdrawn.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-withdrawn-file.js"></script>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <%--<div class="main-content">--%>
    <div class="container">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <%--<div class="center-content">--%>
                <div class="internet-content">
                    <iais:body>
                        <div class="col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading"><strong>Withdrawal Submission</strong></div>
                                <div class="row form-horizontal">
                                        <%--@elvariable id="withdrawnDto" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto"--%>
                                    <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Current Status</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:code code="${withdrawnDto.currentStatus}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Application No</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <c:out value="${withdrawnDto.appNo}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Application Type</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:code code="${withdrawnDto.appType}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Process Type</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:code code="${withdrawnDto.processType}"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Reason for Withdrawn</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <iais:code code="${withdrawnDto.reason}"/>
                                            </div>
                                        </div>
                                            <%--If the Reason for Withdrawal is indicated as "Others", please provide details under Supporting Remarks.--%>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>Supporting Remarks</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <c:out value="${withdrawnDto.remarks}"/>
                                            </div>
                                        </div>
                                            <%--upload file--%>
                                        <div class="form-group">
                                            <div class="col-sm-5 control-label">
                                                <label>File Upload for Withdrawal</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <c:if test="${withdrawnDto.newDocInfos ne null}">
                                                    <c:forEach var="docInfo" items="${withdrawnDto.newDocInfos}">
                                                        <p><a href="javascript:void(0)" onclick="downloadFile('${MaskUtil.maskValue('file', docInfo.tmpId)}')">${docInfo.filename}</a>${String.format("%.1f", docInfo.size/1024.0)}KB</p>
                                                    </c:forEach>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </iais:body>
                </div>
                <%--                </div>--%>
            </div>
        </div>
    </div>
    <%--    </div>--%>
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <a class="back" href="#" id="back"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="form-group">
                <div class="col-xs-12 col-md-6 text-right">
                    <button class="btn btn-secondary save" id="cancelBtn">Cancel</button>
                    <button class="btn btn-primary save" id="submitBtn">Submit</button>
                </div>
            </div>
        </div>
    </div>
</form>