<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="docShowDto" items="${currentPreviewSvcInfo.documentShowDtoList}" varStatus="stat">
            <c:if test="${docShowDto.existDoc}">
                <iais:row>
                    <div class="col-xs-12">
                        <div class="app-title"><c:out value="${docShowDto.premName}"/></div>
                        <div class="font-18 bold">Address: <c:out value="${docShowDto.premAddress}"/></div>
                    </div>
                </iais:row>
                <br/>
                <div class="panel-group" id="${docShowDto.premisesVal}" role="tablist" aria-multiselectable="true">
                    <c:forEach var="secDto" items="${docShowDto.docSectionList}" varStatus="secStat">
                        <c:if test="${secDto.existDoc}"  var="existDoc">
                            <c:set var="panelKey">${docShowDto.premisesVal}-${secDto.svcId}</c:set>
                            <div class="panel panel-default deputy-panel">
                                <div class="panel-heading" role="tab">
                                    <h4 class="panel-title">
                                        <c:out value="${secDto.sectionName}"/>
                                        <%--<a role="button" class="" data-toggle="collapse" href="#${panelKey}" aria-expanded="true" aria-controls="${panelKey}">
                                            <c:out value="${secDto.sectionName}"/>
                                        </a>--%>
                                    </h4>
                                </div>
                                <div id="${panelKey}" class="panel-collapse collapse in">
                                    <div class="panel-body">
                                        <div class="panel-main-content">
                                            <c:forEach var="doc" items="${secDto.docSecDetailList}" varStatus="docStat">
                                                <c:if test="${doc.existDoc}">
                                                    <%@include file="viewSvcDocContent.jsp"%>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not existDoc && empty secDto.docSecDetailList}">
                            <div class="panel panel-default deputy-panel">
                                <div class="panel-heading" role="tab">
                                    <h4 class="panel-title">
                                        <c:out value="${secDto.sectionName}"/>
                                    </h4>
                                </div>
                                <div id="${panelKey}" class="panel-collapse collapse in">
                                    <div class="panel-body">
                                        <div class="panel-main-content">
                                            <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>
