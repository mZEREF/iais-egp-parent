<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach var="docShowDto" items="${currentPreviewSvcInfo.documentShowDtoList}" varStatus="stat">
            <c:set var="oldDocShowDto" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.documentShowDtoList[stat.index]}" />
            <c:if test="${docShowDto.existDoc || oldDocShowDto.existDoc}">
                <iais:row>
                    <div class="col-xs-12">
                        <div class="newVal " attr="${docShowDto.premName}<c:out value="${docShowDto.premAddress}"/>">
                            <div class="app-title"><c:out value="${docShowDto.premName}"/></div>
                            <p class="font-18 bold">Address: <c:out value="${docShowDto.premAddress}"/></p>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="oldVal" attr="${oldDocShowDto.premName}<c:out value="${oldDocShowDto.premAddress}"/>">
                            <div class="app-title"><c:out value="${oldDocShowDto.premName}"/></div>
                            <p class="font-18 bold">Address: <c:out value="${oldDocShowDto.premAddress}"/></p>
                        </div>
                    </div>
                </iais:row>
                <br/>
                <div class="panel-group" id="${docShowDto.premisesVal}" role="tablist" aria-multiselectable="true">
                    <c:forEach var="secDto" items="${docShowDto.docSectionList}" varStatus="secStat">
                        <c:set var="oldSecDto" value="${oldDocShowDto.docSectionList[secStat.index]}" />
                        <c:if test="${secDto.existDoc || oldSecDto.existDoc}">
                            <c:set var="panelKey">${docShowDto.premisesVal}-${secDto.svcId}-${secStat.index}</c:set>
                            <div class="panel panel-default deputy-panel">
                                <div class="panel-heading" role="tab">
                                    <h4 class="panel-title">
                                        <c:out value="${secDto.sectionName}"/>
                                    </h4>
                                </div>
                                <div id="${panelKey}" class="panel-collapse collapse in">
                                    <div class="panel-body">
                                        <div class="panel-main-content">
                                            <c:forEach var="doc" items="${secDto.docSecDetailList}" varStatus="docStat">
                                                <c:set var="oldDoc" value="${oldSecDto.docSecDetailList[docStat.index]}" />
                                                <c:if test="${doc.existDoc || oldDoc.existDoc}">
                                                    <%@include file="viewSvcDocContent.jsp"%>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>
        <%--<p></p>
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <table aria-describedby="" class="col-xs-12">
                        <c:forEach var="svcDoc" items="${currentPreviewSvcInfo.multipleSvcDoc}" varStatus="status">
                            <c:set var="oldSvcDoc" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.multipleSvcDoc[svcDoc.key]}" />
                            <tr>
                                <th scope="col" style="display: none"></th>
                                <td>
                                    <div class="field col-sm-12 control-label formtext"><label>${svcDoc.key}</label></div>
                                </td>
                            </tr>
                            <c:forEach items="${svcDoc.value}" var="sinage" varStatus="inx">
                                <tr>
                                    <td>
                                        <div class="col-xs-6">
                                            <c:if test="${sinage.docSize!=null}">
                              <span class="newVal " attr="${sinage.md5Code}${sinage.docName}">
                                <iais:downloadLink fileRepoIdName="fileRo${inx.index}" fileRepoId="${sinage.fileRepoId}"
                                                   docName="${sinage.docName}"/>
                                <c:out value="(${sinage.docSize} KB)"/>
                              </span>
                                            </c:if>
                                            <c:if test="${sinage.docSize==null}">
                                                <span class="newVal " attr="${sinage.md5Code}${sinage.docName}"></span>
                                            </c:if>
                                        </div>
                                        <div class="col-xs-6">
                                            <c:if test="${oldSvcDoc[inx.index].docSize!=null}">
                          <span class="oldVal " attr="${oldSvcDoc[inx.index].md5Code}${oldSvcDoc[inx.index].docName}">
                            <c:out value="${oldSvcDoc[inx.index].docName}"/>
                            <c:out value="(${oldSvcDoc[inx.index].docSize} KB)"/>
                          </span>
                                            </c:if>
                                            <c:if test="${oldSvcDoc[inx.index].docSize==null}">
                                                <span class="oldVal "
                                                      attr="${oldSvcDoc[inx.index].md5Code}${oldSvcDoc[inx.index].docName}"></span>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>--%>
    </div>
</div>