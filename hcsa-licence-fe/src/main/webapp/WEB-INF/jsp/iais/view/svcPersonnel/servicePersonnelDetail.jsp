<div class="personnel-content">
    <iais:row>
        <iais:field width="5" mandatory="true" value="Select Service Personnel"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.personnelType}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Professional Board"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>
    <c:if test="${appSvcPersonnelDto.personnelType == 'SPPT001'}||${appSvcPersonnelDto.personnelType == 'SPPT002'}">
        <iais:row>
            <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <c:out value="${appSvcPersonnelDto.qualification}"/>
            </iais:value>
        </iais:row>
    </c:if>
    <c:if test="${appSvcPersonnelDto.personnelType == 'SPPT004'}">
        <iais:row>
            <iais:field width="5" mandatory="true" value="Professional Regn. No. " cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <c:out value="${appSvcPersonnelDto.profRegNo}"/>
            </iais:value>
        </iais:row>
    </c:if>
    <c:if test="${appSvcPersonnelDto.personnelType == 'SPPT001'} || ${appSvcPersonnelDto.personnelType == 'SPPT002'}">
        <iais:row>
            <iais:field width="5" mandatory="true" value="Relevant working experience (Years)" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
            </iais:value>
        </iais:row>
    </c:if>

</div>
