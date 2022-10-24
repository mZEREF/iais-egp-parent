<table aria-describedby="" class="col-xs-12">
    <tr>
        <th scope="col" style="display: none"></th>
        <td class="col-xs-6">
            <p class="form-check-label" aria-label="premise-1-cytology">
                <span class="check-square"></span>Do you provide Yellow Fever Vaccination Service
            </p>
        </td>
        <td>
            <div class="col-xs-6 ">
                <span class="newVal" attr="${otherInfo.provideYfVs}">
                   <c:if test="${otherInfo.provideYfVs == 1}">Yes</c:if>
                    <c:if test="${otherInfo.provideYfVs == 0}">No</c:if>
                </span>
            </div>
            <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.provideYfVs}" style="display: none">
                    <c:if test="${oldOtherInfo.provideYfVs == 1}">Yes</c:if>
                    <c:if test="${oldOtherInfo.provideYfVs == 0}">No</c:if>
                </span>
            </div>
        </td>
    </tr>
    <c:if test="${otherInfo.provideYfVs == 1}">
        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Business Name
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                    <c:forEach var="docShowDto" items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" varStatus="stat">
                        <span class="newVal" attr="${docShowDto.businessName}">
                           <c:if test="${stat.index != 0}">
                               <c:if test="${stat.index != stat.index-1}">,</c:if>
                           </c:if>
                            <c:out value="${docShowDto.businessName}"/>
                        </span>
                    </c:forEach>
                </div>
            </td>
        </tr>

        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Address
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                    <span class="newVal" attr="${otherInfo.orgUserDto.email}">
                        <c:out value="${otherInfo.orgUserDto.email}"/>
                    </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.orgUserDto.email}" style="display: none">
                    <c:out value="${oldOtherInfo.orgUserDto.email}"/>
                </span>
                </div>
            </td>
        </tr>

        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Applicant Name
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                    <span class="newVal" attr="${otherInfo.orgUserDto.displayName}">
                        <c:out value="${otherInfo.orgUserDto.displayName}"/>
                    </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.orgUserDto.displayName}" style="display: none">
                    <c:out value="${oldOtherInfo.orgUserDto.displayName}"/>
                </span>
                </div>
            </td>
        </tr>

        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Designation
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                    <span class="newVal" attr="${otherInfo.orgUserDto.designation}">
                        <c:out value="${otherInfo.orgUserDto.designation}"/>
                    </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.orgUserDto.designation}" style="display: none">
                    <c:out value="${oldOtherInfo.orgUserDto.designation}"/>
                </span>
                </div>
            </td>
        </tr>

        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Contact number
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                    <span class="newVal" attr="${otherInfo.orgUserDto.mobileNo}">
                        <c:out value="${otherInfo.orgUserDto.mobileNo}"/>
                    </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.orgUserDto.mobileNo}" style="display: none">
                    <c:out value="${oldOtherInfo.orgUserDto.mobileNo}"/>
                </span>
                </div>
            </td>
        </tr>

        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Date of Commencement
                </p>
            </td>
            <td>
                <div class="col-xs-6 ">
                    <span class="newVal" attr="${otherInfo.yfCommencementDateStr}">
                        <c:out value="${otherInfo.yfCommencementDateStr}"/>
                    </span>
                </div>
                <div class="col-xs-6 ">
                <span class=" oldVal" attr="${oldOtherInfo.yfCommencementDateStr}" style="display: none">
                    <c:out value="${oldOtherInfo.yfCommencementDateStr}"/>
                </span>
                </div>
            </td>
        </tr>
    </c:if>
</table>
