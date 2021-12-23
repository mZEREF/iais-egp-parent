<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="guardianAppliedPartDto" value="${vssTreatmentDto.guardianAppliedPartDto}" />
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
<div>
   <%--<div style="font-size: 1.6rem;font-family: Open Sans, sans-serif; border-bottom: 1px solid #BABABA; margin-top: 0; margin-bottom: 15px; padding-bottom: 20px; font-weight: 600;">
       Particulars of Consent
   </div>--%>
    <iais:row style="border-bottom: 1px solid #BABABA; ">
        <iais:value width="6" cssClass="col-md-6">
            <strong class="app-font-size-22 premHeader">Particulars of Consent</strong>
        </iais:value>
    </iais:row>
    <%@include file="particulars.jsp" %>
</div>

<div>
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <strong class="app-font-size-22 premHeader">Particulars of Court Order</strong>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name Of Person" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="appliedPartName" value="${guardianAppliedPartDto.appliedPartName}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="ID No." mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="appliedPartIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${guardianAppliedPartDto.appliedPartIdType}" />
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="appliedPartIdNo" value="${guardianAppliedPartDto.appliedPartIdNo}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="appliedPartBirthday" value="${guardianAppliedPartDto.appliedPartBirthday}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relationship to Person Who Was Sterilized" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="appliedPartRelationship" firstOption="Please Select" codeCategory=""
                         value="${guardianAppliedPartDto.appliedPartRelationship}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date Court Order Issued" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="courtOrderIssueDate" value="${guardianAppliedPartDto.courtOrderIssueDate}"/>
        </iais:value>
    </iais:row>
</div>
</div>