<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="row form-horizontal">
    <%--    TODO...........--%>
    <input type="hidden" class="not-refresh prepsn" name="${psnContent}" value="${prepsn}"/>
    <input type="hidden" class="not-refresh assignSelVal" name="${prepsn}assignSelVal" value="${person.assignSelect}"/>
    <input type="hidden" class="not-refresh licPerson" name="${prepsn}licPerson" value="${person.licPerson ? 1 : 0}"/>
    <input type="hidden" class="not-refresh isPartEdit" name="${prepsn}isPartEdit" value="0"/>
    <input type="hidden" class="not-refresh indexNo" name="${prepsn}indexNo" value="${person.indexNo}"/>
    <input type="hidden" class="not-refresh psnEditField" name="${prepsn}psnEditField" value="<c:out value="${person.psnEditFieldStr}" />"/>

    <iais:row cssClass="edit-content">
        <c:if test="${canEdit}">
            <div class="text-right app-font-size-16">
                <a class="edit psnEdit" href="javascript:void(0);">
                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
            </div>
        </c:if>
    </iais:row>

    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">Other Information</p>
        </div>
    </iais:row>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Do you provide Termination of Pregnancy&nbsp;"/>
                <%--            TODO.........--%>
        <input type="hidden" class="holdCerByEMSVal" name="holdCerByEMSVal" value="${holdCerByEMSStatus}"/>
        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input holdCerByEMS" <c:if test="${'1' == holdCerByEMSStatus}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "1" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </iais:value>

        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input holdCerByEMS" <c:if test="${'0' == holdCerByEMSStatus}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </iais:value>
    </iais:row>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_holdCerByEMS${index}"></span>
        </iais:value>
    </iais:row>

    <div class="allOtherInformation">

        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="12" cssClass="col-md-12" mandatory="true" value="Please indicate&nbsp;"/>
        </iais:row>

        <iais:row cssClass="row control control-caption-horizontal">
            <%--            TODO.........--%>
            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input holdCerByEMS1" <c:if test="${'1' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Solely by Drug)</label>
            </iais:value>

            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input holdCerByEMS1" <c:if test="${'0' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Solely by Surgical Procedure)</label>
            </iais:value>

            <iais:value width="4" cssClass="form-check col-md-4">
                <input class="form-check-input holdCerByEMS1" <c:if test="${'0' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Termination of Pregnancy(Drug and Surgical Procedure)</label>
            </iais:value>
        </iais:row>

        <%--TODO...practitioners--%>
        <div class="practitioners person-detail">
            <iais:row>
                <div class="col-xs-12 col-md-10">
                        <%--                <p class="bold">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion<span class="psnHeader">${index+1}</span></p>--%>
                    <p class="bold">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion</p>
                </div>
                <%--            <div class="col-xs-12 col-md-2 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">--%>
                <%--                <h4 class="text-danger">--%>
                <%--                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>--%>
                <%--                </h4>--%>
                <%--            </div>--%>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Professional Regn. No."/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="NRIC/FIN No."/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Type of Registration"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Name of medical practitioner"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Specialties"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Qualifications"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row cssClass="row control control-caption-horizontal">
                <iais:value width="6" cssClass="col-md-6">
                    <label class="form-check-label" >Is the medical practitioners authorised by MOH to perform Abortion
                        (if No, please upload a copy of the Obstetrics & Gynaecology certificate and <span style="color:deepskyblue;cursor:pointer;text-decoration: underline;">From 2</span> at the Document page)

                        <span class="mandatory">*</span>
                    </label>
                </iais:value>
                <%--            TODO.........--%>
                <input type="hidden" class="holdCerByEMSVal" name="holdCerByEMSVal${index}" value="${clinicalDirectorDto.holdCerByEMS}"/>
                <iais:value width="3" cssClass="form-check col-md-3">
                    <input class="form-check-input holdCerByEMS" <c:if test="${'1' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "1" aria-invalid="false">
                    <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                </iais:value>

                <iais:value width="3" cssClass="form-check col-md-3">
                    <input class="form-check-input holdCerByEMS" <c:if test="${'0' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "0" aria-invalid="false">
                    <label class="form-check-label" ><span class="check-circle"></span>No</label>
                </iais:value>
            </iais:row>
        </div>
        <%--    TODO......--%>
        <c:if test="${!isRfi}">
            <c:set var="needAddPsn" value="true"/>
            <c:choose>
                <c:when test="${currStepConfig.status =='CMSTAT003'}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${personCount >= currStepConfig.maximumCount}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
            </c:choose>
            <div class="col-md-12 col-xs-12 addPractitionersDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addPersonnelBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">Add more</span>
            </span>
            </div>
        </c:if>
        <%--TODO...anaesthetists--%>
        <div class="person-detail anaesthetists">
            <iais:row>
                <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
                        <%--                <p class="bold">Name, Professional Regn. No. and Qualification of anaesthetists<span class="psnHeader">${index+1}</span></p>--%>
                    <p class="bold">Name, Professional Regn. No. and Qualification of anaesthetists</p>
                </div>
                <%--            <div class="col-xs-12 col-md-2 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">--%>
                <%--                <h4 class="text-danger">--%>
                <%--                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>--%>
                <%--                </h4>--%>
                <%--            </div>--%>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Professional Regn. No."/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="NRIC/FIN No."/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Type of Registration"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Name of anaesthetists"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Qualifications"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>
        </div>
        <%--    TODO......--%>
        <c:if test="${!isRfi}">
            <c:set var="needAddPsn" value="true"/>
            <c:choose>
                <c:when test="${currStepConfig.status =='CMSTAT003'}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${personCount >= currStepConfig.maximumCount}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
            </c:choose>
            <div class="col-md-12 col-xs-12 addPractitionersDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addAnaesthetistsBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">Add more</span>
            </span>
            </div>
        </c:if>
        <%--TODO...nurses--%>
        <div class="person-detail nurses">
            <iais:row>
                <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
                    <p class="bold">Name, Professional Regn. No. and Qualification of trained nurses</p>
                        <%--                <p class="bold">Name, Professional Regn. No. and Qualification of trained nurses<span class="psnHeader">${index+1}</span></p>--%>
                </div>
                <%--            <div class="col-xs-12 col-md-2 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">--%>
                <%--                <h4 class="text-danger">--%>
                <%--                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>--%>
                <%--                </h4>--%>
                <%--            </div>--%>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Name of trained nurses"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Qualifications"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>
        </div>
        <%--    TODO......--%>
        <c:if test="${!isRfi}">
            <c:set var="needAddPsn" value="true"/>
            <c:choose>
                <c:when test="${currStepConfig.status =='CMSTAT003'}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${personCount >= currStepConfig.maximumCount}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
            </c:choose>
            <div class="col-md-12 col-xs-12 addPractitionersDiv <c:if test="${!needAddPsn}">hidden</c:if>">
                <span class="addNursesBtn" style="color:deepskyblue;cursor:pointer;">
                    <span style="">Add more</span>
                </span>
            </div>
        </c:if>

        <%--TODO...counsellors--%>
        <div class="person-detail counsellors">
            <iais:row>
                <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
                    <p class="bold">Name, Professional Regn. No. and Qualification of certified TOP counsellors</p>
                        <%--                <p class="bold">Name, Professional Regn. No. and Qualification of certified TOP counsellors<span class="psnHeader">${index+1}</span></p>--%>
                </div>
                <%--            <div class="col-xs-12 col-md-2 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">--%>
                <%--                <h4 class="text-danger">--%>
                <%--                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>--%>
                <%--                </h4>--%>
                <%--            </div>--%>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Name of certified TOP counsellors(Only Doctor/Nurse)"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="NRIC/FIN No."/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Qualifications"/>
                <iais:value width="6" cssClass="col-md-6">
                    <%--            TODO.....--%>
                    <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
                </iais:value>
            </iais:row>
        </div>
        <%--    TODO......--%>
        <c:if test="${!isRfi}">
            <c:set var="needAddPsn" value="true"/>
            <c:choose>
                <c:when test="${currStepConfig.status =='CMSTAT003'}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${personCount >= currStepConfig.maximumCount}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
                <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                    <c:set var="needAddPsn" value="false"/>
                </c:when>
            </c:choose>
            <div class="col-md-12 col-xs-12 addPractitionersDiv <c:if test="${!needAddPsn}">hidden</c:if>">
            <span class="addCounsellorsBtn" style="color:deepskyblue;cursor:pointer;">
                <span style="">Add more</span>
            </span>
            </div>
        </c:if>

        <iais:row cssClass="row control control-caption-horizontal">
            <iais:value width="6" cssClass="col-md-6">
                <label class="form-check-label" style="padding-top: 25px;">My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)
                    <span class="mandatory">*</span>
                </label>
            </iais:value>
            <%--            TODO.........--%>
            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input holdCerByEMS1" <c:if test="${'1' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </iais:value>

            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input holdCerByEMS1" <c:if test="${'0' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </iais:value>
        </iais:row>

        <iais:row cssClass="row control control-caption-horizontal">
            <iais:value width="6" cssClass="col-md-6">
                <label class="form-check-label" style="padding-top: 25px;">The service provider has the necessary counselling facilities e.g. TV set, video player, video on abortion produced by HPB in different languages and the pamphlets produced by HPB
                    <span class="mandatory">*</span>
                </label>
            </iais:value>
            <%--            TODO.........--%>
            <input type="hidden" class="holdCerByEMSVal" name="holdCerByEMSVal${index}" value="${clinicalDirectorDto.holdCerByEMS}"/>
            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input holdCerByEMS1" <c:if test="${'1' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </iais:value>

            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input holdCerByEMS1" <c:if test="${'0' == clinicalDirectorDto.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="holdCerByEMS${index}" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </iais:value>
        </iais:row>
    </div>
</div>
<%@include file="../../common/prsLoading.jsp"%>
<script>
    $(document).ready(function () {
        initRadio();
        firstRadio();

    });

    function initRadio(){
        $('div.allOtherInformation').addClass("hidden");
    }

    function firstRadio() {
        $('input.holdCerByEMS').unbind('click');
        $('input.holdCerByEMS').on('click', function () {
            let holdCerByEMSVal = $(this).val();
            if (holdCerByEMSVal == 1){
                $('div.allOtherInformation').removeClass("hidden");
            }else {
                $('div.allOtherInformation').addClass("hidden");
            }
        });
    };
</script>

