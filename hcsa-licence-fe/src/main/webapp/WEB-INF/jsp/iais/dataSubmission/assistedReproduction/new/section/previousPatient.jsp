<iais:row>
    <iais:field width="5" value="ID No." mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" name="preIdNumber" id="preIdNumber"
                    value="${previous.idNumber}" />
        <span class="error-msg" name="iaisErrorMsg" id="error_preIdNumber"></span>

    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Nationality" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="preNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY" value="${previous.nationality}"
                     cssClass="nationalitySel"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_preNationality"></span>

    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Name" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" name="preName" id="preName"
                    value="${previous.name}"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_preName"></span>
    </iais:value>
</iais:row>