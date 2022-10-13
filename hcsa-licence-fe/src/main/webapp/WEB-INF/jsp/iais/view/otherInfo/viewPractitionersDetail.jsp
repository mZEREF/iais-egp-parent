<div class="practitioners person-detail">
    <input type="hidden" name="psnType" value="practitioners">
    <iais:row>
        <iais:field width="5" value="Professional Regn. No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.profRegNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="NRIC/FIN No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.idNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Type of Registration"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${person.regType}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Name of medical practitioner"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Specialties"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${person.speciality}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Qualifications"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.qualification}" />
        </iais:value>
    </iais:row>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" value="Is the medical practitioners authorised by MOH to perform Abortion
                (if No, please upload a copy of the Obstetrics & Gynaecology certificate and
                From 2 at the Document page)"/>
        <iais:value width="3" cssClass="col-md-7">
            <c:if test="${'1' == person.medAuthByMoh}">Yes</c:if>
            <c:if test="${'0' == person.medAuthByMoh}">No</c:if>
        </iais:value>

    </iais:row>
</div>