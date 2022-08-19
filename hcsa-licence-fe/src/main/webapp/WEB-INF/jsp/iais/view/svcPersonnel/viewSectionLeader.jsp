<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="app-title">${currStepName}</label>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="sectionLeader" items="${currentPreviewSvcInfo.appSvcSectionLeaderList}" varStatus="status">
            <iais:row>
                <iais:field width="5" value="Name" cssClass="col-md-5 control-font-label"/>
                <iais:value width="3" cssClass="col-md-3">
                    <c:out value="${sectionLeader.salutation}"/>
                </iais:value>
                <iais:value width="4" cssClass="col-md-4">
                    <c:out value="${sectionLeader.name}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Qualification" cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <c:out value="${sectionLeader.qualification}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Working Experience (in terms of years)"
                            cssClass="col-md-5 control-font-label"/>
                <iais:value width="7" cssClass="col-md-7">
                    <c:out value="${sectionLeader.wrkExpYear}"/>
                </iais:value>
            </iais:row>

        </c:forEach>
    </div>

</div>


