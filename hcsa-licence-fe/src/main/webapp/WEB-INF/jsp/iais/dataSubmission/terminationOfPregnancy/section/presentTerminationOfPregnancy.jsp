<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="terminationDto" value="${terminationOfPregnancyDto.terminationDto}" />
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <%--<strong class="app-font-size-22 premHeader">title</strong>--%>
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Type of Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="topType" firstOption="Please Select" codeCategory="TOP_TYPE_TERMINATION_PREGNANCY"
                         value="${terminationDto.topType}" cssClass="topType"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Type of Drug" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="drugType" firstOption="Please Select" codeCategory="TOP_TYPE_OF_DRUG"
                         value="${terminationDto.drugType}" cssClass="drugType"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="topDate" value="${terminationDto.topDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Place of Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="topPlace" value="${terminationDto.topPlace}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Doctor Professional Regn No." mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="doctorRegnNo" value="${terminationDto.doctorRegnNo}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name of Doctor who performed the Termination of Pregnancy" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="doctorName" value="${terminationDto.doctorName}" />
        </iais:value>
    </iais:row>
</div>

