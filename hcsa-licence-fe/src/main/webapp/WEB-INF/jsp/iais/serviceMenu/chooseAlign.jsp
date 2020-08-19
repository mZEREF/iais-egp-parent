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
    <input type="hidden" name="crud_action_type"/>
    <input type="hidden" name="crud_action_additional"/>
    <input type="hidden" name="crud_action_type_form"/>
    <input type="hidden" name="draftNo"/>
    <input type="hidden" name="crud_action_value">
    <div class="container">
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
            <div>
                <%@include file="comm/chooseAlignContent.jsp"%>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                </div>
                <div class="col-xs-12 col-md-2">
                    <div class="text-center-mobile">
                        <a class="back" id="alignBack"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="alignContinue">Continue</a>
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

        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }
        $('#alignBack').click(function () {
            <c:choose>
                <c:when test="${onlyBaseSvc}">
                    submit('chooseSvc',null,'back');
                </c:when>
                <c:otherwise>
                    submit('chooseBaseSvc',null,'back');
                </c:otherwise>
            </c:choose>
        });
        $('#alignContinue').click(function () {
            <c:choose>
                <c:when test="${onlyBaseSvc}">
                submit('chooseSvc',null,'next');
                </c:when>
                <c:otherwise>
                submit('chooseBaseSvc',null,'next');
                </c:otherwise>
            </c:choose>
        });

    });

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
