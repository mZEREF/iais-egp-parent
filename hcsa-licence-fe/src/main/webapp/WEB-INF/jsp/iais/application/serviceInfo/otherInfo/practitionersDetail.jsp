<div class="practitioners person-detail <c:if test="${'1' != provideTop}">hidden</c:if>">
    <iais:row>
        <div class="col-xs-12 col-md-10">
                <%--                <p class="bold">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion<span class="psnHeader">${index+1}</span></p>--%>
            <p class="bold">Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion
                &nbsp;<label class="assign-psn-item"><c:if test="${practitioners.size() > 1}">${index+1}</c:if></label>
            </p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removePractitionersBtn">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 text-right removePractitionersBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" name="psnType${index}" value="practitioners">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="profRegNo" name="profRegNo${index}" value="${person.profRegNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="NRIC/FIN No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="idNo" name="idNo${index}" value="${person.idNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="regType" name="regType${index}" value="${person.regType}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name of medical practitioner"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="name" name="name${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Specialties"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="speciality" name="speciality${index}" value="${person.speciality}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Qualifications"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="qualification" name="qualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:value width="5" cssClass="col-md-5">
            <label class="form-check-label" >Is the medical practitioners authorised by MOH to perform Abortion
                (if No, please upload a copy of the Obstetrics & Gynaecology certificate and
                <a href="${pageContext.request.contextPath}/co-non-hcsa-template-top" style="color:deepskyblue;cursor:pointer;text-decoration: underline;">From 2</a>
                at the Document page)
                <span class="mandatory">*</span>
            </label>
        </iais:value>

        <input type="hidden" class="medAuthByMohVal" name="medAuthByMohVal${index}" value="${person.medAuthByMoh}"/>
        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input medAuthByMoh" <c:if test="${true == person.medAuthByMoh}">checked="checked"</c:if>  type="radio" name="medAuthByMoh${index}" value = "1" aria-invalid="true">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </iais:value>

        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input medAuthByMoh" <c:if test="${false == person.medAuthByMoh}">checked="checked"</c:if>  type="radio" name="medAuthByMoh${index}" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </iais:value>
    </iais:row>
</div>