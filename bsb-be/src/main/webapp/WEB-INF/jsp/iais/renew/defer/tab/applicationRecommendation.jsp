<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="bsb" uri="http://www.ecq.com/iais-bsb" %>

<bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
<br/><br/>
<div class="panel panel-default">
    <div class="panel-heading" style="text-align: center"><strong>Facility Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                        <tr>
                            <th scope="col" style="display: none"></th>
                        </tr>
                        <tr>
                            <td class="col-xs-6" style="font-weight: bold; text-align: right">Facility Name</td>
                            <td class="col-xs-6" style="padding-left : 20px"><c:out value="${facilityDetailsInfo.name}"/></td>
                        </tr>
                        <tr>
                            <td style="font-weight: bold; text-align: right">Facility Classification</td>
                            <td style="padding-left: 20px"><iais:code code="${facilityDetailsInfo.classification}"/></td>
                        </tr>
                        <tr>
                            <td style="font-weight: bold; text-align: right">Existing Facility Activity Type Approval</td>
                            <td style="padding-left: 20px">
                                <c:if test="${facilityDetailsInfo.existingFacilityActivityTypeApprovalList eq null || facilityDetailsInfo.existingFacilityActivityTypeApprovalList.size() == 0}">-</c:if>
                                <c:forEach var="existingFacilityActivityTypeApproval" items="${facilityDetailsInfo.existingFacilityActivityTypeApprovalList}">
                                    <div><iais:code code="${existingFacilityActivityTypeApproval}"/></div>
                                </c:forEach>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
