<c:set var="ptHasIdNumber" value="${ptHasIdNumber}"/>
<iais:row>
    <iais:field width="5" value="ID Type"/>
    <iais:value width="7" display="true">
        <iais:code code="${person.idType}" />
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" value="ID No."/>
    <iais:value width="7" display="true">
        <c:out value="${person.idNumber}" />
    </iais:value>
</iais:row>

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