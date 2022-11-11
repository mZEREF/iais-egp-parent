<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<c:if test="${not empty outsourceDto.radiologicalServiceList}">
    <c:set var="svcCodeRds" value="0"/>
    <c:if test="${!empty outsourceDto.svcCodeList}">
        <c:set var="svcCodeList" value="${outsourceDto.svcCodeList}"/>
        <c:forEach var="svcCode" items="${svcCodeList}">
            <c:if test="${svcCode eq AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES}">
                <c:set var="svcCodeRds" value="1" />
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${svcCodeRds eq 0}">
        <div class="amended-service-info-gp form-horizontal min-row ">
            <div class="col-xs-12">
                <p><strong>Radiological Service</strong></p>
            </div>

            <div class="amend-preview-info form-horizontal min-row">
                <div class="col-lg-12 col-xs-12 col-md-12">
                    <div class="intranet-content">
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th style="width: 15%;">
                                    <div style="margin-left: 12px;">
                                        Licence No.
                                    </div>
                                </th>
                                <th style="width: 15%;">
                                    <div style="margin-left: 12px;">
                                        Business Name
                                    </div>
                                </th>
                                <th style="width: 10%;">
                                    <div style="margin-left: 12px;">
                                        Address
                                    </div>
                                </th>
                                <th style="width: 15%;">
                                    <div style="margin-left: 12px;">
                                        Licence Tenure
                                    </div>
                                </th>
                                <th style="width: 15%;">
                                    <div style="margin-left: 12px;">
                                        Date of Agreement
                                    </div>
                                </th>
                                <th style="width: 15%;">
                                    <div style="margin-left: 12px;">
                                        End Date of Agreement
                                    </div>
                                </th>
                                <th style="width: 15%;">
                                    <div style="margin-left: 12px;">
                                        Scope of Outsourcing
                                    </div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="rds" items="${outsourceDto.radiologicalServiceList}">
                                <c:set var="appPremOutSourceLicenceDto" value="${rds.appPremOutSourceLicenceDto}"/>
                                <c:if test="${!empty appPremOutSourceLicenceDto}">
                                    <tr>
                                        <td>

                                            <p>${appPremOutSourceLicenceDto.licenceNo}</p>
                                        </td>
                                        <td>

                                            <p>${rds.businessName}</p>
                                        </td>
                                        <td>

                                            <p>${rds.address}</p>
                                        </td>
                                        <td>

                                            <p>${rds.expiryDate}</p>
                                        </td>
                                        <td>

                                            <p>${appPremOutSourceLicenceDto.agreementStartDate}</p>
                                                <%--                                    <p><fmt:formatDate value="${appPremOutSourceLicenceDto.agreementStartDate}" pattern="dd/MM/yyyy"/></p>--%>
                                        </td>
                                        <td>

                                            <p>${appPremOutSourceLicenceDto.agreementEndDate}</p>
                                                <%--                                    <p><fmt:formatDate value="${appPremOutSourceLicenceDto.agreementEndDate}" pattern="dd/MM/yyyy"/></p>--%>
                                        </td>
                                        <td>
                                            <p><textarea style="border:none;background-color: transparent;resize: none;" class="scopeOutsource">${appPremOutSourceLicenceDto.outstandingScope}</textarea>
                                            </p>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</c:if>