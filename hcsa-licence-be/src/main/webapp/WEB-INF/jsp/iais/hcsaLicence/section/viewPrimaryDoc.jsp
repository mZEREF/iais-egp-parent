<div class="panel panel-default">
    <div class="panel-heading"  id="PrimaryDocuments" role="tab">
        <h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" href="#collapsePrimaryDocuments" aria-expanded="true" aria-controls="collapsePrimaryDocuments">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse" id="collapsePrimaryDocuments"  role="tabpanel" aria-labelledby="PrimaryDocuments">
        <div class="panel-body">
            <p class="text-right">
                <c:if test="${rfi=='rfi'}">
                    <c:if test="${appEdit.docEdit}">
                        <input class="form-check-input" <c:if test="${pageEdit.docEdit}">checked</c:if>  id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">
                    </c:if>
                </c:if>
            </p>
            <div class="elemClass-1561088919456">
                <div  class="page section control  container-s-1" style="margin: 10px 0px">
                    <div class="control-set-font control-font-header section-header">
                        <label class="title-font-size">Uploaded Documents</label>
                    </div>
                    <table aria-describedby="" class="col-xs-12 col-md-12">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <c:forEach items="${appSubmissionDto.multipleGrpPrimaryDoc}" var="appGrpPrimaryDocDto">
                            <c:set value="${appSubmissionDto.oldAppSubmissionDto.multipleGrpPrimaryDoc[appGrpPrimaryDocDto.key]}" var="oldAppGrpPrimaryDocDto"></c:set>
                            <tr>
                                <td>
                                    <div class="field col-sm-12 control-label formtext"><label>${appGrpPrimaryDocDto.key}:</label></div>
                                </td>
                            </tr>
                            <c:forEach items="${appGrpPrimaryDocDto.value}" var="sinage" varStatus="inx">

                                <tr>
                                    <td>
                                        <div class="col-xs-6 col-md-6 ">
                                            <c:if test="${sinage.docSize!=null}">
                                              <span class="newVal " attr="${sinage.md5Code}${sinage.docName}">
                                                <iais:downloadLink fileRepoIdName="fileRo${inx.index}" fileRepoId="${sinage.fileRepoId}" docName="${sinage.docName}"/>
                                                <c:out value="(${sinage.docSize} KB)"/>
                                            </span>
                                            </c:if>
                                            <c:if test="${sinage.docSize==null}">
                                                <span class="newVal " attr="${sinage.md5Code}${sinage.docName}"></span>
                                            </c:if>
                                        </div>
                                        <div class="col-xs-6 col-md-6">
                                            <c:if test="${oldAppGrpPrimaryDocDto[inx.index].docSize!=null}">
                                                <span class="oldVal " attr="${oldAppGrpPrimaryDocDto[inx.index].md5Code}${oldAppGrpPrimaryDocDto[inx.index].docName}"  style="display: none"
                                                <iais:downloadLink fileRepoIdName="fileRo${inx.index}" fileRepoId="${oldAppGrpPrimaryDocDto[inx.index].fileRepoId}" docName="${oldAppGrpPrimaryDocDto[inx.index].docName}"/>
                                                <c:out value="(${oldAppGrpPrimaryDocDto[inx.index].docSize} KB)"/>
                                                </span>
                                            </c:if>
                                            <c:if test="${oldAppGrpPrimaryDocDto[inx.index].docSize==null}">
                                               <span class="oldVal " attr="${oldAppGrpPrimaryDocDto[inx.index].md5Code}${oldAppGrpPrimaryDocDto[inx.index].docName}"  style="display: none">
                                               </span>
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