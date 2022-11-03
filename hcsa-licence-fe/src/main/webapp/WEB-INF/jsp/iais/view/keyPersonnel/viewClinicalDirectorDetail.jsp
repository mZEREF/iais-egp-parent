<div class="person-detail">
    <iais:row>
        <iais:field width="5" value="Professional Board"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${person.professionBoard}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Professional Regn. No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.profRegNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Salutation"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${person.salutation}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="ID Type"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${person.idType}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="ID No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.idNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Designation"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${person.designation}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Specialty"/>
        <iais:value width="7" cssClass="col-md-7 speciality" display="true">
            <c:out value="${person.speciality}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Date when specialty was obtained"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${person.specialtyGetDateStr}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Type of Current Registration"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.typeOfCurrRegi}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Current Registration Date"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.currRegiDateStr}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Practicing Certificate End Date"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.praCerEndDateStr}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Type of Register"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.typeOfRegister}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Relevant Experience"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${person.relevantExperience}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="false" value="Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services ('EMS') Medical Directors workshop&nbsp;"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:if test="${'1' == person.holdCerByEMS}">Yes</c:if>
        </iais:value>

        <iais:value width="3" cssClass="form-check col-md-3">
            <c:if test="${'0' == person.holdCerByEMS}">No</c:if>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Expiry Date (ACLS)"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.aclsExpiryDateStr}" />
        </iais:value>
    </iais:row>

    <c:if test="${'MTS' == currentSvcCode}">
        <iais:row>
            <iais:field width="5" value="Expiry Date (BCLS and AED)"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${person.bclsExpiryDateStr}" />
            </iais:value>
        </iais:row>
    </c:if>

    <iais:row>
        <iais:field width="5" value="Mobile No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.mobileNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Email Address"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.emailAddr}" />
        </iais:value>
    </iais:row>
</div>