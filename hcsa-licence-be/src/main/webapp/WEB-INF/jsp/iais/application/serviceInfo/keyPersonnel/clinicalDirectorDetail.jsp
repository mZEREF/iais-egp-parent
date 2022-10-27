<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="isCd" value="${pcdType == ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR}"/>
<div class="person-content">
    <input type="hidden" class="not-refresh prepsn" name="${psnContent}" value="${prepsn}"/>
    <input type="hidden" class="not-refresh assignSelVal" name="${prepsn}assignSelVal" value="${person.assignSelect}"/>
    <input type="hidden" class="not-refresh licPerson" name="${prepsn}licPerson" value="${person.licPerson ? 1 : 0}"/>
    <input type="hidden" class="not-refresh isPartEdit" name="${prepsn}isPartEdit" value="0"/>
    <input type="hidden" class="not-refresh indexNo" name="${prepsn}indexNo" value="${person.indexNo}"/>
    <input type="hidden" class="not-refresh psnEditField" name="${prepsn}psnEditField" value="<c:out value="${person.psnEditFieldStr}" />"/>
    <%--<input type="hidden" class="not-refresh" name="existingPsn" value="0"/>--%>
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
        <p class="bold">${singleName} <span class="psnHeader">${index+1}</span></p>
        <p><span class="error-msg" name="iaisErrorMSg" id="error_${prepsn}personError${index}"></span></p>
    </div>
    <div class="col-xs-12 col-md-5 text-right removeBtn removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
        <h4 class="text-danger">
            <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
        </h4>
    </div>
    </iais:row>

    <c:if test="${isRfc || isRenew || isRfi}">
    <iais:row>
    <div class="col-sm-10">
        <label class="control-font-label">
            <c:if test="${!empty person.name && !empty person.idNo && !empty person.idType}">
                ${person.name}, ${person.idNo} (<iais:code code="${person.idType}"/>)
            </c:if>
        </label>
    </div>
    </iais:row>
    </c:if>

    <iais:row cssClass="assignSelDiv ${canEdit && '-1' != person.assignSelect && not empty person.assignSelect ? 'hidden':''}">
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Assign a ${singleName} Person"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="assignSel" name="${prepsn}assignSelect${index}" options="personSelectOpts" value="${person.assignSelect}"/>
        </iais:value>
    </iais:row>
    <div class="person-detail">
        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Board"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select cssClass="professionBoard" name="${prepsn}professionBoard${index}" codeCategory="CATE_ID_PROFESSION_BOARD"
                             value="${person.professionBoard}" firstOption="Please Select"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Regn No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prepsn}profRegNo${index}" value="${person.profRegNo}"/>
            </iais:value>
        </iais:row>

        <c:if test="${'MTS' == currentSvcCode}">
            <iais:row>
                <iais:field width="5" cssClass="col-md-5" mandatory="false" value="Not registered with a Professional Board"/>
                <div class="form-check col-md-3">
                    <input class="form-check-input" name="${prepsn}noRegWithProfBoard${index}" value="1"
                           type="checkbox" aria-invalid="false"
                           <c:if test="${'1' == person.noRegWithProfBoard}">checked="checked"</c:if> />
                    <label class="form-check-label">
                        <span class="check-square"></span><c:out value=""/>
                    </label>
                </div>
            </iais:row>
        </c:if>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name"/>
            <iais:value width="3" cssClass="col-md-3">
                <iais:select cssClass="salutation" name="${prepsn}salutation${index}" firstOption="Please Select"
                             codeCategory="CATE_ID_SALUTATION" value="${person.salutation}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <iais:input maxLength="66" type="text" cssClass="name" name="${prepsn}name${index}" value="${person.name}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="ID No."/>
            <iais:value width="3" cssClass="col-md-3">
                <iais:select name="${prepsn}idType${index}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE" value="${person.idType}"
                             cssClass="idType" onchange="toggleOnVal(this, 'IDTYPE003', '.nationalityDiv')"/>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <iais:input maxLength="20" type="text" cssClass="idNo" name="${prepsn}idNo${index}" value="${person.idNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Designation"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select cssClass="designation" name="${prepsn}designation${index}" value="${person.designation}"
                             options="designationOpList" firstOption="Please Select"
                             onchange="toggleOnVal(this, 'DES999', '.otheDesignationDiv');"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" value="Specialty"/>
            <iais:value width="7" cssClass="col-md-7 speciality" display="true">
                <c:out value="${person.speciality}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Date when specialty was obtained"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker cssClass="specialtyGetDate field-date" name="${prepsn}specialtyGetDate${index}"
                                 value="${person.specialtyGetDateStr}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Registration Date"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi" name="${prepsn}typeOfCurrRegi${index}"
                            value="${person.typeOfCurrRegi}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Current Registration Date"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker cssClass="currRegiDate field-date" name="${prepsn}currRegiDate${index}" value="${person.currRegiDateStr}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Practicing Certificate End Date"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker cssClass="praCerEndDate field-date" name="${prepsn}praCerEndDate${index}" value="${person.praCerEndDateStr}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Register"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="50" type="text" cssClass="typeOfRegister" name="${prepsn}typeOfRegister${index}"
                            value="${person.typeOfRegister}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5 relevantExperienceLabel" mandatory="false" value="Relevant Experience"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="180" type="text" cssClass="relevantExperience" name="${perfix}relevantExperience${index}" value="${person.typeOfRegister}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="${isCd ? 'true' : 'false'}" value="Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services (\"EMS\") Medical Directors workshop"/>
            <input type="hidden" class="holdCerByEMSVal" name="holdCerByEMSVal${index}" value="${person.holdCerByEMS}"/>
            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input holdCerByEMS" <c:if test="${'1' == person.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="${perfix}holdCerByEMS${index}" value = "1" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
            </iais:value>

            <iais:value width="3" cssClass="form-check col-md-3">
                <input class="form-check-input holdCerByEMS" <c:if test="${'0' == person.holdCerByEMS}">checked="checked"</c:if>  type="radio" name="${perfix}holdCerByEMS${index}" value = "0" aria-invalid="false">
                <label class="form-check-label" ><span class="check-circle"></span>No</label>
            </iais:value>
        </iais:row>

        <iais:row cssClass="row control control-caption-horizontal">
            <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
            <iais:value width="7" cssClass="col-md-7 col-xs-12">
                <span class="error-msg" name="iaisErrorMSg" id="${prepsn}error_holdCerByEMS${index}"></span>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="${isCd ? 'true' : 'false'}" value="Expiry Date (ACLS)"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker cssClass="aclsExpiryDate" name="${perfix}aclsExpiryDate${index}" value="${person.aclsExpiryDateStr}"/>
            </iais:value>
        </iais:row>

        <c:if test="${'MTS' == currentSvcCode}">
            <iais:row>
                <iais:field width="5" cssClass="col-md-5" mandatory="${isCd ? 'true' : 'false'}" value="Expiry Date (BCLS and AED)"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:datePicker cssClass="bclsExpiryDate" name="${perfix}bclsExpiryDate${index}" value="${person.bclsExpiryDateStr}"/>
                </iais:value>
            </iais:row>
        </c:if>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Mobile No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="8" type="text" cssClass="mobileNo" name="${prepsn}mobileNo${index}" value="${person.mobileNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Email Address"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="320" type="text" cssClass="emailAddr" name="${prepsn}emailAddr${index}" value="${person.emailAddr}"/>
            </iais:value>
        </iais:row>
    </div>
    <hr/>
</div>
