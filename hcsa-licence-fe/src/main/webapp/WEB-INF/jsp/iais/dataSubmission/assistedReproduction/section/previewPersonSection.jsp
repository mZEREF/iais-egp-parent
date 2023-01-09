<c:set var="ptHasIdNumber" value="${ptHasIdNumber}"/>
<iais:row>
    <iais:field width="5" value="Name (as per NRIC/FIN/Passport Number)"/>
    <iais:value width="7" display="true">
        <c:out value="${person.name}" />
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" value="Date of Birth"/>
    <iais:value width="7" display="true">
        <c:out value="${person.birthDate}" />
    </iais:value>
</iais:row>
<%--<c:if test="${ptHasIdNumber eq 1}">--%>
<%--</c:if>--%>

<iais:row>
    <iais:field width="5" value="Nationality"/>
    <iais:value width="7" display="true">
        <iais:code code="${person.nationality}" />
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Ethnic Group"/>
    <iais:value width="7" display="true">
        <iais:code code="${person.ethnicGroup}" />
    </iais:value>
</iais:row>