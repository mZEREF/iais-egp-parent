<div class="person-detail anaesthetists  <c:if test="${'1' != provideTop}">hidden</c:if>">
    <iais:row>
        <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
            <p class="bold">Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;
                <label class="assign-psn-item"><c:if test="${anaesthetists.size() > 1}">${index+1}</c:if></label>
            </p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removeAnaesthetistsBtn">
            <h4 class="text-danger text-right ">
                <em class="fa fa-times-circle text-right  text del-size-36 removeAnaesthetistsBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <input type="hidden" name="apsnType${index}" value="anaesthetists">
    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="aprofRegNo" name="aprofRegNo${index}" value="${person.profRegNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="NRIC/FIN No."/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="idANo" name="idANo${index}" value="${person.idNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Type of Registration"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="aregType" name="aregType${index}" value="${person.regType}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Name of anaesthetists"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="aname" name="aname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Qualifications"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="aqualification" name="aqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>