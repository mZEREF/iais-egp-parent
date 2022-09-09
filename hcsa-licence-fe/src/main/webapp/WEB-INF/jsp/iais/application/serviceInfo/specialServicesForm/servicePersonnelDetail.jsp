<div class="personnel-content">
    <input type="hidden"  class="personTypeToShow" name="${prefix}personTypeToShow${index}" value="${personTypeToShow}"/>
    <iais:row cssClass="personnel-header">
        <div class="col-xs-12 col-md-6">
            <p class="bold">${title} <label class="assign-psn-item">${index+1}</label></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_${prefix}personError${index}"></span></p>
        </div>
        <div class="col-xs-12 col-md-6 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtns cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <iais:row>
        <c:if test="${'true' == canEdit}">
            <div class="text-right app-font-size-16">
                <a id="edit" class="svcPsnEdit" href="javascript:void(0);">
                    <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                </a>
            </div>
        </c:if>
    </iais:row>
    <input type="hidden" name="isPartEdit" value="0"/>
    <iais:row cssClass="control  svcPsnSel">
        <div class="personnel-sel">
            <iais:field width="5" mandatory="true" value="Select Service Personnel" cssClass="col-sm-5 col-md-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:select cssClass="personnelType" name="${prefix}personnelType${index}" options="${personSelect}"
                             value="${appSvcPersonnelDto.personnelType}" firstOption="Please Select"></iais:select>
            </iais:value>
        </div>
    </iais:row>
    <div class="new-svc-personnel-form">
        <%--        name--%>
        <iais:row cssClass="personnel-name hidden">
            <iais:field width="5" cssClass="col-sm-5 col-md-5" mandatory="true" value="Name"/>
            <iais:value width="3" cssClass="col-md-3">
                <iais:select cssClass="salutation" name="${prefix}salutation${index}" firstOption="Please Select"
                             codeCategory="CATE_ID_SALUTATION" value="${appSvcPersonnelDto.salutation}"/>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <iais:input cssClass="name" maxLength="66" type="text" name="${prefix}name${index}"
                            value="${appSvcPersonnelDto.name}"/>
            </iais:value>
        </iais:row>

        <%--    qualification--%>
        <iais:row cssClass="personnel-qualification hidden">
            <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5 col-md-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="100" cssClass="qualification" type="text" name="${prefix}qualification${index}"
                            value="${appSvcPersonnelDto.qualification}"></iais:input>
            </iais:value>
        </iais:row>
        <%--    years--%>
        <iais:row cssClass="personnel-wrkExpYear hidden">
            <iais:field width="5" mandatory="true" value="Relevant working experience (Years)" cssClass="col-sm-5 col-md-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="2" cssClass="wrkExpYear" type="text" name="${prefix}wrkExpYear${index}"
                            value="${appSvcPersonnelDto.wrkExpYear}"></iais:input>
            </iais:value>
        </iais:row>
    </div>
</div>