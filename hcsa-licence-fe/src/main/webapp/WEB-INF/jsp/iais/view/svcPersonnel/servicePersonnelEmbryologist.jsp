<iais:row>
    <iais:value width="5" cssClass="col-xs-12 col-md-6">
        <strong>
            <c:out value="Embryologist "/>
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


<%--         qualification   --%>
<iais:row>
    <iais:field width="5" value="Qualification"/>
    <iais:value width="7" cssClass="col-md-7">
        <c:out value="${appSvcPersonnelDto.qualification}"/>
    </iais:value>
</iais:row>


<%--           Relevant working experience(Years) --%>
<iais:row>
    <iais:field width="5" mandatory="true" value="Working Experience(in term of years)"/>
    <iais:value width="7" cssClass="col-md-7">
        <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
    </iais:value>
</iais:row>


<%--  Number of AR procedures done under supervision  --%>
<iais:row>
    <iais:field width="5" mandatory="true" value="Number of AR procedures done under supervision"/>
    <iais:value width="7" cssClass="col-md-7">
        <c:out value="${appSvcPersonnelDto.numberSupervision}"/>
    </iais:value>
</iais:row>


<iais:row>
    <iais:field width="5" value="Is the Embryologist authorized?"/>
    <iais:value width="7" display="true">
        <c:choose>
            <c:when test="${appSvcPersonnelDto.locateWtihHcsa == '1'}">Yes</c:when>
            <c:when test="${appSvcPersonnelDto.locateWtihHcsa == '0'}">No</c:when>
        </c:choose>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Is the Embryologist authorized?"/>
    <iais:value width="7" display="true">
        <c:choose>
            <c:when test="${appSvcPersonnelDto.locateWtihNonHcsa == '1'}">Yes</c:when>
            <c:when test="${appSvcPersonnelDto.locateWtihNonHcsa == '0'}">No</c:when>
        </c:choose>
    </iais:value>
</iais:row>



