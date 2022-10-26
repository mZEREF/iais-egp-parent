<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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

        <c:choose>
          <c:when test="${empty reviewAFCReportDto.certificationDocDisPlayDtos}">
            <tr>
              <td colspan="10">
                No certification by MOH-AFC
              </td>
            </tr>
          </c:when>
          <c:otherwise>
          <tbody id="tbodyFileListId">
            <c:forEach var="docInfo" items="${reviewAFCReportDto.certificationDocDisPlayDtos}" varStatus="status">
              <c:set var="newMaskedRepoId" value="${MaskUtil.maskValue('file', docInfo.repoId)}"/>
              <tr>
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
                  <p><c:out value="${docInfo.submitterRole}"/></p>
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
                  <p><a href="javascript:void(0)" class="btn btn-secondary btn-xs" style="padding: 5px;" onclick='downloadAFCCertificationFile("${newMaskedRepoId}")'><span style="font-size: 12px;color: #444444d6">Download</span></a></p>
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
                  <p style="text-align: center"><input type="checkbox" <c:if test="${docInfo.mohMarkFinal eq 'Y'}">checked</c:if> disabled/></p>
                </td>
              </tr>
            </c:forEach>
          </tbody>
          </c:otherwise>
        </c:choose>
      </table>
    </div>
  </div>
</div>
<br><br>
