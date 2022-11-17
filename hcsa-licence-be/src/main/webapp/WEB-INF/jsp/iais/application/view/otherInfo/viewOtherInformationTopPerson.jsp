<iais:row>
    <iais:field width="5"  value="Do you provide Termination of Pregnancy"/>
    <iais:value width="3" cssClass="col-md-7" display="true">
        <c:if test="${'1' == appSvcOtherInfoDto.provideTop}">Yes</c:if>
        <c:if test="${'0' == appSvcOtherInfoDto.provideTop}">No</c:if>
    </iais:value>
</iais:row>

<c:if test="${'1' == appSvcOtherInfoDto.provideTop}">
    <iais:row>
        <iais:field width="5"  value="Please indicate"/>
        <iais:value width="3" cssClass="col-md-3" display="true">
            <c:if test="${'1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Solely by Drug)</c:if>
            <c:if test="${'0' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Solely by Surgical Procedure)</c:if>
            <c:if test="${'-1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy (Drug and Surgical Procedure)</c:if>
        </iais:value>
    </iais:row>
    <div class="">
        <p><strong>Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion</strong></p>
    </div>
    <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonPractitionersList}" varStatus="status">
        <%@include file="viewPractitionersDetail.jsp"%>
    </c:forEach>
    <div class="">
        <p><strong>Name, Professional Regn. No. and Qualification of anaesthetists</strong></p>
    </div>
    <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonAnaesthetistsList}" varStatus="astatus">
        <%@include file="viewAnaesthetistsDetail.jsp"%>
    </c:forEach>

    <div class="">
        <p><strong>Name and Qualifications of trained nurses</strong></p>
    </div>
    <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonNursesList}" varStatus="nstatus">
        <%@include file="viewNursesDetail.jsp"%>
    </c:forEach>
    <div class="">
        <p><strong>Name and Qualifications of certified TOP counsellors</strong></p>
    </div>
    <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonCounsellorsList}" varStatus="cstatus">
        <%@include file="viewCounsellorsDetail.jsp"%>
    </c:forEach>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" value="My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)"/>
        <iais:value width="3" cssClass="col-md-7">
            <c:if test="${true == appSvcOtherInfoDto.appSvcOtherInfoTopDto.hasConsuAttendCourse}">Yes</c:if>
            <c:if test="${false == appSvcOtherInfoDto.appSvcOtherInfoTopDto.hasConsuAttendCourse}">No</c:if>
        </iais:value>
    </iais:row>

    <iais:row cssClass="row control control-caption-horizontal">
        <iais:field width="5" value="The service provider has the necessary counselling facilities e.g. TV set, video player, video on abortion produced by HPB in different languages and the pamphlets produced by HPB"/>
        <iais:value width="3" cssClass="col-md-7">
            <c:if test="${true == appSvcOtherInfoDto.appSvcOtherInfoTopDto.provideHpb}">Yes</c:if>
            <c:if test="${false == appSvcOtherInfoDto.appSvcOtherInfoTopDto.provideHpb}">No</c:if>
        </iais:value>
    </iais:row>
</c:if>






