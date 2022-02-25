<div class="row">
  <div class="col-xs-12">
    <div class="panel-group" id="accordion2" role="tablist" aria-multiselectable="true">
      <div class="alert alert-info" role="alert">
        <strong>
          <h4>View & Mark Submitted Rectifications</h4>
        </strong>
      </div>
      <iais:section title="" id = "retificationView">
        <iais:row>
          <iais:field value="Rectifications submitted by Applicant"/>
        </iais:row>
        <div class="table-gp" id = "processRec">
          <table aria-describedby="" class="table">
            <thead>
            <tr >
              <th scope="col" >Vehicle Number</th>
              <th scope="col" >NC Clause</th>
              <th scope="col" >Checklist Question</th>
              <th scope="col" >Remarks</th>
              <th scope="col" >Documents</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty inspectionPreTaskDto.inspecUserRecUploadDtos}">
                <tr>
                  <td colspan="7">
                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="proRec" items="${inspectionPreTaskDto.inspecUserRecUploadDtos}" varStatus="status">
                  <tr>
                    <td><c:out value="${proRec.vehicleNo}"/></td>
                    <td><c:out value="${proRec.checkClause}"/></td>
                    <td><iais:code code="${proRec.checkQuestion}"/></td>
                    <td><c:out value="${proRec.uploadRemarks}"/></td>
                    <td>
                      <c:if test="${proRec.fileRepoDtos != null}">
                        <c:forEach var="file" items="${proRec.fileRepoDtos}" varStatus="status">
                          <div class="fileList ">
                                                  <span class="filename server-site" id="140">
                                                    <u><iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${file.id}" docName="${file.fileName}"/></u>
                                                  </span>
                          </div>
                        </c:forEach>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </tbody>
          </table>
          <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
        </div>
        <div class="table-gp" id = "processRecRfi">
          <table aria-describedby="" class="table">
            <thead>
            <tr >
              <th scope="col" ><input type="checkbox" name="allNcItemCheck" id="allNcItemCheck" <c:if test="${'check' eq allNcItemCheck}">checked</c:if>
                                      onchange="javascript:doInspectorProRecCheckAll()" value="<c:out value="${allNcItemCheck}"/>"/></th>
              <th scope="col" >Vehicle Number</th>
              <th scope="col" >NC Clause</th>
              <th scope="col" >Checklist Question</th>
              <th scope="col" >Remarks</th>
              <th scope="col" >Documents</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty inspectionPreTaskDto.inspecUserRecUploadDtos}">
                <tr>
                  <td colspan="7">
                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="proRec" items="${inspectionPreTaskDto.inspecUserRecUploadDtos}" varStatus="status">
                  <tr>
                    <td>
                      <input type="checkbox" name="ncItemCheck" id="ncItemCheck${status.index}"
                              <c:if test="${!empty inspectionPreTaskDto.checkRecRfiNcItems}">
                                <c:forEach items="${inspectionPreTaskDto.checkRecRfiNcItems}" var="checkNcItemId">
                                  <c:if test="${proRec.appPremisesPreInspectionNcItemDto.id eq checkNcItemId}">checked="checked"</c:if>
                                </c:forEach>
                              </c:if>
                             onchange="javascript:doInspectorProRecCheck()" value="<c:out value="${proRec.appPremisesPreInspectionNcItemDto.id}"/>"/>
                    </td>
                    <td><c:out value="${proRec.vehicleNo}"/></td>
                    <td><c:out value="${proRec.checkClause}"/></td>
                    <td><iais:code code="${proRec.checkQuestion}"/></td>
                    <td><c:out value="${proRec.uploadRemarks}"/></td>
                    <td>
                      <c:if test="${proRec.fileRepoDtos != null}">
                        <c:forEach var="file" items="${proRec.fileRepoDtos}" varStatus="status">
                          <div class="fileList ">
                                                  <span class="filename server-site" id="130">
                                                    <u><iais:downloadLink fileRepoIdName="fileRo${status.index}" fileRepoId="${file.id}" docName="${file.fileName}"/></u>
                                                  </span>
                          </div>
                        </c:forEach>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </tbody>
            <span class="error-msg" name="iaisErrorMsg" id="error_checkRecRfiNcItems"></span>
          </table>
          <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
        </div>
      </iais:section>
    </div>
  </div>
</div>
