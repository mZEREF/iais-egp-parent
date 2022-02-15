<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.lang.String"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>

<%--@elvariable id="commentInsReportDTO" type="sg.gov.moh.iais.egp.bsb.dto.inspection.CommentInsReportDto"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <h3 style="margin-bottom: 50px">List of Rectifications</h3>
                        <div class="row" style="font-weight: 700;text-align: center">
                            <div class="col-md-1">S/N</div>
                            <div class="col-md-4">Item Description</div>
                            <div class="col-md-2">MOH Remarks</div>
                            <div class="col-md-1">Actions</div>
                            <div class="col-md-2">Rectification uploaded?</div>
                            <div class="col-md-2">Status</div>
                        </div>
                        <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                            <div class="col-md-1">1</div>
                            <div class="col-md-4">The requirement for document control and record retention for BC meeting minutes and other relevant records.</div>
                            <div class="col-md-2">-</div>
                            <div class="col-md-1"><button type="button" class="btn btn-default btn-sm">Rectify</button></div>
                            <div class="col-md-2"><h4 class="text-danger"><em class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                            <div class="col-md-2">Pending Inspection/ Certification</div>
                        </div>
                    </div>
                    <div class="row" style="border-top: 1px solid #D1D1D1;margin-top: 100px;padding: 20px 0">
                        <div class="col-xs-12 col-sm-6 ">
                            <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="button-group" style="float: right">
                                <a class="btn btn-primary" id="submitBtn" >Submit</a>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>