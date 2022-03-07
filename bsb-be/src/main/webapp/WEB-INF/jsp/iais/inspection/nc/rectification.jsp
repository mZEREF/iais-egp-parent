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
            <tr>
              <th scope="col" >S/N</th>
              <th scope="col" >Item Description</th>
              <th scope="col" >MOH Remarks</th>
              <th scope="col">Status</th>
              <th scope="col" >Documents</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty insRectificationList}">
                <tr>
                  <td colspan="7">
                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="item" items="${insRectificationList}" varStatus="status">
                  <tr>
                    <td><c:out value="${status.index+1}"/></td>
                    <td><c:out value="${item.itemText}"/></td>
                    <td><iais:code code="${item.remark}"/></td>
                    <td><c:out value="${item.status}"/></td>
                    <td>
                      <c:set var="inspectionDoc" value="${insRecordInfoSubTypeMap.get(item.itemValue)}"/>
                      <c:if test="${inspectionDoc != null}">
                        <c:forEach var="file" items="${inspectionDoc}" varStatus="status">
                          <div class="fileList ">
                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                                                  <span class="filename server-site" id="140">
                                                    <u><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</u>
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
          <a style="float:left;padding-top: 1.1%;" class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
        </div>
        <div class="table-gp" id = "processRecRfi">
          <table aria-describedby="" class="table">
            <thead>
            <tr >
              <th scope="col" ><input type="checkbox" name="allNcItemCheck" id="allNcItemCheck" <c:if test="${'check' eq allNcItemCheck}">checked</c:if>
                                      onchange="javascript:doInspectorProRecCheckAll()" value="<c:out value="${allNcItemCheck}"/>"/></th>
              <th scope="col" >S/N</th>
              <th scope="col" >Item Description</th>
              <th scope="col" >MOH Remarks</th>
              <th scope="col">Status</th>
              <th scope="col" >Documents</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty insRectificationList}">
                <tr>
                  <td colspan="7">
                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="item" items="${insRectificationList}" varStatus="status">
                  <tr>
                    <td>
                      <label for="ncItemCheck${status.index}"></label><input type="checkbox" name="ncItemCheck" id="ncItemCheck${status.index}" onchange="javascript:doInspectorProRecCheck()"/>
                    </td>
                    <td><c:out value="${status.index+1}"/></td>
                    <td><c:out value="${item.itemText}"/></td>
                    <td><iais:code code="${item.remark}"/></td>
                    <td><c:out value="${item.status}"/></td>
                    <td>
                      <c:set var="inspectionDoc" value="${insRecordInfoSubTypeMap.get(item.itemValue)}"/>
                      <c:if test="${inspectionDoc != null}">
                        <c:forEach var="file" items="${inspectionDoc}" varStatus="status">
                          <div class="fileList ">
                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', file.repoId)}"/>
                            <span class="filename server-site" id="140">
                                                    <u><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${file.filename}</a>(${String.format("%.1f", file.size/1024.0)}KB)</u>
                                                  </span>
                          </div>                        </c:forEach>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </tbody>
            <span class="error-msg" name="iaisErrorMsg" id="error_checkRecRfiNcItems"></span>
          </table>
          <a style="float:left;padding-top: 1.1%;" class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
        </div>
      </iais:section>
    </div>
  </div>
</div>
