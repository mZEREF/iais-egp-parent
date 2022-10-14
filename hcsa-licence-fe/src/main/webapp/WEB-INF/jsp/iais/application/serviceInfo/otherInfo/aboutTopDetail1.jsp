<div class="topByDrug <c:if test="${('0' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
    <iais:row>
        <div class="col-xs-12 col-md-10">
            <p class="bold">TOP (BY Drug)&nbsp;<label class="assign-psn-item" data-prefix="${prefix}"><c:if test="${topByDrug.size() > 1}">${index+1}</c:if></label></p>
        </div>
        <div class="col-xs-12 col-md-2 text-right rDiv removeTopByDrugBtn" data-prefix="${prefix}">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 text-right removeTopByDrugBtn cursorPointer text-danger"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" name="aboutType" value="Drug">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Year."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="4" type="text" cssClass="year" name="${prefix}year${index}" value="${person.year}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="No. of abortions"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="7" type="text" cssClass="abortNum" name="${prefix}abortNum${index}" value="${person.abortNum}"/>
        </iais:value>
    </iais:row>
</div>
