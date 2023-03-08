<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="psnType" value="${ApplicationConsts.OTHER_TOP_NURSES}"/>
<div class="person-detail nurses  nperson-content  <c:if test="${'1' != provideTop}">hidden</c:if>" data-prefix="${prefix}">
    <div class="col-xs-12 col-md-12 text-right removeNDiv removeNursesBtn" data-prefix="${prefix}">
        <h4 class="text-danger text-right">
            <em class="fa fa-times-circle del-size-36 text-right removeNursesBtn cursorPointer"></em>
        </h4>
    </div>
    <input type="hidden" class="nisPartEdit" name="${prefix}nisPartEdit${index}" value="0" data-prefix="${prefix}"/>
    <input type="hidden" class="nursesName" name="nursesName${index}" value="${person.name}"/>
    <input type="hidden" class="npsnType" name="${prefix}npsnType${index}" value="${psnType}" data-prefix="${prefix}">
    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType ? true : false}" value="Name of trained nurses" data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="nname" name="${prefix}nname${index}" value="${person.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${'1' != appSvcOtherInfoTop.topType ? true : false}" value="Qualifications" data="${itemData}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" cssClass="nqualification" name="${prefix}nqualification${index}" value="${person.qualification}"/>
        </iais:value>
    </iais:row>
</div>