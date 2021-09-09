<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${stepNameMap['SVST009']}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach var="appSvcClinicalDirectorDto" items="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList}" varStatus="status">
                        <c:set var="oldClinicalDirecotrDto"  value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcClinicalDirectorDtoList[status.index]}" />
                        <p>
                            <strong class="col-xs-6">
                                Clinical Director
                                <c:if test="${fn:length(currentPreviewSvcInfo.appSvcClinicalDirectorDtoList)>1}">
                                    ${status.index+1}
                                </c:if>:
                            </strong>
                            <span class="col-xs-4 col-md-4"></span>
                        </p>
                        <span class="col-xs-6"></span>
                        <table aria-describedby="" class="col-xs-12">
                            <thead style="display: none">
                            <tr>
                                <th scope="col"></th>
                            </tr>
                            </thead>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Board</p>
                                </td>
                                <td >
                                    <div class="col-xs-6">
                                      <span class="newVal " attr="${appSvcClinicalDirectorDto.professionBoard}">
                                        <iais:code code="${appSvcClinicalDirectorDto.professionBoard}"></iais:code>
                                      </span>
                                    </div>
                                    <div class="col-xs-6">
                                      <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.professionBoard}">
                                        <iais:code code="${oldClinicalDirecotrDto.professionBoard}"></iais:code>
                                      </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn. No.</p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                        <span class="newVal " attr="${appSvcClinicalDirectorDto.profRegNo}">
                                            ${appSvcClinicalDirectorDto.profRegNo}
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                <jsp:param name="profRegNo" value="${appSvcClinicalDirectorDto.profRegNo}"/>
                                                <jsp:param name="methodName" value="showThisTableNewService"/>
                                            </jsp:include>
                                        </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.profRegNo}">
                                            ${oldClinicalDirecotrDto.profRegNo}
                                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecordMark.jsp">
                                                <jsp:param name="profRegNo" value="${oldClinicalDirecotrDto.profRegNo}"/>
                                                <jsp:param name="methodName" value="showThisTableOldService"/>
                                            </jsp:include>
                                        </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${appSvcClinicalDirectorDto.profRegNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/prsDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${oldClinicalDirecotrDto.profRegNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                            <c:if test="${'MTS'==currentPreviewSvcInfo.serviceCode}">
                                <tr>
                                    <td class="col-xs-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Not registered with a Professional Board</p>
                                    </td>
                                    <td>
                                        <div class="col-xs-6">
                                            <span class="newVal " attr="${appSvcClinicalDirectorDto.noRegWithProfBoard}">
                                              <c:choose>
                                                  <c:when test="${appSvcClinicalDirectorDto.noRegWithProfBoard=='1'}">
                                                  <div class="form-check active" style="padding-left:0px">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                  </div>
                                                  </c:when>
                                                  <c:otherwise>
                                                  <div class="form-check " style="padding-left:0px">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                  </div>
                                                  </c:otherwise>
                                              </c:choose>
                                            </span>
                                        </div>
                                        <div class="col-xs-6">
                                            <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.noRegWithProfBoard}">
                                               <c:choose>
                                                   <c:when test="${oldClinicalDirecotrDto.noRegWithProfBoard=='1'}">
                                                   <div class="form-check active">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                  </div>
                                                   </c:when>
                                                   <c:otherwise>
                                                   <div class="form-check ">
                                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span></p>
                                                  </div>
                                                   </c:otherwise>
                                               </c:choose>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Salutation</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${appSvcClinicalDirectorDto.salutation}">
                                          <iais:code code="${appSvcClinicalDirectorDto.salutation}"></iais:code>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.salutation}">
                                          <iais:code code="${oldClinicalDirecotrDto.salutation}"></iais:code>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name</p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                                      <span class="newVal " attr="${appSvcClinicalDirectorDto.name}">
                                        <c:out value="${appSvcClinicalDirectorDto.name}"/>
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${appSvcClinicalDirectorDto.profRegNo}"/>
                                            <jsp:param name="personName" value="${appSvcClinicalDirectorDto.name}"/>
                                            <jsp:param name="methodName" value="showThisNameTableNewService"/>
                                        </jsp:include>
                                      </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                                      <span class="oldVal "
                                            attr="${oldClinicalDirecotrDto.name}"
                                            style="display: none">${oldClinicalDirecotrDto.name}
                                        <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecordMark.jsp">
                                            <jsp:param name="profRegNo" value="${oldClinicalDirecotrDto.profRegNo}"/>
                                            <jsp:param name="personName" value="${oldClinicalDirecotrDto.name}"/>
                                            <jsp:param name="methodName" value="showThisNameTableOldService"/>
                                        </jsp:include>
                                      </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${appSvcClinicalDirectorDto.profRegNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/nameDisciplinaryRecords.jsp">
                                        <jsp:param name="profRegNo" value="${oldClinicalDirecotrDto.profRegNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                                        <span class="newVal " attr="${appSvcClinicalDirectorDto.idType}">
                                            <iais:code code="${appSvcClinicalDirectorDto.idType}"></iais:code>
                                        </span>
                                    </div>
                                    <div class="col-xs-6">
                                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.idType}">
                                            <iais:code code="${oldClinicalDirecotrDto.idType}"></iais:code>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No.</p>
                                </td>
                                <td>
                                    <div class="col-xs-6 img-show">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.idNo}">
                            ${appSvcClinicalDirectorDto.idNo}
                            <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                                <jsp:param name="idNo" value="${appSvcClinicalDirectorDto.idNo}"/>
                                <jsp:param name="methodName" value="showThisTableNewService"/>
                            </jsp:include>
                        </span>
                                    </div>
                                    <div class="col-xs-6 img-show">
                        <span class="oldVal " style="display: none" attr="${oldClinicalDirecotrDto.idNo}">
                              ${oldClinicalDirecotrDto.idNo}
                          <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecordMark.jsp">
                              <jsp:param name="idNo" value="${oldClinicalDirecotrDto.idNo}"/>
                              <jsp:param name="methodName" value="showThisTableOldService"/>
                          </jsp:include>
                        </span>
                                    </div>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${appSvcClinicalDirectorDto.idNo}"/>
                                        <jsp:param name="cssClass" value="new-img-show"/>
                                    </jsp:include>
                                    <jsp:include page="/WEB-INF/jsp/iais/hcsaLicence/section/disciplinaryRecords.jsp">
                                        <jsp:param name="idNo" value="${oldClinicalDirecotrDto.idNo}"/>
                                        <jsp:param name="cssClass" value="old-img-show"/>
                                    </jsp:include>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.designation}">
                            <iais:code code="${appSvcClinicalDirectorDto.designation}"></iais:code>
                        </span>
                                    </div>
                                    <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.designation}">
                            <iais:code code="${oldClinicalDirecotrDto.designation}"></iais:code>
                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Speciality</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal" attr="${appSvcClinicalDirectorDto.speciality}">
                          <iais:code code="${appSvcClinicalDirectorDto.speciality}"></iais:code>
                        </span>
                                    </div>
                                    <div class="col-xs-6">
                        <span class="oldVal"  style="display: none"
                              attr="${oldClinicalDirecotrDto.speciality}">
                          <iais:code
                                  code="${oldClinicalDirecotrDto.speciality}"></iais:code>
                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Date when specialty was obtained</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.specialtyGetDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.specialtyGetDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                    </div>
                                    <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.specialtyGetDate}">
                          <fmt:formatDate value="${oldClinicalDirecotrDto.specialtyGetDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Registration Date</p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.typeOfCurrRegi}">
                                ${appSvcClinicalDirectorDto.typeOfCurrRegi}
                        </span>
                                        <br>
                                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.typeOfCurrRegi}">
                                                ${oldClinicalDirecotrDto.typeOfCurrRegi}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Current Registration Date</p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.currRegiDate}">
                          <fmt:formatDate value="${appSvcClinicalDirectorDto.currRegiDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                    </div>
                                    <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.currRegiDate}">
                          <fmt:formatDate value="${oldClinicalDirecotrDto.currRegiDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Practicing Certificate End Date </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.praCerEndDate}">
                          <fmt:formatDate value="${appSvcClinicalDirectorDto.praCerEndDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                    </div>
                                    <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.praCerEndDate}">
                           <fmt:formatDate value="${oldClinicalDirecotrDto.praCerEndDate}" pattern="dd/MM/yyyy"></fmt:formatDate>
                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Register </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                          <span class="newVal " attr="${appSvcClinicalDirectorDto.typeOfRegister}">
                                  ${appSvcClinicalDirectorDto.typeOfRegister}
                          </span>
                                        <br>
                                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.typeOfRegister}">
                                                ${oldClinicalDirecotrDto.typeOfRegister}
                                        </span>
                                    </div>

                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant Experience </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.relevantExperience}">
                                ${appSvcClinicalDirectorDto.relevantExperience}
                        </span>
                                        <br>
                                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.relevantExperience}">
                                                ${oldClinicalDirecotrDto.relevantExperience}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Clinical Director (CD) holds a valid certification issued by an Emergency Medical Services ("EMS") Medical Directors workshop </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal" attr="${appSvcClinicalDirectorDto.holdCerByEMS}">
                          <c:if test="${appSvcClinicalDirectorDto.holdCerByEMS=='1'}">Yes</c:if>
                          <c:if test="${appSvcClinicalDirectorDto.holdCerByEMS=='0'}">No</c:if>
                        </span>
                                    </div>
                                    <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.holdCerByEMS}">
                          <c:if test="${oldClinicalDirecotrDto.holdCerByEMS=='1'}">Yes</c:if>
                          <c:if test="${oldClinicalDirecotrDto.holdCerByEMS=='0'}">No</c:if>
                        </span>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (ACLS) </p>
                                </td>
                                <td>
                                    <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.aclsExpiryDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.aclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                                    </div>
                                    <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.aclsExpiryDate}">
                           <fmt:formatDate value="${oldClinicalDirecotrDto.aclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${'MTS'==currentPreviewSvcInfo.serviceCode}">

                                <tr>
                                    <td class="col-xs-6">
                                        <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Expiry Date (BCLS and AED) </p>
                                    </td>
                                    <td>
                                        <div class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.bclsExpiryDate}">
                            <fmt:formatDate value="${appSvcClinicalDirectorDto.bclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                                        </div>
                                        <div class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.bclsExpiryDate}">
                            <fmt:formatDate value="${oldClinicalDirecotrDto.bclsExpiryDate}" pattern="dd/MM/yyyy"/>
                        </span>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Mobile No.  </p>
                                </td>
                                <td>
                                    <div  class="col-xs-6">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.mobileNo}">
                                ${appSvcClinicalDirectorDto.mobileNo}
                        </span>
                                    </div>
                                    <div  class="col-xs-6">
                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.mobileNo}">
                                ${oldClinicalDirecotrDto.mobileNo}
                        </span>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="col-xs-6">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email Address  </p>
                                </td>
                                <td>
                                    <div class="col-xs-12">
                        <span class="newVal " attr="${appSvcClinicalDirectorDto.emailAddr}">
                                ${appSvcClinicalDirectorDto.emailAddr}
                        </span>
                                        <br>
                                        <span class="oldVal "  style="display: none" attr="${oldClinicalDirecotrDto.emailAddr}">
                                                ${oldClinicalDirecotrDto.emailAddr}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

</div>