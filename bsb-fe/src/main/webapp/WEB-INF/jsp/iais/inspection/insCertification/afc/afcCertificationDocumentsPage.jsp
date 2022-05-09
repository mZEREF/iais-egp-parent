<%--@elvariable id="commonDocDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto"--%>
<%--@elvariable id="reviewAFCReportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto"--%>
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
      <table aria-describedby="" class="table" id="docTable">
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
        <tbody id="tbodyFileListId">
        <c:if test="${commonDocDto.newDocMap ne null}">
          <c:set var="newDocMap" value="${commonDocDto.newDocMap}"/>
          <c:forEach var="info" items="${newDocMap.values()}" varStatus="status">
              <tr id="${info.disPlayDto.maskedRepoId}FileTr">
                <td>
                  <p class="visible-xs visible-sm table-row-title">S/N</p>
                  <p style="text-align: center"><c:out value="${status.index+1}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Document Name</p>
                  <p style="text-align: center"><c:out value="${info.disPlayDto.docName}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Document Type</p>
                  <p style="text-align: center"><iais:code code="${info.disPlayDto.docType}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Uploaded by</p>
                  <p style="text-align: center"><c:out value="${info.disPlayDto.userDisplayName}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Upload Date</p>
                  <p style="text-align: center"><fmt:formatDate value="${info.disPlayDto.uploadDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Round of Review</p>
                  <p style="text-align: center"><c:out value="${reviewAFCReportDto.maxRound+1}"/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Actions</p>
                  <p><a href="javascript:void(0)" class="btn btn-secondary btn-sm" onclick="downloadFile('new','${info.disPlayDto.maskedRepoId}')">Download</a></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">AFC</p>
                  <p style="text-align: center"><input type="checkbox" disabled/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">Applicant</p>
                  <p style="text-align: center"><input name="${info.disPlayDto.maskedRepoId}Applicant" type="checkbox" value="Y" <c:if test="${canActionRole ne Applicant}">disabled</c:if>/></p>
                </td>
                <td>
                  <p class="visible-xs visible-sm table-row-title">MOH</p>
                  <p style="text-align: center"><input type="checkbox" disabled/></p>
                </td>
              </tr>
          </c:forEach>
        </c:if>
        <c:if test="${reviewAFCReportDto.certificationDocDisPlayDtos ne null}">
          <c:forEach var="docInfo" items="${reviewAFCReportDto.certificationDocDisPlayDtos}" varStatus="status">
            <tr id="${docInfo.maskedRepoId}FileTr">
              <td>
                <p class="visible-xs visible-sm table-row-title">S/N</p>
                <p><c:out value="${status.index+1}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Document Name</p>
                <p><c:out value="${docInfo.docName}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Document Type</p>
                <p><iais:code code="${docInfo.docType}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Uploaded by</p>
                <p><c:out value="${docInfo.uploadBy}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Upload Date</p>
                <p><fmt:formatDate value="${docInfo.uploadDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Round of Review</p>
                <p><c:out value="${docInfo.roundOfReview}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Actions</p>
                <p><a href="javascript:void(0)" class="btn btn-secondary btn-sm" onclick="downloadFile('saved','${docInfo.maskedRepoId}')">Download</a></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">AFC</p>
                <c:choose>
                  <c:when test="${canActionRole eq 'AFC' and docInfo.roundOfReview == reviewAFCReportDto.maxRound}">
                    <p><input name="${docInfo.maskedRepoId}AFC" type="checkbox" value="Y"/></p>
                  </c:when>
                  <c:otherwise>
                    <p><input type="checkbox" disabled/></p>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Applicant</p>
                <c:choose>
                  <c:when test="${canActionRole eq 'Applicant' and docInfo.roundOfReview == reviewAFCReportDto.maxRound}">
                    <p><input name="${docInfo.maskedRepoId}Applicant" type="checkbox" value="Y"/></p>
                  </c:when>
                  <c:otherwise>
                    <p><input type="checkbox" disabled/></p>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">MOH</p>
                <p><input type="checkbox" disabled/></p>
              </td>
            </tr>
          </c:forEach>
        </c:if>
<%--        <c:if test="${commonDocDto.savedDocMap eq null && commonDocDto.newDocMap eq null}">--%>
<%--          <tr>--%>
<%--            <td colspan="6">--%>
<%--              <iais:message key="GENERAL_ACK018" escape="true"/>--%>
<%--            </td>--%>
<%--          </tr>--%>
<%--        </c:if>--%>
        </tbody>
      </table>
    </div>
  </div>
</div>