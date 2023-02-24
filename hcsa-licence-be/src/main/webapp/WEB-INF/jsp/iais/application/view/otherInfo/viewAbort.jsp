<c:if test="${'1' == appSvcOtherInfoDto.provideTop}">
    <c:if test="${'1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType || '-1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
        <div class="">
            <p class="bold">
                TOP (By Drug)
            </p>
        </div>
        <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoAbortDrugList}" varStatus="status">
            <c:if test="${'0' != appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
                <c:if test="${'1' == person.topType}">
                    <div class="topByDrug">
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
    </c:if>

    <c:if test="${'0' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType || '-1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
        <div class="">
            <p class="bold">TOP (By Surgical Procedure)</p>
        </div>
        <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoAbortSurgicalProcedureList}" varStatus="pstatus">
            <c:if test="${'1' != appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
                <c:if test="${'0' == person.topType}">
                    <div class="topBySurgicalProcedure">
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
    </c:if>


    <c:if test="${'-1' == appSvcOtherInfoDto.appSvcOtherInfoTopDto.topType}">
        <div class="">
            <p class="bold">TOP (By Drug and Surgical Procedure)</p>
        </div>
        <c:forEach var="person" items="${appSvcOtherInfoDto.otherInfoAbortDrugAndSurgicalList}" varStatus="astatus">
                <c:if test="${'-1' == person.topType}">
                    <div class="topByDrugandSurgicalProcedure">
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
        </c:forEach>
    </c:if>

    <iais:row>
        <div class="">
            <p class="bold">Declaration</p>
        </div>
    </iais:row>
    <iais:row>
        <div class="form-check active">
            <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                <c:out value="I declare the information in my application to be true, to the best of my knowledge.
                        I also understand that approval of the licence is dependent on satisfactory compliance with the relevant requirements under
                        the Healthcare Services Act, Regulations and Guidelines and the TOP Act, Regulations and Guidelines."/>
            </div>
        </div>
    </iais:row>
</c:if>
