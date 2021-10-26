<iais:row>
    <iais:field width="5" value="Name (as per NRIC/Passport)" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="66" type="text" name="name${suffix}" id="name${suffix}" value="${person.name}"/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="ID No." mandatory="true"/>
    <iais:value width="3" cssClass="col-md-3">
        <iais:select name="idType${suffix}" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${person.idType}"
                     cssClass="idTypeSel${suffix}"/>
    </iais:value>
    <iais:value width="4" cssClass="col-md-4">
        <iais:input maxLength="20" type="text" name="idNumber${suffix}" value="${person.idNumber}" />
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Date of Birth" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:datePicker name="birthDate${suffix}" value="${person.birthDate}"/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Nationality" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="nationality${suffix}" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY" value="${person.nationality}"
                     cssClass="nationalitySel"/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Ethnic Group" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="ethnicGroup${suffix}" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP" value="${person.ethnicGroup}"
                     cssClass="ethnicGroupSel" onchange="toggleOnSelect(this, 'ETHG005', 'ethnicGroupOtherDiv${suffix}')"/>
    </iais:value>
</iais:row>
<div class="form-group" id="ethnicGroupOtherDiv${suffix}" style="<c:if test="${person.ethnicGroup ne 'ETHG005'}">'display:none'</c:if>">
    <iais:field width="5" value="Ethnic Group (Others)" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="66" type="text" name="ethnicGroupOther${suffix}" id="ethnicGroupOther${suffix}" value="${person.ethnicGroupOther}"/>
    </iais:value>
</div>