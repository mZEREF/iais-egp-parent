<c:set var="nurses" value="${nursesList}"/>
<div class="person-detail nurses">
    <iais:row>
        <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
            <p class="bold">Name, Professional Regn. No. and Qualification of trained nurses&nbsp;
                <label class="assign-psn-item"><c:if test="${nursesList.size() > 1}">${index+1}</c:if>
            </p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removeBtn">
            <h4 class="text-danger text-right">
                <em class="fa fa-times-circle del-size-36 text-right removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <input type="hidden" name="npsnType" value="nurses">
    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Name of trained nurses"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="nname" name="nname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Qualifications"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="nqualification" name="nqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>