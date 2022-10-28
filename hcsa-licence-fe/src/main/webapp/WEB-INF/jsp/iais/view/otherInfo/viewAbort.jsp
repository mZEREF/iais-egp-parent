<c:if test="${'1' == appSvcOtherInfoDto.provideTop}">
    <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoAbortDrugList}" varStatus="status">
        <c:if test="${'0' != appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
            <c:if test="${'1' == person.topType}">
                <div class="topByDrug <c:if test="${'0' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                    <iais:row>
                        <div class="col-xs-12 col-md-10">
                            <p class="bold">
                                TOP (By Drug)
                                <c:if test="${appSvcOtherInfoDto.otherInfoAbortDrugList.size() > 1}">${status.index+1}</c:if>
                            </p>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Year"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${person.year}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="No. of abortions"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${person.abortNum}" />
                        </iais:value>
                    </iais:row>
                </div>
            </c:if>
        </c:if>
    </c:forEach>

    <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoAbortSurgicalProcedureList}" varStatus="pstatus">
        <c:if test="${'1' != appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
            <c:if test="${'0' == person.topType}">
                <div class="topBySurgicalProcedure <c:if test="${'1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                    <iais:row>
                        <div class="col-xs-12 col-md-10">
                            <p class="bold">TOP (By Surgical Procedure)&nbsp;<c:if test="${appSvcOtherInfoDto.otherInfoAbortSurgicalProcedureList.size() > 1}">${pstatus.index+1}</c:if></p>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Year"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${person.year}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="No. of abortions"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${person.abortNum}" />
                        </iais:value>
                    </iais:row>
                </div>
            </c:if>
        </c:if>
    </c:forEach>

    <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoAbortDrugAndSurgicalList}" varStatus="astatus">
        <c:if test="${'-1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
            <c:if test="${'-1' == person.topType}">
                <div class="topByDrugandSurgicalProcedure">
                    <iais:row>
                        <div class="col-xs-12 col-md-10">
                            <p class="bold">TOP (By Drug and Surgical Procedure)&nbsp;<c:if test="${appSvcOtherInfoDto.otherInfoAbortDrugAndSurgicalList.size() > 1 }">${astatus.index+1}</c:if></p>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Year"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${person.year}" />
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="No. of abortions"/>
                        <iais:value width="3" cssClass="col-md-7" display="true">
                            <c:out value="${person.abortNum}" />
                        </iais:value>
                    </iais:row>
                </div>
            </c:if>
        </c:if>
    </c:forEach>

    <iais:row>
        <div class="col-xs-12 col-md-12">
            <p class="bold">Declaration</p>
        </div>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="I declare that I have met URA's requirements for gross floor area"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            Yes
        </iais:value>
    </iais:row>
</c:if>
