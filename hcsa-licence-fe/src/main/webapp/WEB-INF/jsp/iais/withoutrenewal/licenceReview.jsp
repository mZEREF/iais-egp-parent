<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>
<style>
    .app-font-size-16{
        font-size: 16px;
    }
</style>
<form class="" method="post" id="LicenceReviewForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="switch_value" value=""/>
    <input type="hidden" id="checkSingle" value="${isSingle}"/>
    <input id="EditValue" type="hidden" name="EditValue" value="" />
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <div class="licence-renewal-content">
                            <%--content--%>
                            <div class="tab-pane" id="serviceInformationTab" role="tabpanel">
                                <div class="multiservice">
                                    <ul class="progress-tracker col-xs-12" ${isSingle == 'Y' ? 'style="margin-left:-8%;"' : ''}>
                                        <li class="tracker-item active">Instructions</li>
                                        <li class="tracker-item active">Licence Review</li>
                                        <li class="tracker-item disabled">Payment</li>
                                        <li class="tracker-item disabled">Acknowledgement</li>
                                    </ul>
                                </div>
                                <div class="multiservice">
                                    <div class="tab-gp side-tab clearfix">
                                        <ul class="nav nav-pills nav-stacked hidden-xs hidden-sm"  ${isSingle == 'Y' ? 'hidden' : ''} role="tablist">
                                            <c:forEach var="serviceName" items="${serviceNames}" varStatus="status">
                                                <li class="complete ${status.index == '0' ? 'active' : ''} tableMain" id="dtoList${status.index}" role="presentation"><a href="#serviceName${status.index}" aria-controls="lorem1" role="tab" data-toggle="tab">${serviceName}</a></li>
                                            </c:forEach>
                                        </ul>
                                        <div class="mobile-side-nav-tab visible-xs visible-sm" ${isSingle == 'Y' ? 'hidden' : ''}>
                                            <select id="serviceSelect">
                                                <c:forEach var="serviceName" items="${serviceNames}" varStatus="status">
                                                    <option value="serviceName${status.index}">${serviceName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <%--main content--%>

                                        <div class="tab-gp steps-tab">
                                            <div class="tab-content" style="padding-top: 0px;">
                                                <c:if test="${isSingle == 'Y'}">
                                                    <p>Please review your licence details and click edit to make necessary changes before renewal.</p>
                                                </c:if>
                                                <input hidden id="DtoSize" value="${renewDto.appSubmissionDtos.size() - 1}"/>
                                            <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}" varStatus="status">
                                                <c:set var="AppSubmissionDto" value="${AppSubmissionDto}" scope="request"/>
                                                <c:set var="documentIndex" value="${status.index}" scope="request"/>
                                                <div class="tab-pane ${status.index == '0' ? 'active' : ''}" id="serviceName${status.index}" role="tabpanel">
                                                    <div class="preview-gp">
                                                        <div class="row">
                                                            <div class="col-xs-12">
                                                                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                                    <c:if test="${isSingle == 'N'}">
                                                                        <h2 style='border-bottom:none;'>${AppSubmissionDto.serviceName}; Licence No. ${AppSubmissionDto.licenceNo}</h2>
                                                                    </c:if>
                                                                    <%@include file="../common/previewPremises.jsp"%>
                                                                    <%@include file="../common/previewPrimary.jsp"%>
                                                                    <div class="panel panel-default svc-content">
                                                                        <div class="panel-heading"  id="headingServiceInfo${status.index}" role="tab">
                                                                            <h4 class="panel-title"><a class="svc-pannel-collapse collapsed"  role="button" data-toggle="collapse" href="#collapseServiceInfo${documentIndex}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information </a></h4>
                                                                        </div>
                                                                        <div class=" panel-collapse collapse" id="collapseServiceInfo${documentIndex}" role="tabpanel" aria-labelledby="headingServiceInfo">
                                                                            <div class="panel-body">
                                                                                <c:if test="${AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.serviceEdit}">
                                                                                    <p class="text-right"><div class="text-right app-font-size-16"><a href="#" id="doSvcEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
                                                                                </c:if>
                                                                                <div class="panel-main-content">
                                                                                    <c:set var="appGrpPremisesDtoList" value="${AppSubmissionDto.appGrpPremisesDtoList}"></c:set>
                                                                                    <%--<c:set var="currentPreviewSvcInfo" value="${currentPreviewSvcInfoList.get(documentIndex)}"></c:set>--%>
                                                                                    <c:set var="reloadDisciplineAllocationMap" value="${reloadDisciplineAllocationMapList.get(documentIndex)}"></c:set>
                                                                                    <c:set var="ReloadPrincipalOfficers" value="${ReloadPrincipalOfficersList.get(documentIndex)}"></c:set>
                                                                                    <c:set var="ReloadDeputyPrincipalOfficers" value="${deputyPrincipalOfficersDtosList.get(documentIndex)}"></c:set>
                                                                                    <c:set var="currentPreviewSvcInfo" value="${AppSubmissionDto.appSvcRelatedInfoDtoList.get(0)}"></c:set>
                                                                                    <%@include file="../common/previewSvcInfo.jsp"%>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <c:if test="${changeRenew eq 'Y'}">
                                                                    <div class="row">
                                                                        <div class="col-xs-5">
                                                                            Please indicate an effective date of change for your licence information to be updated.The date of change will be effected on the indicated date or approval date, whichever is the later date
                                                                        </div>
                                                                        <div class="col-xs-7">
                                                                            <iais:datePicker cssClass="renewEffectiveDate" name="renewEffectiveDate" value="${AppSubmissionDto.effectiveDateStr}" />
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="col-xs-5">
                                                                        </div>
                                                                        <div class="col-xs-7">
                                                                            <span class="error-msg" name="iaisErrorMsg" id="error_rfcEffectiveDate"></span>
                                                                        </div>
                                                                    </div>
                                                                    </c:if>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                            <c:if test="${isSingle == 'Y'}">
                                                <div class="form-check">
                                                    <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" value="1" aria-invalid="false" <c:if test="${userAgreement}">checked="checked"</c:if> >
                                                    <label class="form-check-label" for="verifyInfoCheckbox"><span class="check-square"></span>I hereby certify that the information I provided is all correct and accurate</label>
                                                </div>
                                                <div>
                                                    <span id="error_fieldMandatory"  class="error-msg"></span>
                                                </div>
                                            </c:if>
                                        </div>
                                        </div>
                                        <%--main content--%>
                                    </div>
                                </div>
                            </div>
                            <%--content--%>
                        </div>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-8">
                                    <a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <c:if test="${isSingle == 'Y'}">
                                    <div class="col-xs-12 col-sm-1">
                                        <p class="print text-right"><a href="#" id="print-review"> <em class="fa fa-print"></em>Print</a></p>
                                    </div>
                                </c:if>
                                <div class="col-xs-12 col-sm-3">
                                    <div class="text-right text-center-mobile" id="submitButton"><a id="SUBMIT" class="btn btn-primary">Submit and Pay</a></div>
                                    <div class="text-right text-center-mobile hidden" id="nextButton"><a id="Next" class="btn btn-primary">Preview the Next Service</a></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<script>

    $(document).ready(function () {
        if('Y' != '${isSingle}'){
            checkSubmitButton();
        }
    });

    $('.tableMain').click(function () {
        var DtoSize = $('#DtoSize').val();
        var lastId = 'dtoList' + DtoSize;
        var thisId = $(this).attr('id');
        //if the last service is selected
        if(thisId == lastId){
            $('#submitButton').removeClass("hidden");
            $('#nextButton').addClass("hidden");
        }else{
            $('#submitButton').addClass("hidden");
            $('#nextButton').removeClass("hidden");
        }
    });

    function checkSubmitButton(){
        var DtoSize = $('#DtoSize').val();
        var lastId = 'dtoList' + DtoSize;
        var thisId = $(this).attr('id');
        //if the last service is selected
        if(thisId == lastId){
            $('#submitButton').removeClass("hidden");
            $('#nextButton').addClass("hidden");
        }else{
            $('#submitButton').addClass("hidden");
            $('#nextButton').removeClass("hidden");
        }
    }

    $('#nextButton').click(function (){
        var currentId =  $('.tableMain.active').attr('id');
        var nextId = '#'+currentId;
        // $(nextId).removeClass('active');
        // $(nextId).next().addClass('active');
        $(nextId).next().trigger('click');
        $(nextId).next().find('a').trigger('click');
    });

    $('#BACK').click(function () {
        $('[name="switch_value"]').val('instructions');
        $('#LicenceReviewForm').submit();
    });
    $('#SUBMIT').click(function () {
        let jQuery = $('#verifyInfoCheckbox').prop("checked");
        let isSingle = $('#checkSingle').val();
        if(!jQuery && (isSingle == 'Y')){
            $('#error_fieldMandatory').html("The field is mandatory");
            return;
        }else if(jQuery || (isSingle == 'N')) {
            $('#error_fieldMandatory').html("");
            $('[name="switch_value"]').val('doLicenceReview');
            $('#LicenceReviewForm').submit();
        }
    });

    $('#premisesEdit').click(function () {
        $('#EditValue').val('premises');
        $('[name="switch_value"]').val('doEdit');
        $('#LicenceReviewForm').submit();
    });
    $('#docEdit').click(function () {
        $('#EditValue').val('doc');
        $('[name="switch_value"]').val('doEdit');
        $('#LicenceReviewForm').submit();
    });
    $('#doSvcEdit').click(function () {
        $('#EditValue').val('service');
        $('[name="switch_value"]').val('doEdit');
        $('#LicenceReviewForm').submit();
    });

    $("#print-review").click(function () {
        window.print();
    })


</script>