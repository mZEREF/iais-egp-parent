<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<br/>
<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type_form_value" value="">
    <input type="hidden" name="crud_action_type_value" value="">

    <%@include file="dashboard.jsp" %>
    <%--Validation fields Start--%>
    <input type="hidden" name="paramController" id="paramController"
           value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <%--Validation fields End--%>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane active" id="premisesTab" role="tabpanel">
                                <c:set value="${errorMap_premises}" var="errMsg"/>
                                <input type="hidden" id="premTypeVal" value="${appGrpPremisesDto.premisesType}"/>
                                <div class="row" id="mainPrem">
                                    <div class="col-xs-12">

                                        <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                            <h2>Payment Summary</h2>
                                            <p >
                                                Total amount due:
                                                <c:out value="${AppSubmissionDto.amountStr}"></c:out>
                                            </p>
                                            <%--<table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Service</th>
                                                    <th>Application Type</th>
                                                    <th>Application No.</th>
                                                    <th>Amount</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach items="${AppSubmissionDto.feeInfoDtos}" var="feeInfoDto">
                                                    <c:set var="baseSvcFeeExt" value="${feeInfoDto.baseSvcFeeExt}"/>
                                                    <c:set var="complexSpecifiedFeeExt" value="${feeInfoDto.complexSpecifiedFeeExt}"/>
                                                    <c:set var="simpleSpecifiedFeeExt" value="${feeInfoDto.simpleSpecifiedFeeExt}"/>
                                                    <!--todo:includedSvcFeeExtList -->
                                                    <!--base -->
                                                    <tr>
                                                        <td>
                                                            <c:forEach var="svcName" items="${baseSvcFeeExt.svcNames}">
                                                                <p> <c:out value="${svcName}"></c:out></p>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <p>
                                                                New Licence
                                                            </p>
                                                        </td>
                                                        <td>
                                                            <p>
                                                                <c:out value="${AppSubmissionDto.appGrpNo}"></c:out>
                                                            </p>
                                                        </td>
                                                        <td>
                                                            <p>
                                                                <c:out value="${baseSvcFeeExt.amountStr}"></c:out>
                                                            </p>
                                                        </td>
                                                    </tr>

                                                    <!--simpleSpecifiedFeeExt -->
                                                    <c:if test="${simpleSpecifiedFeeExt.svcNames.size()>0 }">
                                                        <tr>
                                                            <td>
                                                                <p>&nbsp;&nbsp;Simple Specified Services</p>
                                                                <c:forEach var="svcName" items="${simpleSpecifiedFeeExt.svcNames}">
                                                                    <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
                                                                </c:forEach>

                                                            </td>
                                                            <td>
                                                                <p></p>
                                                            </td>
                                                            <td>
                                                                <p> </p>
                                                            </td>
                                                            <td>
                                                                <p >
                                                                    <c:out value="${simpleSpecifiedFeeExt.amountStr}"></c:out>
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </c:if>

                                                    <!--complexSpecifiedFeeExt -->
                                                    <c:if test="${complexSpecifiedFeeExt.svcNames.size()>0 }">
                                                        <tr>
                                                            <td class="breakdown">
                                                                <p>&nbsp;&nbsp;Complex Specified Services (${complexSpecifiedFeeExt.svcNames.size()})</p>
                                                                <c:forEach var="svcName" items="${complexSpecifiedFeeExt.svcNames}">
                                                                    <p>&nbsp;&nbsp;- <c:out value="${svcName}"></c:out></p>
                                                                </c:forEach>
                                                            </td>
                                                            <td>
                                                                <p></p>
                                                            </td>
                                                            <td>
                                                                <p> </p>
                                                            </td>
                                                            <td>
                                                                <p >
                                                                    <c:out value="${complexSpecifiedFeeExt.amountStr}"></c:out>
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </c:if>

                                                </c:forEach>
                                                </tbody>
                                            </table>--%>
                                            <%@include file="../cc/newApplication/paymentMethod.jsp "%>
                                             </div>

                                    </div>
                                    <div class="application-tab-footer">
                                        <div class="row">
                                            <div class="col-xs-12 col-sm-6 ">
                                                <p><a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a></p>
                                            </div>
                                            <div class="col-xs-12 col-sm-6">
                                                <div class="button-group">
                                                    <a class="btn btn-primary next" id="previewAndSub">Preview and Submit</a>
                                                </div>
                                                <%--<div class="color-small-block" style="border: 0.5px solid rgb(25, 137, 191); border-image: none; background-color: rgb(25, 137, 191);">
                                                  <p style="color: rgb(255, 255, 255);">Dark blue #1989BF</p>
                                                </div>--%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%--Validation Field--%>
            <%@ include file="/include/validation.jsp" %>
        </div>
    </div>
</form>


<script>
    $(document).ready(function () {


    });


</script>