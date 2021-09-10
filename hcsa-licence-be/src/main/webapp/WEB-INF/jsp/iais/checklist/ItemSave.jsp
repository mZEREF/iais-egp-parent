<%--
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

<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="itemId" value="<iais:mask name="itemId" value="${itemRequestAttr.itemId}"/><%--don't remove--%>">
    <br><br>
    <span id="error_question" name="iaisErrorMsg" class="error-msg"></span>

    <span id="error_messageContent" name="iaisErrorMsg" class="error-msg"></span>
    <br><br>
    <div class="tab-pane active" id="tabInbox" role="tabpanel">
        <div class="form-horizontal">

            <iais:row>
                <iais:field value="Regulation Clause Number" required="true"></iais:field>
                <div class="col-xs-5 col-md-3">
                    <iais:select name="regulationClauseNo" onchange="displayRegulation()" options="clauseSelect"
                                 firstOption="Please Select"  value="${itemRequestAttr.regulationId}" needErrorSpan="false" />
                    <span id="error_regulationId" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </iais:row>

            <iais:row>
                <iais:field value="Regulation" required="false"></iais:field>
                <div class="col-xs-5 col-md-3">
                    <textarea cols="70" rows="7" name="regulationClause" id="regulationClause"  readonly maxlength="8000"></textarea>
                    <span id="error_regulationClause" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </iais:row>

            <iais:row>
                <iais:field value="Checklist Item" required="true"></iais:field>
                <div class="col-xs-5 col-md-3">
                    <textarea cols="70" rows="7" name="checklistItem" id="checklistItem" maxlength="500"><c:out value="${itemRequestAttr.checklistItem}"> </c:out></textarea>
                    <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </iais:row>

            <%--the updating page no need to change status--%>
            <iais:row>
                <iais:field value="Status" required="true"></iais:field>

                <div class="col-xs-5 col-md-3">
                    <iais:select name="status" id="status" codeCategory="CATE_ID_COMMON_STATUS" firstOption="Please Select"
                                 value="${itemRequestAttr.status}" filterValue="CMSTAT002,CMSTAT004,DRAFT001"/>
                </div>
            </iais:row>

            <iais:row>
                <iais:field value="Risk Level" required="true"></iais:field>
                <div class="col-xs-5 col-md-3">
                    <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_RISK_LEVEL" firstOption="Please Select"
                                 value="${itemRequestAttr.riskLevel}" />
                </div>
            </iais:row>

            <iais:row>
                <iais:field value="Answer Type" required="true" />
                <div class="col-xs-5 col-md-3">
                    <iais:select name="answerType" id="answerType" codeCategory="CATE_ID_ANSWER_TYPE" firstOption="Please Select"
                                 filterValue="ANTP003, ANTP001" value="${itemRequestAttr.answerType}" />
                </div>
            </iais:row>
        </div>
    </div>
    <div class="col-xs-12 col-md-6">
        <a class="back" href="javascript:void(0)" onclick="doCancel();"><em class="fa fa-angle-left" ></em> Back</a>
    </div>
    <div class="text-right text-center-mobile">
        <a class="btn btn-secondary" href="javascript:void(0)" onclick="Utils.clearClickStatus('form-horizontal');">Clear</a>
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
<%@include file="/WEB-INF/jsp/include/utils.jsp"%>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script>

    if (window.attachEvent) {
        window.attachEvent("onload", init);
    } else if (window.addEventListener) {
        window.addEventListener("load", init, false);
    }

    function init() {
        var regulationId = $("select[name='regulationClauseNo']").val();
        if (regulationId == undefined || regulationId == ''){
            $('#regulationClause').val('-');
        }else {
            displayRegulation()
        }
    }

    function displayRegulation() {
        var regulationId = $("select[name='regulationClauseNo']").val();
        $.ajax({
            type: "GET",
            url: '${pageContext.request.contextPath}/checklist-item-clause',
            data: {regulationId : regulationId},
            success: function (data) {
                $('#regulationClause').val(data);
            },
            error: function (msg) {
                alert("error");
            }
        });
    }

    function doSubmit(itemId){
        SOP.Crud.cfxSubmit("mainForm", "saveChecklistItem", itemId);
    }

    function doCancel(){
        SOP.Crud.cfxSubmit("mainForm", "doCancel");
    }

</script>