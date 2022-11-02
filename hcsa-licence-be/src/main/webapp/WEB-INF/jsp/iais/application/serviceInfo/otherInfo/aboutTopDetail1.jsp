<div class="topByDrug <c:if test="${('0' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
    <div class="col-xs-12 col-md-12 text-right rDiv removeTopByDrugBtn" data-prefix="${prefix}">
        <h4 class="text-danger">
            <em class="fa fa-times-circle del-size-36 text-right removeTopByDrugBtn cursorPointer text-danger"></em>
        </h4>
    </div>
    <input type="hidden" class="isPartEditDrug" name="${prefix}isPartEditDrug${index}" value="0" data-prefix="${prefix}"/>
    <input type="hidden" class="topTypeDrug" name="${prefix}topTypeDrug${index}" value="${person.topType}" data-prefix="${prefix}"/>
    <input type="hidden" name="aboutType" value="Drug">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Year"/>
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
