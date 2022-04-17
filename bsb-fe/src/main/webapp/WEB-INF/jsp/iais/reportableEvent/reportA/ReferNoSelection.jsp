<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-incident.js"></script>
<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp"%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="col-xs-12">
                            <div class="row" style="margin:25px 0;">
                                <div class="form-group">
                                    <div class="col-sm-5 control-label">
                                        <label for="refNo">Reference No</label>
                                    </div>
                                    <div class="col-sm-6 col-md-7">
                                        <select name="refNo" class="refDropdown" id="refNo">
                                            <c:forEach var="item" items="${referNoOps}">
                                                <option value="${MaskUtil.maskValue('refNo',item.value)}" <c:if test="${followup1A.referenceNo eq item.value}">selected = "selected"</c:if>>${item.text}</option>
                                            </c:forEach>
                                        </select>
                                        <span data-err-ind="refNo" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 30px">
                                <div class="col-xs-12 col-sm-6">
                                    <a class="back" id="homeBack" href="/bsb-fe/eservice/INTERNET/MohBsbReportableEvents"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-6">
                                    <div class="button-group" style="float: right">
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
</form>

