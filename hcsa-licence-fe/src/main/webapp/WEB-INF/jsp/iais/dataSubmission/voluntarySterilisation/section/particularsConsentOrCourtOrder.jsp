<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="guardianAppliedPartDto" value="${vssTreatmentDto.guardianAppliedPartDto}" />
<c:set var="treatmentDto" value="${vssTreatmentDto.treatmentDto}" />
<div class="form-horizontal treatmentDetails">
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
<div style="${treatmentDto.age<21 && treatmentDto.maritalStatus != 'VSSMS002' ? '' : 'display: none'}">
    <iais:row style="border-bottom: 1px solid #BABABA; ">
        <iais:value width="7" cssClass="col-md-7">
            <strong class="app-font-size-22 premHeader">Particulars of Consent</strong>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name Of Person" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="guardianName" value="${guardianAppliedPartDto.guardianName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="ID No." mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="guardianIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${guardianAppliedPartDto.guardianIdType}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianIdType"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="guardianIdNo" value="${guardianAppliedPartDto.guardianIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="guardianBirthday" dateVal="${guardianAppliedPartDto.guardianBirthday}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianBirthday"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relationship to Person Who Was Sterilized" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="guardianRelationship" firstOption="Please Select" codeCategory=""
                         value="${guardianAppliedPartDto.guardianRelationship}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianRelationship"></span>
        </iais:value>
    </iais:row>
</div>

<div style="${treatmentDto.sterilizationReason =='VSSRFS002' ? '' : 'display: none'}">
    <iais:row style="border-bottom: 1px solid #BABABA; ">
        <iais:value width="7" cssClass="col-md-7">
            <strong class="app-font-size-22 premHeader">Particulars of Court Order</strong>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name Of Person" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="appliedPartName" value="${guardianAppliedPartDto.appliedPartName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="ID No." mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="appliedPartIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${guardianAppliedPartDto.appliedPartIdType}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartIdType"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="appliedPartIdNo" value="${guardianAppliedPartDto.appliedPartIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="appliedPartBirthday" dateVal="${guardianAppliedPartDto.appliedPartBirthday}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartBirthday"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relationship to Person Who Was Sterilized" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <%--<iais:select name="appliedPartRelationship" firstOption="Please Select" codeCategory=""
                         value="${guardianAppliedPartDto.appliedPartRelationship}"/>--%>
            <iais:input maxLength="15" type="text" name="appliedPartRelationship" value="${guardianAppliedPartDto.appliedPartRelationship}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartRelationship"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date Court Order Issued" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="courtOrderIssueDate"  dateVal="${guardianAppliedPartDto.courtOrderIssueDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_courtOrderIssueDate"></span>
        </iais:value>
    </iais:row>
</div>
</div>