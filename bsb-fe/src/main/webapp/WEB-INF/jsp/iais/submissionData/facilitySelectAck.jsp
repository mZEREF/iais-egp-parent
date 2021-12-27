<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-sub-ack.js"></script>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="facId" id="facId" value="">
    <div class="main-content">
        <div class="container">
            <div class="row form-horizontal">
                <div class="col-xs-8" style="margin: 50px 0 0 150px">
                    <div class="form-group">
                        <div class="col-sm-3 control-label">
                            <label for="facSelect">Facility</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <select name="facSelect" id="facSelect" onchange="facChange(this)">
                                <c:forEach items="${facSelection}" var="selectList">
                                    <option value="${MaskUtil.maskValue('id',selectList.value)}">${selectList.text}</option>
                                </c:forEach>
                            </select>
                            <span id="facSelectError" class="error-msg"></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-3 control-label">
                            <label for="dataSubNo">Data Submission No</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <select name="dataSubNo" id="dataSubNo">
                                <option value="Please Select">Please Select</option>
                            </select>
                            <span id="dataSubNoError" class="error-msg"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <a class="back" href="#" id="back"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="form-group">
                <div class="col-xs-12 col-md-6 text-right">
                    <button class="btn btn-primary save" id="next">NEXT</button>
                </div>
            </div>
        </div>
    </div>
</form>