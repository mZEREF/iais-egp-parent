<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="psnType" value="${ApplicationConsts.OTHER_TOP_ANAESTHETISTS}"/>
<div class="person-detail anaesthetists  aperson-content  <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}" data-prefix="${prefix}">
    <div class="col-xs-12 col-md-12 text-right removeADiv removeAnaesthetistsBtn" data-prefix="${prefix}">
        <h4 class="text-danger text-right ">
            <em class="fa fa-times-circle text-right  text del-size-36 removeAnaesthetistsBtn cursorPointer"></em>
        </h4>
    </div>
    <input type="hidden" class="aisPartEdit" name="${prefix}aisPartEdit${index}" value="0" data-prefix="${prefix}"/>
    <input type="hidden" class="anaesthetistsIndexNo" name="${prefix}anaesthetistsIndexNo${index}" value="${person.idNo}"/>
    <input type="hidden" class="apsnType" name="${prefix}apsnType${index}" value="${psnType}" data-prefix="${prefix}">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType}" value="Professional Regn. No." data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="aprofRegNo" name="${prefix}aprofRegNo${index}" value="${person.profRegNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType ? true : false}" value="NRIC/FIN No." data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="9" type="text" cssClass="idANo" name="${prefix}idANo${index}" value="${person.idNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType ? true : false}" value="Type of Registration" data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" cssClass="aregType" name="${prefix}aregType${index}" value="${person.regType}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType ? true : false}" value="Name of anaesthetists" data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="aname" name="${prefix}aname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType ? true : false}" value="Qualifications" data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="aqualification" name="${prefix}aqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>