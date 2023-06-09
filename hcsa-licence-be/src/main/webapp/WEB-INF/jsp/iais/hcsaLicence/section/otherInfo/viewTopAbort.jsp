<c:if test="${'0' != otherInfo.appSvcOtherInfoTopDto.topType}">
    <p class="col-xs-12">
        <strong>
            TOP (By Drug)
        </strong>
    </p>
    <c:forEach var="person" items="${otherInfo.otherInfoAbortDrugList}" varStatus="status">
    <c:set var="oldPerson" value="${oldOtherInfo.otherInfoAbortDrugList[status.index]}"/>
        <c:if test="${'1' == person.topType}">
            <table aria-describedby="" class="col-xs-12 <c:if test="${'0' == otherInfo.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <div class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Year
                        </div>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.year}">
                               <c:out value="${person.year}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.year}" style="display: none">
                                 <c:out value="${oldPerson.year}"/>
                            </span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <div class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>No. of abortions
                        </div>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.abortNum}">
                               <c:out value="${person.abortNum}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.abortNum}" style="display: none">
                                 <c:out value="${oldPerson.abortNum}"/>
                            </span>
                        </div>
                    </td>
                </tr>
            </table>
        </c:if>
    </c:forEach>
</c:if>

<c:if test="${'1' != otherInfo.appSvcOtherInfoTopDto.topType}">
    <p class="col-xs-12">
        <strong>
            TOP (By Surgical Procedure)
        </strong>
    </p>
    <c:forEach var="person" items="${otherInfo.otherInfoAbortSurgicalProcedureList}" varStatus="pstatus">
    <c:set var="oldPerson" value="${oldOtherInfo.otherInfoAbortDrugList[pstatus.index]}"/>
        <c:if test="${'0' == person.topType}">
            <table aria-describedby="" class="col-xs-12 <c:if test="${'1' == otherInfo.appSvcOtherInfoTopDto.topType}">hidden</c:if>">
                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <div class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Year
                        </div>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.year}">
                               <c:out value="${person.year}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.year}" style="display: none">
                                <c:out value="${oldPerson.year}"/>
                            </span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <div class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>No. of abortions
                        </div>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.abortNum}">
                               <c:out value="${person.abortNum}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.abortNum}" style="display: none">
                                 <c:out value="${oldPerson.abortNum}"/>
                            </span>
                        </div>
                    </td>
                </tr>
            </table>
        </c:if>
    </c:forEach>
</c:if>

<c:if test="${'-1' == otherInfo.appSvcOtherInfoTopDto.topType}">
    <p class="col-xs-12">
        <strong>
            TOP (By Drug and Surgical Procedure)
        </strong>
    </p>
    <c:forEach var="person" items="${otherInfo.otherInfoAbortDrugAndSurgicalList}" varStatus="astatus">
    <c:set var="oldPerson" value="${oldOtherInfo.otherInfoAbortDrugList[astatus.index]}"/>
        <c:if test="${'-1' == person.topType}">
            <table aria-describedby="" class="col-xs-12">
                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <div class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>Year
                        </div>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.year}">
                               <c:out value="${person.year}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.year}" style="display: none">
                                <c:out value="${oldPerson.year}"/>
                            </span>
                        </div>
                    </td>
                </tr>

                <tr>
                    <th scope="col" style="display: none"></th>
                    <td class="col-xs-6">
                        <div class="form-check-label" aria-label="premise-1-cytology">
                            <span class="check-square"></span>No. of abortions
                        </div>
                    </td>
                    <td>
                        <div class="col-xs-6 ">
                            <span class="newVal" attr="${person.abortNum}">
                               <c:out value="${person.abortNum}"/>
                            </span>
                        </div>
                        <div class="col-xs-6 ">
                            <span class=" oldVal" attr="${oldPerson.abortNum}" style="display: none">
                                 <c:out value="${oldPerson.abortNum}"/>
                            </span>
                        </div>
                    </td>
                </tr>
            </table>
        </c:if>
    </c:forEach>
</c:if>
<c:if test="${'1' == otherInfo.provideTop}">
    <p class="bold col-xs-12">Declaration</p>
    <table aria-describedby="" class="col-xs-12">
        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <div class="form-check-label longWord" aria-label="premise-1-cytology">
                    <span class="check-square"></span>I declare the information in my application to be true, to the best of my knowledge.
                    I also understand that approval of the licence is dependent on satisfactory compliance with the relevant requirements under
                    the Healthcare Services Act, Regulations and Guidelines and the TOP Act, Regulations and Guidelines.
                </div>
            </td>
            <td>
                <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.declaration}">
                     <c:if test="${otherInfo.declaration eq '0'}">Yes</c:if>
                </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.declaration}" style="display: none">
                     <c:if test="${oldOtherInfo.declaration eq '0'}">Yes</c:if>
                </span>
                </div>
            </td>
        </tr>
    </table>
</c:if>
