<div class="amended-service-info-gp ">
    <div class="amend-preview-info form-horizontal min-row ">
        <iais:row>
            <iais:field width="5"  value="Do you provide Termination of Pregnancy"/>
            <iais:value width="3" cssClass="col-md-3">
                <c:if test="${'1' == appSvcOtherInfoDto.provideTop}">Yes</c:if>
                <c:if test="${'0' == appSvcOtherInfoDto.provideTop}">No</c:if>
            </iais:value>
        </iais:row>

        <div class="personTop <c:if test="${'0' == appSvcOtherInfoDto.provideTop}">hidden</c:if>">
            <iais:row>
                <iais:field width="5"  value="Please indicate"/>
                <iais:value width="3" cssClass="col-md-3">
                    <c:if test="${'1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy(Solely by Drug)</c:if>
                    <c:if test="${'0' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy(Solely by Surgical Procedure)</c:if>
                    <c:if test="${'-1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">Termination of Pregnancy(Drug and Surgical Procedure)</c:if>
                </iais:value>
            </iais:row>
            <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonPractitionersList}" varStatus="status">
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Name, Professional Regn. No. and Qualification of medical practitioners authorised to perform Abortion&nbsp;<c:if test="${appSvcOtherInfoDto.otherInfoTopPersonPractitionersList.size()>1}">${status.index+1}:</c:if></strong></p>
                    </div>
                </iais:row>
                <%@include file="viewPractitionersDetail.jsp"%>
            </c:forEach>

            <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonAnaesthetistsList}" varStatus="astatus">
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Name, Professional Regn. No. and Qualification of anaesthetists&nbsp;<c:if test="${appSvcOtherInfoDto.otherInfoTopPersonAnaesthetistsList.size()>1}">${astatus.index+1}:</c:if></strong></p>
                    </div>
                </iais:row>
                <%@include file="viewAnaesthetistsDetail.jsp"%>
            </c:forEach>

            <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonNursesList}" varStatus="nstatus">
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Name and Qualification of trained nurses&nbsp;<c:if test="${appSvcOtherInfoDto.otherInfoTopPersonNursesList.size()>1}">${nstatus.index+1}:</c:if></strong></p>
                    </div>
                </iais:row>
                <%@include file="viewNursesDetail.jsp"%>
            </c:forEach>

            <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoTopPersonCounsellorsList}" varStatus="cstatus">
                <iais:row>
                    <div class="col-xs-12">
                        <p><strong>Name, Professional Regn. No. and Qualification of certified TOP counsellors&nbsp;<c:if test="${appSvcOtherInfoDto.otherInfoTopPersonCounsellorsList.size()>1}">${cstatus.index+1}:</c:if></strong></p>
                    </div>
                </iais:row>
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
                    <c:if test="${true == appSvcOtherInfoDto.appSvcOtherInfoTopDto.isProvideHpb}">Yes</c:if>
                    <c:if test="${false == appSvcOtherInfoDto.appSvcOtherInfoTopDto.isProvideHpb}">No</c:if>
                </iais:value>
            </iais:row>
        </div>
    </div>
</div>






