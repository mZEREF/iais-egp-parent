<div class="topByDrug <c:if test="${('0' == appSvcOtherInfoTop.topType) || ('0' == provideTop)}">hidden</c:if>">
    <iais:row>
        <div class="col-xs-12 col-md-10">
            <p class="bold">TOP (BY Drug)&nbsp;<label class="assign-psn-item"><c:if test="${topByDrug.size() > 1}">${index+1}</c:if></label></p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removeTopByDrugBtn">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 text-right removeTopByDrugBtn cursorPointer text-danger"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" name="aboutType" value="Drug">
    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Year."/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="year" name="year${index}" value="${person.year}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="No. of abortions"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="abortNum" name="abortNum${index}" value="${person.abortNum}"/>
        </iais:value>
    </iais:row>
</div>
