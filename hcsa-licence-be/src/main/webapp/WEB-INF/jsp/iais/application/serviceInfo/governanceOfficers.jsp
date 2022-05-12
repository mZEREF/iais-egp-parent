<style>
    table.control-grid.columns1 > tbody > tr > td > .section.control input[type=text], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=email], table.control-grid.columns1 > tbody > tr > td > .section.control input[type=number], table.control-grid.columns1 > tbody > tr > td > .section.control .nice-select {
        margin-bottom: 15px;
        margin-top: 25px;
    }

    .control-font-label {
        margin-top: 19px;
    }
</style>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" name="rfiObj" value="<c:if test="${requestInformationConfig == null}">0</c:if><c:if test="${requestInformationConfig != null}">1</c:if>"/>
<input type="hidden" name="prsFlag" value="${prsFlag}"/>
<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
    <h4>
        A Clinical Governance Officer (CGO) is a suitably qualified person appointed by the licensee and
        who is responsible for the oversight of clinical and technical matters related to the <iais:code code="CDN001"/> provided.
    </h4>
    <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
        <div id="control--runtime--0" class="page control control-area  container-p-1">
            <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
            <table aria-describedby="" class="control-grid columns1 " style="width: 100%;">
                <thead style="display: none">
                <tr><th scope="col"></th></tr>
                </thead>
                <tbody>
                <tr height="1">
                    <td class="first last" style="width: 100%;">
                        <div id="control--runtime--85" class="control control-caption-horizontal">
                        </div>
                    </td>
                </tr>
                <tr>
                </tr>
                <tr>
                    <td class="first last" style="width: 100%;">
                        <div class="section control  container-s-1">
                            <table aria-describedby="" class="assignContent control-grid">
                                <thead style="display: none">
                                <tr><th scope="col"></th></tr>
                                </thead>
                                <tbody>
                                <tr height="1">
                                    <td class="first last" style="width: 100%;">
                                        <div id="" class="control control-caption-horizontal">
                                            <div class="form-group form-horizontal formgap">
                                                <div class="col-sm-4">
                                                    <strong style="font-size: 20px;">
                                                        <c:out value="${currStepName}"/>
                                                    </strong>
                                                </div>
                                                <div class="col-sm-8 text-right">
                                                    <c:if test="${AppSubmissionDto.needEditController }">
                                                        <c:forEach var="clickEditPage"
                                                                   items="${AppSubmissionDto.clickEditPage}">
                                                            <c:if test="${'APPSPN02' == clickEditPage}">
                                                                <c:set var="isClickEdit" value="true"/>
                                                            </c:if>
                                                        </c:forEach>
                                                        <c:choose>
                                                            <c:when test="${'true' != isClickEdit && !('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
                                                                <input id="isEditHiddenVal" type="hidden" name="isEdit"
                                                                       value="0"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input id="isEditHiddenVal" type="hidden" name="isEdit"
                                                                       value="1"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                                                            <div class="app-font-size-16">
                                                                <a class="back" id="RfcSkip" href="javascript:void(0);">
                                                                    Skip<span style="display: inline-block;">&nbsp;</span>
                                                                    <em class="fa fa-angle-right"></em>
                                                                </a>
                                                            </div>
                                                        </c:if>
                                                        <c:if test="${'true' != isClickEdit}">
                                                            <c:set var="locking" value="true"/>
                                                            <c:set var="canEdit"
                                                                   value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                                                        </c:if>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span class="error-msg" name="iaisErrorMsg" id="error_psnMandatory"></span>
                    </td>
                </tr>
                <tr height="1">
                    <td class="first last" style="width: 100%;">
                        <div id="control--runtime--1" class="section control  container-s-1">
                            <div class="control-set-font control-font-header section-header"></div>
                            <c:set value="${GovernanceOfficersList}" var="cgoList"/>
                            <c:set var="editControl" value="${(!empty cgoList && AppSubmissionDto.needEditController) || !AppSubmissionDto.needEditController}" />
                            <c:if test="${CgoMandatoryCount >0 && editControl}">
                                <c:forEach begin="0" end="${CgoMandatoryCount-1}" step="1" varStatus="status">
                                    <c:set value="cgo-${status.index}-" var="cgoIndeNo"/>
                                    <c:set value="${cgoList[status.index]}" var="currentCgo"/>
                                    <c:set value="${errorMap_governanceOfficers[status.index]}" var="errorMap"/>
                                    <c:set value="${status.index}" var="suffix"/>
                                    <div class="cgo-content">
                                        <table aria-describedby="" class="assignContent control-grid" style="width:100%;">
                                            <thead style="display: none">
                                            <tr><th scope="col"></th></tr>
                                            </thead>
                                            <input type="hidden" name="isPartEdit" value="0"/>
                                            <input type="hidden" name="indexNo" value="${currentCgo.indexNo}"/>
                                            <input type="hidden" name="existingPsn" value="0"/>
                                            <input type="hidden" name="psnEditField" value="${currentCgo.psnEditDto}"/>
                                            <c:choose>
                                                <c:when test="${currentCgo.licPerson}">
                                                    <input class="licPerson"  type="hidden" name="licPerson" value="1"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input class="licPerson"  type="hidden" name="licPerson" value="0"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <tbody>
                                            <tr height="1">
                                                <td class="first last" style="width: 100%;">
                                                    <c:choose>
                                                        <c:when test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType || requestInformationConfig != null}">
                                                            <div id="control--runtime--2"
                                                                 class="control control-caption-horizontal">
                                                                <c:if test="${currentCgo != null}">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext control">
                                                                            <div class="cgo-header">
                                                                                <strong>
                                                                                    Clinical Governance Officer
                                                                                    <label class="assign-psn-item"><c:if test="${cgoList.size() > 1}">${status.index+1}</c:if></label>
                                                                                </strong>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-sm-8 text-right">
                                                                            <c:if test="${status.index - HcsaSvcPersonnel.mandatoryCount >=0}">
                                                                                <div class="">
                                                                                    <h4 class="text-danger"><em
                                                                                            class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                                                                                    </h4>
                                                                                </div>
                                                                            </c:if>
                                                                        </div>
                                                                        <div class="col-sm-10">
                                                                            <label class="control-font-label">
                                                                                <c:if test="${!empty currentCgo.name && !empty currentCgo.idNo && !empty currentCgo.idType}">
                                                                                    ${currentCgo.name}, ${currentCgo.idNo} (<iais:code code="${currentCgo.idType}"/>)
                                                                                </c:if>
                                                                            </label>
                                                                        </div>
                                                                        <div class="col-sm-2" style="margin-top:3%;">
                                                                            <div class="edit-content">
                                                                                <c:if test="${'true' == canEdit}">
                                                                                    <div class="text-right app-font-size-16">
                                                                                        <a class="edit cgoEdit" href="javascript:void(0);">
                                                                                            <em class="fa fa-pencil-square-o"></em>
                                                                                            <span>&nbsp;</span>Edit
                                                                                        </a>
                                                                                    </div>
                                                                                </c:if>
                                                                            </div>
                                                                        </div>
                                                                        <div class="<c:if test="${!empty currentCgo.assignSelect && '-1' != currentCgo.assignSelect}"> hidden </c:if>">
                                                                            <div class="col-sm-5 control-label formtext ">
                                                                                <label class="control-label control-set-font control-font-label">Add/Assign a Clinical Governance Officer</label>
                                                                                <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7" id="assignSelect${suffix}">
                                                                                <div class="">
                                                                                    <iais:select cssClass="assignSel"
                                                                                                 name="assignSelect"
                                                                                                 options="CgoSelectList"
                                                                                                 needSort="false"
                                                                                                 value="${currentCgo.assignSelect}"></iais:select>
                                                                                    <span class="error-msg" name="iaisErrorMsg"
                                                                                          id="error_assignSelect${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </c:if>
                                                            </div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div id="control--runtime--"
                                                                 class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap"
                                                                     <c:if test="${status.first}">style="width:194%;"</c:if> >
                                                                    <div class="col-sm-8 control-label formtext control">
                                                                        <div class="cgo-header">
                                                                            <strong>Clinical Governance Officer <label
                                                                                    class="assign-psn-item"><c:if test="${cgoList.size() > 1}">${status.index+1}</c:if></label></strong>
                                                                        </div>
                                                                    </div>
                                                                    <div class="col-sm-4 text-right">
                                                                        <c:if test="${status.index - HcsaSvcPersonnel.mandatoryCount >=0}">
                                                                            <div class="">
                                                                                <h4 class="text-danger"><em
                                                                                        class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em>
                                                                                </h4>
                                                                            </div>
                                                                        </c:if>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div id="control--runtime--2"
                                                                 class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-5 control-label formtext ">
                                                                        <label id="control--runtime--2--label"
                                                                               class="control-label control-set-font control-font-label">
                                                                            Add/Assign a Clinical Governance Officer
                                                                        </label>
                                                                        <span class="mandatory">*</span>
                                                                        <span class="upload_controls"></span>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7"
                                                                         id="assignSelect${suffix}">
                                                                        <div class="">
                                                                            <iais:select cssClass="assignSel"
                                                                                         name="assignSelect"
                                                                                         options="CgoSelectList"
                                                                                         needSort="false"
                                                                                         value="${currentCgo.assignSelect}"></iais:select>
                                                                            <span class="error-msg" name="iaisErrorMsg"
                                                                                  id="error_assignSelect${status.index}"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <div class="profile-info-gp hidden"></div>
                                                    <div id="newOfficer" class="new-officer-form hidden">
                                                        <table aria-describedby="" class="control-grid">
                                                            <thead style="display: none">
                                                            <tr><th scope="col"></th></tr>
                                                            </thead>
                                                            <tbody>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class=" form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext ">
                                                                                <label class="control-label control-set-font control-font-label">
                                                                                    Name
                                                                                </label>
                                                                                <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-3 col-xs-12"
                                                                                 id="salutation${suffix}">
                                                                                <div>
                                                                                    <iais:select cssClass="salutationSel"
                                                                                                 name="salutation"
                                                                                                 codeCategory="CATE_ID_SALUTATION"
                                                                                                 value="${currentCgo.salutation}"
                                                                                                 firstOption="Please Select"></iais:select>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_salutation${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                            <div class="col-sm-3 col-md-4 col-xs-12"
                                                                                 id="name${suffix}">
                                                                                <div class="">
                                                                                    <iais:input cssClass="field-name" maxLength="66"
                                                                                                type="text" name="name"
                                                                                                value="${currentCgo.name}"></iais:input>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_name${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div id="control--runtime--28"
                                                                         class="control control-caption-horizontal">
                                                                        <div class=" form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext ">
                                                                                <label id="control--runtime--28--label"
                                                                                       class="control-label control-set-font control-font-label">
                                                                                    ID No.
                                                                                </label>
                                                                                <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div></div>
                                                                            <div class="col-sm-5 col-md-3 col-xs-12"
                                                                                 id="idType${suffix}">
                                                                                <div class="">
                                                                                    <iais:select cssClass="idTypeSel"
                                                                                                 name="idType"
                                                                                                 needSort="false"
                                                                                                 value="${currentCgo.idType}"
                                                                                                 firstOption="Please Select"
                                                                                                 codeCategory="CATE_ID_ID_TYPE"></iais:select>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_idTyp${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-4 col-xs-12">
                                                                                <div class="">
                                                                                    <iais:input maxLength="20"
                                                                                                type="text" name="idNo"
                                                                                                value="${currentCgo.idNo}"/>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMSg"
                                                                                          id="error_idNo${status.index}"></span>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMSg"
                                                                                          id="error_idNo"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1" class="nationalityDiv">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                                <label class="control-label control-set-font control-font-label">Country of issuance</label>
                                                                                <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7" id="nationality${suffix}">
                                                                                <div class="">
                                                                                    <iais:select firstOption="Please Select" name="nationality" codeCategory="CATE_ID_NATIONALITY"
                                                                                                 cssClass="nationality" value="${currentCgo.nationality}" needErrorSpan="false"/>
                                                                                    <span class="error-msg" name="iaisErrorMsg"
                                                                                          id="error_nationality${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class=" form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext ">
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7">
                                                                                <span class="error-msg"
                                                                                      name="iaisErrorMSg"
                                                                                      id="error_idTypeNo${status.index}"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                                <label class="control-label control-set-font control-font-label">Designation</label>
                                                                                <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7"
                                                                                 id="designation${suffix}">
                                                                                <div class="">
                                                                                    <iais:select
                                                                                            cssClass="designationSel"
                                                                                            name="designation"
                                                                                            options="designationOpList"
                                                                                            value="${currentCgo.designation}"
                                                                                            firstOption="Please Select"></iais:select>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_designation${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal otherDesignationDiv
                                                                        <c:if test="${currentCgo.designation != 'DES999' }">
                                                                            hidden
                                                                        </c:if>
                                                                    ">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7" >
                                                                                <div class="">
                                                                                    <iais:input  maxLength="100" type="text" cssClass="otherDesignation" name="otherDesignation" value="${currentCgo.otherDesignation}"/>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_otherDesignation${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                                <label class="control-label control-set-font control-font-label">Professional
                                                                                    Type</label>
                                                                                <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7"
                                                                                 id="professionType${suffix}">
                                                                                <div class="professionRegoType">
                                                                                    <iais:select
                                                                                            cssClass="professionTypeSel"
                                                                                            name="professionType"
                                                                                            codeCategory="CATE_ID_PROFESSIONAL_TYPE"
                                                                                            value="${currentCgo.professionType}"
                                                                                            firstOption="Please Select"></iais:select>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_professionType${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div id="control--runtime--31"
                                                                         class="control control-caption-horizontal">
                                                                        <div class=" form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext ">
                                                                                <label id="control--runtime--31--label"
                                                                                       class="control-label control-set-font control-font-label">
                                                                                    Professional Regn. No.
                                                                                </label>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7">
                                                                                <div class="">
                                                                                    <input maxLength="20"
                                                                                           type="text"
                                                                                           name="professionRegoNo" autocomplete="off"
                                                                                           value="${currentCgo.profRegNo}">
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_professionRegoNo${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div id="control--runtime--29"
                                                                         class="control control-caption-horizontal">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                                <label id="control--runtime--29--label" class="control-label control-set-font control-font-label">Specialty</label>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7">
                                                                                <label class="control-label control-set-font control-font-label specialty-label">
                                                                                    <c:out value=" ${currentCgo.speciality}"/>
                                                                                </label>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>

                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                                <label class="control-label control-set-font control-font-label">Sub-specialty</label>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7">
                                                                                <label class="control-label control-set-font control-font-label sub-specialty-label">
                                                                                        ${currentCgo.subSpeciality}
                                                                                </label>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                                <label class="control-label control-set-font control-font-label">Qualification</label>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7">
                                                                                <label class="control-label control-set-font control-font-label qualification-label">
                                                                                        ${currentCgo.qualification}
                                                                                </label>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class="form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext">
                                                                                <label class="control-label control-set-font control-font-label">Other Qualification</label>
                                                                                <span class="mandatory otherQualificationSpan"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7">
                                                                                <div class="">
                                                                                    <iais:input maxLength="100" type="text" cssClass="otherQualification" name="otherQualification" value="${currentCgo.otherQualification}"></iais:input>
                                                                                    <span class="error-msg" name="iaisErrorMsg" id="error_otherQualification${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>

                                                            <tr height="1">
                                                                <td class="first last" style="width: 100%;">
                                                                    <div class="control control-caption-horizontal">
                                                                        <div class=" form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext ">
                                                                                <label class="control-label control-set-font control-font-label">Mobile
                                                                                    No.</label> <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-5 col-md-7">
                                                                                <div class="">
                                                                                    <iais:input maxLength="8"
                                                                                                type="text"
                                                                                                name="mobileNo"
                                                                                                value="${currentCgo.mobileNo}"></iais:input>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_mobileNo${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            <tr height="1">
                                                                <td class="first last" style="">
                                                                    <div id="control--runtime--33"
                                                                         class="control control-caption-horizontal">
                                                                        <div class=" form-group form-horizontal formgap">
                                                                            <div class="col-sm-4 control-label formtext ">
                                                                                <label id="control--runtime--33--label"
                                                                                       class="control-label control-set-font control-font-label">Email
                                                                                    Address</label>
                                                                                <span class="mandatory">*</span>
                                                                                <span class="upload_controls"></span>
                                                                            </div>
                                                                            <div class="col-sm-4 col-md-7">
                                                                                <div class="">
                                                                                    <iais:input maxLength="66"
                                                                                                type="text"
                                                                                                name="emailAddress"
                                                                                                value="${currentCgo.emailAddr}"></iais:input>
                                                                                    <span class="error-msg"
                                                                                          name="iaisErrorMsg"
                                                                                          id="error_emailAddr${status.index}"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                        <c:if test="${!status.last}">
                                            <hr/>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <div class="cgo-content-point"></div>
                        </div>
                    </td>
                </tr>
                <c:if test="${ requestInformationConfig==null}">
                    <c:choose>
                        <c:when test="${!empty GovernanceOfficersList}">
                            <c:set var="cgoDtoLength" value="${GovernanceOfficersList.size()}"/>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${AppSubmissionDto.needEditController}">
                                    <c:set var="cgoDtoLength" value="0"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="cgoDtoLength" value="${HcsaSvcPersonnel.mandatoryCount}"/>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    <c:set var="needAddPsn" value="true"/>
                    <c:choose>
                        <c:when test="${HcsaSvcPersonnel.status =='CMSTAT003'}">
                            <c:set var="needAddPsn" value="false"/>
                        </c:when>
                        <c:when test="${cgoDtoLength >= HcsaSvcPersonnel.maximumCount}">
                            <c:set var="needAddPsn" value="false"/>
                        </c:when>
                    </c:choose>
                    <tr id="addPsnDiv" class="<c:if test="${!needAddPsn}">hidden</c:if>">
                        <td>
                            <div class="col-sm-5 col-md-5">
                                <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">
                                    <span style="margin-left: -14px">+ Add Another ${singleName}</span>
                                </span>
                            </div>
                            <div class="col-sm-5 col-md-5"><span class="errorMsg" style="color: red;"></span></div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@include file="../common/prsLoading.jsp"%>
<div style="display: none;">
    <select id="nice_select_effect"></select>
</div>
<script type="text/javascript">
    var init;
    $(document).ready(function () {
        //init start
        init = 0;
        if($('#PRS_SERVICE_DOWN_INPUT').val()=='PRS_SERVICE_DOWN'){
            $('#PRS_SERVICE_DOWN').modal('show');
        }

        $('.hideen-div').addClass('hidden');
        //init font-size
        $('.cgo-header').css('font-size', "18px");

        psnSelect();

        $('select.assignSel').trigger('change');

        // reLoadChange();
        showSpecialty();

        removeCgo();

        profRegNoBlur();

        designationChange();

        $('input[name="licPerson"]').each(function (k, v) {
            if ('1' == $(this).val()) {
                var $currentPsn = $(this).closest('.assignContent').find('.new-officer-form');
                disabledPartPage($currentPsn);
            }
        });

        if (${AppSubmissionDto.needEditController && !isClickEdit}) {
            disabledPage();
            //$('.addListBtn').addClass('hidden');
        }

        initNationality('div.cgo-content', 'select[name="idType"]', '.nationalityDiv');

        var appType = $('input[name="applicationType"]').val();
        var rfiObj = $('input[name="rfiObj"]').val();
        //new and not rfi
        if ('APTY002' == appType && '0' == rfiObj) {
            <c:choose>
            <c:when test="${!empty GovernanceOfficersList}">
            <c:set var="psnLength" value="${GovernanceOfficersList.size()-1}"/>
            </c:when>
            <c:otherwise>
            <c:set var="psnLength" value="0"/>
            </c:otherwise>
            </c:choose>
            <c:forEach begin="0" end="${psnLength}" step="1" varStatus="stat">
            var $currentPsn = $('.assignContent').eq(${stat.index+1}).find('.new-officer-form');
            //remove dis style
            $currentPsn.find('input[type="text"]').css('border-color', '');
            $currentPsn.find('input[type="text"]').css('color', '');
            //add edit and set style
            var psnDto = {};
            <c:if test="${!empty GovernanceOfficersList[stat.index].psnEditFieldStr}">
            psnDto = ${GovernanceOfficersList[stat.index].psnEditFieldStr};
            </c:if>
            setPsnDisabled($currentPsn,psnDto);
            </c:forEach>
            var prsFlag = $('input[name="prsFlag"]').val();
            if('Y' == prsFlag){
                $('div.cgo-content').each(function () {
                    var prgNo = $(this).find('input[name="professionRegoNo"]').val();
                    if(prgNo != null && prgNo != '' && prgNo != undefined){
                        inputReadonly($(this).find('input[name="name"]'));
                    }
                });
            }
        } else if (('APTY005' == appType || 'APTY004' == appType) && '0' == rfiObj) {
            disabledPage();
        } else {
            //rfi

        }
        doEdit();
        //ajac();
        //init end
        if($("#errorMapIs").val()=='error'){
            $('.edit').trigger('click');
        }

        //$('input[name="professionRegoNo"]').trigger('blur');
        updateOtherQualificationMandatory();//75823
        init = 1;
    });

    function updateOtherQualificationMandatory(){
        $('table.assignContent').each(function () {
            var prgNo = $(this).find('input[name="professionRegoNo"]').val();
            var specialty = $(this).find('label.specialty-label').html();
            if(prgNo != undefined && specialty != undefined){
                if(prgNo.trim().length == 0 || specialty.trim().length == 0){
                    $(this).find('span.otherQualificationSpan').html('*');
                }
            }
        })
    }

    var profRegNoBlur = function () {
        $('input[name="professionRegoNo"]').unbind('blur');
        $('input[name="professionRegoNo"]').blur(function(event, action){
            var prgNo = $(this).val();
            var $currContent = $(this).closest('.new-officer-form');
            var $prsLoadingContent = $(this).closest('table.assignContent');
            var specialty = $prsLoadingContent.find('label.specialty-label').html();
            //prs loading
            if(init == 1){
                prdLoading($prsLoadingContent, prgNo, action, null);
            }
            //add Remark For Subspecialty
            if(prgNo.trim().length == 0 || specialty.trim().length == 0){
                $currContent.find('span.otherQualificationSpan').html('*');
            }
        });
    };

    var psnSelect = function () {
        $('select.assignSel').change(function () {
            var $parentEle = $(this).closest('td.first');
            var $CurrentPsnEle = $(this).closest('table.assignContent');
            if(init == 1){
                clearPrsInfo($CurrentPsnEle);
            }
            if ('newOfficer' == $(this).val()) {
                $parentEle.find('> .new-officer-form').removeClass('hidden');
                $parentEle.find('> .profile-info-gp').addClass('hidden');
                unDisabledPartPage($CurrentPsnEle.find('.new-officer-form'));
                if (1 == init) {
                    var emptyData = {};
                    $CurrentPsnEle.find('div.specialtyDiv').html('${SpecialtyHtml}');
                    fillPsnForm($CurrentPsnEle, emptyData, 'CGO');
                    showSpecialty();
                    $CurrentPsnEle.find('input[name="licPerson"]').val('0');
                    $CurrentPsnEle.find('input[name="existingPsn"]').val('0');
                }
            } else if ('-1' == $(this).val()) {
                $parentEle.find('> .profile-info-gp').removeClass('hidden');
                $parentEle.find('> .new-officer-form').addClass('hidden');
                if (1 == init) {
                    var emptyData = {};
                    $CurrentPsnEle.find('div.specialtyDiv').html('${SpecialtyHtml}');
                    fillPsnForm($CurrentPsnEle, emptyData, 'CGO');
                    showSpecialty();
                    $CurrentPsnEle.find('input[name="licPerson"]').val('0');
                    $CurrentPsnEle.find('input[name="existingPsn"]').val('0');
                }
            } else {
                $parentEle.find('> .new-officer-form').removeClass('hidden');
                $parentEle.find('> .profile-info-gp').addClass('hidden');
                if (1 == init) {
                    var arr = $(this).val().split(',');
                    var nationality = arr[0];
                    var idType = arr[1];
                    var idNo = arr[2];
                    loadSelectPsn($CurrentPsnEle, nationality, idType, idNo, 'CGO');
                }
            }
        });
    }

    var showSpecialty = function () {
        $('select.specialty').change(function () {
            var $specialtyEle = $(this).closest('.specialtyContent');
            var val = $(this).val();
            if ('other' == val) {
                $specialtyEle.find('input[name="specialtyOther"]').removeClass('hidden');
            } else {
                $specialtyEle.find('input[name="specialtyOther"]').addClass('hidden');
            }
        });
    };


    $('.addListBtn').click(function () {
        /*var assignContent = $('.assignContent:last').html();
        var appendHtml = '<hr/> <table aria-describedby="" class="testTable">'+ assignContent+'</table>';
        $('.assignContent:last').after(appendHtml);*/
        showWaiting();
        $('.hideen-div').addClass('hidden');
        var number = $('.assign-psn-item').length;
        var addNumber = ${HcsaSvcPersonnel.maximumCount} -number;
        $.ajax({
            url: '${pageContext.request.contextPath}/governance-officer-html',
            dataType: 'json',
            data: {
                "HasNumber": number,
                "AddNumber": addNumber
            },
            type: 'POST',
            success: function (data) {
                console.log(data.res);
                if ('success' == data.res) {
                    $('.cgo-content-point').before(data.sucInfo);
                    showSpecialty();
                    psnSelect();
                    removeCgo();
                    //init font-size
                    $('.cgo-header').css('font-size', "18px");
                    <!--change psn item -->
                    changePsnItem();

                    profRegNoBlur();

                    designationChange();
                    <!--set Scrollbar -->
                    /*$("div.assignSel->ul").mCustomScrollbar({
                            advanced: {
                                updateOnContentResize: true
                            }
                        }
                    );*/
                    //hidden add more
                    var psnLength = $('.assignContent').length - 1;
                    if (psnLength >= '${HcsaSvcPersonnel.maximumCount}') {
                        $('#addPsnDiv').addClass('hidden');
                    }
                    if(psnLength <= '${HcsaSvcPersonnel.mandatoryCount}'){
                        $('.assignContent:last .removeBtn').remove();
                    }
                    initNationality('div.cgo-content:last', 'select[name="idType"]', '.nationalityDiv');
                } else {
                    $('.errorMsg').html(data.errInfo);
                    dismissWaiting();
                }
                dismissWaiting();
            },
            error: function (data) {
                console.log("err");
            }
        });
    });


    var doEdit = function () {
        $('.edit').click(function () {
            var $contentEle = $(this).closest('.assignContent');
            $contentEle.find('input[name="isPartEdit"]').val('1');
            $contentEle.find('.edit-content').addClass('hidden');
            $contentEle.find('input[type="text"]').prop('disabled', false);
            $contentEle.find('div.nice-select').removeClass('disabled');
            $contentEle.find('input[type="text"]').css('border-color', '');
            $contentEle.find('input[type="text"]').css('color', '');
            //get data from page
            var cgoSelectVal = $contentEle.find('select[name="assignSelect"]').val();
            if('-1' != cgoSelectVal && '' != cgoSelectVal){
                $contentEle.find('select[name="assignSelect"] option[value="newOfficer"]').prop('selected', true);
            }
            $('#isEditHiddenVal').val('1');

            var appType = $('input[name="applicationType"]').val();
            var assignSelectVal = $contentEle.find('select[name="assignSelect"]').val();
            var licPerson = $contentEle.find('input[name="licPerson"]').val();
            var needControlName = isNeedControlName(assignSelectVal, licPerson, appType);
            if(needControlName){
                var prgNo = $contentEle.find('input[name="professionRegoNo"]').val();
                if(!isEmpty(prgNo)){
                    inputReadonly($contentEle.find('input[name="name"]'));
                }
            }
        });
    };

    var changePsnItem = function () {
        $('.assign-psn-item').each(function (k, v) {
            $(this).html(k + 1);
        });

    };
    var removeCgo = function () {
        $('.removeBtn').click(function () {
            var assignContentLength = $('table.assignContent').length - 1;
            var $premContentEle = $(this).closest('div.cgo-content');
            if (assignContentLength <= 2) {
                $('hr').remove();
            }
            $premContentEle.remove();
            $('.errorMsg').html("");
            //show add more
            var psnLength = $('div.cgo-content').length;
            if (psnLength < '${HcsaSvcPersonnel.maximumCount}') {
                $('#addPsnDiv').removeClass('hidden');
            }
            $('div.cgo-content').each(function (k,v) {
                $(this).find('.assign-psn-item').html(k+1);
            });

            if(psnLength <= 1){
                $('.assign-psn-item:eq(0)').html('');
            }
            $('#isEditHiddenVal').val('1');
        });

    };
    var designationChange = function () {
        $('.designationSel').unbind('change');
        $('.designationSel').change(function () {
            var thisVal = $(this).val();
            if("DES999" == thisVal){
                $(this).closest('table.assignContent').find('div.otherDesignationDiv').removeClass('hidden');
            }else{
                $(this).closest('table.assignContent').find('div.otherDesignationDiv').addClass('hidden');
            }
        });
    };
    function cancel() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }
</script>