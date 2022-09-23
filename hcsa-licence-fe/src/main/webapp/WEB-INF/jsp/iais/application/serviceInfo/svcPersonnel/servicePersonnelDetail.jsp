<div class="personnel-content">
    <input type="hidden" class="not-refresh not-clear" name="${logo}speCount" value="size"/>
    <input type="hidden" name="indexNo" value="${appSvcPersonnelDto.indexNo}"/>
    <iais:row cssClass="personnel-header">
        <iais:value width="5" cssClass="col-xs-12 col-md-6">
            <strong>
                <c:out value="ServicePersonnel "/>
                <label class="assign-psn-item">${index+1}</label>
            </strong>
        </iais:value>
        <iais:value width="7" cssClass="col-xs-12 col-md-4 text-right">
            <span class="error-msg" name="iaisErrorMSg" id="error_personError${index}"></span>
            <div class="removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
                <h4 class="text-danger">
                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                </h4>
            </div>
        </iais:value>
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
            <iais:field width="5" mandatory="true" value="Select Service Personnel" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select cssClass="personnelType" name="personnelType${index}" options="ServicePersonnelType"
                             value="${appSvcPersonnelDto.personnelType}" firstOption="Please Select"></iais:select>
            </iais:value>
        </div>
    </iais:row>
    <div class="new-svc-personnel-form">
        <%--        name--%>
        <iais:row cssClass="personnel-name hidden ">
            <iais:field width="5" mandatory="true" value="Name" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="66" cssClass="name" type="text" name="name${index}"
                            value="${appSvcPersonnelDto.name}"></iais:input>
            </iais:value>
        </iais:row>


        <iais:row cssClass="personnel-designation hidden ">
            <iais:field width="5" mandatory="true" value="Designation" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:select cssClass="designation" name="designation${index}"
                             options="NuclearMedicineImagingDesignation"
                             value="${appSvcPersonnelDto.designation}" firstOption="Please Select"></iais:select>
            </iais:value>
        </iais:row>

        <iais:row cssClass="${appSvcPersonnelDto.designation=='Others' ? '' : 'hidden'} otherDesignationDiv">
            <iais:field width="5" cssClass="col-sm-5" value="OtherDesignation" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" cssClass="otherDesignation"
                            name="otherDesignation${index}"
                            value="${appSvcPersonnelDto.otherDesignation}"/>
            </iais:value>
        </iais:row>

        <%--    qualification--%>
        <iais:row cssClass="personnel-qualification hidden ">
            <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="100" cssClass="qualification" type="text" name="qualification${index}"
                            value="${appSvcPersonnelDto.qualification}"></iais:input>
            </iais:value>
        </iais:row>
        <%--    regnNo--%>
        <iais:row cssClass="personnel-regnNo hidden ">
            <iais:field width="5" mandatory="true" value="Professional Regn. No. " cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="20" cssClass="profRegNo" type="text" name="profRegNo${index}"
                            value="${appSvcPersonnelDto.profRegNo}"></iais:input>
            </iais:value>
        </iais:row>
        <%--    years--%>
        <iais:row cssClass="personnel-wrkExpYear hidden ">
            <iais:field width="5" mandatory="true" value="Relevant working experience (Years)"
                        cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="2" cssClass="wrkExpYear" type="text" name="wrkExpYear${index}"
                            value="${appSvcPersonnelDto.wrkExpYear}"></iais:input>
            </iais:value>
        </iais:row>
    </div>
</div>