<div class="personnel-content NURSE">
    <input class="not-refresh premTypeValue" type="hidden" name="isUpload" value="true"/>
    <input type="hidden" class="not-refresh " name="${logo}nuCount" value="size"/>
    <input type="hidden" class="not-refresh indexNo" name="${logo}indexNo" value="${appSvcPersonnelDto.indexNo}"/>
    <input type="hidden" class="not-refresh isPartEdit" name="${logo}isPartEdit" value="0"/>
    <input type="hidden" class="not-refresh not-clear nurse" value="0"/>
    <c:set var="isSpeciality" value="${not empty appSvcPersonnelDto.speciality}"/>
    <c:set var="isSubSpeciality" value="${not empty appSvcPersonnelDto.subSpeciality}"/>
    <c:set var="isSpecialityOther" value="${not empty appSvcPersonnelDto.specialityOther}"/>
    <iais:row cssClass="edit-content">
        <c:if test="${canEdit}">
            <div class="text-right app-font-size-16 col-xs-12">
                <a class="edit psnEdit" href="javascript:void(0);">
                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
            </div>
        </c:if>
    </iais:row>
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <strong><c:out value="Nurse"/> <span class="assign-psn-item">${index+1}</span></strong>
        </div>
        <div class="col-xs-12 col-md-6 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" name="isPartEdit" value="0"/>
    <iais:row>
        <iais:value width="10" cssClass="col-md-12 col-xs-12">
            <span class="error-msg" name="iaisErrorMSg" id="error_${logo}personError${index}"></span>
        </iais:value>
    </iais:row>
    <%--    name--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select cssClass="salutation" name="${logo}salutation${index}" firstOption="Please Select"
                         codeCategory="CATE_ID_SALUTATION" value="${appSvcPersonnelDto.salutation}"/>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="100" type="text" cssClass="name" name="${logo}name${index}"
                        value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>

    <%--   Designation --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Designation"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="designation" name="${logo}designation${index}"
                         value="${appSvcPersonnelDto.designation}"
                         options="designationOpList" firstOption="Please Select"/>
        </iais:value>
    </iais:row>



    <iais:row cssClass="${appSvcPersonnelDto.designation=='DES999' ? '' : 'hidden'} otherDesignationDiv">
        <iais:field width="5" cssClass="col-md-5" value=""/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="otherDesignation" name="${logo}otherDesignation${index}"
                        value="${appSvcPersonnelDto.otherDesignation}"/>
        </iais:value>
    </iais:row>


    <%--   Professional Board --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Board"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="professionBoard" name="${logo}professionBoard${index}"
                         codeCategory="CATE_ID_PROFESSION_BOARD"
                         value="${appSvcPersonnelDto.professionBoard}" firstOption="Please Select"/>
        </iais:value>
    </iais:row>

    <%--  Professional Type  --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Type"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="professionType" name="${logo}professionType${index}"
                         codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                         value="${appSvcPersonnelDto.professionType}" firstOption="Please Select"/>
        </iais:value>
    </iais:row>

    <%--    Professional Regn. No--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${logo}profRegNo${index}"
                        value="${appSvcPersonnelDto.profRegNo}"/>
        </iais:value>
    </iais:row>


    <%--    Type of Current Registration--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Current Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi" name="${logo}typeOfCurrRegi${index}"
                        value="${appSvcPersonnelDto.typeOfCurrRegi}"/>
        </iais:value>
    </iais:row>


    <%--   Current Registration Date --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Current Registration Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="currRegiDate" name="${logo}currRegiDate${index}"
                             value="${appSvcPersonnelDto.currRegiDate}"/>
        </iais:value>
    </iais:row>


    <%--    Practicing Certificate End Date--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Practicing Certificate End Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="praCerEndDate" name="${logo}praCerEndDate${index}"
                             value="${appSvcPersonnelDto.praCerEndDate}"/>

        </iais:value>
    </iais:row>

    <%--    Type of Register--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Register"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="50" type="text" cssClass="typeOfRegister" name="${logo}typeOfRegister${index}"
                        value="${appSvcPersonnelDto.typeOfRegister}"/>
        </iais:value>
    </iais:row>

    <%--           Specialty --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Specialty"/>
        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
            <c:out value="${appSvcPersonnelDto.speciality}"/>
        </iais:value>
    </iais:row>

    <%--   Sub-specialty --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Sub-specialty"/>
        <iais:value width="7" cssClass="col-md-7 subSpeciality" display="true">
            <c:out value="${appSvcPersonnelDto.subSpeciality}"/>
        </iais:value>
    </iais:row>

    <%--   Other Specialties --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Other Specialties"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="specialityOther nurseSpecial" name="${logo}specialityOther${index}"
                        value="${appSvcPersonnelDto.specialityOther}"/>
        </iais:value>
    </iais:row>

    <%--  Date when specialty was gotten  --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5 specialtyGetDateLabel" value="Date when specialty was obtained"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="specialtyGetDate" name="${logo}specialtyGetDate${index}"
                             value="${appSvcPersonnelDto.specialtyGetDate}"/>
        </iais:value>
    </iais:row>


    <%--    Qualification--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Qualification"/>
        <iais:value width="7" cssClass="col-md-7 qualification" display="true">
            <c:out value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>


    <%--           Relevant working experience(Years) --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Relevant working experience (Years)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${logo}wrkExpYear${index}"
                        value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>


    <%--          Expiry Date (BCLS and AED)  --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Expiry Date (BCLS and AED)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="bclsExpiryDate" name="${logo}bclsExpiryDate${index}"
                             value="${appSvcPersonnelDto.bclsExpiryDate}"/>
        </iais:value>
    </iais:row>


    <%--   Expiry Date (CPR) --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Expiry Date (CPR)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="cprExpiryDate" name="${logo}cprExpiryDate${index}"
                             value="${appSvcPersonnelDto.cprExpiryDate}"/>
        </iais:value>
    </iais:row>

    <%--下载组件--%>
</div>



