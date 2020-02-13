<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<style>
  .btn.btn-primary {
    font-size: 1.6rem;
    font-weight: 700;
    background: #F2B227;
    border: 1px solid #F2B227;
    color: black;
    padding: 5px 10px;
    text-transform: uppercase;
    border-radius: 30px;
  }

  .panel-default {
    border-color: #dddddd;
  }

  .black_overlay{
    display: none;
    position: absolute;
    top: 0%;
    left: 0%;
    width: 100%;
    height: 100%;
    background-color: black;
    z-index:1001;
    -moz-opacity: 0.8;
    opacity:.80;
    filter: alpha(opacity=88);
  }
  .white_content {
    display: none;
    position: absolute;
    top: 25%;
    left: 25%;
    width: 55%;
    height: 55%;
    padding: 20px;
    border: 10px solid orange;
    background-color: white;
    z-index:1002;
    overflow: auto;
  }

</style>

<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/include/formHidden.jsp" %>
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <input type="hidden" name="currentValidateId" value="">

  <div class="main-content">
    <div class="container">
      <div class="row">
        <span id="error_configErrorMsg" name="iaisErrorMsg" class="error-msg"></span>
        <div class="col-xs-12">
        <c:choose>
          <c:when test="${operationType == 'doEdit'}">
            <h2>Edit Section</h2>
          </c:when>
          <c:when test="${operationType == 'doClone'}">
            <h2>Clone Section</h2>
          </c:when>
          <c:otherwise>
            <h2>Add Section</h2>
          </c:otherwise>
        </c:choose>
          <div class="instruction-content center-content" id="instruction">
            <div class="sort section" id="sortSection">
              <c:forEach var = "chklsec" items = "${configSessionAttr.sectionDtos}" varStatus="sectionStatus">
                <div class="gray-content-box sectionClass sectionSon" data-id="${sectionStatus.index}">
                  <input type="hidden" name="${chklsec.section}" data-name="sectionWant" value="${sectionStatus.index}">
                  <div class="panel panel-default">
                    <div class="panel-heading"  role="tab">
                      <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">${chklsec.section}</a>
                        <p class="text-right"><a class="btnUp" data-id="${sectionStatus.index}" onclick="javascript:swapPosition(${sectionStatus.index},'UP', 'sectionSon')"><em class=""></em>Up</a></p>
                        <p class="text-right"><a class="btnDown" data-id="${sectionStatus.index}"  onclick="javascript:swapPosition(${sectionStatus.index},'DOWN', 'sectionSon')"><em class=""></em>Down</a></p>
                        <p class="text-right"><a class="btnDown" data-id="${sectionStatus.index}"  onclick="javascript:removeSection('${chklsec.id}')"><em class=""></em>Remove</a></p>
                      </h4>

                    </div>
                    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                      <div class="panel-body">
                        <p class="text-right"><a onclick="javascript:routeToItemProcess('${chklsec.id}');"><em class="fa fa-pencil-square-o"></em>Config Checklist Item </a></p>


                        <table class="table">
                          <thead>
                          <tr>
                            <th>Regulation Clause Number</th>
                            <th>Regulations</th>
                            <th>Checklist Item</th>
                            <th>Risk Level</th>
                            <th>Status</th>
                            <th>Action</th>
                          </tr>
                          </thead>
                          <tbody>

                            <c:forEach var = "chklitem" items = "${chklsec.checklistItemDtos}" varStatus="status">

                                    <tr class="itemClass" data-id="${chklitem.itemId}${sectionStatus.index}">
                                      <input type="hidden" name="${chklitem.itemId}${sectionStatus.index}" data-name="itemWant" value="${status.index}">
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
                                          <p><iais:code code="${chklitem.riskLevel}"></iais:code></p>
                                        </td>
                                        <td>
                                          <p class="visible-xs visible-sm table-row-title">Status</p>
                                          <p><iais:code code="${chklitem.status}"></iais:code></p>
                                        </td>

                                        <td>
                                          <p class="visible-xs visible-sm table-row-title">Date</p>
                                          <p><a class="btnUp" data-id="${chklitem.itemId} + ${sectionStatus.index}" onclick="javascript:swapPosition('${chklitem.itemId}${sectionStatus.index}','UP', 'itemClass')"><em class=""> </em>Up</a>
                                            <a class="btnUp" data-id="${chklitem.itemId} + ${sectionStatus.index}" onclick="javascript:swapPosition('${chklitem.itemId}${sectionStatus.index}','DOWN', 'itemClass')"><em class=""></em>Down</a>
                                            <a class="btnUp"  onclick="javascript:removeSectionItem('${chklsec.id}','${chklitem.itemId}')"><em class=""></em>Remove</a></p>
                                          </p>

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
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" href="javascript:void(0);" onclick="doBack()">Cancel</a>
                    <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:addSectionItem();">Add Section Item</a>
                    <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript:preViewConfig();">Next</a>
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

  </div>

<%@include file="/include/validation.jsp"%>
<script type="text/javascript">
    function addSectionItem() {
        SOP.Crud.cfxSubmit("mainForm","addSectionItem");
    }

    function preViewConfig(){
        saveSortValue();
        SOP.Crud.cfxSubmit("mainForm","preViewConfig");
    }

    function swapPosition(ind, status, clzName){
        var dom1 = document.querySelector("." + clzName + "[data-id='"+ ind +"']")
        var dom2 = status == "UP" ? dom1.previousElementSibling : dom1.nextElementSibling
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

       /* if (itemIndex){
            for(var i = 0,j = itemIndex.length;i<j;i++){
                itemIndex[i].value = i
                console.log(itemIndex[i].value);
            }
        }*/

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

    function removeSectionItem(sectionId, itemId){
        $('input[name=currentValidateId]').val(sectionId);
        SOP.Crud.cfxSubmit("mainForm","removeSectionItem", itemId);
    }

    function removeSection(id){
        SOP.Crud.cfxSubmit("mainForm","removeSection", id);
    }
</script>