<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="app-title">${currStepName}</label>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="sectionLeader" items="${currentPreviewSvcInfo.appSvcSectionLeaderList}" varStatus="status">
            <iais:row>
                <iais:field width="5" value="Name"/>
                <iais:value width="4" cssClass="col-md-4" display="true">
                    <c:out value="${sectionLeader.name}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" value="Salutation"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <iais:code code="${sectionLeader.salutation}"/>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field width="5" value="Qualification"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${sectionLeader.qualification}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Working Experience (in terms of years)"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${sectionLeader.wrkExpYear}"/>
                </iais:value>
            </iais:row>

        </c:forEach>
    </div>

</div>


