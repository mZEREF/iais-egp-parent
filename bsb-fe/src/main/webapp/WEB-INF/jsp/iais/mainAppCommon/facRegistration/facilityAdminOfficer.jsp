<%--@elvariable id="facAdminOfficer" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto"--%>
<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Main Adminstrator</h3>

<section id="mainAdmin">
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="salutationM">Salutation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="salutationM" class="salutationMSelect" id="salutationM">
                <option value="" <c:if test="${facAdminOfficer.mainAdmin.salutation eq null || facAdminOfficer.mainAdmin.salutation eq ''}">selected = 'selected'</c:if>>Please Select</option>
                <c:forEach var="item" items="${salutationOps}">
                    <option value="${item.value}" <c:if test="${facAdminOfficer.mainAdmin.salutation eq item.value}">selected = 'selected'</c:if>>${item.text}</option>
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
            <label id="employeeNameM">${facAdminOfficer.mainAdmin.name}</label>
            <span data-err-ind="nameM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="nationalityM">Nationality</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="nationalityM" class="nationalityDropdown" id="nationalityM">
                <option value="">Please Select</option>
                <c:forEach items="${nationalityOps}" var="na">
                    <option value="${na.value}" <c:if test="${facAdminOfficer.mainAdmin.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="nationalityM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="idNumberM">ID No</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <label id="idNumberM">${facAdminOfficer.mainAdmin.idNumber}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationM">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="designationM" id="designationM" value='<c:out value="${facAdminOfficer.mainAdmin.designation}"/>'/>
            <span data-err-ind="designationM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoM">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="20" type="text" autocomplete="off" name="contactNoM" id="contactNoM" value='<c:out value="${facAdminOfficer.mainAdmin.contactNo}"/>'/>
            <span data-err-ind="contactNoM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailM">Email</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="emailM" id="emailM" value='<c:out value="${facAdminOfficer.mainAdmin.email}"/>'/>
            <span data-err-ind="emailM" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDtM">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDtM" id="employmentStartDtM" data-date-start-date="01/01/1900" value="<c:out value="${facAdminOfficer.mainAdmin.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDtM" class="error-msg"></span>
        </div>
    </div>
</section>

<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Alternative Adminstrator</h3>
<section id="alternativeAdmin">
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="salutationA">Salutation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="salutationA" class="salutationDropDown" id="salutationA">
                <option value="" <c:if test="${facAdminOfficer.alternativeAdmin.salutation eq null || facAdminOfficer.alternativeAdmin.salutation eq ''}">selected = 'selected'</c:if>>Please Select</option>
                <c:forEach var="item" items="${salutationOps}">
                    <option value="${item.value}" <c:if test="${facAdminOfficer.alternativeAdmin.salutation eq item.value}">selected = 'selected'</c:if>>${item.text}</option>
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
            <input maxLength="60" type="text" autocomplete="off" name="employeeNameA" id="employeeNameA" value='<c:out value="${facAdminOfficer.alternativeAdmin.name}"/>'/>
            <span data-err-ind="nameA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="nationalityA">Nationality</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <select name="nationalityA" class="nationalityDown" id="nationalityA">
                <option value="">Please Select</option>
                <c:forEach items="${nationalityOps}" var="na">
                    <option value="${na.value}" <c:if test="${facAdminOfficer.alternativeAdmin.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="nationalityA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="idNumberA">ID No</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-3">
            <select name="idTypeA" class="idTypeADrop" id="idTypeA">
                <option value="IDTYPE001" <c:if test="${facAdminOfficer.alternativeAdmin.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                <option value="IDTYPE002" <c:if test="${facAdminOfficer.alternativeAdmin.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
            </select>
            <span data-err-ind="idTypeA" class="error-msg"></span>
        </div>
        <div class="col-sm-3 col-md-4">
            <input maxLength="9" type="text" autocomplete="off" name="idNumberA" id="idNumberA" value='<c:out value="${facAdminOfficer.alternativeAdmin.idNumber}"/>'/>
            <span data-err-ind="idNumberA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="designationA">Designation</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="designationA" id="designationA" value='<c:out value="${facAdminOfficer.alternativeAdmin.designation}"/>'/>
            <span data-err-ind="designationA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoA">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="20" type="text" autocomplete="off" name="contactNoA" id="contactNoA" value='<c:out value="${facAdminOfficer.alternativeAdmin.contactNo}"/>'/>
            <span data-err-ind="contactNoA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailA">Email</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input maxLength="66" type="text" autocomplete="off" name="emailA" id="emailA" value='<c:out value="${facAdminOfficer.alternativeAdmin.email}"/>'/>
            <span data-err-ind="emailA" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="employmentStartDtA">Employment Start Date</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6 col-md-7">
            <input type="text" autocomplete="off" name="employmentStartDtA" id="employmentStartDtA" data-date-start-date="01/01/1900" value="<c:out value="${facAdminOfficer.alternativeAdmin.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
            <span data-err-ind="employmentStartDtA" class="error-msg"></span>
        </div>
    </div>
</section>


<div id="sectionGroup">
    <c:forEach var="officer" items="${facAdminOfficer.officerList}" varStatus="status">
        <section id="officerSection--v--${status.index}">
            <div class="form-group">
                <h3 class="col-xs-9 col-sm-10 col-md-11" style="border-bottom: 1px solid black">Facility Officer <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="" data-original-title="<p>Note: The Facility Officer refers to personnel who are authorised by the Facility Administrator/Alternate Facility Administrator to login to HALP to perform selected transactions for the facility. Nomination of a Facility Officer is optional. The Facility Administrator/Alternate Facility Administrator is responsible to ensure that the list of Facility Officer is always kept up to date i.e. prompt submission of updates to include newly appointed Facility Officer or to remove Facility Officer who are no longer authorised to transact for the facility.</p>">i</a></h3>
                <c:if test="${status.index gt 0}">
                    <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                </c:if>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="salutation--v--${status.index}">Salutation</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6 col-md-7">
                    <select name="salutation--v--${status.index}" class="salutationvv" id="salutation--v--${status.index}">
                        <option value="">Please Select</option>
                        <c:forEach var="item" items="${salutationOps}">
                            <option value="${item.value}" <c:if test="${officer.salutation eq item.value}">selected = 'selected'</c:if>>${item.text}</option>
                        </c:forEach>
                    </select>
                    <span data-err-ind="salutation--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="employeeName--v--${status.index}">Name</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6 col-md-7">
                    <input maxLength="132" type="text" autocomplete="off" name="employeeName--v--${status.index}" id="employeeName--v--${status.index}" value='<c:out value="${officer.name}"/>'/>
                    <span data-err-ind="name--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="nationality--v--${status.index}">Nationality</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6 col-md-7">
                    <select name="nationality--v--${status.index}" class="nationalityvv" id="nationality--v--${status.index}">
                        <option value="">Please Select</option>
                        <c:forEach items="${nationalityOps}" var="na">
                            <option value="${na.value}" <c:if test="${officer.nationality eq na.value}">selected="selected"</c:if>>${na.text}</option>
                        </c:forEach>
                    </select>
                    <span data-err-ind="nationality--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="idNumber--v--${status.index}">ID No</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-3">
                    <select name="idType--v--${status.index}" class="idTypevv" id="idType--v--${status.index}">
                        <option value="IDTYPE001" <c:if test="${officer.idType eq 'IDTYPE001'}">selected="selected"</c:if>>NRIC</option>
                        <option value="IDTYPE002" <c:if test="${officer.idType eq 'IDTYPE002'}">selected="selected"</c:if>>FIN</option>
                    </select>
                    <span data-err-ind="idType--v--${status.index}" class="error-msg"></span>
                </div>
                <div class="col-sm-3 col-md-4">
                    <input maxLength="9" type="text" autocomplete="off" name="idNumber--v--${status.index}" id="idNumber--v--${status.index}" value='<c:out value="${officer.idNumber}"/>'/>
                    <span data-err-ind="idNumber--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="designation--v--${status.index}">Designation</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6 col-md-7">
                    <input maxLength="66" type="text" autocomplete="off" name="designation--v--${status.index}" id="designation--v--${status.index}" value='<c:out value="${officer.designation}"/>'/>
                    <span data-err-ind="designation--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="contactNo--v--${status.index}">Contact No.</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6 col-md-7">
                    <input maxLength="20" type="text" autocomplete="off" name="contactNo--v--${status.index}" id="contactNo--v--${status.index}" value='<c:out value="${officer.contactNo}"/>'/>
                    <span data-err-ind="contactNo--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="email--v--${status.index}">Email</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6 col-md-7">
                    <input maxLength="66" type="text" autocomplete="off" name="email--v--${status.index}" id="email--v--${status.index}" value='<c:out value="${officer.email}"/>'/>
                    <span data-err-ind="email--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="employmentStartDt--v--${status.index}">Employment Start Date</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6 col-md-7">
                    <input type="text" autocomplete="off" name="employmentStartDt--v--${status.index}" id="employmentStartDt--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${officer.employmentStartDt}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                    <span data-err-ind="employmentStartDt--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
        </section>
    </c:forEach>
</div>
<div class="form-group">
    <div class="col-12">
        <a id="addNewSection" style="text-decoration: none" href="javascript:void(0)">+ Add Facility Officer</a>
        <span data-err-ind="facOfficer" class="error-msg"></span>
    </div>
</div>