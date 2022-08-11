<div class="practitioners person-detail">
    <input type="hidden" name="psnType" value="practitioners">
    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="Professional Regn. No."/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.profRegNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="NRIC/FIN No."/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.idNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="Type of Registration"/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.regType}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="Name of medical practitioner"/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="Specialties"/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.profRegNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="Qualifications"/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.qualification}" />
        </iais:value>
    </iais:row>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:value width="6" cssClass="col-md-6">
            <label class="form-check-label" >Is the medical practitioners authorised by MOH to perform Abortion
                (if No, please upload a copy of the Obstetrics & Gynaecology certificate and
                From 2 at the Document page)
            </label>
        </iais:value>

        <iais:value width="6" cssClass="col-md-6">
            <c:if test="${true == person.medAuthByMoh}">Yes</c:if>
            <c:if test="${false == person.medAuthByMoh}">No</c:if>
        </iais:value>

    </iais:row>
</div>