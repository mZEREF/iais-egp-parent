<iais:row>
    <div class="col-xs-12 col-md-6">
        <p class="bold">AmBulatorySurgicalCentreService Other Information</p>
    </div>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="GFA Value (in sqm)"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="7" type="number" cssClass="agfaValue" name="agfaValue" value="${m.gfaValue}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="I declare that I have met URA's requirements for gross floor area"/>
    <div class="form-check col-md-3">
        <input class="form-check-input" name="ascsDeclaration" value="1"
               type="checkbox" aria-invalid="false"
               <c:if test="${'1' == ascsDeclaration}">checked="checked"</c:if> />
        <label class="form-check-label">
            <span class="check-square"></span><c:out value=""/>
        </label>
    </div>
</iais:row>
