<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="sexualSterilizationDto" value="${vssTreatmentDto.sexualSterilizationDto}" />
<div class="form-horizontal treatmentDetails">
   <%-- <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            &lt;%&ndash;<strong class="app-font-size-22 premHeader">title</strong>&ndash;%&gt;
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>--%>
    <iais:row>
        <iais:field width="5" value="Doctor Professional Registration No." mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="doctorReignNo" value="${sexualSterilizationDto.doctorReignNo}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name of Doctor who performed the sterilization" />
        <iais:value width="7" cssClass="col-md-7">
            <iais:input type="text" name="doctorName" value="${sexualSterilizationDto.doctorName}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Hospital/Clinic where the sterilization was performed" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="" codeCategory="" firstOption="Please Select"
                         value="" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Method of Sterilization" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="200" type="text" name="sterilizationMethod" value="${sexualSterilizationDto.sterilizationMethod}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Operation " mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="" name="operationDate" dateVal="${sexualSterilizationDto.operationDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Reviewed by Hospital Ethics Committee (HEC) (state name of hospital)" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="reviewedByHec"
                       value="true"
                       id="genderMale"
                       <c:if test="${sexualSterilizationDto.reviewedByHec == true}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="genderMale"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_reviewedByHec"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="reviewedByHec"
                       value="false"
                       id="genderFemale"
                       <c:if test="${sexualSterilizationDto.reviewedByHec == false}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="genderFemale"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of HEC Review" />
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="hecReviewDate" name="hecReviewDate" dateVal="${sexualSterilizationDto.hecReviewDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_hecReviewDate"></span>
        </iais:value>
    </iais:row>
</div>