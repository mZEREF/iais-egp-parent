<div class="medAlertContent">
    <input type="hidden" class="not-refresh assignSelVal" name="assignSelVal" value="${medAlertPsn.assignSelect}"/>
    <input type="hidden" class="not-refresh licPerson" name="licPerson" value="${medAlertPsn.licPerson ? 1 : 0}"/>
    <input type="hidden" name="isPartEdit" name="isPartEdit" value="0"/>
    <input type="hidden" name="indexNo" value="${medAlertPsn.indexNo}"/>
    <input type="hidden" class="not-refresh psnEditField" name="psnEditField" value="<c:out value="${medAlertPsn.psnEditFieldStr}" />"/>
    <input type="hidden" name="existingPsn" value="0"/>
    <input type="hidden" name="loadingType" value="${medAlertPsn.loadingType}"/>

    <iais:row cssClass="edit-content">
        <c:if test="${canEdit}">
            <div class="text-right app-font-size-16">
                <a class="edit psnEdit" href="javascript:void(0);">
                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
            </div>
        </c:if>
    </iais:row>

    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">MedAlert Person <label class="assign-psn-item"><c:if test="${AppSvcMedAlertPsn.size() > 1}">${status.index+1}</c:if></label></p>
        </div>
        <div class="col-xs-12 col-md-5 text-right">
            <c:if test="${!isRfi}">
                <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 mapDelBtn cursorPointer"></em></h4>
            </c:if>
        </div>
    </iais:row>

    <iais:row>
        <iais:value width="10" cssClass="col-md-10 col-xs-12">
            <span class="error-msg" name="iaisErrorMSg" id="error_medAlertPsnError${index}"></span>
        </iais:value>
    </iais:row>

    <c:if test="${'APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType || requestInformationConfig != null}">
        <iais:row>
            <div class="col-sm-10">
                <label class="control-font-label">
                    <c:if test="${!empty medAlertPsn.name && !empty medAlertPsn.idNo && !empty medAlertPsn.idType}">
                        ${medAlertPsn.name}, ${medAlertPsn.idNo} (<iais:code code="${medAlertPsn.idType}"/>)
                    </c:if>
                </label>
            </div>
        </iais:row>
    </c:if>

    <iais:row cssClass="assignSelDiv ${canEdit && '-1' != medAlertPsn.assignSelect && not empty medAlertPsn.assignSelect ? 'hidden':''}">
        <iais:field width="5" mandatory="true" value="Assign a MedAlert Person"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select cssClass="assignSel" name="assignSelect${index}" options="MedAlertAssignSelect"
                         value="${medAlertPsn.assignSelect}"/>
        </iais:value>
    </iais:row>

    <div class="medAlertPerson hidden">
        <iais:row>
            <iais:field width="5" mandatory="true" value="Name"/>
            <iais:value width="3" cssClass="col-md-3">
                <iais:select cssClass="salutation" name="salutation${index}" firstOption="Please Select"
                             codeCategory="CATE_ID_SALUTATION" value="${medAlertPsn.salutation}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <iais:input maxLength="66" type="text" cssClass="name" name="name${index}" value="${medAlertPsn.name}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="true" value="ID No."/>
            <iais:value width="3" cssClass="col-md-3">
                <iais:select cssClass="idType" name="idType${index}" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE" value="${medAlertPsn.idType}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <iais:input maxLength="20" type="text" cssClass="idNo" name="idNo${index}" value="${medAlertPsn.idNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="" value=""/>
            <iais:value width="7" cssClass="col-md-7 col-xs-12">
                <span class="error-msg" name="iaisErrorMSg" id="error_idTypeNo${index}"></span>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="true" value="Mobile No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="8" type="text" cssClass="mobileNo" name="mobileNo${index}" value="${medAlertPsn.mobileNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" mandatory="true" value="Email Address"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="320" type="text" cssClass="emailAddr" name="emailAddr${index}" value="${medAlertPsn.emailAddr}"/>
            </iais:value>
        </iais:row>
    </div>
    <hr/>
</div>
