<c:set var="companyType" value="LICTSUB001" />
<c:set var="individualType" value="LICTSUB002" />
<div class="licensee-com">
    <iais:row cssClass="company-no ${subLicenseeDto.licenseeType == companyType ? '' : 'hidden'}">
        <iais:field width="5" value="UEN No."/>
        <iais:value width="7" display="true">
            <iais:code code="${subLicenseeDto.uenNo}" />
        </iais:value>
    </iais:row>

    <iais:row cssClass="ind-no ${subLicenseeDto.licenseeType == individualType ? '' : 'hidden'}">
        <iais:field width="5" value="ID Type"/>
        <iais:value width="7" display="true">
            <iais:code code="${subLicenseeDto.idType}" />
        </iais:value>
    </iais:row>
    <iais:row cssClass="ind-no ${subLicenseeDto.licenseeType == individualType ? '' : 'hidden'}">
        <iais:field width="5" value="ID No."/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.idNumber}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field value="Licensee Name" width="5"/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.licenseeName}" />
        </iais:value>
    </iais:row>

    <%-- Address start --%>
    <iais:row cssClass="postalCodeDiv">
        <iais:field value="Postal Code " width="5"/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.postalCode}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Address Type" width="5"/>
        <iais:value width="7" display="true">
            <iais:code code="${subLicenseeDto.addrType}" />
        </iais:value>
    </iais:row>
    <iais:row cssClass="address">
        <iais:field value="Block / House No." width="5"/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.blkNo}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Floor / Unit No." width="5"/>
        <iais:value width="7">
            <c:out value="${subLicenseeDto.floorNo}" />
            <c:if test="${not empty subLicenseeDto.floorNo}">-</c:if>
            <c:out value="${subLicenseeDto.unitNo}" />
        </iais:value>
    </iais:row>
    <iais:row cssClass="address">
        <iais:field value="Street Name" width="5"/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.streetName}" />
        </iais:value>
    </iais:row>

    <iais:row cssClass="address">
        <iais:field value="Building Name" width="5"/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.buildingName}" />
        </iais:value>
    </iais:row>
    <%-- Address end --%>

    <iais:row>
        <iais:field value="${subLicenseeDto.licenseeType == companyType ? 'Office Telephone No.' : 'Telephone No.'}" width="5"/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.telephoneNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field value="${subLicenseeDto.licenseeType == companyType ? 'Office' : ''} Email Address" width="5"/>
        <iais:value width="7" display="true">
            <c:out value="${subLicenseeDto.emailAddr}" />
        </iais:value>
    </iais:row>
</div>