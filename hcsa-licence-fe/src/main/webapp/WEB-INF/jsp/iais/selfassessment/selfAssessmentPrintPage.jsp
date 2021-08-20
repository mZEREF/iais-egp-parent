<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/3/23
  Time: 17:25
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/6
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<webui:setLayout name="iais-internet"/>
<div class="container">
    <br>
    <div id="printContent">
        <div class="row">
            <div class="col-xs-12">
                <div class="center-content">
                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                        <c:forEach  var="selfAssessmentConfig" items="${selfAssessmentDetail.selfAssessmentConfig}" varStatus="status">
                            <c:choose>
                                <c:when test="${selfAssessmentConfig.common}">
                                    <div class="panel panel-default">
                                        <div class="panel-heading" id="headingPremise${status.index}" role="tab">
                                            <h4 class="panel-title"><a id="sub_style_answer${status.index}" role="button" data-toggle="collapse" href="#collapsePremise${status.index}" aria-expanded="true"
                                                                       aria-controls="collapsePremise${status.index}" class="">General Regulation</a></h4>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="panel panel-default">
                                        <div class="panel-heading" id="headingPremise${status.index}" role="tab">
                                            <h4 class="panel-title"><a id="sub_style_answer${status.index}" role="button" data-toggle="collapse" href="#collapsePremise${status.index}" aria-expanded="true"
                                                                       aria-controls="collapsePremise${status.index}" class="">${selfAssessmentConfig.svcName}</a></h4>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>


                            <div class="panel-collapse collapse in" id="collapsePremise${status.index}" role="tabpanel" aria-labelledby="headingPremise${status.index}" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <c:forEach var="sqMap" items="${selfAssessmentConfig.sqMap}">
                                        <c:forEach var="item" items="${sqMap.value}">
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col" width="25%">Regulation Clause Number</th>
                                                    <th scope="col" width="40%">Item</th>
                                                    <th scope="col" width="5%">Yes</th>
                                                    <th scope="col" width="5%">No</th>
                                                    <th scope="col" width="5%">N/A</th>
                                                </tr>
                                                </thead>
                                                <tbody>

                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title"></p>
                                                        <p><a id="regOutsideWindow${status.index + 1}" data-toggle="modal"
                                                              data-target="#DeleteTemplateModal${status.index + 1}">${item.regulation}</a>
                                                        </p>
                                                    </td>

                                                    <div class="modal fade " id="DeleteTemplateModal${status.index + 1}" tabindex="-1"
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

                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title"></p>
                                                        <p><c:out value="${item.checklistItem}"></c:out></p>
                                                    </td>

                                                    <td>
                                                        <p><input name="${item.answerKey}" type="radio"
                                                                  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>
                                                                  <c:if test="${item.answer == 'YES'}">checked="checked"
                                                                  value="YES" </c:if>
                                                                  onclick="javascript:draftAnswer('YES', '${item.answerKey}')"/></p>
                                                    </td>

                                                    <td>
                                                        <p><input name="${item.answerKey}" type="radio"
                                                                  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>
                                                                  <c:if test="${item.answer == 'NO'}">checked="checked"
                                                                  value="NO"</c:if>
                                                                  onclick="javascript:draftAnswer('NO', '${item.answerKey}')"/></p>
                                                    </td>

                                                    <td>
                                                        <p><input name="${item.answerKey}" type="radio"
                                                                  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>
                                                                  <c:if test="${item.answer == 'NA'}">checked="checked"
                                                                  value="NA"</c:if>
                                                                  onclick="javascript:draftAnswer('NA', '${item.answerKey}')"/></p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </c:forEach>
                                    </c:forEach>
                                </div>
                            </div>

                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>


<script>
    $(document).ready(function () {
        let aTagList = $("a");
        if (aTagList != null){
            for(let i = 0; i < aTagList.length; i++){
                if ("sub_style_answer".startsWith(aTagList[i].id)){
                    aTagList[i].href= '#';
                }
            }
        }
        printpage('printContent');
    });
</script>
