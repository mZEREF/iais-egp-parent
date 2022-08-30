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
        <p class="bold">RenalDialysisCentreService Other Information</p>
    </div>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Nurses per Shift"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" cssClass="perShiftNum" name="perShiftNum" value="${n.perShiftNum}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Total number of dialysis stations"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" cssClass="dialysisStationsNum" name="dialysisStationsNum" value="${n.dialysisStationsNum}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" value="Number of Hep B stations"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" cssClass="helpBStationNum" name="helpBStationNum" value="${n.helpBStationNum}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Is the clinic open to general public?"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input nisOpenToPublic" <c:if test="${'1' == n.isOpenToPublic}">checked="checked"</c:if>  type="radio" name="nisOpenToPublic" value = "1" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input nisOpenToPublic" <c:if test="${'0' == n.isOpenToPublic}">checked="checked"</c:if> type="radio" name="nisOpenToPublic" value = "0" aria-invalid="false">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>