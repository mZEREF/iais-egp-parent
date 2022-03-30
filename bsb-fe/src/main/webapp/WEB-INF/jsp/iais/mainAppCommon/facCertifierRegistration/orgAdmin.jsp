<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Main Administrator</h3>

<section id="mainAdmin">
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="salutationM">Salutation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="salutationM" id="salutationM">
                <option value="" <c:if test="${companyAdmin.mainAdmin.salutation eq null || companyAdmin.mainAdmin.salutation eq ''}">selected = 'selected'</c:if>>Please Select</option>
                <c:forEach var="item" items="${salutationOps}">
                    <option value="${item.value}" <c:if test="${companyAdmin.mainAdmin.salutation eq item.value}">selected = 'selected'</c:if>>${item.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="salutationM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employeeNameM">Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <label id="employeeNameM"><c:out value="${companyAdmin.mainAdmin.name}" /></label>
            <span data-err-ind="employeeNameM" class="error-msg"></span>
        </div>
    </div>

    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="idNoM">NRIC/FIN</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <label id="idNoM"><c:out value="${companyAdmin.mainAdmin.idNumber}"/></label>
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
                    <option value="${naM.value}" <c:if test="${companyAdmin.mainAdmin.nationality eq naM.value}">selected="selected"</c:if>>${naM.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="nationalityM" class="error-msg"></span>
        </div>
    </div>


    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationM">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="designationM" id="designationM" maxlength="66" value='<c:out value="${companyAdmin.mainAdmin.designation}"/>' />
            <span data-err-ind="designationM" class="error-msg"></span>
        </div>
    </div>

    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoM">Contact No</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="contactNoM" id="contactNoM" maxlength="20" value='<c:out value="${companyAdmin.mainAdmin.contactNo}"/>' />
            <span data-err-ind="contactNoM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailM">Email Address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="emailM" id="emailM" maxlength="66" value='<c:out value="${companyAdmin.mainAdmin.email}"/>' />
            <span data-err-ind="emailM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDtM">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDtM" id="employmentStartDtM" data-date-start-date="01/01/1900" value="<c:out value="${companyAdmin.mainAdmin.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDtM" class="error-msg"></span>
        </div>
    </div>
</section>

<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Alternative Administrator</h3>
<section id="alternativeAdmin">
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="salutationA">Salutation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="salutationA" id="salutationA">
                <option value="" <c:if test="${companyAdmin.alternativeAdmin.salutation eq null || companyAdmin.alternativeAdmin.salutation eq ''}">selected = 'selected'</c:if>>Please Select</option>
                <c:forEach var="item" items="${salutationOps}">
                    <option value="${item.value}" <c:if test="${companyAdmin.alternativeAdmin.salutation eq item.value}">selected = 'selected'</c:if>>${item.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="salutationA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employeeNameA">Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employeeNameA" id="employeeNameA" maxlength="132" value='<c:out value="${companyAdmin.alternativeAdmin.name}"/>'/>
            <span data-err-ind="employeeNameA" class="error-msg"></span>
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
                    <option value="${naA.value}" <c:if test="${companyAdmin.alternativeAdmin.nationality eq naA.value}">selected="selected"</c:if>>${naA.text}</option>
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
                <option value="IDTYPE001" <c:if test="${companyAdmin.alternativeAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                <option value="IDTYPE002" <c:if test="${companyAdmin.alternativeAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                <option value="IDTYPE003" <c:if test="${companyAdmin.alternativeAdmin.idType eq 'IDTYPE003'}">selected="selected"</c:if>>Passport</option>
            </select>
            <span data-err-ind="idNumberA" class="error-msg"></span>
        </div>
        <div class="col-sm-3 col-md-4">
            <input type="text" autocomplete="off" name="idNumberA" id="idNumberA" maxlength="9" value='<c:out value="${companyAdmin.alternativeAdmin.idNumber}"/>'/>
            <span data-err-ind="idNumberA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationA">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="designationA" id="designationA" maxlength="66" value='<c:out value="${companyAdmin.alternativeAdmin.designation}"/>'/>
            <span data-err-ind="designationA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoA">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="contactNoA" id="contactNoA" maxlength="20" value='<c:out value="${companyAdmin.alternativeAdmin.contactNo}"/>'/>
            <span data-err-ind="contactNoA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailA">Email Address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="emailA" id="emailA" maxlength="66" value='<c:out value="${companyAdmin.alternativeAdmin.email}"/>'/>
            <span data-err-ind="emailA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDtA">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDtA" id="employmentStartDtA" data-date-start-date="01/01/1900" value="<c:out value="${companyAdmin.alternativeAdmin.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDtA" class="error-msg"></span>
        </div>
    </div>
</section>