<div class="topByDrugandSurgicalProcedure
<c:if test="${('-1' != appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
    <div class="col-xs-12 col-md-12 text-right removeTopByAllBtn rTDiv" data-prefix="${prefix}">
        <h4 class="text-danger">
            <em class="fa fa-times-circle del-size-36 text-right removeTopByAllBtn cursorPointer text-danger"></em>
        </h4>
    </div>
    <input type="hidden" class="isPartEditAll" name="${prefix}isPartEditAll${index}" value="0" data-prefix="${prefix}"/>
    <input type="hidden" class="topTypeAll" name="${prefix}topTypeAll${index}" value="${person.topType}" data-prefix="${prefix}"/>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Year"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="4" type="text" cssClass="ayear" name="${prefix}ayear${index}" value="${person.year}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="No. of abortions"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="7" type="text" cssClass="aabortNum" name="${prefix}aabortNum${index}" value="${person.abortNum}"/>
        </iais:value>
    </iais:row>
</div>
