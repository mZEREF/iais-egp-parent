<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-data-submission.js"></script>

<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="facId" id="facId" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div class="form-group form-horizontal formgap">
                                    <div class="col-sm-4 control-label formtext ">
                                        <label class="control-label control-set-font control-font-label">Facility</label>
                                        <span class="mandatory">*</span>
                                    </div>
                                    <div class="col-sm-4 col-md-7 control-font-label">
                                        <select name="facSelect" class="facDropdown" id="facSelect">
                                            <option value="">Please Select</option>
                                            <c:forEach items="${facSelection}" var="selectList">
                                                <option value="${selectList.value}" <c:if test="${facId eq MaskUtil.unMaskValue('id',selectList.value)}">selected="selected"</c:if>>${selectList.text}</option>
                                            </c:forEach>
                                        </select>
                                        <span id="facSelectError" class="error-msg"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="container">
            <div class="col-xs-12 col-md-6 text-left">
                <c:choose>
                    <c:when test="${back eq 'app'}">
                        <a class="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em>Back</a>
                    </c:when>
                    <c:otherwise>
                        <a class="back" href="#" id="back"><em class="fa fa-angle-left"></em> Back</a>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="form-group">
                <div class="col-xs-12 col-md-6 text-right">
                    <button type="button"  class="btn btn-primary" id="facNextBtn">NEXT</button>
                </div>
            </div>
        </div>
    </div>
</form>