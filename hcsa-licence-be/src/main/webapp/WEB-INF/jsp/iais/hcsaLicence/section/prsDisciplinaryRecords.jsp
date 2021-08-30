<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="profRegNo" value="${param.profRegNo}" />
<c:set var="cssClass" value="${param.cssClass}" />
<c:set var="style" value="${empty param.style ? '' : param.style}" />

<c:if test="${not empty profRegNo && not empty listHashMap[profRegNo]}" >
    <div class="disciplinary-record ${cssClass}" style="display: none">
        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;width: 100%;margin-top: 5%;${style}">
            <label style="font-weight: normal">
                The Professional has existing disciplinary records in PRS
            </label>
            <span style="position: absolute;right: 0px;color: black" onclick="javascript:closeThis(this);">X&nbsp;</span>
            <div class="table-responsive">
                <table aria-describedby="" border="1px"
                       style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                    <tr>
                        <th scope="col" style="font-weight:lighter">Professional Regn. No.</th>
                        <th scope="col" style="font-weight:lighter">Complaints</th>
                        <th scope="col" style="font-weight:lighter">Final Outcome</th>
                        <th scope="col" style="font-weight:lighter">Fine Amounts</th>
                        <th scope="col" style="font-weight:lighter">Restrictive Practice Start Date</th>
                        <th scope="col" style="font-weight:lighter">Restrictive Practice End Date</th>
                        <th scope="col" style="font-weight:lighter">Suspension Start Date</th>
                        <th scope="col" style="font-weight:lighter">Suspension End Date</th>
                        <th scope="col" style="font-weight:lighter">Other disciplinary action Start Date</th>
                        <th scope="col" style="font-weight:lighter">Other Disciplinary action End Date</th>
                    </tr>
                    <c:forEach items="${listHashMap[profRegNo]}" var="list">
                        <tr>
                            <td>${profRegNo}</td>
                            <td style="text-align: left">
                                <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint1}</
                                <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint2}</p>
                                <p style="margin-bottom: 10%;white-space: nowrap">${list.complaint3}</p
                            </td>
                            <td>${list.finaloutcome}</td>
                            <td>${list.fineamount}</td>
                            <td>${list.restrictstartdate}</td>
                            <td>${list.restrictenddate}</td>
                            <td>${list.suspendstartdate}</td>
                            <td>${list.suspendenddate}</td>
                            <td>${list.otherstartdate}</td>
                            <td>${list.otherenddate}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</c:if>
