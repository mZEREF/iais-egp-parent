<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%--@elvariable id="appBasicInfo" type="sg.gov.moh.iais.egp.bsb.dto.info.common.AppBasicInfo"--%>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Submission Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="text-align: right">Application No.</td>
                        <td class="col-xs-6" style="padding-left : 20px"><c:out value="${appBasicInfo.appNo}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Type</td>
                        <td style="padding-left : 20px"><iais:code code="${appBasicInfo.appType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Sub-Type</td>
                        <td style="padding-left : 20px"><iais:code code="${appBasicInfo.processType}"/></td>
                    </tr>
                    <tr>
                        <td style="text-align: right">Application Status</td>
                        <td style="padding-left : 20px"><iais:code code="${appBasicInfo.status}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<%--@elvariable id="appId" type="java.lang.String"--%>
<%--@elvariable id="appViewModuleType" type="java.lang.String"--%>
<c:set var="maskedAppId"><iais:mask name="appId" value="${appId}"/></c:set>
<c:set var="maskedAppViewModuleType"><iais:mask name="appViewModuleType" value="${appViewModuleType}"/></c:set>
<div style="text-align: center">
    <a href="javascript:void(0);" onclick="viewApplication('${maskedAppId}', '${maskedAppViewModuleType}')">
        <button type="button" class="btn btn-primary">
            View Application
        </button>
    </a>
</div>
<c:choose>
    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
    <c:when test="${goBackUrl ne null}">
        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:when>
    <c:otherwise>
        <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:otherwise>
</c:choose>