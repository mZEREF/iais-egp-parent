<div class="personnel-content">
    <c:set var="isNIC" value="${psnType == ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE}"/>
    <input type="hidden" class="not-refresh prepsn" name="${psnContent}" value="${prefix}"/>
    <input type="hidden" class="not-refresh specialPerson" value="1"/>
    <input type="hidden" class="indexNo" name="${prefix}indexNo${index}" value="${appSvcPersonnelDto.indexNo}"/>
    <input type="hidden" class="isPartEdit" name="${prefix}isPartEdit${index}" value="0"/>
    <iais:row>
        <div class="col-md-12 col-xs-12 edit-content">
            <c:if test="${'true' == canEdit}">
                <input type="hidden" class="isPartEdit" name="${status.index}isPartEdit${index}" value="0"/>
                <div class="text-right app-font-size-16">
                    <a class="edit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </iais:row>
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">${title} <label class="assign-psn-item"><strong>${index+1}</strong></label></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_${prefix}personError${index}"></span></p>
        </div>
        <div class="col-xs-12 col-md-6 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtns cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <input type="hidden" name="isPartEdit" value="0"/>
    <%--    name--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select cssClass="salutation" name="${prefix}salutation${index}" firstOption="Please Select" codeCategory="CATE_ID_SALUTATION" value="${appSvcPersonnelDto.salutation}"/>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="100" type="text" cssClass="name" name="${prefix}name${index}" value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>

    <%--   Designation --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Designation"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="designation" name="${prefix}designation${index}" value="${appSvcPersonnelDto.designation}"
                         options="designationOpList" firstOption="Please Select"
                         onchange="toggleOther(this, 'DES999', '.otheDesignationDiv');"/>
        </iais:value>
    </iais:row>

    <iais:row cssClass="${appSvcPersonnelDto.designation=='DES999' ? '' : 'hidden'} otheDesignationDiv">
        <iais:field width="5" cssClass="col-md-5" value=""/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="otherDesignation" name="${prefix}otherDesignation${index}"
                        value="${appSvcPersonnelDto.otherDesignation}"/>
        </iais:value>
    </iais:row>

    <%--   Professional Board --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Board"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="professionBoard" name="${prefix}professionBoard${index}"
                         codeCategory="CATE_ID_PROFESSION_BOARD"
                         value="${appSvcPersonnelDto.professionBoard}" firstOption="Please Select"/>
        </iais:value>
    </iais:row>

    <%--  Professional Type  --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Type"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="professionType" name="${prefix}professionType${index}"
                         codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                         value="${appSvcPersonnelDto.professionType}" firstOption="Please Select"/>
        </iais:value>
    </iais:row>

    <%--    Professional Regn. No--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prefix}profRegNo${index}"
                        value="${appSvcPersonnelDto.profRegNo}"/>
        </iais:value>
    </iais:row>

    <%--    Type of Current Registration--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Current Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi" name="${prefix}typeOfCurrRegi${index}"
                        value="${appSvcPersonnelDto.typeOfCurrRegi}"/>
        </iais:value>
    </iais:row>

    <%--   Current Registration Date --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Current Registration Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="currRegiDate" name="${prefix}currRegiDate${index}"
                             value="${appSvcPersonnelDto.currRegiDate}"/>
        </iais:value>
    </iais:row>

    <%--    Practicing Certificate End Date--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Practicing Certificate End Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="praCerEndDate" name="${prefix}praCerEndDate${index}"
                             value="${appSvcPersonnelDto.praCerEndDate}"/>

        </iais:value>
    </iais:row>

    <%--    Type of Register--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Register"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="50" type="text" cssClass="typeOfRegister" name="${prefix}typeOfRegister${index}"
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
            <iais:input maxLength="100" type="text" cssClass="specialityOther" name="${prefix}specialityOther${index}"
                        value="${appSvcPersonnelDto.specialityOther}"/>
        </iais:value>
    </iais:row>

    <%--  Date when specialty was obtained  --%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Date when specialty was obtained"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="specialtyGetDate" name="${prefix}specialtyGetDate${index}"
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
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${prefix}wrkExpYear${index}"
                        value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
    <hr/>
</div>
