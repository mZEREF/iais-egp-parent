<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="psnType" value="${ApplicationConsts.OTHER_TOP_PRACTITIONERS}"/>
<div class="practitioners person-detail <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
    <iais:row>
        <div class="col-xs-12 col-md-10">
                <%--                <p class="bold">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion<span class="psnHeader">${index+1}</span></p>--%>
            <p class="bold">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion
                &nbsp;<label class="assign-psn-item"  data-prefix="${prefix}"><c:if test="${practitioners.size() > 1}">${index+1}</c:if></label>
            </p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removePractitionersBtn removePDiv" data-prefix="${prefix}">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 text-right removePractitionersBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" class="psnType" name="${prefix}psnType${index}" value="${psnType}" data-prefix="${prefix}">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prefix}profRegNo${index}" value="${person.profRegNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="NRIC/FIN No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="9" type="text" cssClass="idNo" name="${prefix}idNo${index}" value="${person.idNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" cssClass="regType" name="${prefix}regType${index}" value="${person.regType}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name of medical practitioner"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" cssClass="name" name="${prefix}name${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Specialties"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="speciality" name="${prefix}speciality${index}" value="${person.speciality}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Qualifications"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="qualification" name="${prefix}qualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>

    <iais:row cssClass="row">
        <iais:value width="5" cssClass="col-md-5">
            <label class="form-check-label" >Is the medical practitioners authorised by MOH to perform Abortion
                (if No, please upload a copy of the Obstetrics & Gynaecology certificate and
                <a href="${pageContext.request.contextPath}/co-non-hcsa-template-top" style="color:deepskyblue;cursor:pointer;text-decoration: underline;">From 2</a>
                at the Document page)
                <span class="mandatory">*</span>
            </label>
        </iais:value>

        <input type="hidden" class="medAuthByMohVal" name="${prefix}medAuthByMohVal${index}" value="${person.medAuthByMoh}"/>
        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input medAuthByMoh" <c:if test="${'1' == person.medAuthByMoh}">checked="checked"</c:if>  type="radio" name="${prefix}medAuthByMoh${index}" value = "1" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </iais:value>

        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input medAuthByMoh" <c:if test="${'0' == person.medAuthByMoh}">checked="checked"</c:if>  type="radio" name="${prefix}medAuthByMoh${index}" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </iais:value>
    </iais:row>
    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}medAuthByMoh${index}"></span>
        </iais:value>
    </iais:row>
</div>