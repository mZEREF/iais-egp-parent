<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="psnType" value="${ApplicationConsts.OTHER_TOP_NURSES}"/>
<div class="person-detail nurses  <c:if test="${'1' != provideTop}">hidden</c:if>">
    <iais:row>
        <div class="col-xs-12 col-md-10" style="padding-top: 25px;">
            <p class="bold">Name, Professional Regn. No. and Qualification of trained nurses&nbsp;
                <label class="assign-psn-item"><c:if test="${nurses.size() > 1}">${index+1}</c:if>
            </p>
        </div>
        <div class="col-xs-12 col-md-2 text-right removeNursesBtn">
            <h4 class="text-danger text-right">
                <em class="fa fa-times-circle del-size-36 text-right removeNursesBtn cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <input type="hidden" name="npsnType${index}" value="${psnType}">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Name of trained nurses"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="nname" name="nname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5" mandatory="true" value="Qualifications"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" cssClass="nqualification" name="nqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>