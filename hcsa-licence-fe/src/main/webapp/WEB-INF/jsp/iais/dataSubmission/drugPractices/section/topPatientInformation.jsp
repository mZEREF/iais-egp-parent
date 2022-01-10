<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<iais:row>
    <iais:field width="5" value="Name of Patient" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="66" type="text" name="name" value="${patientDto.name}" />
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="ID No." mandatory="true"/>
    <iais:value width="3" cssClass="col-md-3">
        <iais:select name="" firstOption="Please Select" codeCategory=""
                     value="" cssClass=""/>
    </iais:value>
    <iais:value width="4" cssClass="col-md-4">
        <iais:input maxLength="15" type="text" name="" value="" />
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Date of Birth" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:datePicker id="" name="" value=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Nationality" mandatory="false"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="" firstOption="Please Select" codeCategory=""
                     value="" cssClass=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Date Commenced Residence In Singapore" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:datePicker name="" value=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Residence Status" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select cssClass=""  name="" firstOption="Please Select" codeCategory="" value=""/>
    </iais:value>
</iais:row>


<iais:row>
    <iais:field width="5" value="Ethnic Group" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select cssClass=""  name="" firstOption="Please Select" codeCategory="" value=""/>
    </iais:value>
</iais:row>
<div c:if="">
    <iais:row>
        <iais:field width="5" value="Other Ethnic Group" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="200" type="text" name="" value=""/>
        </iais:value>
    </iais:row>
</div>
<iais:row>
    <iais:field width="5" value="Marital Status" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select cssClass=""  name="" firstOption="Please Select" codeCategory="" value=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Education Level" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select cssClass=""  name="" firstOption="Please Select" codeCategory="" value=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="No. of Living Children" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="2" type="text" name="" value=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Activity Status" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select cssClass=""  name="" firstOption="Please Select" codeCategory="" value=""/>
    </iais:value>
</iais:row>
<iais:row>
    <iais:field width="5" value="Occupation" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select cssClass=""  name="" firstOption="Please Select" codeCategory="" value=""/>
    </iais:value>
</iais:row>
<div c:if="">
    <iais:row>
        <iais:field width="5" value="Other Occupation" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="" value=""/>
        </iais:value>
    </iais:row>
</div>


