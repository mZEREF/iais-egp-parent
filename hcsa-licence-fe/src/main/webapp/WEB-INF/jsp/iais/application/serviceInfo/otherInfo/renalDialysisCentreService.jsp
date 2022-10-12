<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Nurses per Shift"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="2" type="number" cssClass="perShiftNum" name="${prefix}perShiftNum" value="${n.perShiftNum}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Total number of dialysis stations"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="2" type="number" cssClass="dialysisStationsNum" name="${prefix}dialysisStationsNum" value="${n.dialysisStationsNum}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Number of Hep B stations"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="2" type="number" cssClass="helpBStationNum" name="${prefix}helpBStationNum" value="${n.helpBStationNum}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Is the clinic open to general public?"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input nisOpenToPublic" <c:if test="${'1' == n.isOpenToPublic}">checked="checked"</c:if>  type="radio" name="${prefix}nisOpenToPublic" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input nisOpenToPublic" <c:if test="${'0' == n.isOpenToPublic}">checked="checked"</c:if> type="radio" name="${prefix}nisOpenToPublic" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>
<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
    <iais:value width="7" cssClass="col-md-7 col-xs-12">
        <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}nisOpenToPublic"></span>
    </iais:value>
</iais:row>