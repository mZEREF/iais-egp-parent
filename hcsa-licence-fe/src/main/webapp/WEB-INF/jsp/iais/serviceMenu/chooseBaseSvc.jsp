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
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
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
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="container">
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
            <div>
                <input type="hidden" name="MSNoteShow" value="${!existPendMS?1:0}"/>
                <c:if test="${!notShow}">
                    <%@include file="comm/chooseBaseSvcContent.jsp"%>
                </c:if>
                <br>
                <c:if test="${bundleAchOrMs}">
                    <div class="row svcNote">
                        <div class="col-xs-12 col-md-3">
                        </div>
                        <div class="col-xs-12 col-md-6">
                            <div class="self-assessment-checkbox-gp gradient-light-grey">
                                <div class="row">
                                    <div class="col-xs-12 col-md-12">
                                        <strong>The minimum suite of services for Acute Hospital are</strong>
                                    </div>
                                    <div class="col-xs-12 col-md-12">
                                        <ul style="padding-left: 20px">
                                            <li>${AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY}</li>
                                            <li>${AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES}</li>
                                        </ul>
                                    </div>
                                    <div class="col-xs-12 col-md-12">
                                        <p><strong><iais:message key="NEW_ACK046"/></strong></p>
                                    </div>
                                    <div class="col-xs-12 col-md-12">
                                        <div class="form-check clbNote <c:if test="${notContainedCLB!=1}">hidden</c:if>">
                                            <input class="form-check-input" disabled checked="checked" type="checkbox" aria-invalid="false">
                                            <label class="form-check-label"><span class="check-square"></span>${AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY}</label>
                                        </div>
                                        <div class="form-check rdsNote <c:if test="${notContainedRDS!=1}">hidden</c:if>">
                                            <input class="form-check-input" disabled checked="checked" type="checkbox" aria-invalid="false">
                                            <label class="form-check-label"><span class="check-square"></span>${AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES}</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-3">
                </div>
                <div class="col-xs-12 col-md-2">
                    <div class="text-center-mobile">
                        <a class="back" id="baseBack"><em class="fa fa-angle-left"></em> Back</a>
                    </div>
                </div>
                <div class="col-xs-12 col-md-4">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-primary next" id="baseContinue" href="javascript:void(0);">Continue</a>
                    </div>
                </div>
            </div>
            <br>
            <input type="text" style="display: none" id="draftsave" name="draftsave" value="<c:out value="${selectDraftNo}"/>">
            <c:if test="${ not empty selectDraftNo }">
                <iais:confirm msg="${new_ack001}" callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
            </c:if>
            <iais:confirm msg="NEW_ACK047" popupOrder="saveApplicationAddress" needCancel="false" yesBtnDesc="OK" yesBtnCls="btn btn-primary" callBack="baseContinue()"></iais:confirm>
            <iais:confirm msg="NEW_ACK048" popupOrder="existSameAddress" needCancel="false" yesBtnDesc="OK" yesBtnCls="btn btn-primary" callBack="sameAddressContinue()" needFungDuoJi="false" needEscapHtml="false"/>
            <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
        </div>
    </div>
</form>
<script type="text/javascript">
    var init=true;
    $(document).ready(function () {
        //disabled
        $('.disabledPart').find('input[type="radio"]').prop('disabled',true);
        $('.disabledPart').find('input[type="checkbox"]').prop('disabled',true);
        $('.existing-base').find('input[type="radio"]').prop('disabled',true);

        $('#baseBack').click(function () {
            showWaiting();
            submit('chooseSvc',null,'back');
        });
        $('#baseContinue').click(function () {
            var flag = $('input[name="MSNoteShow"]').val();
            if (flag==1){
                showWaiting();
                submit('chooseAlign',null,'next');
            }else {
                $('#saveApplicationAddress').modal('show');
            }
        });

        if( $('#draftsave').val()!=null|| $('#draftsave').val()!=''){
            $('#saveDraft').modal('show');
        }
    });

    function initSelect(){
        $('input[type="radio"]:checked').trigger('click');
    }

    function jumpToPagechangePage () {
        showWaiting();
        submit('chooseBaseSvc','doPage','doPage');
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

    function baseContinue() {
        $('#saveApplicationAddress').modal('hide');
        $('input[name="MSNoteShow"]').val('1');
    }

    function sameAddressContinue() {
        $('#existSameAddress').modal('hide');
    }

    function svcNoteFunction() {
        $('input[type="radio"]').on('click', function (){
            var attr = $(this).attr("id");
            var index= /[0-9]*$/g.exec(attr);
            if (index!=0){
                var data = {
                    'number': $(this).closest('td').next().find('label.form-check-label').text(),
                    'serviceName':$(this).closest('td').next().next().find('label.form-check-label').text()
                };
                var opt = {
                    url: '${pageContext.request.contextPath}' + "/sameAddressService",
                    type: 'GET',
                    data: data
                };
                callCommonAjax(opt, "checkSvcNoteSelCallBack");
            }else {
                checkSvcNoteSelCallBack(null);
                toggleTag($('div.clbNote'),${notContainedCLB==1});
                toggleTag($('div.rdsNote'),${notContainedRDS==1});
            }
        });
    }

    function checkSvcNoteSelCallBack(data) {
        var maxCount=${notContainedSvcSize};
        var count=0;
        toggleTag($('div.clbNote'),${notContainedCLB==1});
        toggleTag($('div.rdsNote'),${notContainedRDS==1});
        for(var key in data){
            if (key.startsWith("service")){
                count+=1;
                if (data[key]=="${AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY}"){
                    hideTag($('div.clbNote'))
                }
                if (data[key]=="${AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES}"){
                    hideTag($('div.rdsNote'))
                }
            }
            if (key.startsWith("exist")){
                if (data[key]&&!init){
                    $('#existSameAddress').modal('show');
                }
            }
        }
        init=false;
        toggleTag($('div.svcNote'),count<maxCount)
    }

    function doAfterInitMemoryPage(){
        svcNoteFunction();
        initSelect();
    }

    function toggleTag(ele, show) {
        var $ele = getJqueryNode(ele);
        if (isEmptyNode($ele)) {
            return;
        }
        if (show) {
            $ele.show();
            $ele.removeClass('hidden');
        } else {
            $ele.hide();
            $ele.addClass('hidden');
        }
    }

    function hideTag(ele) {
        var $ele = getJqueryNode(ele);
        if (isEmptyNode($ele)) {
            return;
        }
        $ele.hide();
        $ele.addClass('hidden');
    }
</script>