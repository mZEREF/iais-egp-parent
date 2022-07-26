<div class="MedAlertPerson-detail">
    <iais:row>
        <iais:field width="5" value="Salutation"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcMedAlertPerson.salutation}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcMedAlertPerson.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="ID Type"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcMedAlertPerson.idType}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="ID No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcMedAlertPerson.idNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Mobile No."/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcMedAlertPerson.mobileNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Email Address"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcMedAlertPerson.emailAddr}" />
        </iais:value>
    </iais:row>
</div>