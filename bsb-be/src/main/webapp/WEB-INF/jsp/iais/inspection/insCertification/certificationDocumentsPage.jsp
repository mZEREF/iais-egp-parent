<%--@elvariable id="commonDocDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto"--%>
<%--@elvariable id="reviewAFCReportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto"--%>
<%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
<iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
<h3>List of Documents uploaded</h3>
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
                <p  class="visible-xs visible-sm table-row-title">S/N</p>
                <p ><c:out value="${status.index+1}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title" >Document Name</p>
                <p ><c:out value="${info.disPlayDto.docName}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title" >Document Type</p>
                <p ><iais:code code="${info.disPlayDto.docType}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title" >Uploaded by</p>
                <p ><c:out value="${info.disPlayDto.userDisplayName}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title" >Upload Date</p>
                <p ><fmt:formatDate value="${info.disPlayDto.uploadDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title" >Round of Review</p>
                <p ><c:out value="${reviewAFCReportDto.maxRound+1}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title" >Actions</p>
                <p><a href="javascript:void(0)" class="btn btn-secondary btn-sm" onclick="downloadFile('new','${info.disPlayDto.maskedRepoId}')">Download</a></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">AFC</p>
                <p style="text-align: center"><input type="checkbox" disabled/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Applicant</p>
                <p style="text-align: center"><input type="checkbox" disabled/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">MOH</p>
                <p style="text-align: center"><input name="${info.disPlayDto.maskedRepoId}Applicant" type="checkbox" value="Y" <c:if test="${canActionRole ne Applicant and canActionRole ne AFC}">disabled</c:if>/></p>
              </td>
            </tr>
          </c:forEach>
        </c:if>
        <c:if test="${reviewAFCReportDto.certificationDocDisPlayDtos ne null}">
          <c:forEach var="docInfo" items="${reviewAFCReportDto.certificationDocDisPlayDtos}" varStatus="status">
            <tr id="${docInfo.maskedRepoId}FileTr">
              <td>
                <p class="visible-xs visible-sm table-row-title" style="text-align: center">S/N</p>
                <p><c:out value="${status.index+1}"/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title" style="text-align: center">Document Name</p>
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
                <p style="text-align: center"><input type="checkbox" <c:if test="${docInfo.afcMarkFinal eq 'Y'}">checked</c:if> disabled/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">Applicant</p>
                <p style="text-align: center"><input type="checkbox" <c:if test="${docInfo.applicantMarkFinal eq 'Y'}">checked</c:if> disabled/></p>
              </td>
              <td>
                <p class="visible-xs visible-sm table-row-title">MOH</p>
                <c:choose>
                  <c:when test="${canActionRole eq 'BSB_DO' and docInfo.roundOfReview == reviewAFCReportDto.maxRound}">
                    <p style="text-align: center"><input name="${docInfo.maskedRepoId}BSB_DO" type="checkbox" <c:if test="${docInfo.mohMarkFinal eq 'Y'}">checked</c:if>  value="Y"/></p>
                  </c:when>
                  <c:when test="${canActionRole eq 'BSB_AO' and docInfo.roundOfReview == reviewAFCReportDto.maxRound}">
                    <p style="text-align: center"><input name="${docInfo.maskedRepoId}BSB_AO" type="checkbox" <c:if test="${docInfo.mohMarkFinal eq 'Y'}">checked</c:if> value="Y"/></p>
                  </c:when>
                  <c:otherwise>
                    <p style="text-align: center"><input type="checkbox" <c:if test="${docInfo.mohMarkFinal eq 'Y'}">checked</c:if> disabled/></p>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
        </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>
<br><br>
<%@ include file="upload.jsp" %>
<iais:action>
  <c:choose>
    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
    <c:when test="${goBackUrl ne null}">
      <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:when>
    <c:otherwise>
      <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:otherwise>
  </c:choose>
  <button type="button" name="saveReportBtn" id="saveReportBtn" style="float:right" class="btn btn-secondary">
    SAVE
  </button>
</iais:action>