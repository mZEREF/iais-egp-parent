<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Main Adminstrator</h3>

<section id="mainAdmin">
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="adminNameM">Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="adminNameM" id="adminNameM" maxlength="132" value='<c:out value="${orgAdmin.mainAdmin.adminName}"/>'/>
            <span data-err-ind="adminNameM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-5 control-label">
            <label for="nationalityM">Nationality</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="nationalityM" id="nationalityM">
                <c:forEach items="${nationalityOps}" var="naM">
                    <option value="${naM.value}" <c:if test="${orgAdmin.mainAdmin.nationality eq naM.value}">selected="selected"</c:if>>${naM.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="nationalityM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="idNoM">NRIC/FIN</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-3">
            <select name="idTypeM" id="idTypeM">
                <option value="IDTYPE001" <c:if test="${orgAdmin.mainAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                <option value="IDTYPE002" <c:if test="${orgAdmin.mainAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                <option value="IDTYPE003" <c:if test="${orgAdmin.mainAdmin.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
            </select>
            <span data-err-ind="idTypeM" class="error-msg"></span>
        </div>
        <div class="col-sm-3 col-md-4">
            <input type="text" autocomplete="off" name="idNoM" id="idNoM" maxlength="10" value='<c:out value="${orgAdmin.mainAdmin.idNo}"/>'/>
            <span data-err-ind="idNoM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationM">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="designationM" id="designationM" maxlength="66" value='<c:out value="${orgAdmin.mainAdmin.designation}"/>' />
            <span data-err-ind="designationM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoM">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="contactNoM" id="contactNoM" maxlength="20" value='<c:out value="${orgAdmin.mainAdmin.contactNo}"/>' />
            <span data-err-ind="contactNoM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailM">Email Address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="emailM" id="emailM" maxlength="66" value='<c:out value="${orgAdmin.mainAdmin.email}"/>' />
            <span data-err-ind="emailM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDateM">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDateM" id="employmentStartDateM" data-date-start-date="01/01/1900" value="<c:out value="${orgAdmin.mainAdmin.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDateM" class="error-msg"></span>
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
            <input type="text" autocomplete="off" name="adminNameA" id="adminNameA" maxlength="132" value='<c:out value="${orgAdmin.alternativeAdmin.adminName}"/>'/>
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
                <c:forEach items="${nationalityOps}" var="naA">
                    <option value="${naA.value}" <c:if test="${orgAdmin.alternativeAdmin.nationality eq naA.value}">selected="selected"</c:if>>${naA.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="nationalityA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="idNoA">NRIC/FIN</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-3">
            <select name="idTypeA" id="idTypeA">
                <option value="IDTYPE001" <c:if test="${orgAdmin.alternativeAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                <option value="IDTYPE002" <c:if test="${orgAdmin.alternativeAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                <option value="IDTYPE003" <c:if test="${orgAdmin.alternativeAdmin.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
            </select>
            <span data-err-ind="idTypeA" class="error-msg"></span>
        </div>
        <div class="col-sm-3 col-md-4">
            <input type="text" autocomplete="off" name="idNoA" id="idNoA" maxlength="9" value='<c:out value="${orgAdmin.alternativeAdmin.idNo}"/>'/>
            <span data-err-ind="idNoA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationA">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="designationA" id="designationA" maxlength="66" value='<c:out value="${orgAdmin.alternativeAdmin.designation}"/>'/>
            <span data-err-ind="designationA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoA">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="contactNoA" id="contactNoA" maxlength="20" value='<c:out value="${orgAdmin.alternativeAdmin.contactNo}"/>'/>
            <span data-err-ind="contactNoA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailA">Email Address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="emailA" id="emailA" maxlength="66" value='<c:out value="${orgAdmin.alternativeAdmin.email}"/>'/>
            <span data-err-ind="emailA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDateA">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDateA" id="employmentStartDateA" data-date-start-date="01/01/1900" value="<c:out value="${orgAdmin.alternativeAdmin.employmentStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDateA" class="error-msg"></span>
        </div>
    </div>
</section>