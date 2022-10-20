<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="psnType" value="${ApplicationConsts.OTHER_TOP_COUNSELLORS}"/>
<div class="person-detail counsellors  <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
    <iais:row>
        <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
            <p class="bold">Name, Professional Regn. No. and Qualification of certified TOP counsellors&nbsp;
                <label class="assign-psn-item" style="font-weight: bold;!important;" data-prefix="${prefix}"><c:if test="${counsellors.size() > 1}">${index+1}</c:if></label>
            </p>
        </div>

        <div class="col-xs-12 col-md-2 text-right removeCODiv removeBtn" data-prefix="${prefix}">
            <h4 class="text-danger text-right">
                <em class="fa fa-times-circle del-size-36 text-right removeBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>
    <input type="hidden" class="cisPartEdit" name="${prefix}cisPartEdit${index}" value="0" data-prefix="${prefix}"/>
    <input type="hidden" class="counsellorsIndexNo" name="counsellorsIndexNo${index}" value="${person.idNo}"/>
    <input type="hidden" class="cpsnType" name="${prefix}cpsnType${index}" value="${psnType}" data-prefix="${prefix}">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name of certified TOP counsellors(Only Doctor/Nurse)"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" cssClass="cname" name="${prefix}cname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="NRIC/FIN No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="9" type="text" cssClass="cidNo" name="${prefix}cidNo${index}" value="${person.idNo}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Qualifications"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="cqualification" name="${prefix}cqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>