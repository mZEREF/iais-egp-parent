<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
  <label class="svc-title">${currStepName}</label>
  <div class="amend-preview-info">
    <div class="form-check-gp">
      <div class="row">
        <div class="col-xs-12">
          <c:forEach items="${currentPreviewSvcInfo.appSvcMedAlertPersonList}" var="appSvcMedAlertPerson" varStatus="status">

            <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
              <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>MedAlert Person<c:if test="${currentPreviewSvcInfo.appSvcMedAlertPersonList.size() > 1}"> ${status.index+1}</c:if>: </strong></p>
            </div>
            <table aria-describedby="" class="col-xs-12">
              <thead style="display: none">
              <tr><th scope="col"></th></tr>
              </thead>
              <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.salutation}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.name}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.idType}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.idNo}"/></p>
                </td>
              </tr>
              <c:if test="${appSvcMedAlertPerson.idType == 'IDTYPE003'}">
              <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span>Country of issuance
                  </p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology">
                    <span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.nationality}" />
                  </p>
                </td>
              </tr>
              </c:if>
              <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.mobileNo}"/></p>
                </td>
              </tr>

              <tr>
                <td class="col-xs-6">
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address</p>
                </td>
                <td>
                  <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${appSvcMedAlertPerson.emailAddr}"/></p>
                </td>
              </tr>

            </table>
          </c:forEach>
        </div>
      </div>
    </div>

  </div>

</div>

