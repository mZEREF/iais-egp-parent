<div class="topByDrugandSurgicalProcedure <c:if test="${'-1' != appSvcOtherInfoTop.topType}">hidden</c:if>">
    <iais:row>
        <div class="col-xs-12 col-md-10">
            <p class="bold">TOP (By Drug and Surgical Procedure)&nbsp;<label class="assign-psn-item"><c:if test="${topBySurgicalProcedure.size() > 1}">${index+1}</c:if></label></p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removeTopByAllBtn">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 text-right removeTopByAllBtn cursorPointer text-danger"></em>
            </h4>
        </div>
    </iais:row>
    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="Year."/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="ayear" name="ayear${index}" value="${person.year}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" mandatory="true" value="No. of abortions"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" cssClass="aabortNum" name="aabortNum${index}" value="${person.abortNum}"/>
        </iais:value>
    </iais:row>
</div>
