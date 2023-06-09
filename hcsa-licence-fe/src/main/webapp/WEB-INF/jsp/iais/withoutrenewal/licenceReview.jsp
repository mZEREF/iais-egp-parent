<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<br/>
<%@include file="../common/dashboard.jsp" %>

<c:set var="isRenew" value="true" scope="request"/>

<form class="table-responsive" method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="switch_value" value=""/>
    <input type="hidden" id="checkSingle" value="${isSingle}"/>
    <input id="EditValue" type="hidden" name="EditValue" value=""/>
    <input type="hidden" name="crud_action_additional" value="" id="crud_action_additional">
    <iais:input type="hidden" name="viewPrint" value="${viewPrint}"/>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <div class="licence-renewal-content">
                            <%--content--%>
                            <div class="tab-pane" id="serviceInformationTab" role="tabpanel">
                                <ul class="progress-tracker">
                                    <li class="tracker-item active">Instructions</li>
                                    <li class="tracker-item active">Licence Review</li>
                                    <li class="tracker-item disabled">Payment</li>
                                    <li class="tracker-item disabled">Acknowledgement</li>
                                </ul>
                                <br/>
                                <br/>
                                <br/>
                                <div class="multiservice">
                                    <!-- for desktop -->
                                    <div class="tab-gp side-tab clearfix">
                                     <c:if test="${isSingle != '1'}">
                                        <ul class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
                                            <c:forEach var="serviceName" items="${serviceNames}" varStatus="status">
                                                <li class="complete ${status.index == '0' ? 'active' : ''} tableMain" id="dtoList${status.index}" onclick="javascirpt:changeTabForMoreRenew('${status.index}')" role="presentation">
                                                    <a href="#serviceName${status.index}" aria-controls="lorem1" role="tab" data-toggle="tab">${serviceName}</a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                        <!-- for Mobile -->
                                        <div class="mobile-side-nav-tab visible-xs visible-sm">
                                            <select  aria-label="serviceSelectMobile" id="moblieSelectRenew" onchange="javascirpt:changeTabForMoreRenewMobile(this.value)">
                                                <c:forEach var="serviceName" items="${serviceNames}" varStatus="status">
                                                    <option value="${status.index}" ${status.index == '0' ? 'selected' : ''}>${serviceName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        </c:if>
                                        <div class="tab-gp steps-tab">
                                            <div class="tab-content" style="padding-top: 0px;">
                                                <c:if test="${isSingle == '1'}">
                                                    <p>
                                                        Please review your licence information and click edit to make necessary changes
                                                        before renewal.
                                                    </p>
                                                </c:if>
                                                <input hidden id="DtoSize" value="${renewDto.appSubmissionDtos.size() - 1}"/>
                                                <c:forEach var="AppSubmissionDto" items="${renewDto.appSubmissionDtos}" varStatus="status">
                                                    <c:set var="AppSubmissionDto" value="${AppSubmissionDto}" scope="request"/>
                                                    <c:set var="coMap" value="${AppSubmissionDto.coMap}" scope="request"/>
                                                    <c:set var="documentIndex" value="${status.index}" scope="request"/>
                                                    <%--<c:set var="key" value="${AppSubmissionDto.serviceName}${status.index}"/>
                                                    <c:set var="svcSecMap" value="${svcSecMaps[key]}" scope="request"/>--%>
                                                    <div class="tab-pane ${status.index == '0' ? 'active' : ''}"
                                                         id="serviceName${status.index}" role="tabpanel">
                                                        <div class="preview-gp">
                                                            <div class="row">
                                                                <div class="col-xs-12">
                                                                    <div class="panel-group" id="accordion" role="tablist"
                                                                         aria-multiselectable="true">
                                                                        <c:if test="${isSingle == '0'}">
                                                                            <h2 style='border-bottom:none;'>
                                                                                ${AppSubmissionDto.serviceName};
                                                                                Licence No. ${AppSubmissionDto.licenceNo}
                                                                            </h2>
                                                                        </c:if>
                                                                        <jsp:include page="/WEB-INF/jsp/iais/view/viewLicensee.jsp"/>
                                                                        <jsp:include page="/WEB-INF/jsp/iais/view/viewPremises.jsp"/>
                                                                        <jsp:include page="/WEB-INF/jsp/iais/view/viewSpecialised.jsp"/>
                                                                        <c:set var="currentPreviewSvcInfo" value="${AppSubmissionDto.appSvcRelatedInfoDtoList.get(0)}" scope="request"/>
                                                                        <c:if test="${showHeadingSign}">
                                                                            <%--<c:set var="headingSign" value="${coMap.information == 'information' ? 'completed' : 'incompleted'}"/>--%>
                                                                            <c:set var="headingSign" value="${StringUtil.isIn(currentPreviewSvcInfo.serviceCode, coMap.multiSvcInfo) ? 'incompleted' : 'completed'}" />
                                                                        </c:if>
                                                                        <div class="panel panel-default svc-content">
                                                                            <div class="panel-heading ${headingSign}" id="headingServiceInfo${status.index}"
                                                                                 role="tab">
                                                                                <h4 class="panel-title">
                                                                                    <a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                                                                        href="#collapseServiceInfo${documentIndex}" aria-expanded="true" aria-controls="collapseServiceInfo">
                                                                                        Service Related Information
                                                                                    </a>
                                                                                </h4>
                                                                            </div>
                                                                            <div class=" panel-collapse collapse"
                                                                                 id="collapseServiceInfo${documentIndex}"
                                                                                 role="tabpanel" aria-labelledby="headingServiceInfo">
                                                                                <div class="panel-body">
                                                                                    <c:if test="${AppSubmissionDto.appEditSelectDto.serviceEdit && isSingle != 0}">
                                                                                        <div class="text-right font-16">
                                                                                            <a href="#" id="doSvcEdit">
                                                                                                <em class="fa fa-pencil-square-o"></em>Edit
                                                                                            </a>
                                                                                        </div>
                                                                                    </c:if>
                                                                                    <div class="panel-main-content">
                                                                                        <jsp:include page="/WEB-INF/jsp/iais/view/viewSvcInfo.jsp"/>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <c:choose>
                                                                            <c:when test="${isSingle == '1'}">
                                                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                                                    <%@include file="/WEB-INF/jsp/iais/common/declarations/declarations.jsp" %>
                                                                                </div>
                                                                            </c:when>
                                                                        </c:choose>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
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
                                <div class="col-xs-12 col-sm-7">
                                    <a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-2">
                                    <p class="print text-right"><a href="#" id="print-review">
                                        <em class="fa fa-print"></em>Print</a>
                                    </p>
                                </div>
                                <div class="col-xs-12 col-sm-3">
                                    <div class="text-right text-center-mobile" id="submitButton">
                                        <a id="SUBMIT" class="btn btn-primary">Submit</a>
                                    </div>
                                    <div class="text-right text-center-mobile hidden" id="nextButton">
                                        <a id="Next" class="btn btn-primary">Preview the Next Service</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" id="rfcPendingApplication" value="${rfcPendingApplication}">
    <div class="modal fade" id="rfcPending" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12"><span style="font-size: 2rem;">The changes you have made affect licences with pending application</span>
                        </div>
                    </div>
                </div>
                <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                    <button type="button" class="btn btn-secondary col-md-6" data-dismiss="modal" onclick="cancel()">Continue
                        amending
                    </button>
                    <button type="button" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="exitAndSave()">Exit and
                        save as draft
                    </button>
                </div>
            </div>
        </div>
    </div>
    <iais:confirm msg="${SERVICE_CONFIG_CHANGE}" callBack="cancel()" needCancel="false"
                  popupOrder="SERVICE_CONFIG_CHANGE"/>
    <input type="hidden" id="SERVICE_CONFIG_HAVE_CHANGE" value="${SERVICE_CONFIG_CHANGE}">
    <%@include file="/WEB-INF/jsp/include/validation.jsp" %>
</form>
<iais:confirm msg="${showOtherError}" callBack="$('#showOtherError').modal('hide');" popupOrder="showOtherError"
              needCancel="false" needEscapHtml="false"/>
<input type="hidden" value="${not empty showOtherError ? '1' : ''}" id="showOtherErrorCheck">
<script>
    $(document).ready(function () {
        if ('1' != '${isSingle}') {
            checkSubmitButton();
        }
        if ($('#rfcPendingApplication').val() == 'errorRfcPendingApplication') {
            $('#rfcPending').modal('show');
        }
        if ($('#SERVICE_CONFIG_HAVE_CHANGE').val() != '') {
            $('#SERVICE_CONFIG_CHANGE').modal('show');
        }
        if ($('#showOtherErrorCheck').val() != '') {
            $('#showOtherError').modal('show');
        }
    });

    $('.tableMain').click(function () {
        var DtoSize = $('#DtoSize').val();
        var lastId = 'dtoList' + DtoSize;
        var thisId = $(this).attr('id');
        //if the last service is selected
        if (thisId == lastId) {
            $('#submitButton').removeClass("hidden");
            $('#nextButton').addClass("hidden");
        } else {
            $('#submitButton').addClass("hidden");
            $('#nextButton').removeClass("hidden");
        }
    });

    function cancel() {
        $('#rfcPending').modal('hide');
        $('#SERVICE_CONFIG_CHANGE').modal('hide');
    }

    function exitAndSave() {
        $('#crud_action_additional').val('exitSaveDraft');
        $('#SUBMIT').trigger("click");
    }

    function checkSubmitButton() {
        var DtoSize = $('#DtoSize').val();
        var lastId = 'dtoList' + DtoSize;
        var thisId = $(this).attr('id');
        //if the last service is selected
        if (thisId == lastId) {
            $('#submitButton').removeClass("hidden");
            $('#nextButton').addClass("hidden");
        } else {
            $('#submitButton').addClass("hidden");
            $('#nextButton').removeClass("hidden");
        }
    }

    $('#nextButton').click(function () {
        var currentId = $('.tableMain.active').attr('id');
        var nextId = '#' + currentId;
        // $(nextId).removeClass('active');
        // $(nextId).next().addClass('active');
        $(nextId).next().trigger('click');
        $(nextId).next().find('a').trigger('click');
    });

    $('#BACK').click(function () {
        showWaiting();
        $('[name="switch_value"]').val('instructions');
        $('#mainForm').submit();
    });
    $('#SUBMIT').click(function () {
        $('#error_fieldMandatory').html("");
        $('[name="switch_value"]').val('doLicenceReview');
        showWaiting();
        $('#mainForm').submit();
    });

    $('#premisesEdit').click(function () {
        showWaiting();
        $('#EditValue').val('premises');
        $('[name="switch_value"]').val('doEdit');
        $('#mainForm').submit();
    });
    $('.specialisedEdit').click(function () {
        showWaiting();
        $('#EditValue').val('specialised');
        $('[name="switch_value"]').val('doEdit');
        $('#mainForm').submit();
    });
    $('#doSvcEdit').click(function () {
        showWaiting();
        $('#EditValue').val('service');
        $('[name="switch_value"]').val('doEdit');
        $('#mainForm').submit();
    });

    $("#print-review").click(function () {
        // window.print();
        var url = '${pageContext.request.contextPath}<%=RedirectUtil.appendCsrfGuardToken("/eservice/INTERNET/MohFePrintView/1/?appType=APTY004&serviceNameMiss=Y",request)%>';
        var txt = '';
        $(':checked, textarea', '#declarations').each(function () {
            txt += '&' + $(this).attr('name') + '=' + $(this).val();
        });
        if (url.indexOf('?') < 0) {
            url += '?';
            if (txt != '') {
                txt = txt.substring()
            }
        }
        window.open(url + txt, '_blank');
    })

    $('#subLicenseeEdit').click(function () {
        showWaiting();
        $('#EditValue').val('licensee');
        $('[name="switch_value"]').val('doEdit');
        $('#mainForm').submit();
    });

    function changeTabForMoreRenew(value) {
        if ($('#moblieSelectRenew').val() != value)
            fillValue('#moblieSelectRenew', value);
    }

    function changeTabForMoreRenewMobile(value) {
        setTimeout(function () {
            $("#dtoList" + value).find("a").click();
        }, 100);
    }

</script>