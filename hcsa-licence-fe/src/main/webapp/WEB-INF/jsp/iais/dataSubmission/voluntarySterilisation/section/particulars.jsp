<iais:row>
    <iais:field width="5" value="Name Of Person" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="66" type="text" name="guardianName" value="" />
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="ID No." mandatory="true"/>
    <iais:value width="3" cssClass="col-md-3">
        <iais:select name="guardianIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                     value="" />
    </iais:value>
    <iais:value width="4" cssClass="col-md-4">
        <iais:input maxLength="15" type="text" name="guardianIdNo" value="" />
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Date of Birth" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:datePicker id="birthDate" name="guardianBirthday" value=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Relationship to Person Who Was Sterilized" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="guardianRelationship" firstOption="Please Select" codeCategory=""
                     value=""/>
    </iais:value>
</iais:row>