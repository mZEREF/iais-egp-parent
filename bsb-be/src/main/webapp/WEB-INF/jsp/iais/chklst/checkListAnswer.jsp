<%--@elvariable id="checklistConfigDto" type="ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto"--%>
<%--@elvariable id="answerMap" type="java.util.HashMap<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="editable" type="java.lang.Boolean"--%>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants" %>

<c:choose>
    <c:when test="${checklistConfigDto.common}">
        <h3>General Regulation</h3>
    </c:when>
    <c:otherwise>
        <h3>BSB Regulation</h3>
    </c:otherwise>
</c:choose>
<c:forEach var="section" items="${checklistConfigDto.sectionDtos}" varStatus="status">
    <div class="panel panel-default">
        <div class="panel-heading" role="tab">
            <h4 class="panel-title">
                <a role="button" data-toggle="" href="javascript:void(0)">${section.section}</a>
            </h4>
        </div>
        <div class="panel-collapse collapse in" id="collapse${section.section}" role="tabpanel">
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col" style="width: 15%">Checklist Item Clause</th>
                        <th scope="col" style="width: 35%;">Item</th>
                        <th scope="col" style="width: 5%">Yes</th>
                        <th scope="col" style="width: 5%">No</th>
                        <th scope="col" style="width: 5%">N/A</th>
                        <th scope="col" style="width: 35%">Remarks</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${section.checklistItemDtos}" varStatus="itemStatus">
                        <c:set var="itemKey" value="${checklistConfigDto.id}--${section.id}--${item.itemId}"/>
                        <c:set var="answer" value="${answerMap.get(itemKey).answer}"/>
                        <c:set var="remarks" value="${answerMap.get(itemKey).remarks}"/>
                        <tr>
                            <td>
                                <p class="visible-xs visible-sm table-row-title"></p>
                                <p>${status.index + 1}.${itemStatus.index + 1}</p>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title"></p>
                                <p>${item.checklistItem}</p>
                            </td>
                            <td>
                                <p><input name="${itemKey}" type="radio"
                                          <c:if test="${!editable}">disabled</c:if>
                                          <c:if test="${ChecklistConstants.ANSWER_YES eq answer}">checked="checked"</c:if>
                                          value="${ChecklistConstants.ANSWER_YES}"/></p>
                            </td>
                            <td>
                                <p><input name="${itemKey}" type="radio"
                                          <c:if test="${!editable}">disabled</c:if>
                                          <c:if test="${ChecklistConstants.ANSWER_NO eq answer}">checked="checked"</c:if>
                                          value="${ChecklistConstants.ANSWER_NO}"/></p>
                            </td>
                            <td>
                                <p><input name="${itemKey}" type="radio"
                                          <c:if test="${!editable}">disabled</c:if>
                                          <c:if test="${ChecklistConstants.ANSWER_NA eq answer}">checked="checked"</c:if>
                                          value="${ChecklistConstants.ANSWER_NA}"/></p>
                            </td>
                            <td>
                                <textarea id="${itemKey}-remark"
                                          style="width: 100%;margin-bottom: 15px;resize:none;"
                                          <c:if test="${!editable}">disabled</c:if>
                                          rows="6" name="${itemKey}-remarks"
                                          maxlength="100">${remarks}</textarea>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="modal fade " id="ShowRegClauseModal${status.index + 1}" tabindex="-1"
                     role="dialog" aria-labelledby="regOutsideWindow">
                    <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-body">
                                <div class="row" style="height:500px;overflow:auto; ">
                                    <div class="col-md-10 col-md-offset-2"
                                         style="width: 100%; margin: 0;white-space:pre-wrap;"><span
                                            id="regulationClauseText"
                                            style="font-size: 2rem">${item.regulationClause}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary"
                                        data-dismiss="modal">Close
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:forEach>