<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <label class="app-title">${currStepName}</label>
    <div class="amend-preview-info">
        <c:forEach items="${currentPreviewSvcInfo.svcPersonnelDto.normalList}" var="appSvcPersonnelDtoList"
                   varStatus="status">
            <div class="form-check-gp">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p><strong>Service Personnel<c:if
                                    test="${currentPreviewSvcInfo.appSvcPersonnelDtoList.size() > 1}"> ${status.index+1}</c:if>:</strong>
                            </p>
                        </div>
                        <table aria-describedby="" class="col-xs-12">
                            <thead style="display: none">
                            <tr><th scope="col"></th></tr>
                            </thead>
                            <c:choose>
                                <c:when test="${currentPreviewSvcInfo.serviceCode=='BLB'}">
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.designation}</p>
                                        </td>
                                    </tr>
                                    <c:if test="${'Others' == appSvcPersonnelDtoList.designation}">
                                        <tr>
                                            <td class="col-xs-6">
                                            </td>
                                            <td>
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.otherDesignation}</p>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn. No.</p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.profRegNo}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:when test="${currentPreviewSvcInfo.serviceCode=='TSB'}">
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.qualification}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                                        </td>
                                    </tr>

                                </c:when>
                                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT001'}">
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="SPPT001"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Designation </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.designation}</p>
                                        </td>
                                    </tr>
                                    <c:if test="${'Others' == appSvcPersonnelDtoList.designation}">
                                        <tr>
                                            <td class="col-xs-6">
                                            </td>
                                            <td>
                                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.otherDesignation}</p>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.qualification}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT002'}">
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="SPPT002"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.qualification}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT003'}">
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="SPPT003"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:when test="${appSvcPersonnelDtoList.personnelType=='SPPT004'}">
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Select Service Personnel </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span><iais:code code="SPPT004"/></p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Professional Regn. No. </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.profRegNo}</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Name </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.name}</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Qualification </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.qualification}
                                            </p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="col-xs-6">
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Relevant working experience (Years) </p>
                                        </td>
                                        <td>
                                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${appSvcPersonnelDtoList.wrkExpYear}</p>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </table>
                    </div>
                </div>

            </div>

<c:if test="${embryologistMinCount != 0}">
    <div class="panel-main-content">
        <c:forEach begin="0" end="${embryologistMinCount - 1}" step="1" varStatus="status">
            <input class="length" type="hidden" name="emCount" value="1"/>
            <c:set value="SP001" var="logo"/>
            <c:set var="index" value="${status.index}"/>
            <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.embryologistList[index]}"/>
            <%@include file="servicePersonnelEmbryologist.jsp" %>
        </c:forEach>
    </div>
    </div>
</c:if>


<c:if test="${specialCount != 0}">
    <c:forEach begin="0" end="${specialCount - 1}" step="1" varStatus="status">
        <input class="length" type="hidden" name="speCount" value="1"/>
        <c:set var="index" value="${status.index}"/>
        <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.specialList[index]}"/>
        <%@include file="servicePersonnelDetail.jsp" %>
    </c:forEach>
</c:if>


<c:if test="${normalCount != 0}">

    mmmmm
    <div class="panel-main-content">
        <c:forEach begin="0" end="${normalCount - 1}" step="1" varStatus="status">
            <c:set var="index" value="${status.index}"/>
            <c:set var="appSvcPersonnelDto" value="${currentPreviewSvcInfo.svcPersonnelDto.normalList[index]}"/>
            <%@include file="servicePersonnelBlood.jsp" %>
        </c:forEach>
    </div>
</c:if>

</div>


</div>