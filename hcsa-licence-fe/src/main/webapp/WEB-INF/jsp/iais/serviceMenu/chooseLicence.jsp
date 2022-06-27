<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2020/6/15
  Time: 14:33
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@include file="comm/comFun.jsp"%>
    <%--<input type="hidden" name="crud_action_type"/>--%>
    <%--<input type="hidden" name="crud_action_additional"/>--%>
    <%--<input type="hidden" name="crud_action_type_form"/>--%>
    <%--<input type="hidden" name="crud_action_value">--%>
    <input type="hidden" name="draftNo"/>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="container">
        <br>
        <div class="row">
            <%@ include file="../common/dashboardDropDown.jsp" %>
        </div>
        <div class="row">
            <div class="col-xs-3">
            </div>
            <div class="col-xs-8">
                <div class="dashboard-page-title">
                    <h1>New Licence Application</h1>
                </div>
            </div>
        </div>
        <div class="component-gp">
            <br>
            <div class="disabledPart">
                <%@include file="comm/chooseSvcContent.jsp"%>
            </div>
            <br/>
            <c:if test="${appSelectSvc.chooseBaseSvc}">
                <div class="disabledPart">
                    <%@include file="comm/chooseBaseSvcContent.jsp"%>
                </div>
                <br/>
            </c:if>
            <%--<c:if test="${!appSelectSvc.chooseBaseSvc}">--%>
                <%--<div class="disabledPart">--%>
                    <%--<%@include file="comm/chooseAlignContent.jsp"%>--%>
                <%--</div>--%>
                <%--<br/>--%>
            <%--</c:if>--%>
            <div>
                <%@include file="comm/chooseLicContent.jsp"%>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                </div>
                <div class="col-xs-12 col-md-2">
                    <div class="text-center-mobile">
                        <a class="back" id="licBack"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="licContinue" href="javascript:void(0);">Continue</a>
                    </div>
                </div>
            </div>

            <br>
            <input type="text" style="display: none" id="draftsave" name="draftsave" value="${selectDraftNo}">
            <c:if test="${ not empty selectDraftNo }">
                <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
            </c:if>
            <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
        </div>
    </div>
</form>
<script type="text/javascript">
    $(document).ready(function () {
        //disabled
        $('.disabledPart').find('input[type="radio"]').prop('disabled',true);
        $('.disabledPart').find('input[type="checkbox"]').prop('disabled',true);
        //default check the first
        $('#licBodyDiv').find('input[name="licPagDivCheck"]:eq(0)').prop('checked',true);

        $('#licBack').click(function () {
            showWaiting();
            submit('chooseAlign',null,'back');
        });

        $('#licContinue').click(function () {
            showWaiting();
            submit(null,'next','next');
        });

        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }

    });

    function jumpToPagechangePage () {
        showWaiting();
        submit('chooseLic','doPage','doPage');
    }

    function saveDraft() {
        let val = $('#draftsave').val();
        $("[name='draftNo']").val(val);
        $("[name='crud_action_value']").val('continue');
        $('#mainForm').submit();
    }

    function cancelSaveDraft() {
        let val = $('#draftsave').val();
        $("[name='draftNo']").val(val);
        $("[name='crud_action_value']").val('resume');
        $('#mainForm').submit();
    }

</script>

