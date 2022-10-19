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
                <input type="hidden" name="otherInfoServiceCode" value="${currSvcInfoDto.serviceCode}">
                <%@include file="dentalService.jsp" %>
                <c:if test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE}">
                    <div class="otherInfoTopContent" data-prefix="${prefix}" data-group="${appSvcOtherInfoTop.topType}" ata-seq="${provideTop}">
                        <input type="hidden" class ="isPartEditTop" name="isPartEditTop" value="0"/>
                        <input type="hidden" class="otherInfoIndexNo" name="otherInfoIndexNo" value="${appSvcOtherInfoDto.premiseIndex}"/>
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
                <input type="hidden" name="otherInfoServiceCode" value="${currSvcInfoDto.serviceCode}">
                <%@include file="renalDialysisCentreService.jsp" %>
            </c:when>
            <c:when test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE}">
                <input type="hidden" name="otherInfoServiceCode" value="${currSvcInfoDto.serviceCode}">
                <%@include file="ambulatorySurgicalCentreService.jsp" %>
                <div class="otherInfoTopContent" data-prefix="${prefix}">
                    <input type="hidden" class ="isPartEditTop" name="isPartEditTop" value="0"/>
                    <input type="hidden" class="otherInfoPremisesVal" name="otherInfoPremisesVal" value="${appSvcOtherInfoDto.premisesVal}"/>
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
                </div>
            </c:when>
            <c:when test="${currSvcInfoDto.serviceCode == AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL}">
                <input type="hidden" name="otherInfoServiceCode" value="${currSvcInfoDto.serviceCode}">
                <div class="otherInfoTopContent" data-prefix="${prefix}">
                    <input type="hidden" class ="isPartEditTop" name="isPartEditTop" value="0"/>
                    <input type="hidden" class="otherInfoIndexNo" name="otherInfoIndexNo" value="${appSvcOtherInfoDto.premiseIndex}"/>
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
                <input type="hidden" name="otherInfoServiceCode" value="${currSvcInfoDto.serviceCode}">
                <div class="otherInfoTopContent" data-prefix="${prefix}">
                    <input type="hidden" class ="isPartEditTop" name="isPartEditTop" value="0"/>
                    <input type="hidden" class="otherInfoIndexNo" name="otherInfoIndexNo" value="${appSvcOtherInfoDto.premiseIndex}"/>
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
<%@include file="/WEB-INF/jsp/iais/application/common/personFun.jsp" %>
<script>
    $(document).ready(function () {
        doEditOtherInfoRDCEvent();
        doEditOtherInfoDentalServiceEvent();
        doEditOtherInfoASCSEvent();
        doEditOtherInfoTopEvent();
        //other service
        otherServiceCheckboxEvent();
        doEditOtherServiceEvent();
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
        $('a.otherInfoTopEdit').click(function () {
            let $tag = $('div.otherInfoTopContent');
            let prefix = $tag.data('prefix');
            console.log("doTopEdit-prefix:"+prefix);
            if (!isEmpty(prefix)){
                $('input.rfcEdit[data-prefix="' + prefix + '"]').val('doEditPractitioners');
                $('input.rfcAnaesthetistsEdit[data-prefix="' + prefix + '"]').val('doEditAnaesthetists');
                $('input.rfcNursesEdit[data-prefix="' + prefix + '"]').val('doEditNurses');
                $('input.rfcCounsellorsEdit[data-prefix="' + prefix + '"]').val('doEditCounsellors');
                $('input.rfcDrugEdit[data-prefix="' + prefix + '"]').val('doEditDrug');
                $('input.rfcSurgicalEdit[data-prefix="' + prefix + '"]').val('doEditSurgical');
                $('input.rfcAllEdit[data-prefix="' + prefix + '"]').val('doEditAll');
            }
            let $currContent = $(this).closest('div.otherInfoTopContent');
            $currContent.find('input.isPartEditTop').val('1');
            $currContent.find('input.isPartEdit').val('1');
            $currContent.find('input.aisPartEdit').val('1');
            $currContent.find('input.nisPartEdit').val('1');
            $currContent.find('input.cisPartEdit').val('1');
            $currContent.find('input.isPartEditDrug').val('1');
            $currContent.find('input.isPartEditSurgical').val('1');
            $currContent.find('input.isPartEditAll').val('1');
            removeB(prefix);
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    }

    function otherServiceCheckboxEvent() {
        $('input[type="checkbox"]').on('click', function () {
            checkOtherServiceCheckbox($(this));
        });
    }
    function checkOtherServiceCheckbox($input) {
        let data = $input.data('prem') + '-' + $input.val();
        if ($input.is(':checked')) {
            showTag($('div[data-parent="' + data + '"]'));
        } else {
            hideTag($('div[data-parent="' + data + '"]'));
        }
    }


    let doEditOtherServiceEvent = function (){
        let $target = $('.otherServiceEdit');
        if (isEmptyNode($target)) {
            return;
        }
        $target.unbind('click');
        $target.on('click', function () {
            console.log("otherService.............")
            let $currContent = $(this).closest('div.otherServiceContent');
            $currContent.find('input.isPartEditOtherService').val('1');
            hideTag($currContent.find('.edit-content'));
            unDisableContent($currContent);
            $('#isEditHiddenVal').val('1');
        });
    }
    function refreshOtherPAddBtn(prefix) {
        if (isEmpty(prefix)){
            prefix = "";
        }
        toggleTag('.addPractitionersDiv', $('div.practitioners[data-prefix="' + prefix + '"]').length < '10');
    }
    function refreshOtherAAddBtn(prefix) {
        if (isEmpty(prefix)){
            prefix = "";
        }
        toggleTag('.addAnaesthetistsDiv', $('div.anaesthetists[data-prefix="' + prefix + '"]').length < '10');
    }
    function refreshOtherNAddBtn(prefix) {
        if (isEmpty(prefix)){
            prefix = "";
        }
        toggleTag('.addNursesDiv', $('div.nurses[data-prefix="' + prefix + '"]').length < '10');
    }
    function refreshOtherCAddBtn(prefix) {
        if (isEmpty(prefix)){
            prefix = "";
        }
        toggleTag('.addCounsellorsDiv', $('div.counsellors[data-prefix="' + prefix + '"]').length < '10');
    }
    function refreshOtherDAddBtn(prefix) {
        if (isEmpty(prefix)){
            prefix = "";
        }
        toggleTag('.addTopByDrugDiv', $('div.topByDrug[data-prefix="' + prefix + '"]').length < '10');
    }
    function refreshOtherSAddBtn(prefix) {
        if (isEmpty(prefix)){
            prefix = "";
        }
        toggleTag('.addTopBySurgicalProcedureDiv', $('div.topBySurgicalProcedure[data-prefix="' + prefix + '"]').length < '10');
    }
    function refreshOtherLAddBtn(prefix) {
        if (isEmpty(prefix)){
            prefix = "";
        }
        toggleTag('.addTopAllDiv', $('div.topByDrugandSurgicalProcedure[data-prefix="' + prefix + '"]').length < '10');
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
        let $tag = $topContent.find('.otherInfoTopEdit');
        let prefix = $topContent.data('prefix');
        let topType = $topContent.data('group');
        let provideTop = $topContent.data('seq');
        console.log("topType:"+topType);
        console.log("provideTop:"+provideTop);
        if (provideTop == '1'){
            if (!isEmptyNode($tag)) {
                showTag($currContent.find('.removePDiv:not(:first)'));
                showTag($currContent.find('.removeADiv:not(:first)'));
                showTag($currContent.find('.removeNDiv:not(:first)'));
                showTag($currContent.find('.removeCODiv:not(:first)'));
                showTag($currContent.find('.rDiv:not(:first)'));
                showTag($currContent.find('.rdDiv:not(:first)'));
                showTag($currContent.find('.rTDiv:not(:first)'));
                refreshOtherPAddBtn(prefix);
                refreshOtherAAddBtn(prefix);
                refreshOtherNAddBtn(prefix);
                refreshOtherCAddBtn(prefix);
                if (topType == '1' || topType == '0'){
                    refreshOtherDAddBtn(prefix);
                }
                if (topType == '-1' || topType == '0'){
                    refreshOtherSAddBtn(prefix);
                }
                if (topType == '0'){
                    refreshOtherLAddBtn(prefix);
                }
            } else {
                hideTag($currContent.find('.removePDiv'));
                hideTag($currContent.find('.addPractitionersDiv'));
                hideTag($currContent.find('.removeADiv'));
                hideTag($currContent.find('.addAnaesthetistsDiv'));
                hideTag($currContent.find('.removeNDiv'));
                hideTag($currContent.find('.addNursesDiv'));
                hideTag($currContent.find('.removeCODiv'));
                hideTag($currContent.find('.addCounsellorsDiv'));
                if (topType == '1' || topType == '0'){
                    hideTag($currContent.find('.rDiv'));
                    hideTag($currContent.find('.addTopByDrugDiv'));
                }
                if (topType == '-1' || topType == '0'){
                    hideTag($currContent.find('.rdDiv'));
                    hideTag($currContent.find('.addTopBySurgicalProcedureDiv'));
                }
                if (topType == '0'){
                    hideTag($currContent.find('.rTDiv'));
                    hideTag($currContent.find('.addTopAllDiv'));
                }
            }
        }
        disableR(prefix)
        disableContent($topContent);
        let $otherServiceContent = $('.otherServiceContent');
        disableContent($otherServiceContent);
    }

    function disableR(prefix){
        console.log("dis:"+prefix);
        if (isEmpty(prefix)){
            prefix = "";
        }
        $('div.removePDiv[data-prefix="' + prefix + '"]').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $('div.removeADiv[data-prefix="' + prefix + '"]').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $('div.removeNDiv[data-prefix="' + prefix + '"]').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $('div.removeCODiv[data-prefix="' + prefix + '"]').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $('div.rDiv[data-prefix="' + prefix + '"]').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $('div.rdDiv[data-prefix="' + prefix + '"]').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
        $('div.rTDiv[data-prefix="' + prefix + '"]').prop('disabled',true).css('pointer-events','none').css('border-color', '#ededed').css('color', '#999');
    }

    function removeB(prefix){
        console.log("rem:"+prefix);
        if (isEmpty(prefix)){
            prefix = "";
        }
        $('div.removePDiv[data-prefix="' + prefix + '"]').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
        $('div.removeADiv[data-prefix="' + prefix + '"]').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
        $('div.removeNDiv[data-prefix="' + prefix + '"]').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
        $('div.removeCODiv[data-prefix="' + prefix + '"]').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
        $('div.rDiv[data-prefix="' + prefix + '"]').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
        $('div.rdDiv[data-prefix="' + prefix + '"]').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
        $('div.rTDiv[data-prefix="' + prefix + '"]').prop('disabled',false).css('pointer-events','').css('border-color', '').css('color', '');
    }
</script>