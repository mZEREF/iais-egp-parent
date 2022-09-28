<div class="row">
    <table aria-describedby="" class="col-xs-12">
        <thead style="display: none">
        <tr>
            <th scope="col"></th>
        </tr>
        </thead>
        <tr>
            <div class="col-xs-12">
                <p class="bold">${title}</p>
            </div>
        </tr>
        <tr>
            <th scope="col" style="display: none"></th>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Salutation
                </div>
            </td>
            <td>
                <div class="col-xs-6 col-md-6">
                    <span class="newVal" attr="${person.salutation}">
                        <iais:code code="${person.salutation}"/>
                    </span>
                </div>
                <div class="col-xs-6 col-md-6">
                    <span class=" oldVal" attr="${oldPerson.salutation}" style="display: none">
                        <iais:code code="${oldPerson.salutation}"/>
                    </span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Name
                </div>
            </td>
            <td>
                <div class="col-xs-6 img-show">
                    <div class="newVal" attr="<c:out value="${person.name}" />">
                        <c:out value="${person.name}"/>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                            <jsp:param name="profRegNo" value="${person.profRegNo}"/>
                            <jsp:param name="personName" value="${person.name}"/>
                            <jsp:param name="methodName" value="showThisNameTableNewService"/>
                        </jsp:include>
                    </div>
                </div>
                <div class="col-xs-6 img-show">
                    <div class="oldVal" attr="<c:out value="${oldPerson.name}" />" style="display: none">
                        <c:out value="${oldPerson.name}"/>
                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                            <jsp:param name="profRegNo" value="${oldPerson.profRegNo}"/>
                            <jsp:param name="personName" value="${oldPerson.name}"/>
                            <jsp:param name="methodName" value="showThisNameTableOldService"/>
                        </jsp:include>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                    <jsp:param name="profRegNo" value="${person.profRegNo}"/>
                    <jsp:param name="cssClass" value="new-img-show"/>
                </jsp:include>
                <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                    <jsp:param name="profRegNo" value="${oldPerson.profRegNo}"/>
                    <jsp:param name="cssClass" value="old-img-show"/>
                </jsp:include>
            </td>
        </tr>
        <c:if test="${isShowMore==1}">
            <tr>
                <td class="col-xs-6">
                    <div class="form-check-label" aria-label="premise-1-cytology">
                        <span class="check-square"></span>Qualification
                    </div>
                </td>
                <td>
                    <div class="col-xs-12">
                        <div class="newVal " attr="<c:out value="${person.qualification}"/>">
                            <c:out value="${person.qualification}"/>
                        </div>
                        <div class="oldVal " attr="<c:out value="${oldPerson.qualification}"/>" style="display: none">
                            <c:out value="${oldPerson.qualification}"/>
                        </div>
                    </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="col-xs-6">
                    <div class="form-check-label" aria-label="premise-1-cytology">
                        <span class="check-square"></span>Working Experience (in terms of years)
                    </div>
                </td>
                <td>
                    <div class="col-xs-12">
                        <div class="newVal " attr="<c:out value="${person.wrkExpYear}"/>">
                            <c:out value="${person.wrkExpYear}"/>
                        </div>
                        <div class="oldVal " attr="<c:out value="${oldPerson.wrkExpYear}"/>" style="display: none">
                            <c:out value="${oldPerson.wrkExpYear}"/>
                        </div>
                    </div>
                    </div>
                </td>
            </tr>
        </c:if>
    </table>
</div>


