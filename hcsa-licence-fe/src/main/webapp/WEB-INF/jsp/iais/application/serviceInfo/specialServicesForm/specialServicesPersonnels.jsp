<div class="personnel-content">
    <input type="hidden" class="not-refresh prepsn" name="${psnContent}" value="${prefix}"/>
    <input type="hidden" class="not-refresh specialPerson" value="1"/>
    <input type="hidden" class="isPartEdit" name="${prefix}isPartEdit${index}" value="0"/>
    <input type="hidden" class="indexNo" name="${prefix}indexNo${index}" value="${appSvcPersonnelDto.indexNo}"/>
    <iais:row>
        <div class="col-md-12 col-xs-12 edit-content">
            <c:if test="${'true' == canEdit}">
                <input type="hidden" class="isPartEdit" name="${status.index}isPartEdit${index}" value="0"/>
                <div class="text-right app-font-size-16">
                    <a class="edit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </iais:row>
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">${title} <label class="assign-psn-item"><strong>${index+1}</strong></label></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_${prefix}personError${index}"></span></p>
        </div>
        <div class="col-xs-12 col-md-6 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtns cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <input type="hidden" name="isPartEdit" value="0"/>
    <%--    name--%>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select cssClass="salutation" name="${prefix}salutation${index}" firstOption="Please Select" codeCategory="CATE_ID_SALUTATION" value="${appSvcPersonnelDto.salutation}"/>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="100" type="text" cssClass="name" name="${prefix}name${index}" value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>

    <c:if test="${type == 'ro'}">
        <iais:row>
            <iais:field value="Is the RO employed on a full-time basis?" cssClass="col-md-5" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3 form-check">
                <input
                        <c:if test="${'1'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="1"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4 form-check">
                <input
                        <c:if test="${'0'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="0"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </iais:value>
            <iais:value cssClass="col-md-offset-5 col-md-8 col-xs-12">
                <span class="error-msg " name="iaisErrorMsg" id="error_${prefix}employedBasis${index}"></span>
            </iais:value>
        </iais:row>

        <%--           Relevant working experience(Years) --%>
        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Relevant working experience (Years)"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${prefix}wrkExpYear${index}"
                            value="${appSvcPersonnelDto.wrkExpYear}"/>
            </iais:value>
        </iais:row>

        <%--    SMC Registration No.--%>
        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="SMC Registration No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input cssClass="regnNo" maxLength="20" type="text" name="${prefix}regnNo${index}"
                            value="${appSvcPersonnelDto.regnNo}"/>
            </iais:value>
        </iais:row>

    </c:if>

    <c:if test="${type == 'md'}">
        <iais:row>
            <iais:field value="Is the Medical Dosimetrist employed on a full-time basis?" cssClass="col-md-5" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3 form-check">
                <input
                        <c:if test="${'1'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="1"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4 form-check">
                <input
                        <c:if test="${'0'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="0"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </iais:value>
            <iais:value cssClass="col-md-offset-5 col-md-8 col-xs-12">
                <span class="error-msg " name="iaisErrorMsg" id="error_${prefix}employedBasis${index}"></span>
            </iais:value>
        </iais:row>
    </c:if>

    <c:if test="${type == 'rt'}">
        <iais:row>
            <iais:field value="Is the RT employed on a full-time basis?" cssClass="col-md-5" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3 form-check">
                <input
                        <c:if test="${'1'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="1"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4 form-check">
                <input
                        <c:if test="${'0'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="0"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </iais:value>
            <iais:value cssClass="col-md-offset-5 col-md-8 col-xs-12">
                <span class="error-msg " name="iaisErrorMsg" id="error_${prefix}employedBasis${index}"></span>
            </iais:value>
        </iais:row>

        <%--    AHPC Registration No.--%>
        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="AHPC Registration No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input cssClass="regnNo" maxLength="20" type="text" name="${prefix}regnNo${index}"
                            value="${appSvcPersonnelDto.regnNo}"/>
            </iais:value>
        </iais:row>
    </c:if>

    <c:if test="${type == 'cqmp'}">
        <iais:row>
            <iais:field value="Is the CQMP employed on a full-time basis?" cssClass="col-md-5" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3 form-check">
                <input
                        <c:if test="${'1'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="1"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4 form-check">
                <input
                        <c:if test="${'0'==appSvcPersonnelDto.employedBasis}">checked="checked"</c:if>
                        class="form-check-input ROMDRT" type="radio" name="${prefix}employedBasis${index}"
                        value="0"
                        aria-invalid="false">
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </iais:value>
            <iais:value cssClass="col-md-offset-5 col-md-8 col-xs-12">
                <span class="error-msg " name="iaisErrorMsg" id="error_${prefix}employedBasis${index}"></span>
            </iais:value>
        </iais:row>
    </c:if>

    <c:if test="${type == 'nic'}">
        <%--    Professional Regn. No--%>
        <iais:row>
            <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Regn. No."/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="20" type="text" cssClass="profRegNo" name="${prefix}profRegNo${index}"
                            value="${appSvcPersonnelDto.profRegNo}"/>
            </iais:value>
        </iais:row>
    </c:if>
</div>
