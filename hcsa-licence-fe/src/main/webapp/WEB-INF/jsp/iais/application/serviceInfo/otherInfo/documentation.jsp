<iais:row cssClass="edit-content">
    <c:if test="${canEdit}">
        <div class="text-right app-font-size-16">
            <a class="edit psnEdit" href="javascript:void(0);">
                <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
            </a>
        </div>
    </c:if>
</iais:row>
<iais:row>
    <div class="col-xs-12 col-md-6">
        <p class="bold">Documentation</p>
    </div>
</iais:row>
<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Outcome of procedures are recorded "/>
    <input type="hidden" class="isOutcomeProcRecordVal" name="isOutcomeProcRecordVal" value="${appSvcOtherInfoTop.isOutcomeProcRecord}"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input isOutcomeProcRecord" <c:if test="${true == appSvcOtherInfoTop.isOutcomeProcRecord}">checked="checked"</c:if>  type="radio" name="isOutcomeProcRecord" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input isOutcomeProcRecord" <c:if test="${false == appSvcOtherInfoTop.isOutcomeProcRecord}">checked="checked"</c:if>  type="radio" name="isOutcomeProcRecord" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>

<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="5" mandatory="" value=""/>
    <iais:value width="7" cssClass="col-md-7 col-xs-12">
        <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_isOutcomeProcRecordVal"></span>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Number of cases with complications, if any"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="100" type="text" cssClass="compCaseNum" name="compCaseNum" value="${appSvcOtherInfoTop.compCaseNum}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-12" mandatory="false" value="Statistics on abortion (For renewal application only)"/>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-12" mandatory="false" value="Number of abortions performed during the previous 2 years"/>
</iais:row>