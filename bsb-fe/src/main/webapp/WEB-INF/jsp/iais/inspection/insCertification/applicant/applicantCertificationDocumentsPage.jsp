<%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
<iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
<h3>List of Certification Documents</h3>
<div class="row">
  <div class="col-xs-12" style="text-align: right">
      <strong><span style="font-size: larger;padding-right: 75px">Mark as final</span></strong>
  </div>
</div>
<div class="row">
  <div class="col-xs-12">
    <div class="table-gp">
      <table aria-describedby="" class="table">
        <thead>
          <tr>
            <th scope="col" style="display: none"></th>
            <iais:sortableHeader needSort="false" field="" value="S/N" isFE="true" style="width: 5%"/>
            <iais:sortableHeader needSort="true" field="" value="Document Name" isFE="true" style="width: 15%"/>
            <iais:sortableHeader needSort="true" field="" value="Document Type" isFE="true" style="width: 15%"/>
            <iais:sortableHeader needSort="true" field="" value="Uploaded by" isFE="true" style="width: 12%"/>
            <iais:sortableHeader needSort="true" field="" value="Upload Date" isFE="true" style="width: 12%"/>
            <iais:sortableHeader needSort="true" field="" value="Round of Review" isFE="true" style="width: 15%"/>
            <iais:sortableHeader needSort="false" field="" value="Actions" isFE="true"/>
            <iais:sortableHeader needSort="false" field="" value="AFC" isFE="true"/>
            <iais:sortableHeader needSort="false" field="" value="Applicant" isFE="true"/>
            <iais:sortableHeader needSort="false" field="" value="MOH" isFE="true"/>
          </tr>
        </thead>
        <tbody>
        <c:choose>
          <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo>"--%>
          <c:when test="${empty dataList}">
            <tr>
              <td colspan="6">
                <iais:message key="GENERAL_ACK018" escape="true"/>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="app" items="${dataList}" varStatus="status">
              <tr>
                <td>
                  <p class="visible-xs visible-sm table-row-title">S/N</p>
                  <p><c:out value="${status.index}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Document Name</p>
                  <p><c:out value="${app.appType}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Document Type</p>
                  <p><iais:code code="${app.appType}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Uploaded by</p>
                  <p><c:out value="${app.processType}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Upload Date</p>
                  <p><fmt:formatDate value="${app.date}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Round of Review</p>
                  <p><c:out value="${app.status}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Actions</p>
                  <p><a class="btn-info">Download</a></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">AFC</p>
                  <p><input type="checkbox" readonly/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Applicant</p>
                  <select id="appAction${status.index}" class="appAction-drop" name="appAction${status.index}">
                    <option value="null" selected="selected">Null</option>
                    <option value="Y">Yes</option>
                    <option value="N">No</option>
                  </select>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">MOH</p>
                  <p><input type="checkbox" readonly/></p>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
        </tbody>
      </table>
    </div>
  </div>
</div>