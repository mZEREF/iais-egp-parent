<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="psnType" value="${ApplicationConsts.OTHER_TOP_ANAESTHETISTS}"/>
<div class="person-detail anaesthetists  <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}" data-prefix="${prefix}">
    <iais:row>
        <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
            <p class="bold">Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;
                <label class="assign-psn-item" data-prefix="${prefix}"><c:if test="${anaesthetists.size() > 1}">${index+1}</c:if></label>
            </p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removeADiv removeAnaesthetistsBtn" data-prefix="${prefix}">
            <h4 class="text-danger text-right ">
                <em class="fa fa-times-circle text-right  text del-size-36 removeAnaesthetistsBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <input type="hidden" class="apsnType" name="${prefix}apsnType${index}" value="${psnType}" data-prefix="${prefix}">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Professional Regn. No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="aprofRegNo" name="${prefix}aprofRegNo${index}" value="${person.profRegNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="NRIC/FIN No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="9" type="text" cssClass="idANo" name="${prefix}idANo${index}" value="${person.idNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Type of Registration"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" cssClass="aregType" name="${prefix}aregType${index}" value="${person.regType}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name of anaesthetists"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" cssClass="aname" name="${prefix}aname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Qualifications"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="aqualification" name="${prefix}aqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>