<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>

<webui:setLayout name="iais-blank"/>
<style>
    .form-check input.form-check-input:checked + .form-check-label span.check-circle:before,
    .form-check input.form-check-input:active + .form-check-label span.check-circle:before {
        color: #147aab !important;
        background-color: #FFF;
        content: "\f111";
        font-family: FontAwesome, sans-serif;
        position: absolute;
        font-size: 12px;
        top: 38%;
        left: 48%;
    }

    @media all and (min-width: 429px) {
        label.control-label {
            width: 40%;
        }

        div.col-sm-7 {
            width: 60%;
        }

        .col-md-3 {
            width: 30%;
        }
    }

    .panel-group .panel.panel-default > .panel-heading h4 a {
        text-decoration: none;
    }
</style>
<br/>

<c:set var="isRfi" value="${not empty requestInformationConfig}" scope="request"/>

<%--<%@include file="../common/dashboard.jsp" %>--%>
<form method="post" class="table-responsive" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <iais:input type="hidden" name="viewPrint" value="${viewPrint}" />
    <div class="main-content">
        <div class="container">
            <div class="row">
                <c:set var="printFlag" value="test" scope="request"/>
                <c:forEach begin="0" end="${viewSubmissons.size()-1}" step="1" varStatus="submisonStat">
                    <c:set var="AppSubmissionDto" value="${viewSubmissons[submisonStat.index]}" scope="request"/>
                    <c:set var="isRFC" value="${'APTY005' == AppSubmissionDto.appType}" scope="request"/>
                    <c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>
                    <c:set var="specialSubLic" value="${subLicenseeDto.licenseeType eq 'LICT002' || subLicenseeDto.licenseeType eq 'LICTSUB002'}" />
                    <c:set var="isLicence" value="${not empty licenceView}" scope="request"/>

                    <div class="col-xs-12">
                        <div class="tab-gp steps-tab">
                            <div class=""><%--tab-content--%>
                                <div class="tab-pane active" id="previewTab" role="tabpanel">
                                    <div class="preview-gp">
                                        <c:if test="${submisonStat.first}">
                                            <div class="row">
                                                <br/><br/><br/>
                                            </div>
                                        </c:if>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                                    <c:set var="documentIndex" value="${submisonStat.index}"/>
                                                    <jsp:include page="/WEB-INF/jsp/iais/view/viewLicensee.jsp"/>
                                                    <jsp:include page="/WEB-INF/jsp/iais/view/viewPremises.jsp"/>
                                                    <jsp:include page="/WEB-INF/jsp/iais/view/viewSpecialised.jsp"/>

                                                    <c:set var="appGrpPremisesDtoList" value="${AppSubmissionDto.appGrpPremisesDtoList}" scope="request"/>
                                                    <c:forEach var="currentPreviewSvcInfo" items="${AppSubmissionDto.appSvcRelatedInfoDtoList}" varStatus="svcStat">
                                                        <c:set var="currentPreviewSvcInfo" value="${currentPreviewSvcInfo}" scope="request"/>

                                                        <div class="panel panel-default svc-content">
                                                            <div class="panel-heading"  id="headingServiceInfo" role="tab">
                                                                <h4 class="panel-title"><a class="svc-pannel-collapse collapsed a-panel-collapse" style="text-decoration: none;"  role="button" data-toggle="collapse" href="#collapseServiceInfo${submisonStat.index}${svcStat.index}" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information<c:if test="${empty licenceView  && serviceNameMiss != 'Y'}"> - ${currentPreviewSvcInfo.serviceName}</c:if></a></h4>
                                                            </div>

                                                            <div class=" panel-collapse collapse in" id="collapseServiceInfo${submisonStat.index}${svcStat.index}" role="tabpanel" aria-labelledby="headingServiceInfo">
                                                                <div class="panel-body">
                                                                    <div class="panel-main-content">
                                                                        <jsp:include page="/WEB-INF/jsp/iais/view/viewSvcInfo.jsp"/>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                    <c:if test="${AppSubmissionDto.appType != 'APTY009'}">
                                                        <%@include file="../common/declarations/declarations.jsp"%>
                                                    </c:if>
                                                    <c:if test="${AppSubmissionDto.appType == 'APTY005'}">
                                                        <c:set var="rfc_from_renew" value="${(not empty AppSubmissionDto.appDeclarationMessageDto && AppSubmissionDto.appDeclarationMessageDto.appType == 'APTY004') ? 'Y' : 'N'}"/>
                                                        <c:set var="showDeclartion" value="${not empty RFC_eqHciNameChange && RFC_eqHciNameChange != 'RFC_eqHciNameChange'}"/>
                                                        <c:if test="${showDeclartion && rfc_from_renew != 'Y'}">
                                                            <div class="row">
                                                                <div class="form-check col-md-8 col-lg-9 col-xs-12">
                                                                    Please indicate the date which you would like the changes to be effective (subject to approval). If not indicated, the effective date will be the approval date of the change.
                                                                </div>
                                                                <div class="col-md-4 col-lg-3 col-xs-12">
                                                                    <iais:datePicker cssClass="rfcEffectiveDate" name="rfcEffectiveDate" value="${AppSubmissionDto.effectiveDateStr}" />
                                                                </div>
                                                            </div>
                                                            <div class="form-check">
                                                                <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" value="1" aria-invalid="false" <c:if test="${AppSubmissionDto.userAgreement}">checked="checked"</c:if> >
                                                                <label class="form-check-label" for="verifyInfoCheckbox">
                                                                    <span class="check-square"></span>
                                                                    <iais:message key="ACK_DEC001" escape="false" />
                                                                </label>
                                                            </div>
                                                            <div>
                                                                <span id="error_fieldMandatory"  class="error-msg"></span>
                                                            </div>

                                                            <div>
                                                                <span id="error_charityHci"  class="error-msg"></span>
                                                            </div>
                                                        </c:if>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

</form>
<script type="text/javascript">
    $(document).ready(function () {
        $(':input', '#declarations').prop('disabled', true);
        $('.panel-collapse').collapse('show');
        var btn = $('.file-upload-gp a', '#declarations');
        if (btn.length > 0) {
            btn.each(function (index, ele) {
                $(ele).parent().html($(ele).text());
            });
        }
        // textarea
        $('textarea', '#declarations').each(function (index, ele) {
            $(ele).parent().append('<div style="border-radius:8px;border: 1px solid #000;padding: 5px;">'
                + $(ele).val() + '</div>');
            $(ele).remove();
        });

        var userAgent = navigator.userAgent;
        var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1;

        // disabled <a>
        $('a').prop('disabled', true);
        if (isChrome) {
            addPrintListener();
            window.print();
        } else {
            window.print();
            window.close();
        }

    });

    var addPrintListener = function () {
        if (window.matchMedia) {
            var mediaQueryList = window.matchMedia('print');
            mediaQueryList.addListener(function(mql) {
                if (mql.matches) {

                } else {
                    window.close();
                }
            });
        }
    }

</script>