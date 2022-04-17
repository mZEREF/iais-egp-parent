<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%--@elvariable id="bsbInspectionConfig" type="java.util.List<ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto>"--%>
<%--@elvariable id="answerMap" type="java.util.HashMap<java.lang.String, java.lang.String>"--%>
<%--@elvariable id="editable" type="java.lang.Boolean"--%>

<c:forEach var="checklistConfigDto" items="${bsbInspectionConfig}">
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
</c:forEach>
<iais:action>
    <c:choose>
        <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
        <c:when test="${goBackUrl ne null}">
            <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
        </c:when>
        <c:otherwise>
            <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList" style="float:left"><em
                    class="fa fa-angle-left"></em> Previous</a>
        </c:otherwise>
    </c:choose>

    <div style="float:right">
        <button type="button" class="btn btn-primary">
            DOWNLOAD
        </button>
        <%--TODO AdHocCheckList is no exits--%>
        <button type="button" style="float:right" class="btn btn-primary" >
            EDIT
        </button>
    </div>
</iais:action>