<div class="otherInfoASCSContent">
    <input type="hidden" class ="isPartEdit" name="isPartEdit" value="0"/>
    <input type="hidden" class="otherInfoMedASCId" name="otherInfoMedASCId" value="${m.id}"/>
    <div class="col-md-12 col-xs-12">
        <div class="edit-content">
            <c:if test="${canEdit}">
                <div class="text-right app-font-size-16">
                    <a class="edit otherInfoASCSEdit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </div>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="GFA Value (in sqm)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="7" type="text" cssClass="agfaValue" name="${prefix}agfaValue" value="${m.gfaValue}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="I declare that I have met URA's requirements for gross floor area"/>
        <div class="form-check col-md-3">
            <input class="form-check-input" name="${prefix}ascsDeclaration" value="0"
                   type="checkbox" aria-invalid="false"
                   <c:if test="${'0' == ascsDeclaration}">checked="checked"</c:if> />
            <label class="form-check-label">
                <span class="check-square"></span><c:out value=""/>
            </label>
        </div>
    </iais:row>
    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}ascsDeclaration"></span>
        </iais:value>
    </iais:row>
</div>
