<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2020/6/9
  Time: 13:26
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
    <input type="hidden" name="crud_action_type"/>
    <input type="hidden" name="crud_action_additional"/>
    <input type="hidden" name="crud_action_type_form"/>
    <input type="hidden" name="crud_action_value">
    <input type="hidden" name="draftNo"/>
    <input type="hidden" name="MSNoteShow" value="1"/>
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
            <div id="svcStep1">
                <%@include file="comm/chooseSvcContent.jsp"%>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                </div>
                <div class="col-xs-12 col-md-2">
                    <div class="text-center-mobile">
                        <a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="submitService" href="javascript:void(0);">Continue</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <input type="text" style="display: none" id="draftsave" name="draftsave" value="<c:out value="${selectDraftNo}"/>">
    <c:if test="${ not empty selectDraftNo }">
        <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
    </c:if>
    <iais:confirm msg="NEW_ACK047" popupOrder="saveApplicationAddress" needCancel="false" yesBtnDesc="OK" yesBtnCls="btn btn-primary" callBack="baseContinue()"></iais:confirm>
</form>
<script type="text/javascript">

    $(document).ready(function () {
        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }

        $('#Back').click(function(){
            showWaiting();
            submit('toInbox',null,'back');
        });
        $('#submitService').click(function(){
            var flag = $('input[name="MSNoteShow"]').val();
            if (flag==1){
                showWaiting();
                submit('chooseSvc',null,'next');
            }else {
                $('#saveApplicationAddress').modal('show');
            }
        });

        svcNoteFunction();
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

    function sameAddressContinue() {
        $('#existSameAddress').modal('hide');
    }

    function svcNoteFunction() {
        $('input[type="checkbox"]').on('click', function (){
            var svcNameList=new Array();
            $('input[type="checkbox"]:checked').each(function (i, x) {
                svcNameList.push($(x).next().text());
            })
            var data = {
                'svcNameList': svcNameList
            };
            var opt = {
                url: '${pageContext.request.contextPath}' + "/checkIsExistPendMs",
                type: 'GET',
                data: data
            };
            callCommonAjax(opt, "checkSvcNoteSelCallBack");
        });
    }

    function checkSvcNoteSelCallBack(data) {
        console.log(data);
        if (data==true){
            $('input[name="MSNoteShow"]').val(0);
        }else{
            $('input[name="MSNoteShow"]').val(1);
        }
    }

    function baseContinue() {
        $('#saveApplicationAddress').modal('hide');
        $('input[name="MSNoteShow"]').val('1');
    }
</script>