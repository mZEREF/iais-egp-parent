<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<div class="row form-horizontal normal-label">
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
    <iais:row>
        <div class="col-xs-12">
            <h2 class="app-title"><c:out value="${currStepName}"/></h2>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>
    <c:set var="appSvcOtherInfoList" value="${currSvcInfoDto.appSvcOtherInfoList}"/>
    <c:forEach var="appSvcOtherInfoDto" items="${appSvcOtherInfoList}">
        <c:set var="provideTop" value="${appSvcOtherInfoDto.provideTop}"/>
        <c:set var="practitioners" value="${appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}"/>
        <c:set var="anaesthetists" value="${appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList1}"/>
        <c:set var="nurses" value="${appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList2}"/>
        <c:set var="counsellors" value="${appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList3}"/>
        <c:set var="appSvcSuplmFormDto" value="${appSvcOtherInfoDto.appSvcSuplmFormDto}"/>
        <c:set var="appSvcOtherInfoTop" value="${appSvcOtherInfoDto.appSvcOtherInfoTopDto}" />
        <c:set var="topByDrug" value="${appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList}"/>
        <c:set var="topBySurgicalProcedure" value="${appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList1}"/>
        <c:set var="topByAll" value="${appSvcOtherInfoDto.appSvcOtherInfoAbortDtoList2}"/>
        <c:set var="med" value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto}"/>
        <c:set var="m" value="${appSvcOtherInfoDto.appSvcOtherInfoMedDto1}"/>
        <c:set var="n" value="${appSvcOtherInfoDto.appSvcOtherInfoNurseDto}"/>
        <c:set var="dsDeclaration" value="${appSvcOtherInfoDto.dsDeclaration}"/>
        <c:set var="ascsDeclaration" value="${appSvcOtherInfoDto.ascsDeclaration}"/>
        <c:set var="orgUse" value="${orgUserDto}"/>
        <iais:row>
            <div class="col-xs-12">
                <div class="app-title">${appSvcOtherInfoDto.premName}</div>
                <p class="font-18 bold">${appSvcOtherInfoDto.premAddress}</p>
            </div>
        </iais:row>

        <%@include file="dentalService.jsp" %>
        <%@include file="renalDialysisCentreService.jsp"%>
        <%@include file="ambulatorySurgicalCentreService.jsp"%>
        <%@include file="otherInformationTopPerson.jsp" %>
        <%@include file="otherInfoItemForm.jsp"%>
        <%@include file="documentation.jsp" %>
        <%@include file="aboutTop.jsp" %>
        <%@include file="yFV.jsp"%>
    </c:forEach>
</div>
<script>
    $(document).ready(function () {
        firstRadio();
        topRadio();
    });

    function firstRadio() {
        $('input.provideTop').unbind('click');
        $('input.provideTop').on('click', function () {
            let holderPregnancyVal = $(this).val();
            console.log('holderPregnancyVal:'+holderPregnancyVal);
            if (holderPregnancyVal == 1){
                $('input[name="t"]').val(1);
                console.log("input.name.ttt:"+$('input[name="t"]').val());
                $('div.topt').removeClass("hidden");
                $('div.practitioners').removeClass("hidden");
                $('div.addPractitionersDiv').removeClass("hidden");
                $('div.anaesthetists').removeClass("hidden");
                $('div.addAnaesthetistsDiv').removeClass("hidden");
                $('div.nurses').removeClass("hidden");
                $('div.addNursesDiv').removeClass("hidden");
                $('div.counsellors').removeClass("hidden");
                $('div.addCounsellorsDiv').removeClass("hidden");
                $('div.lowt').removeClass("hidden");
                $('div.de').removeClass("hidden");
                $('div.oitem').removeClass("hidden");
                topAboutHAS();
            }else {
                $('input[name="t"]').val(0);
                console.log("input.name.t:"+$('input[name="t"]').val());
                $('div.topt').addClass("hidden");
                $('div.practitioners').addClass("hidden");
                $('div.addPractitionersDiv').addClass("hidden");
                $('div.anaesthetists').addClass("hidden");
                $('div.addAnaesthetistsDiv').addClass("hidden");
                $('div.nurses').addClass("hidden");
                $('div.addNursesDiv').addClass("hidden");
                $('div.counsellors').addClass("hidden");
                $('div.addCounsellorsDiv').addClass("hidden");
                $('div.lowt').addClass("hidden");
                $('div.de').addClass("hidden");
                $('div.oitem').addClass("hidden");
                topAboutHAS();
            }
        });
    };

    function topRadio(){
        $('input.topType').unbind('click');
        $('input.topType').on('click', function () {
            let topTypeVal = $(this).val();
            console.log('topTypeVal:'+topTypeVal);
            if (topTypeVal == 1){
                $('div.topByDrug').removeClass("hidden");
                $('div.topBySurgicalProcedure').addClass("hidden");
                $('div.topByDrugandSurgicalProcedure').addClass("hidden");
                $('div.addTopByDrugDiv').removeClass("hidden");
                $('div.addTopBySurgicalProcedureDiv').addClass("hidden");
                $('div.addTopAllDiv').addClass("hidden");
            }else if (topTypeVal == 0){
                $('div.topByDrug').addClass("hidden");
                $('div.topBySurgicalProcedure').removeClass("hidden");
                $('div.topByDrugandSurgicalProcedure').addClass("hidden");
                $('div.addTopByDrugDiv').addClass("hidden");
                $('div.addTopBySurgicalProcedureDiv').removeClass("hidden");
                $('div.addTopAllDiv').addClass("hidden");
            }else if (topTypeVal == -1){
                $('div.topByDrug').removeClass("hidden");
                $('div.topBySurgicalProcedure').removeClass("hidden");
                $('div.topByDrugandSurgicalProcedure').removeClass("hidden");
                $('div.addTopByDrugDiv').removeClass("hidden");
                $('div.addTopBySurgicalProcedureDiv').removeClass("hidden");
                $('div.addTopAllDiv').removeClass("hidden");
            }else {
                $('div.topByDrug').addClass("hidden");
                $('div.topBySurgicalProcedure').addClass("hidden");
                $('div.topByDrugandSurgicalProcedure').addClass("hidden");
                $('div.addTopByDrugDiv').addClass("hidden");
                $('div.addTopBySurgicalProcedureDiv').addClass("hidden");
                $('div.addTopAllDiv').addClass("hidden");
            }
        });
    }
    function topAboutHAS(){
        let m = $('input[name="t"]').val();
        if (m==0){
            $('div.topByDrug').addClass("hidden");
            $('div.topBySurgicalProcedure').addClass("hidden");
            $('div.topByDrugandSurgicalProcedure').addClass("hidden");
            $('div.addTopByDrugDiv').addClass("hidden");
            $('div.addTopBySurgicalProcedureDiv').addClass("hidden");
            $('div.addTopAllDiv').addClass("hidden");
        }else {
            $('div.topByDrug').removeClass("hidden");
            $('div.topBySurgicalProcedure').removeClass("hidden");
            $('div.topByDrugandSurgicalProcedure').removeClass("hidden");
            $('div.addTopByDrugDiv').removeClass("hidden");
            $('div.addTopBySurgicalProcedureDiv').removeClass("hidden");
            $('div.addTopAllDiv').removeClass("hidden");
        }
    }

</script>


