<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMapcurrStepName}</label>
    <div class="amend-preview-info">
        <p></p>
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
        </div>
    </div>
</div>