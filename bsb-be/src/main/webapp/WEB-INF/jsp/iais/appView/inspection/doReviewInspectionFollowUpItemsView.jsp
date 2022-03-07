<div class="table-gp">
    <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
        <thead>
        <tr>
            <th scope="col" style="text-align:center;">S/N</th>
            <th scope="col" style="text-align:center;">Follow-Up Item</th>
            <th scope="col" style="text-align:center;">Due Date</th>
            <th scope="col" style="text-align:center;">Applicant Remarks</th>
            <th scope="col" style="text-align:center;">Request extension of due date</th>
            <th scope="col" style="text-align:center;">Reason for extension</th>
        </tr>
        </thead>
        <tbody style="text-align:center;">
        <c:forEach var="item" items="${inspectionFollowUpItemsDto.itemDtoList}" varStatus="status">
            <tr>
                <td>
                    <p><c:out value="${status.index + 1}"/></p>
                </td>
                <td>
                    <p><c:out value="${item.itemText}"/></p>
                </td>
                <td>
                    <p><c:out value="${item.deadline}"/></p>
                </td>
                <td>
                    <p><c:out value="${item.remarks}"/></p>
                </td>
                <td>
                    <p><c:out value="${item.requestExtensionOfDueDate}"/></p>
                </td>
                <td>
                    <p><c:out value="${item.reasonForExtension}"/></p>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>