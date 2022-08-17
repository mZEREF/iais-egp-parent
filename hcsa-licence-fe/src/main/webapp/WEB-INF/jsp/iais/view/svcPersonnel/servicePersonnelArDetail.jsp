<div class="personnel-content">
    <iais:row>
        <iais:field width="5" mandatory="true" value="Name"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Designation"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.designation}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.profRegNo}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Type of Current Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.typeOfCurrRegi}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Current Registration Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.currRegiDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Practicing Certificate End Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.praCerEndDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Type of Register"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.typeOfRegister}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Specialty"/>
        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
            <c:out value="${appSvcPersonnelDto.speciality}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Date when specialty was obtained"/>
        <iais:value width="7" cssClass="col-md-7">
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
</div>



