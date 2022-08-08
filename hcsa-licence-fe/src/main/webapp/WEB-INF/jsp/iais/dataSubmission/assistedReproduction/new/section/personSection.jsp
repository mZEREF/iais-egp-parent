<iais:row>
    <iais:field width="5" value="Name (as per NRIC/FIN/Passport)" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="66" type="text" name="name${suffix}" id="name${suffix}" value="${person.name}"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_name${suffix}"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Date of Birth" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:datePicker name="birthDate${suffix}" value="${person.birthDate}"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_birthDate${suffix}"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Nationality" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="nationality${suffix}" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY" value="${person.nationality}"
                     cssClass="nationalitySel" onchange="checkMantory(this, '#ethnicGroup${suffix}Label', 'NAT0001')"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_nationality${suffix}"></span>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Ethnic Group" id="ethnicGroup${suffix}Label" mandatory="${person.nationality eq 'NAT0001' ? true : false}"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="ethnicGroup${suffix}" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP" value="${person.ethnicGroup}"
                     cssClass="ethnicGroupSel" onchange="toggleOnSelect(this, 'ETHG005', 'ethnicGroupOtherDiv${suffix}')"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_ethnicGroup${suffix}"></span>
    </iais:value>
</iais:row>
<div class="form-group" id="ethnicGroupOtherDiv${suffix}" style="<c:if test="${person.ethnicGroup ne 'ETHG005'}">display:none</c:if>">
    <iais:field width="5" value="Ethnic Group (Others)" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" name="ethnicGroupOther${suffix}" id="ethnicGroupOther${suffix}"
                    value="${person.ethnicGroupOther}"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_ethnicGroupOther${suffix}"></span>
    </iais:value>
</div>