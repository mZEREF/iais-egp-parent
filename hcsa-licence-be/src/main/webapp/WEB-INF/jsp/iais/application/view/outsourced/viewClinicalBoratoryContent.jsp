<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<c:if test="${not empty outsourceDto.clinicalLaboratoryList}">
    <div class="amended-service-info-gp form-horizontal min-row">
        <div class="col-xs-12">
            <p><strong>Clinical Laboratory Service</strong></p>
        </div>
        <div class="amend-preview-info form-horizontal min-row">
<%--            <div class="col-lg-12 col-xs-12 col-md-12">--%>
                <div class="intranet-content">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th style="width: 5%;">Licence No.
<%--                                <div style="margin-left: 12px;">--%>
<%--                                    Licence No.--%>
<%--                                </div>--%>
                            </th>
                            <th style="width: 8%;">Business Name
<%--                                <div style="margin-left: 12px;">--%>
<%--                                    Business Name--%>
<%--                                </div>--%>
                            </th>
                            <th style="width: 10%;">Address
<%--                                <div style="margin-left: 12px;">--%>
<%--                                    Address--%>
<%--                                </div>--%>
                            </th>
                            <th style="width: 15%;">Licence Tenure
<%--                                <div style="margin-left: 49px;">--%>
<%--                                    Licence Tenure--%>
<%--                                </div>--%>
                            </th>
                            <th style="width: 15%;">Date of Agreement
<%--                                <div style="margin-left: 12px;">--%>
<%--                                    Date of Agreement--%>
<%--                                </div>--%>
                            </th>
                            <th style="width: 15%;">End Date of Agreement
<%--                                <div style="margin-left: 12px;width: 100%;">--%>
<%--                                    End Date of Agreement--%>
<%--                                </div>--%>
                            </th>
                            <th style="width: 32%;">Scope of Outsourcing
<%--                                <div style="margin-left: 12px;">--%>
<%--                                    Scope of Outsourcing--%>
<%--                                </div>--%>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="clb" items="${outsourceDto.clinicalLaboratoryList}">
                            <c:set var="appPremOutSourceLicenceDto" value="${clb.appPremOutSourceLicenceDto}"/>
                            <c:if test="${!empty appPremOutSourceLicenceDto}">
                                <tr>
                                    <td>
                                        <p>${appPremOutSourceLicenceDto.licenceNo}</p>
                                    </td>
                                    <td>

                                        <p>${clb.businessName}</p>
                                    </td>
                                    <td>

                                        <p>${clb.address}</p>
                                    </td>
                                    <td>
                                        <p>${clb.expiryDate}</p>
                                    </td>
                                    <td>
                                        <p>${clb.startDateStr}</p>
                                            <%--                                            <p>${appPremOutSourceLicenceDto.agreementStartDate}</p>--%>
                                            <%--                                    <p><fmt:formatDate value="${appPremOutSourceLicenceDto.agreementStartDate}" pattern="dd/MM/yyyy"/></p>--%>
                                    </td>
                                    <td>
                                        <p>${clb.endDateStr}</p>
                                            <%--                                            <p>${appPremOutSourceLicenceDto.agreementEndDate}</p>--%>
                                            <%--                                    <p><fmt:formatDate value="${appPremOutSourceLicenceDto.agreementEndDate}" pattern="dd/MM/yyyy"/></p>--%>
                                    </td>
                                    <td>
                                        <p class="outstanding">${appPremOutSourceLicenceDto.outstandingScope}</p>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
        <%--        </div>--%>
        </div>
    </div>
</c:if>

