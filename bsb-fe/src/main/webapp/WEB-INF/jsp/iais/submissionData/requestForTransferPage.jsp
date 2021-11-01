<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div class="row form-horizontal">
                                    <iais:row>
                                        <iais:field value="Schedule Type" width="11" required="true"/>
                                        <iais:value width="11">
                                            <iais:select name="scheduleType" id="scheduleType"
                                                         value=""
                                                         codeCategory="CATE_ID_BSB_SCH_TYPE"
                                                         firstOption="Please Select"/>
                                            <span class="error-msg" name="errorMsg" id="error_scheduleType"></span>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Biological Agent/Toxin Code" width="11" required="true"/>
                                        <iais:value width="11">
                                            <input type="text" name="BATCode" id="BATCode" maxlength="20" value="">
                                            <span class="error-msg" name="errorMsg" id="error_BATCode"></span>
                                        </iais:value>
                                    </iais:row>
                                    <c:if test="1=1">
                                        <%--Schedule Type 不是 Fifth Schedule，而且biological type为agent时，不显示--%>
                                        <iais:row>
                                            <iais:field value="Expected Quantity of Biological Agent" width="11"
                                                        required="true"/>
                                            <iais:value width="11">
                                                <input type="text" name="expectedBatQty" id="expectedBatQty"
                                                       maxlength="66" value="">
                                                <span class="error-msg" name="errorMsg"
                                                      id="error_expectedBatQty"></span>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="1=1">
                                        <%--Schedule Type 为 Fifth Schedule,biological_type为toxin，显示--%>
                                        <iais:row>
                                            <iais:field value="Expected Quantity to Receive" width="11"
                                                        required="true"/>
                                            <iais:value width="11">
                                                <input type="number" name="receivedQty" id="receivedQty" value=""
                                                       maxlength="11"
                                                       οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                                <span class="error-msg" name="errorMsg" id="error_receivedQty"></span>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <c:if test="1=1">
                                        <%--Schedule Type 为 Fifth Schedule,biological_type为toxin，显示--%>
                                        <iais:row>
                                            <iais:field value="Unit of Measurement" width="11" required="true"/>
                                            <iais:value width="11">
                                                <iais:select name="measurementUnit" id="measurementUnit"
                                                             value=""
                                                             codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                                             firstOption="Please Select"/>
                                                <span class="error-msg" name="errorMsg"
                                                      id="error_measurementUnit"></span>
                                            </iais:value>
                                        </iais:row>
                                    </c:if>
                                    <iais:row>
                                        <iais:field value="Transferring Facility" width="11" required="false"/>
                                        <iais:value width="11">
                                            <label><p>facility name</p></label>
                                        </iais:value>
                                    </iais:row>
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
                <a class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
            </div>
            <div class="col-xs-12 col-md-6 text-right">
                <button class="btn btn-primary save" id="savebtn" onclick="javascript:save()">NEXT</button>
            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>