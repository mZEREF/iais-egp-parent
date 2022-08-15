<iais:row>
    <iais:value width="5" cssClass="col-xs-12 col-md-6">
        <strong>
            <c:out value="Service Personnel "/>
            <label class="assign-psn-item">${index+1}</label>
        </strong>
    </iais:value>
</iais:row>
<iais:field width="5" mandatory="true" value="Select Service Personnel" cssClass="col-sm-5"/>
<iais:value width="7" cssClass="col-sm-5 col-md-7">
    <c:out value="${appSvcPersonnelDto.personnelType}"/>
</iais:value>


<%--        name--%>
<iais:row>
    <iais:field width="5" mandatory="true" value="Name" cssClass="col-sm-5"/>
    <iais:value width="7" cssClass="col-sm-5 col-md-7">
        <c:out value="${appSvcPersonnelDto.name}"/>
    </iais:value>
</iais:row>
<%--   designation --%>
<c:if test="${appSvcPersonnelDto.personnelType == 'SPPT001'}? '':'hidden'">
<iais:row cssClass="${appSvcPersonnelDto.personnelType == 'SPPT001'}||">
    <iais:field width="5" mandatory="true" value="designation" cssClass="col-sm-5"/>
    <iais:value width="7" cssClass="col-sm-5 col-md-7">
        <c:out value="${appSvcPersonnelDto.designation}"/>
    </iais:value>
</iais:row>
</c:if>

<iais:row cssClass="${appSvcPersonnelDto.personnelType == 'SPPT001'}? '':'hidden'">
    <iais:field width="5" mandatory="true" value="otherDesignation" cssClass="col-sm-5"/>
    <iais:value width="7" cssClass="col-sm-5 col-md-7">
        <c:out value="${appSvcPersonnelDto.otherDesignation}"/>
    </iais:value>
</iais:row>

<%--    qualification--%>
<iais:row cssClass="${appSvcPersonnelDto.personnelType == 'SPPT001'}||${appSvcPersonnelDto.personnelType == 'SPPT002'}?'':'hidden'">
    <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5"/>
    <iais:value width="7" cssClass="col-sm-5 col-md-7">
        <c:out value="${appSvcPersonnelDto.qualification}"/>
    </iais:value>
</iais:row>

<%--    regnNo--%>
<iais:row cssClass="${appSvcPersonnelDto.personnelType == 'SPPT004'}?'':'hidden'">
    <iais:field width="5" mandatory="true" value="Professional Regn. No. " cssClass="col-sm-5"/>
    <iais:value width="7" cssClass="col-sm-5 col-md-7">
        <c:out value="${appSvcPersonnelDto.regnNo}"/>
    </iais:value>
</iais:row>

<%--    years--%>
<iais:row cssClass="${appSvcPersonnelDto.personnelType == 'SPPT001'}||${appSvcPersonnelDto.personnelType == 'SPPT002'}?'':'hidden'">
    <iais:field width="5" mandatory="true" value="Relevant working experience (Years)" cssClass="col-sm-5"/>
    <iais:value width="7" cssClass="col-sm-5 col-md-7">
        <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
    </iais:value>
</iais:row>
