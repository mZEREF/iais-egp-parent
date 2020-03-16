<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 10/14/2019
  Time: 1:46 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
.form-check-gp{
    width: 50%;
    float:left;
}

.col-md-3 {
    width: 50%;
}
</style>


<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="itemId" value="<iais:mask name="itemId" value="${itemRequestAttr.itemId}"/><%--don't remove--%>">
    <br><br>
    <span id="error_question" name="iaisErrorMsg" class="error-msg"></span>
    <div class="main-content">
        <div class="container">

            <span id="error_messageContent" name="iaisErrorMsg" class="error-msg"></span>
            <br><br>
            <div class="tab-pane active" id="tabInbox" role="tabpanel">
                <div class="form-horizontal">

                    <div class="form-group">
                        <div class="col-xs-5 col-md-3">
                            <iais:field value="Regulation Clause Number" required="true"></iais:field>
                            <div class="col-xs-5 col-md-3">
                                <iais:select name="regulationClauseNo"  options="clauseSelect" firstOption="Select Clause Number"  value="${itemRequestAttr.regulationClauseNo}"></iais:select>
                                <span id="error_regulationClauseNo" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-5 col-md-3">
                            <iais:field value="Checklist Item" required="true"></iais:field>
                            <div class="col-xs-5 col-md-3">
                                <input type="text" name="checklistItem" value="${itemRequestAttr.checklistItem}" />
                                <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>

                    <%--the updating page no need to change status--%>
                    <div class="form-group">
                        <div class="col-xs-5 col-md-3">
                            <iais:field value="Status" required="true"></iais:field>

                            <div class="col-xs-5 col-md-3">
                                    <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS" firstOption="Select Status" value="${itemRequestAttr.status}" filterValue="CMSTAT002,CMSTAT003,CMSTAT004"></iais:select>
                                <span id="error_status" name="iaisErrorMsg" class="error-msg" ></span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-5 col-md-3">
                            <iais:field value="Risk Level" required="true"></iais:field>
                            <div class="col-xs-5 col-md-3">
                                <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL" firstOption="Select Risk Level" value="${itemRequestAttr.riskLevel}"></iais:select>
                                <span id="error_riskLevel" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>


                    <div class="form-group">
                        <div class="col-xs-5 col-md-3">
                            <iais:field value="Answer Type" required="true"></iais:field>
                            <div class="col-xs-5 col-md-3">
                                <iais:select name="answerType" id="answerType" codeCategory="CATE_ID_ANSWER_TYPE" firstOption="Select Answer Type" value="${itemRequestAttr.answerType}"></iais:select>
                                <span id="error_answerType" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 col-sm-6">
                <p><a class="back" onclick="doCancel();"><em class="fa fa-angle-left" ></em> Back</a></p>
            </div>
            <div class="text-right text-center-mobile">
                <a class="btn btn-primary next" href="javascript:void(0);" onclick="Utils.clearClickStatus();">Clear</a>
                <c:choose>
                    <c:when test="${btnTag eq 'SubmitButton'}">
                        <a class="btn btn-primary next" onclick="javascript:doSubmit();">Submit</a>
                    </c:when>
                    <c:when test="${btnTag eq 'UpdateButton'}">
                        <a class="btn btn-primary next" onclick="javascript:doSubmit('${itemRequestAttr.itemId}');">Update</a>
                    </c:when>
                </c:choose>
            </div>
        </div>


    </div>


</>
<%@include file="/include/utils.jsp"%>
<%@include file="/include/validation.jsp"%>
<script>
    function doSubmit(itemId){
        SOP.Crud.cfxSubmit("mainForm", "saveChecklistItem", itemId);
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm", "doCancel");
    }

</script>