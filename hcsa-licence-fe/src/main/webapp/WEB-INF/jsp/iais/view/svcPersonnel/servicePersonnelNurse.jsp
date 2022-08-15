
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <strong>
                <c:out value="Nurse"/>
                <label class="assign-psn-item">${index+1}</label>
            </strong>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Name"/>
        <iais:value width="3" cssClass="col-md-3">
            <c:out value="${appSvcPersonnelDto.salutation}"/>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>

    <%--   Designation --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Designation"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.designation}"/>
        </iais:value>
    </iais:row>

    <%--   Professional Board --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Professional Board"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.professionBoard}"/>
        </iais:value>
    </iais:row>

    <%--  Professional Type  --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Professional Type"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.professionType}"/>
        </iais:value>
    </iais:row>

    <%--    Professional Regn. No--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.profRegNo}"/>
        </iais:value>
    </iais:row>


    <%--    Type of Current Registration--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Type of Current Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.typeOfCurrRegi}"/>
        </iais:value>
    </iais:row>


    <%--   Current Registration Date --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Current Registration Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.currRegiDate}"/>
        </iais:value>
    </iais:row>


    <%--    Practicing Certificate End Date--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Practicing Certificate End Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.praCerEndDate}"/>
        </iais:value>
    </iais:row>

    <%--    Type of Register--%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Type of Register"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.typeOfRegister}"/>
        </iais:value>
    </iais:row>

    <%--           Specialty --%>
    <iais:row>
        <iais:field width="5" value="Specialty"/>
        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
            <c:out value="${appSvcPersonnelDto.speciality}"/>
        </iais:value>
    </iais:row>

    <%--   Sub-Specialty --%>
    <iais:row>
        <iais:field width="5" value="Sub-Specialty"/>
        <iais:value width="7" cssClass="col-md-7 subSpeciality" display="true">
            <c:out value="${appSvcPersonnelDto.subSpeciality}"/>
        </iais:value>
    </iais:row>

    <%--   Other Specialties --%>
    <iais:row>
        <iais:field width="5" value="Other Specialties"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.specialityOther}"/>
        </iais:value>
    </iais:row>

    <%--  Date when specialty was obtained  --%>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Date when specialty was obtained"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.specialtyGetDate}"/>
        </iais:value>
    </iais:row>


    <%--    Qualification--%>
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
            <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Expiry Date (BCLS and AED)"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.bclsExpiryDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Expiry Date(CPR)"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.cprExpiryDate}"/>
        </iais:value>
    </iais:row>
</div>



