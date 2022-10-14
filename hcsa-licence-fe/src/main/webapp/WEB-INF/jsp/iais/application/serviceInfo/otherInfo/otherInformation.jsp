<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit"
       value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
<div class="row">
    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </iais:row>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>
</div>
<div class="row form-horizontal normal-label">
    <iais:row>
        <div class="col-xs-12">
            <h2 class="app-title"><c:out value="${currStepName}"/></h2>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>
    <c:set var="appSvcOtherInfoList" value="${currSvcInfoDto.appSvcOtherInfoList}"/>
    <c:forEach var="appSvcOtherInfoDto" items="${appSvcOtherInfoList}" varStatus="status">
        <c:set var="provideTop" value="${appSvcOtherInfoDto.provideTop}"/>
        <c:set var="practitioners" value="${appSvcOtherInfoDto.otherInfoTopPersonPractitionersList}"/>
        <c:set var="anaesthetists" value="${appSvcOtherInfoDto.otherInfoTopPersonAnaesthetistsList}"/>
        <c:set var="nurses" value="${appSvcOtherInfoDto.otherInfoTopPersonNursesList}"/>
        <c:set var="counsellors" value="${appSvcOtherInfoDto.otherInfoTopPersonCounsellorsList}"/>
        <c:set var="appSvcSuplmFormDto" value="${appSvcOtherInfoDto.appSvcSuplmFormDto}"/>
        <c:set var="appSvcOtherInfoTop" value="${appSvcOtherInfoDto.appSvcOtherInfoTopDto}"/>
        <c:set var="topByDrug" value="${appSvcOtherInfoDto.otherInfoAbortDrugList}"/>
        <c:set var="topBySurgicalProcedure" value="${appSvcOtherInfoDto.otherInfoAbortSurgicalProcedureList}"/>
        <c:set var="topByAll" value="${appSvcOtherInfoDto.otherInfoAbortDrugAndSurgicalList}"/>
        <c:set var="med" value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto}"/>
        <c:set var="m" value="${appSvcOtherInfoDto.otherInfoMedAmbulatorySurgicalCentre}"/>
        <c:set var="n" value="${appSvcOtherInfoDto.appSvcOtherInfoNurseDto}"/>
        <c:set var="dsDeclaration" value="${appSvcOtherInfoDto.dsDeclaration}"/>
        <c:set var="ascsDeclaration" value="${appSvcOtherInfoDto.ascsDeclaration}"/>
        <c:set var="orgUse" value="${orgUserDto}"/>
        <c:set var="prefix" value="${appSvcOtherInfoDto.premisesVal}"/>
        <iais:row>
            <div class="col-xs-12">
                <div class="app-title">${appSvcOtherInfoDto.premName}</div>
                <p class="font-18 bold">${appSvcOtherInfoDto.premAddress}</p>
            </div>
        </iais:row>

        <c:choose>
            <c:when test="${(currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE) || (currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)}">
                <%@include file="dentalService.jsp" %>
                <c:if test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE}">
                    <div class="otherInfoTopContent">
                        <input type="hidden" class="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                            <%--        <input type="hidden" class="chargesIndexNo" name="chargesIndexNo${status.index}" value="${appSvcOtherInfoDto.chargesIndexNo}"/>--%>
                        <div class="col-md-12 col-xs-12">
                            <div class="edit-content">
                                <c:if test="${canEdit}">
                                    <div class="text-right app-font-size-16">
                                        <a class="edit otherInfoTopEdit" href="javascript:void(0);">
                                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                        </a>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <%@include file="otherInformationTopPerson.jsp" %>
                        <%@include file="otherInfoItemForm.jsp" %>
                        <%@include file="documentation.jsp" %>
                        <%@include file="aboutTop.jsp" %>
                        <%@include file="yFV.jsp" %>
                    </div>
                </c:if>
            </c:when>
            <c:when test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_RENAL_DIALYSIS_CENTRE}">
                <%@include file="renalDialysisCentreService.jsp" %>
            </c:when>
            <c:when test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE}">
                <%@include file="ambulatorySurgicalCentreService.jsp" %>
                <div class="otherInfoTopContent">
                    <input type="hidden" class="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                        <%--        <input type="hidden" class="chargesIndexNo" name="chargesIndexNo${status.index}" value="${appSvcOtherInfoDto.chargesIndexNo}"/>--%>
                    <div class="col-md-12 col-xs-12">
                        <div class="edit-content">
                            <c:if test="${canEdit}">
                RF                <div class="text-right app-font-size-16">
                                    <a class="edit otherInfoTopEdit" href="javascript:void(0);">
                                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    <%@include file="otherInformationTopPerson.jsp" %>
                    <%@include file="otherInfoItemForm.jsp" %>
                    <%@include file="documentation.jsp" %>
                    <%@include file="aboutTop.jsp" %>
                </div>
            </c:when>
            <c:when test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL}">
                <div class="otherInfoTopContent">
                    <input type="hidden" class="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                        <%--        <input type="hidden" class="chargesIndexNo" name="chargesIndexNo${status.index}" value="${appSvcOtherInfoDto.chargesIndexNo}"/>--%>
                    <div class="col-md-12 col-xs-12">
                        <div class="edit-content">
                            <c:if test="${canEdit}">
                                <div class="text-right app-font-size-16">
                                    <a class="edit otherInfoTopEdit" href="javascript:void(0);">
                                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    <%@include file="otherInformationTopPerson.jsp" %>
                    <%@include file="otherInfoItemForm.jsp" %>
                    <%@include file="documentation.jsp" %>
                    <%@include file="aboutTop.jsp" %>
                    <%@include file="yFV.jsp" %>
                </div>
            </c:when>
            <c:when test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}">
                <div class="otherInfoTopContent">
                    <input type="hidden" class="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                        <%--        <input type="hidden" class="chargesIndexNo" name="chargesIndexNo${status.index}" value="${appSvcOtherInfoDto.chargesIndexNo}"/>--%>
                    <div class="col-md-12 col-xs-12">
                        <div class="edit-content">
                            <c:if test="${canEdit}">
                                <div class="text-right app-font-size-16">
                                    <a class="edit otherInfoTopEdit" href="javascript:void(0);">
                                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    <%@include file="yFV.jsp" %>
                </div>
            </c:when>
            <c:otherwise>

            </c:otherwise>
        </c:choose>

        <%@include file="otherService.jsp" %>
    </c:forEach>
</div>
<script>
    $(document).ready(function () {
        doEditOtherInfoRDCEvent();
        doEditOtherInfoDentalServiceEvent();
        doEditOtherInfoASCSEvent();
        doEditOtherInfoTopEvent();
        //rfc,renew,rfi
        <c:if test="${AppSubmissionDto.needEditController}">
            disableOtherInfoContent();
        </c:if>
    })

    let doEditOtherInfoDentalServiceEvent = function () {
        $('a.otherInfoDSEdit').click(function () {
            console.log("ds.............")
            let $currContent = $(this).closest('div.otherInfoPageContent');
            $currContent.find('input.isPartEdit').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    };

    let doEditOtherInfoRDCEvent = function () {
        $('a.otherInfoRDCEdit').click(function () {
            let $currContent = $(this).closest('div.otherInfoRDCPageContent');
            $currContent.find('input.isPartEdit').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    }

    let doEditOtherInfoASCSEvent = function () {
        $('a.otherInfoASCSEdit').click(function () {
            let $currContent = $(this).closest('div.otherInfoASCSContent');
            $currContent.find('input.isPartEdit').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    }

    let doEditOtherInfoTopEvent = function () {
        $('a.otherInfoTopContent').click(function () {
            let $currContent = $(this).closest('div.otherInfoTopContent');
            $currContent.find('input.isPartEdit').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    }

    function disableOtherInfoContent() {
        // edit btn
        let $currContent = $('div.otherInfoPageContent');
        disableContent($currContent);
        let $rdcContent = $('.otherInfoRDCPageContent');

        disableContent($rdcContent);
        let $ascsContent = $('.otherInfoASCSContent');
        disableContent($ascsContent);
        let $topContent = $('.otherInfoTopContent');
        disableContent($topContent);
    }
</script>