<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<iais:row>
    <div class="col-xs-12 col-md-6">
        <p class="bold">Yellow Fever Vaccination </p>
    </div>
</iais:row>
<iais:row>
    <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Do you provide Yellow Fever Vaccination Service"/>
    <input type="hidden" class="provideYfVsVal" name="provideYfVsVal" value="${appSvcOtherInfoDto.provideYfVs}"/>
    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideYfVs" <c:if test="${'1' == appSvcOtherInfoDto.provideYfVs}">checked="checked"</c:if>  type="radio" name="${prefix}provideYfVs" value = "1" onclick="provideYfVsBtn('${prefix}','1');" aria-invalid="false" data-prefix="${prefix}">
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
    </iais:value>

    <iais:value width="3" cssClass="form-check col-md-3">
        <input class="form-check-input provideYfVs" <c:if test="${'0' == appSvcOtherInfoDto.provideYfVs}">checked="checked"</c:if>  type="radio" name="${prefix}provideYfVs" value = "0" onclick="provideYfVsBtn('${prefix}','0');" aria-invalid="false" data-prefix="${prefix}">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
    </iais:value>
</iais:row>
<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="5" cssClass="col-md-5" mandatory="" value=""/>
    <iais:value width="7" cssClass="col-md-7 col-xs-12">
        <span class="error-msg" name="iaisErrorMsg" id="error_${prefix}provideYfVs"></span>
    </iais:value>
</iais:row>
<div class="yft <c:if test="${'1' != appSvcOtherInfoDto.provideYfVs}">hidden</c:if>" data-prefix="${prefix}">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Business Name"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:forEach var="docShowDto" items="${currSvcInfoDto.appSvcBusinessDtoList}" varStatus="stat">
                <c:if test="${stat.index != 0}">
                    <c:if test="${stat.index != stat.index-1}">,</c:if>
                </c:if>
                <c:out value="${docShowDto.businessName}"/>
            </c:forEach>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Address"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcOtherInfoDto.premAddress}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Applicant name"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${orgUse.displayName}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Designation"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <iais:code code="${orgUse.designation}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <c:set var="toolMsg"><iais:message  key="NEW_ACK40"/></c:set>
        <iais:field width="5" cssClass="col-md-5" value="Contact number" info="${toolMsg}"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${orgUse.mobileNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Date of Commencement"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker cssClass="yfCommencementDate field-date" name="${prefix}yfCommencementDate"
                             value="${appSvcOtherInfoDto.yfCommencementDateStr}"/>
        </iais:value>
    </iais:row>
</div>
<script>
    function provideYfVsBtn(prefix,value){
        if (value == 0){
            $('.yft[data-prefix="' + prefix + '"]').addClass('hidden');
        }else {
            $('.yft[data-prefix="' + prefix + '"]').removeClass('hidden');
        }
    }
</script>



