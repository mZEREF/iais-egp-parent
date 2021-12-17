<%--@elvariable id="facAdmin" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdministratorDto"--%>
<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Main Adminstrator</h3>

<section id="mainAdmin">
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="adminNameM">Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="60" type="text" autocomplete="off" name="adminNameM" id="adminNameM" value='<c:out value="${facAdmin.mainAdmin.adminName}"/>'/>
            <span data-err-ind="adminNameM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="nationalityM">Nationality</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="nationalityM" id="nationalityM">
                <c:forEach items="${nationalityOps}" var="na">
                    <option value="${na.value}" <c:if test="${facAdmin.mainAdmin.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="nationalityM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="idNumberM">NRIC/FIN</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-3">
            <select name="idTypeM" id="idTypeM">
                <option value="IDTYPE001" <c:if test="${facAdmin.mainAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                <option value="IDTYPE002" <c:if test="${facAdmin.mainAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                <option value="IDTYPE003" <c:if test="${facAdmin.mainAdmin.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
            </select>
            <span data-err-ind="idTypeM" class="error-msg"></span>
        </div>
        <div class="col-sm-3 col-md-4">
            <input maxLength="9" type="text" autocomplete="off" name="idNumberM" id="idNumberM" value='<c:out value="${facAdmin.mainAdmin.idNumber}"/>'/>
            <span data-err-ind="idNumberM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationM">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="designationM" id="designationM" value='<c:out value="${facAdmin.mainAdmin.designation}"/>'/>
            <span data-err-ind="designationM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoM">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="20" type="text" autocomplete="off" name="contactNoM" id="contactNoM" value='<c:out value="${facAdmin.mainAdmin.contactNo}"/>'/>
            <span data-err-ind="contactNoM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailM">Email Address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="emailM" id="emailM" value='<c:out value="${facAdmin.mainAdmin.email}"/>'/>
            <span data-err-ind="emailM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDtM">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDtM" id="employmentStartDtM" data-date-start-date="01/01/1900" value="<c:out value="${facAdmin.mainAdmin.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDtM" class="error-msg"></span>
        </div>
    </div>
</section>

<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Alternative Adminstrator</h3>
<section id="alternativeAdmin">
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="adminNameA">Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="60" type="text" autocomplete="off" name="adminNameA" id="adminNameA" value='<c:out value="${facAdmin.alternativeAdmin.adminName}"/>'/>
            <span data-err-ind="adminNameA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="nationalityA">Nationality</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="nationalityA" id="nationalityA">
                <c:forEach items="${nationalityOps}" var="na">
                    <option value="${na.value}" <c:if test="${facAdmin.alternativeAdmin.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="nationalityA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="idNumberA">NRIC/FIN</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-3">
            <select name="idTypeA" id="idTypeA">
                <option value="IDTYPE001" <c:if test="${facAdmin.alternativeAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                <option value="IDTYPE002" <c:if test="${facAdmin.alternativeAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                <option value="IDTYPE003" <c:if test="${facAdmin.alternativeAdmin.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
            </select>
            <span data-err-ind="idTypeA" class="error-msg"></span>
        </div>
        <div class="col-sm-3 col-md-4">
            <input maxLength="9" type="text" autocomplete="off" name="idNumberA" id="idNumberA" value='<c:out value="${facAdmin.alternativeAdmin.idNumber}"/>'/>
            <span data-err-ind="idNumberA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationA">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="designationA" id="designationA" value='<c:out value="${facAdmin.alternativeAdmin.designation}"/>'/>
            <span data-err-ind="designationA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoA">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="20" type="text" autocomplete="off" name="contactNoA" id="contactNoA" value='<c:out value="${facAdmin.alternativeAdmin.contactNo}"/>'/>
            <span data-err-ind="contactNoA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailA">Email Address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="emailA" id="emailA" value='<c:out value="${facAdmin.alternativeAdmin.email}"/>'/>
            <span data-err-ind="emailA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDtA">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDtA" id="employmentStartDtA" data-date-start-date="01/01/1900" value="<c:out value="${facAdmin.alternativeAdmin.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDtA" class="error-msg"></span>
        </div>
    </div>
</section>