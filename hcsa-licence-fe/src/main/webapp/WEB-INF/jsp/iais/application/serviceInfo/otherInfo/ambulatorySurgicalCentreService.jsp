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
        <p class="bold">AmBulatorySurgicalCentreService Other Information</p>
    </div>
</iais:row>

<iais:row>
    <iais:field width="3" cssClass="col-md-6" mandatory="true" value="GFA Value (in sqm)"/>
    <iais:value width="4" cssClass="col-md-6">
        <iais:input maxLength="20" type="text" cssClass="gfaValue" name="gfaValue" value="${med.gfaValue}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="3" cssClass="col-md-6" mandatory="true" value="I declare that I have met URA's requirements for gross floor area"/>
    <iais:value width="4" cssClass="col-md-6">
        <input class="form-check-input requirements"  type="checkbox" checked="checked" name="requirements">
    </iais:value>
</iais:row>