<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 12/2/2019
  Time: 12:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>


<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">

  <div class="main-content">
    <div class="container">
        <div class="row">
          <div class="form-horizontal">
            <div class="form-group">
            </div>

          <div class="col-xs-12">

            <div class="panel panel-default">
              <c:if test="${configSessionAttr.common eq true}">
                <div class="panel-heading" id="headingOne" role="tab">
                  <p>You are applying for <b>Common</b></p>
                </div>
              </c:if>
              <c:if test="${configSessionAttr.common eq false}">
                <c:if test="${configSessionAttr.svcSubType == null}">
                  <p>You are applying for <b>${configSessionAttr.svcName}</b></p>
                  <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">${configSessionAttr.svcName}</a></h4>
                </c:if>

                <c:if test="${configSessionAttr.svcSubType != null}">
                  <p>You are applying for <b>${configSessionAttr.svcName} | ${configSessionAttr.svcSubType}</b></p>
                </c:if>
              </c:if>
            </div>

            <div class="instruction-content center-content" id="instruction">
              <div class="sort section" id="sortSection">
                <c:forEach var = "chklsec" items = "${configSessionAttr.sectionDtos}" varStatus="status">
                  <div class="gray-content-box sectionClass sectionSon" data-id="${status.index}">
                    <input type="hidden" name="${chklsec.section}" data-name="sectionWant" value="${status.index}">
                    <div class="panel panel-default">
                      <div class="panel-heading"  role="tab">
                        <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">${chklsec.section}</a>
                          <p class="text-right"><a class="btnUp" data-id="${status.index}" onclick="javascript:swapPosition(${status.index},'UP', 'sectionSon')"><i class=""></i>UP</a></p>
                          <p class="text-right"><a class="btnDown" data-id="${status.index}"  onclick="javascript:swapPosition(${status.index},'DOWN', 'sectionSon')"><i class=""></i>DOWN</a></p>
                        </h4>

                      </div>
                      <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                        <div class="panel-body">
                          <p class="text-right"><a onclick="javascript:routeToItemProcess('${chklsec.id}');"><i class="fa fa-pencil-square-o"></i>Config Checklist Item </a></p>


                          <table class="table">
                            <thead>
                            <tr>
                              <th>Regulation Clause Number</th>
                              <th>Regulations</th>
                              <th>Checklist Item</th>
                              <th>Risk Level</th>
                              <th>Status</th>
                              <th>Sort</th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:forEach var = "chklitem" items = "${chklsec.checklistItemDtos}" varStatus="status">

                              <tr class="itemClass" data-id="${status.index}">
                                <input type="hidden" name="${chklitem.itemId}" data-name="itemWant" value="${status.index}">
                                <td>
                                  <p class="visible-xs visible-sm table-row-title">Regulation Clause Number</p>
                                  <p>${chklitem.regulationClauseNo}</p>
                                </td>
                                <td>
                                  <p class="visible-xs visible-sm table-row-title">Regulations</p>
                                  <p>${chklitem.regulationClause}</p>
                                </td>
                                <td>
                                  <p class="visible-xs visible-sm table-row-title">Checklist Item</p>
                                  <p>${chklitem.checklistItem}</p>
                                </td>
                                <td>
                                  <p class="visible-xs visible-sm table-row-title">Risk Level</p>
                                  <p>${chklitem.riskLevel}</p>
                                </td>
                                <td>
                                  <p class="visible-xs visible-sm table-row-title">Date</p>
                                  <p>${chklitem.status}</p>
                                </td>

                                <td>
                                  <p class="visible-xs visible-sm table-row-title">Date</p>
                                  <p><a class="btnUp" data-id="${status.index}" onclick="javascript:swapPosition(${status.index},'UP', 'itemClass')"><i class=""> </i>UP</a>
                                    <a class="btnUp" data-id="${status.index}" onclick="javascript:swapPosition(${status.index},'DOWN', 'itemClass')"><i class=""></i>DOWN</a></p>
                                    <a class="btnUp" onclick="javascript:removeSectionItem('${chklitem.itemId}')"><i class=""></i>Remove</a></p>
                                </td>
                              </tr>
                            </c:forEach>
                            </tbody>
                          </table>

                        </div>
                      </div>
                    </div>
                  </div>
                </c:forEach>
              </div>

              <div class="application-tab-footer">
                <div class="row">
                  <div class="col-xs-12 col-sm-6">
                    <p><a class="back" href="#" onclick="javascript:doBack()"><i class="fa fa-angle-left"></i> Back</a></p>
                  </div>
                  <div class="col-xs-12 col-sm-6">
                    <div class="text-right text-center-mobile">
                      <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:submitEditData('${configSessionAttr.id}');">Next</a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
    </div>
  </div>


</>


<script type="text/javascript">
    function removeSectionItem(id){
        SOP.Crud.cfxSubmit("mainForm","addSectionItem", id);
    }

    function submitEditData(id){
        SOP.Crud.cfxSubmit("mainForm","addSectionItem", id);
    }

    function addSectionItem() {
        SOP.Crud.cfxSubmit("mainForm","addSectionItem");
    }

    function preViewConfig(){
        saveSortValue();
        SOP.Crud.cfxSubmit("mainForm","preViewConfig");
    }

    function swapPosition(ind, status, clzName){
        var dom1 = document.querySelector("." + clzName + "[data-id='"+ ind +"']")
        console.log(dom1)
        var dom2 = status == "UP" ? dom1.previousElementSibling : dom1.nextElementSibling
        console.log(dom2)
        if(dom2){
            var a = dom1.cloneNode(true)
            var b = dom2.cloneNode(true)

            dom1.parentNode.replaceChild(b,dom1)
            dom2.parentNode.replaceChild(a,dom2)
            /*
                        console.log(document.querySelectorAll("input[data-name='want']"))*/
        }
    }

    function saveSortValue(){
        var secIndex = document.querySelectorAll("input[data-name='sectionWant']")
        var itemIndex = document.querySelectorAll("input[data-name='itemWant']")
        if (secIndex){
            for(var i = 0,j = secIndex.length;i<j;i++){
                secIndex[i].value = i
                console.log(secIndex[i].value);
            }
        }

        if (itemIndex){
            for(var i = 0,j = itemIndex.length;i<j;i++){
                itemIndex[i].value = i
                console.log(itemIndex[i].value);
            }
        }
    }

    function routeToItemProcess(id) {
        var inputs = $('#mainForm').find("input");
        if(inputs.length != 0){
            inputs.each(function(index, obj){
                if('currentValidateId' == obj.name){
                    obj.value = id;
                }
            });
        }

        SOP.Crud.cfxSubmit("mainForm","routeToItemProcess");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backLastPage");
    }
</script>