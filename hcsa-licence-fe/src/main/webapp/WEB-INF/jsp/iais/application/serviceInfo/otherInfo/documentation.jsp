<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="docTop <c:if test="${'1' != provideTop}">hidden</c:if> " data-prefix="${prefix}">
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">Documentation</p>
        </div>
    </iais:row>
    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType}" value="Outcome of procedures are recorded" data="${itemData}"/>
        <input type="hidden" class="outcomeProcRecordVal" name="${prefix}outcomeProcRecordVal" value="${appSvcOtherInfoTop.outcomeProcRecord}"/>
        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input outcomeProcRecord" <c:if test="${true eq appSvcOtherInfoTop.outcomeProcRecord}">checked="checked"</c:if>  type="radio" name="${prefix}outcomeProcRecord" value = "1" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
        </iais:value>

        <iais:value width="3" cssClass="form-check col-md-3">
            <input class="form-check-input outcomeProcRecord" <c:if test="${false eq appSvcOtherInfoTop.outcomeProcRecord}">checked="checked"</c:if>  type="radio" name="${prefix}outcomeProcRecord" value = "0" aria-invalid="false">
            <label class="form-check-label" ><span class="check-circle"></span>No</label>
        </iais:value>
    </iais:row>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
        <iais:value width="7" cssClass="col-md-7 col-xs-12">
            <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}outcomeProcRecord"></span>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType}" value="Number of cases with complications, if any" data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="compCaseNum" name="${prefix}compCaseNum" value="${appSvcOtherInfoTop.compCaseNum}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-12" mandatory="false" value="Statistics on abortion (For renewal application only)"/>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-12" mandatory="false" value="Number of abortions performed during the previous 2 years"/>
    </iais:row>
</div>
