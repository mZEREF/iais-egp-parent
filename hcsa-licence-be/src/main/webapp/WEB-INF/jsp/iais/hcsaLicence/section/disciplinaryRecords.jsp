<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="idNo" value="${param.idNo}" />
<c:set var="cssClass" value="${param.cssClass}" />
<c:set var="style" value="${empty param.style ? '' : param.style}" />

<c:if test="${not empty idNo && not empty hashMap[idNo]}" >
    <div class="disciplinary-record ${cssClass}" style="display: none">
        <div style="position: absolute;z-index: 100;background-color: #F5F5F5;width: 100%;margin-top: 5%;${style}">
            <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label>
            <span style="position: absolute;right: 0px;color: black" onclick="javascript:closeThis(this);">X&nbsp;</span>
            <div class="table-responsive">
                <table aria-describedby="" border="1px"
                       style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                    <tr>
                        <th scope="col" style="font-weight:normal">Indentification No.</th>
                        <th scope="col" style="font-weight:normal">Case No.</th>
                        <th scope="col" style="font-weight:normal">Case Type Description</th>
                        <th scope="col" style="font-weight:normal">Case Status Description</th>
                        <th scope="col" style="font-weight:normal">Offence Description</th>
                        <th scope="col" style="font-weight:normal">Outcome Description</th>
                        <th scope="col" style="font-weight:normal">Outcome Issue Date</th>
                        <th scope="col" style="font-weight:normal">Prosecution Outcome Description</th>
                        <th scope="col" style="font-weight:normal">Created Date</th>
                        <th scope="col" style="font-weight:normal">Update Date</th>
                    </tr>
                    <c:forEach items="${hashMap[idNo]}" var="map">
                        <tr>
                            <td>${map.identificationNo}</td>
                            <td>${map.caseNo}</td>
                            <td>${map.caseType}</td>
                            <td>${map.caseStatus}</td>
                            <td>${map.offenceDesc}</td>
                            <td>${map.outcome}</td>
                            <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"/></td>
                            <td>${map.prosecutionOutcome}</td>
                            <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"/></td>
                        </tr>
                    </c:forEach>
                    <tr></tr>
                </table>
            </div>
        </div>
    </div>
</c:if>
