<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/4/25
  Time: 18:00
  To change this template use File | Settings | File Templates.
--%>
<c:forEach var="stepSchem" items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}">
<c:if test="${stepSchem.stepCode == 'SVST009'}">
    <c:set var="currStepName" value="${stepSchem.stepName}"/>
</c:if>
</c:forEach>
<div class="amended-service-info-gp">
    <label style="font-size: 2.2rem">Key Clinical Personnel</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList}" var="cdDto" varStatus="status">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><strong>Key Clinical Personnel<c:if test="${currentPreviewSvcInfo.appSvcClinicalDirectorDtoList.size() > 1}"> ${status.index+1}</c:if>: </strong></p>
                        </div>
                        <table class="col-xs-8">
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Board</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.professionBoard}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Profession Regn No.</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.profRegNo}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Not registered with a Professional Board</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.noRegWithProfBoard}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Years of experience in patient transport</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="cdDto.transportYear"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Years of experience in patient transport</p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.transportYear}"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.name }"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID No. </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value="${cdDto.idNo }"/></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>ID Type </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.idType}"></iais:code></p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="${cdDto.designation}"/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Specialty </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value=""/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Date when specialty was gotten </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value=""/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Current Registration </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value=""/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Current Registration Date </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value=""/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Practicing Certificate End Date </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value=""/> </p>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-xs-8">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Type of Register </p>
                                </td>
                                <td>
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><c:out value=""/> </p>
                                </td>
                            </tr>


                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
