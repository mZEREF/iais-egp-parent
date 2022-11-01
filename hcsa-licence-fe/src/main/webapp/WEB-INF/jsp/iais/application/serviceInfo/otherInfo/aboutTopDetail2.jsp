<div class="topBySurgicalProcedure
<c:if test="${('1' == appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
    <div class="col-xs-12 col-md-12 text-right removeTopBySurgicalProcedureBtn rdDiv" data-prefix="${prefix}">
        <h4 class="text-danger">
            <em class="fa fa-times-circle del-size-36 text-right removeTopBySurgicalProcedureBtn cursorPointer text-danger"></em>
        </h4>
    </div>
    <input type="hidden" class="isPartEditSurgical" name="${prefix}isPartEditSurgical${index}" value="0" data-prefix="${prefix}"/>
    <input type="hidden" class="topTypeSurgical" name="${prefix}topTypeSurgical${index}" value="${person.topType}" data-prefix="${prefix}"/>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Year"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="4" type="text" cssClass="pyear" name="${prefix}pyear${index}" value="${person.year}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="No. of abortions"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="7" type="text" cssClass="pabortNum" name="${prefix}pabortNum${index}" value="${person.abortNum}"/>
        </iais:value>
    </iais:row>
</div>
