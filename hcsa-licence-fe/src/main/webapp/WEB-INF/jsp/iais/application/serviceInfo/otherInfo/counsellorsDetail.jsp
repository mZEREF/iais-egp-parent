<c:set var="counsellors" value="${counsellorsList}"/>
<div class="person-detail counsellors">
    <iais:row>
        <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
            <p class="bold">Name, Professional Regn. No. and Qualification of certified TOP counsellors&nbsp;
                <label class="assign-psn-item"><c:if test="${counsellorsList.size() > 1}">${index+1}</c:if></label>
            </p>
        </div>

        <div class="col-xs-12 col-md-2 text-right removeBtn">
            <h4 class="text-danger text-right">
                <em class="fa fa-times-circle del-size-36 text-right removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <input type="hidden" name="cpsnType" value="counsellors">
    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Name of certified TOP counsellors(Only Doctor/Nurse)"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="cname" name="cname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="NRIC/FIN No."/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="cidNo" name="cidNo${index}" value="${person.idNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Qualifications"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="cqualification" name="cqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>