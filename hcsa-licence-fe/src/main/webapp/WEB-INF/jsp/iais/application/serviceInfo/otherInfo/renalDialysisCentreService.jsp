<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="otherInfoRDCPageContent">
    <input type="hidden" class ="isPartEdit" name="isPartEdit" value="0"/>
    <input type="hidden" class="otherInfoNurseId" name="otherInfoNurseId" value="${n.id}"/>
    <div class="col-md-12 col-xs-12">
        <div class="edit-content">
            <c:if test="${canEdit}">
                <div class="text-right app-font-size-16">
                    <a class="edit otherInfoRDCEdit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </div>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Nurses per Shift" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" cssClass="perShiftNum" name="${prefix}perShiftNum" value="${n.perShiftNum}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Total number of dialysis stations" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" cssClass="dialysisStationsNum" name="${prefix}dialysisStationsNum" value="${n.dialysisStationsNum}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Number of Hep B stations" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" cssClass="helpBStationNum" name="${prefix}helpBStationNum" value="${n.helpBStationNum}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Is the clinic open to general public?"/>
        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input nisOpenToPublic" <c:if test="${true eq n.openToPublic}">checked="checked"</c:if>  type="radio" name="${prefix}nisOpenToPublic" value = "1" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </iais:value>

        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input nisOpenToPublic" <c:if test="${false eq n.openToPublic}">checked="checked"</c:if> type="radio" name="${prefix}nisOpenToPublic" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </iais:value>
    </iais:row>
    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}nisOpenToPublic"></span>
        </iais:value>
    </iais:row>
</div>
