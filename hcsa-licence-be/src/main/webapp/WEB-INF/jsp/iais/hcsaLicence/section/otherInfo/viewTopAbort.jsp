<c:forEach var="person" items="${otherInfo.otherInfoAbortDrugList}" varStatus="status">
    <c:set var="oldPerson" value="${oldOtherInfo.otherInfoAbortDrugList[status.index]}"/>
    <c:if test="${'0' != otherInfo.appSvcOtherInfoTopDto.topType}">
        <c:if test="${'1' == person.topType}">
            <p class="col-xs-12">
                <strong>
                    TOP (By Drug)
                    <c:if test="${fn:length(otherInfo.otherInfoAbortDrugList)>1}">${status.index+1}</c:if>
                </strong>
            </p>
            <table aria-describedby="" class="col-xs-12 <c:if test="${'0' == otherInfo.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Year.
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.year}">
                               <iais:code code="${person.year}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.year}" style="display: none">
                                 <iais:code code="${oldPerson.year}"/>
                            </span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>No. of abortions
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.abortNum}">
                               <iais:code code="${person.abortNum}"></iais:code>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.abortNum}" style="display: none">
                                 <iais:code code="${oldPerson.abortNum}"></iais:code>
                            </span>
                        </div>
                    </td>
                </tr>
            </table>
        </c:if>
    </c:if>
</c:forEach>

<c:forEach var="person" items="${otherInfo.otherInfoAbortSurgicalProcedureList}" varStatus="pstatus">
    <c:set var="oldPerson" value="${oldOtherInfo.otherInfoAbortDrugList[pstatus.index]}"/>
    <c:if test="${'1' != otherInfo.appSvcOtherInfoTopDto.topType}">
        <c:if test="${'0' == person.topType}">
            <p class="col-xs-12">
                <strong>
                    TOP (By Surgical Procedure)&nbsp;<c:if test="${otherInfo.otherInfoAbortSurgicalProcedureList.size() > 1}">${pstatus.index+1}</c:if>
                </strong>
            </p>
            <table aria-describedby="" class="col-xs-12 <c:if test="${'1' == otherInfo.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Year.
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.year}">
                               <iais:code code="${person.year}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.year}" style="display: none">
                                <iais:code code="${oldPerson.year}"/>
                            </span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>No. of abortions
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.abortNum}">
                               <iais:code code="${person.abortNum}"></iais:code>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.abortNum}" style="display: none">
                                 <iais:code code="${oldPerson.abortNum}"></iais:code>
                            </span>
                        </div>
                    </td>
                </tr>
            </table>
        </c:if>
    </c:if>
</c:forEach>

<c:forEach var="person" items="${otherInfo.otherInfoAbortDrugAndSurgicalList}" varStatus="astatus">
    <c:set var="oldPerson" value="${oldOtherInfo.otherInfoAbortDrugList[astatus.index]}"/>
    <c:if test="${'-1' == otherInfo.appSvcOtherInfoTopDto.topType}">
        <c:if test="${'-1' == person.topType}">
            <p class="col-xs-12">
                <strong>
                    TOP (By Drug and Surgical Procedure)&nbsp;&nbsp;<c:if test="${otherInfo.otherInfoAbortDrugAndSurgicalList.size() > 1}">${astatus.index+1}</c:if>
                </strong>
            </p>
            <table aria-describedby="" class="col-xs-12">
                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Year.
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.year}">
                               <iais:code code="${person.year}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.year}" style="display: none">
                                <iais:code code="${oldPerson.year}"/>
                            </span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <p class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>No. of abortions
                        </p>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.abortNum}">
                               <iais:code code="${person.abortNum}"></iais:code>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.abortNum}" style="display: none">
                                 <iais:code code="${oldPerson.abortNum}"></iais:code>
                            </span>
                        </div>
                    </td>
                </tr>
            </table>
        </c:if>
    </c:if>
</c:forEach>
<c:if test="${'0' == otherInfo.provideTop}">
    <p class="bold">Declaration</p>
    <table aria-describedby="" class="col-xs-12">
        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>I declare that I have met URA's requirements for gross floor area
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.declaration}">
                     <c:if test="${otherInfo.declaration eq '1'}">Yes</c:if>
                </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.declaration}" style="display: none">
                     <c:if test="${oldOtherInfo.declaration eq '1'}">Yes</c:if>
                </span>
                </div>
            </td>
        </tr>
    </table>
</c:if>
