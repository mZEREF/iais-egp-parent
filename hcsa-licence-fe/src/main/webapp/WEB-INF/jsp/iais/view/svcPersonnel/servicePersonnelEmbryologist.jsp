<div class="personnel-content">
<iais:row>
    <iais:field width="5" mandatory="true" value="Name"/>
    <iais:value width="3" cssClass="col-md-3">
        <c:out value="${appSvcPersonnelDto.salutation}"/>
    </iais:value>
    <iais:value width="4" cssClass="col-md-4">
        <c:out value="${appSvcPersonnelDto.name}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" value="Qualification"/>
    <iais:value width="7" cssClass="col-md-7">
        <c:out value="${appSvcPersonnelDto.qualification}"/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" mandatory="true" value="Working Experience(in term of years)"/>
    <iais:value width="7" cssClass="col-md-7">
        <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" mandatory="true" value="Number of AR procedures done under supervision"/>
    <iais:value width="7" cssClass="col-md-7">
        <c:out value="${appSvcPersonnelDto.numberSupervision}"/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Is the Embryologist authorized?"/>
    <iais:value width="7">
        <c:if test="${appSvcPersonnelDto.locateWtihHcsa == '1'}">
            Yes
        </c:if>
        <c:if test="${appSvcPersonnelDto.locateWtihHcsa == '0'}">
            NO
        </c:if>
    </iais:value>
</iais:row>
</div>



