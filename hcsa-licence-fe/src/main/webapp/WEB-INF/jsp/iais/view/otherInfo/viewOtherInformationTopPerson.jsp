<iais:row>
    <div class="col-xs-12">
        <p><strong>Other Information</strong></p>
    </div>
</iais:row>
<iais:row>
    <iais:field width="6" cssClass="col-md-6" value="Please indicate"/>
    <iais:value width="6" cssClass="col-md-6">
        <c:if test="${'1' == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy(Solely by Drug)</c:if>
        <c:if test="${'0' == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy(Solely by Surgical Procedure)</c:if>
        <c:if test="${'-1' == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy(Drug and Surgical Procedure)</c:if>
    </iais:value>
</iais:row>
<c:set var="row_total_practitioners" value="0"></c:set>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}" varStatus="s">
    <c:set var="row_count_practitioners" value="${row_total_practitioners+1}" />
    <c:if test="${person.psnType == 'practitioners'}">
        <c:set var="row_total_practitioners" value="${row_count_practitioners}" />
    </c:if>
</c:forEach>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}" varStatus="status">
    <c:if test="${person.psnType == 'practitioners'}">
        <c:if test="${row_total_practitioners > 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion
                        &nbsp;&nbsp;${person.seqNum+1}:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewPractitionersDetail.jsp"%>
        </c:if>

        <c:if test="${row_total_practitioners == 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion :</strong></p>
                </div>
            </iais:row>
            <%@include file="viewPractitionersDetail.jsp"%>
        </c:if>
    </c:if>
</c:forEach>

<c:set var="row_total_anaesthetists" value="0"></c:set>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}" varStatus="s">
    <c:set var="row_count_anaesthetists" value="${row_total_anaesthetists+1}" />
    <c:if test="${person.psnType == 'anaesthetists'}">
        <c:set var="row_total_anaesthetists" value="${row_count_anaesthetists}" />
    </c:if>
</c:forEach>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}" varStatus="status">
    <c:if test="${person.psnType == 'anaesthetists'}">
        <c:if test="${row_total_anaesthetists > 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;${person.seqNum+1}:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewAnaesthetistsDetail.jsp"%>
        </c:if>
        <c:if test="${row_total_anaesthetists == 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewAnaesthetistsDetail.jsp"%>
        </c:if>
    </c:if>
</c:forEach>

<c:set var="row_total_nurses" value="0" />
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}" varStatus="s">
    <c:set var="row_count_nurses" value="${row_total_nurses+1}" />
    <c:if test="${person.psnType == 'nurses'}">
        <c:set var="row_total_nurses" value="${row_count_nurses}" />
    </c:if>
</c:forEach>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}">
    <c:if test="${person.psnType == 'nurses'}">
        <c:if test="${row_total_nurses > 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of trained nurses&nbsp;${person.seqNum+1}:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewNursesDetail.jsp"%>
        </c:if>

        <c:if test="${row_total_nurses == 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of trained nurses&nbsp;:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewNursesDetail.jsp"%>
        </c:if>

    </c:if>
</c:forEach>

<c:set var="row_total_counsellors" value="0" />
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}" varStatus="s">
    <c:set var="row_count_counsellors" value="${row_total_counsellors+1}" />
    <c:if test="${person.psnType == 'counsellors'}">
        <c:set var="row_total_counsellors" value="${row_count_counsellors}" />
    </c:if>
</c:forEach>
<c:forEach var="person" items="${currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopPersonDtoList}" varStatus="status">
    <c:if test="${person.psnType == 'counsellors'}">
        <c:if test="${row_total_counsellors > 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of certified TOP counsellors&nbsp;${person.seqNum+1}:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewCounsellorsDetail.jsp"%>
        </c:if>

        <c:if test="${row_total_counsellors == 1}">
            <iais:row>
                <div class="col-xs-12">
                    <p><strong>Name, Professional Regn. No. and Qualification of certified TOP counsellors&nbsp;:</strong></p>
                </div>
            </iais:row>
            <%@include file="viewCounsellorsDetail.jsp"%>
        </c:if>
    </c:if>
</c:forEach>

<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="6" cssClass="col-md-6" value="My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)"/>
    <iais:value width="6" cssClass="col-md-6">
        <c:if test="${true == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.hasConsuAttendCourse}">Yes</c:if>
        <c:if test="${false == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.hasConsuAttendCourse}">No</c:if>
    </iais:value>
</iais:row>

<iais:row cssClass="row control control-caption-horizontal">
    <iais:field width="6" cssClass="col-md-6" value="The service provider has the necessary counselling facilities e.g. TV set, video player, video on abortion produced by HPB in different languages and the pamphlets produced by HPB"/>
    <iais:value width="6" cssClass="col-md-6">
        <c:if test="${true == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.isProvideHpb}">Yes</c:if>
        <c:if test="${false == currentPreviewSvcInfo.appSvcOtherInfoDto.appSvcOtherInfoTopDto.isProvideHpb}">No</c:if>
    </iais:value>
</iais:row>
