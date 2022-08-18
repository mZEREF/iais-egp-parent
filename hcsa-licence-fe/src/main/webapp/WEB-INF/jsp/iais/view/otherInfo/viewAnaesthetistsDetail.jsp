<div class="person-detail anaesthetists">
    <input type="hidden" name="apsnType" value="anaesthetists">
    <iais:row>
        <iais:field width="5" value="Professional Regn. No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.profRegNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="NRIC/FIN No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.idNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Type of Registration"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.regType}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Name of anaesthetists"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Qualifications"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.qualification}" />
        </iais:value>
    </iais:row>
</div>