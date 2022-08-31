<%--    大标题,不动--%>
<div class="personnel-content">
    <input type="hidden" class="not-refresh " name="${logo}arCount" value="size"/>
    <iais:row cssClass="personnel-header">
        <iais:value width="5" cssClass="col-xs-12 col-md-6">
            <strong>
                <c:out value="AR Practitioner "/>
                <label class="assign-psn-item">${index+1}</label>
            </strong>
        </iais:value>
        <iais:value width="7" cssClass="col-xs-12 col-md-4 text-right">
            <div class="removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
                <h4 class="text-danger">
                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                </h4>
            </div>
        </iais:value>
    </iais:row>
    <input type="hidden" name="isPartEdit" value="0"/>
    <iais:row>
        <iais:value width="10" cssClass="col-md-10 col-xs-12">
            <span class="error-msg" name="iaisErrorMSg" id="error_personError${index}"></span>
        </iais:value>
    </iais:row>
    <%--        name--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Name"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" cssClass="name" name="${logo}name${index}"
                        value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>
    <%--   Designation --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Designation"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="designation" name="${logo}designation${index}"
                         value="${appSvcPersonnelDto.designation}" options="designationOpList"
                         firstOption="Please Select"/>
        </iais:value>
    </iais:row>
    <%--    Professional Regn. No--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">

            <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${logo}profRegNo${index}"
                        value="${appSvcPersonnelDto.profRegNo}"/>
        </iais:value>
    </iais:row>
    <%--    Type of Current Registration--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Type of Current Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="50" type="text" cssClass="typeOfCurrRegi" name="${logo}typeOfCurrRegi${index}"
                        value="${appSvcPersonnelDto.typeOfCurrRegi}"/>
        </iais:value>
    </iais:row>
    <%--   Current Registration Date --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Current Registration Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="currRegiDate field-date" name="${logo}currRegiDate${index}"
                             value="${appSvcPersonnelDto.currRegiDate}"/>
        </iais:value>
    </iais:row>
    <%--    Practicing Certificate End Date--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Practicing Certificate End Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="praCerEndDate field-date" name="${logo}praCerEndDate${index}"
                             value="${appSvcPersonnelDto.praCerEndDate}"/>
        </iais:value>
    </iais:row>
    <%--    Type of Register--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Type of Register"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="50" type="text" cssClass="typeOfRegister" name="${logo}typeOfRegister${index}"
                        value="${appSvcPersonnelDto.typeOfRegister}"/>
        </iais:value>
    </iais:row>
    <%--           Specialty --%>
    <iais:row>
        <iais:field width="5" value="Specialty"/>
        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
            <c:out value="${appSvcPersonnelDto.speciality}"/>
        </iais:value>
    </iais:row>
    <%--            Date when specialty was obtained--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Date when specialty was obtained"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="specialtyGetDate field-date" name="${logo}specialtyGetDate${index}"
                             value="${appSvcPersonnelDto.specialtyGetDate}"/>
        </iais:value>
    </iais:row>
    <%--         qualification   --%>
    <iais:row>
        <iais:field width="5" value="Qualification"/>
        <iais:value width="7" cssClass="col-md-7 qualification" display="true">
            <c:out value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>
    <%--           Relevant working experience(Years) --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Relevant working experience(Years)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${logo}wrkExpYear${index}"
                        value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
    <%--          Expiry Date (BCLS and AED)  --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Expiry Date (BCLS and AED)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="bclsExpiryDate" name="${logo}bclsExpiryDate${index}"
                             value="${appSvcPersonnelDto.bclsExpiryDate}"/>
        </iais:value>
    </iais:row>
</div>


