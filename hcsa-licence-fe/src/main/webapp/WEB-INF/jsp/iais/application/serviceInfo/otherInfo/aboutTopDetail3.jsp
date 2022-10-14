<div class="topByDrugandSurgicalProcedure
<c:if test="${('-1' != appSvcOtherInfoTop.topType) || ('0' == provideTop) || (empty appSvcOtherInfoTop.topType)}">hidden</c:if>" data-prefix="${prefix}">
    <iais:row>
        <div class="col-xs-12 col-md-10">
            <p class="bold">TOP (By Drug and Surgical Procedure)&nbsp;<label class="assign-psn-item" data-prefix="${prefix}"><c:if test="${topBySurgicalProcedure.size() > 1}">${index+1}</c:if></label></p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removeTopByAllBtn rTDiv" data-prefix="${prefix}">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 text-right removeTopByAllBtn cursorPointer text-danger"></em>
            </h4>
        </div>
    </iais:row>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Year."/>
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
