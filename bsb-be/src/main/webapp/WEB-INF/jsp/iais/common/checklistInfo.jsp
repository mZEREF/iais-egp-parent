<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%--@elvariable id="bsbInspectionConfig" type="ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto"--%>
<%--@elvariable id="answerMap" type="java.util.HashMap<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="editable" type="java.lang.Boolean"--%>
<%--@elvariable id="adhocActionFlag" type="java.lang.String"--%>
<h3>BSB Regulation</h3>
<c:forEach var="section" items="${bsbInspectionConfig.sectionDtos}" varStatus="status">
    <div class="panel panel-default">
        <div class="panel-heading" role="tab">
            <h4 class="panel-title">
                <a role="button" data-toggle="" href="javascript:void(0)">${section.section}</a>
            </h4>
        </div>
        <div class="panel-collapse collapse in" id="collapse${section.section}" role="tabpanel">
            <div class="panel-body">
                <table class="table" aria-describedby="BSB Regulation">
                    <thead>
                    <tr>
                        <th scope="col" style="width: 15%">Checklist Item Clause</th>
                        <th scope="col" style="width: 35%;">Item</th>
                        <th scope="col" style="width: 35%">Risk Level</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${section.checklistItemDtos}" varStatus="itemStatus">
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
                                <p><iais:code code="${item.riskLevel}"/></p>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</c:forEach>
<%--@elvariable id="adhocCheckListAttr" type="sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto"--%>
<c:if test="${!empty adhocCheckListAttr}">
    <div class="panel panel-default">
        <div class="panel-heading" role="tab">
            <h4 class="panel-title">Adhoc Item</h4>
        </div>
        <div class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingPremise">
            <div class="panel-body">
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" width="50%">Item Description</th>
<%--                        <th scope="col" width="40%">Answer Type</th>--%>
<%--                        <th scope="col" width="10%">Risk Level</th>--%>
                        <c:if test="${adhocActionFlag == 'Y'}">
                            <th scope="col" width="10%">Action</th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="adhocItem" items="${adhocCheckListAttr.adhocChecklistItemList}" varStatus="status">
                        <tr>
                            <td>
                                <p><c:out value="${adhocItem.question}"/></p>
                            </td>

<%--                            <td>--%>
<%--                                <p><iais:code code="${adhocItem.answerType}"/></p>--%>
<%--                            </td>--%>

<%--                            <td>--%>
<%--                                <p><iais:code code="${adhocItem.riskLevel}"/></p>--%>
<%--                            </td>--%>

                            <td>
                                <c:if test="${adhocActionFlag == 'Y'}">
                                    <button onclick="removeAdhocItem('<iais:mask name="crud_action_value"
                                                                                 value="${status.index}"/>')"
                                            type="button"> Delete
                                    </button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</c:if>