<div class="personnel-content" id="personnelRemoveId${status.index}">

    <input type="hidden" class="not-refresh " name="${logo}speCount" value="size"/>

    <input type="hidden" name="indexNo" value="${appSvcPersonnelDto.indexNo}"/>


    <iais:row cssClass="personnel-header">

        <iais:value width="5" cssClass="col-xs-12 col-md-6">
            <strong>
                <c:out value="Service Personnel "/>
                <label class="assign-psn-item"></label>
            </strong>
        </iais:value>
        <iais:value width="7" cssClass="col-xs-12 col-md-4 text-right">

            <div class=" removeBtn <c:if test="${index == 0}">hidden</c:if>">
                <h4 class="text-danger">
                    <em class="fa fa-times-circle del-size-36 cursorPointer"></em>
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

            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:select cssClass="personnelSel" name="personnelSel" options="ServicePersonnelType"
                             value="${appSvcPersonnelDto.personnelType}" firstOption="Please Select"></iais:select>
                <span class="error-msg" name="iaisErrorMsg" id="error_personnelSelErrorMsg${status.index}"></span>
            </iais:value>
        </div>
    </iais:row>

    <div class="new-svc-personnel-form">

        <%--        name--%>
        <iais:row cssClass="personnel-name hidden ">
            <iais:field width="5" mandatory="true" value="Name" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <input type="hidden" name="prsLoading" value="${appSvcPersonnelDto.prsLoading}"/>
                <iais:input maxLength="66" type="text" name="name" value="${appSvcPersonnelDto.name}"></iais:input>
                <span class="error-msg" name="iaisErrorMsg" id="error_name${status.index}"></span>
            </iais:value>
        </iais:row>
        <%--   designation --%>
        <iais:row cssClass="personnel-designation hidden ">
            <iais:field width="5" mandatory="true" value="Designation" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:select cssClass="designation" name="designation" options="NuclearMedicineImagingDesignation"
                             value="${appSvcPersonnelDto.designation}" firstOption="Please Select"></iais:select>
                <span class="error-msg" name="iaisErrorMsg" id="error_designation${status.index}"></span>
            </iais:value>
        </iais:row>
        <iais:row cssClass="${appSvcPersonnelDto.designation !='Others' ? 'hidden' : ''} otherDesignationDiv">
            <div class="personnel-designation hidden ">
                <iais:field width="5" mandatory="true" value="otherDesignation" cssClass="col-sm-5"/>
                <iais:value width="7" cssClass="col-sm-5 col-md-7">
                    <iais:input maxLength="100" type="text" cssClass="otherDesignation" name="otherDesignation"
                                value="${appSvcPersonnelDto.otherDesignation}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_otherDesignation${status.index}"></span>
                </iais:value>

            </div>
        </iais:row>

        <%--    qualification--%>
        <iais:row cssClass="personnel-qualification hidden ">
            <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="100" type="text" name="qualification"
                            value="${appSvcPersonnelDto.qualification}"></iais:input>
                <span class="error-msg" name="iaisErrorMsg" id="error_qualification${status.index}"></span>
            </iais:value>
        </iais:row>

        <%--    regnNo--%>
        <iais:row cssClass="personnel-regnNo hidden ">
            <iais:field width="5" mandatory="true" value="Professional Regn. No. " cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="20" type="text" name="regnNo" value="${appSvcPersonnelDto.profRegNo}"
                            onblur="aaa(this)"></iais:input>
                <span class="error-msg" name="iaisErrorMsg" id="error_regnNo${status.index}"></span>
            </iais:value>
        </iais:row>

        <%--    years--%>
        <iais:row cssClass="personnel-wrkExpYear hidden ">
            <iais:field width="5" mandatory="true" value="Relevant working experience (Years)" cssClass="col-sm-5"/>
            <iais:value width="7" cssClass="col-sm-5 col-md-7">
                <iais:input maxLength="2" type="text" name="wrkExpYear"
                            value="${appSvcPersonnelDto.wrkExpYear}"></iais:input>
                <span class="error-msg" name="iaisErrorMsg" id="error_wrkExpYear${status.index}"></span>
            </iais:value>
        </iais:row>

    </div>
</div>