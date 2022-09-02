<div class="personnel-content">
    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="4" cssClass="col-md-4" display="true">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Salutation"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcPersonnelDto.salutation}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Designation"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcPersonnelDto.designation}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Professional Board"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcPersonnelDto.professionBoard}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Professional Type"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcPersonnelDto.professionType}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.profRegNo}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Type of Current Registration"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.typeOfCurrRegi}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Current Registration Date"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.currRegiDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Practicing Certificate End Date"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.praCerEndDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Type of Register"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.typeOfRegister}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Specialty"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.speciality}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Sub-Specialty"/>
        <iais:value width="7" cssClass="col-md-7 subSpeciality" display="true">
            <c:out value="${appSvcPersonnelDto.subSpeciality}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Other Specialties"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.specialityOther}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date when specialty was obtained"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.specialtyGetDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Qualification"/>
        <iais:value width="7" cssClass="col-md-7 qualification" display="true">
            <c:out value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relevant working experience(Years)"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Expiry Date (BCLS and AED)"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.bclsExpiryDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Expiry Date(CPR)"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.cprExpiryDate}"/>
        </iais:value>
    </iais:row>
</div>



