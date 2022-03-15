<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Facility Operator Profile</h3>
<%--@elvariable id="facOperator" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto"--%>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="operatorDesc">Designation of Facility Operator</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input maxLength="60" type="text" autocomplete="off" name="operatorDesc" id="operatorDesc" value='<c:out value="${facOperator.operatorDesc}"/>'/>
        <span data-err-ind="operatorDesc" class="error-msg"></span>
    </div>
</div>

<div class="form-group">
    <div class="col-sm-12 control-label">
        Note: The Facility Operator is the person who has overall control and oversight of the management of the facility such as the Chief Executive Officer of the company or a person of equivalent level.
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label><strong>Facility Operator Designee:</strong></label>
        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>The Facility Operator Designee is the person who is appointed by the Facility Operator to oversee the day to day management and operations at the facility.</p>">i</a>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="salutation">Salutation</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <select name="salutation" id="salutation">
            <option value="" <c:if test="${facOperator.salutation eq item.value}">selected = 'selected'</c:if>>Please Select</option>
            <c:forEach var="item" items="${salutationOps}">
            <option value="${item.value}" <c:if test="${facOperator.salutation eq item.value}">selected = 'selected'</c:if>>${item.text}</option>
            </c:forEach>
        </select>
        <span data-err-ind="salutation" class="error-msg"></span>
    </div>
</div>

<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="operatorName">Name</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input maxLength="60" type="text" autocomplete="off" name="operatorName" id="operatorName" value='<c:out value="${facOperator.designeeName}"/>'/>
        <span data-err-ind="designeeName" class="error-msg"></span>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="idNumber">ID No</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-3">
        <select name="idType" id="idType">
            <option value="IDTYPE001" <c:if test="${facOperator.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
            <option value="IDTYPE002" <c:if test="${facOperator.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
        </select>
        <span data-err-ind="idType" class="error-msg"></span>
    </div>
    <div class="col-sm-3 col-md-4">
        <input maxLength="9" type="text" autocomplete="off" name="idNumber" id="idNumber" value='<c:out value="${facOperator.idNumber}"/>'/>
        <span data-err-ind="idNumber" class="error-msg"></span>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="nationality">Nationality</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <select name="nationality" id="nationality">
            <c:forEach items="${nationalityOps}" var="na">
                <option value="${na.value}" <c:if test="${facOperator.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
            </c:forEach>
        </select>
        <span data-err-ind="nationality" class="error-msg"></span>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="designation">Designation</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input maxLength="66" type="text" autocomplete="off" name="designation" id="designation" value='<c:out value="${facOperator.designation}"/>'/>
        <span data-err-ind="designation" class="error-msg"></span>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="contactNo">Contact No.</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input maxLength="20" type="text" autocomplete="off" name="contactNo" id="contactNo" value='<c:out value="${facOperator.contactNo}"/>'/>
        <span data-err-ind="contactNo" class="error-msg"></span>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="email">Email</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input maxLength="66" type="text" autocomplete="off" name="email" id="email" value='<c:out value="${facOperator.email}"/>'/>
        <span data-err-ind="email" class="error-msg"></span>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="employmentStartDt">Employment Start Date</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6 col-md-7">
        <input type="text" autocomplete="off" name="employmentStartDt" id="employmentStartDt" data-date-start-date="01/01/1900" value="<c:out value="${facOperator.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
        <span data-err-ind="employmentStartDt" class="error-msg"></span>
    </div>
</div>