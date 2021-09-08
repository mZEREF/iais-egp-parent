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
/*  .btn.btn-primary {
    font-size: 1.6rem;
    font-weight: 700;
    background: #F2B227;
    border: 1px solid #F2B227;
    color: black;
    padding: 5px 10px;
    text-transform: uppercase;
    border-radius: 30px;
  }*/

 /* .panel-default {
    border-color: #dddddd;
  }*/

  /*.black_overlay{
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
  }*/

</style>
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
  <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
  <input type="hidden" name="currentValidateId" value="">
  <input type="hidden" name="sectionItemid" value="">
    <div class="">
      <div class="row">
        <br><br>
        <span id="error_configErrorMsg" name="iaisErrorMsg" class="error-msg"></span>
        <br><br>
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
                      <h4 class="panel-title">
                        <a role="button" data-toggle="collapse" href="#collapseOne${sectionStatus.index}" aria-expanded="true" style="color: black" aria-controls="collapseOne">${chklsec.section}</a>
                        <p class="text-right">
                          <a href="javascript:void(0)" class="btnUp" data-id="${sectionStatus.index}"
                              onclick="javascript:swapPosition(${sectionStatus.index},'UP', 'sectionSon')">
                            <em class=""></em>Up
                          </a>
                        </p>
                        <p class="text-right">
                          <a href="javascript:void(0)" class="btnDown" data-id="${sectionStatus.index}"
                              onclick="javascript:swapPosition(${sectionStatus.index},'DOWN', 'sectionSon')">
                            <em class=""></em>Down
                          </a>
                        </p>
                        <p class="text-right">
                          <a href="javascript:void(0)" class="btnDown" data-id="${sectionStatus.index}"
                             onclick="javascript:removeSection('<iais:mask name="currentValidateId" value="${chklsec.id}"/>')">
                            <em class=""></em>Remove
                          </a>
                        </p>
                      </h4>

                    </div>
                    <div class="panel-collapse collapse in" id="collapseOne${sectionStatus.index}" role="tabpanel" aria-labelledby="headingOne">
                      <div class="panel-body">
                        <p class="text-right">
                          <a href="javascript:void(0)" onclick="javascript:routeToItemProcess('<iais:mask name="currentValidateId" value="${chklsec.id}"/>');">
                            <em class="fa fa-pencil-square-o"></em>Config Checklist Item
                          </a>
                        </p>

                        <table aria-describedby="" id="resultTable" class="table">
                          <thead>
                          <tr>
                            <th scope="col" >Regulation Clause Number</th>
                            <th scope="col" >Regulations</th>
                            <th scope="col" >Checklist Item</th>
                            <th scope="col" >Risk Level</th>
                            <th scope="col" >Status</th>
                            <th scope="col" >Action</th>
                          </tr>
                          </thead>
                          <tbody>
                            <c:forEach var = "chklitem" items = "${chklsec.checklistItemDtos}" varStatus="status">
                              <tr class="itemClass" data-id="${chklitem.itemId}${sectionStatus.index}">
                                <input type="hidden" name="${chklitem.itemId}" data-name="itemWant" value="${status.index}">
                                  <td class="word-wrap"><p class="visible-xs visible-sm table-row-title">Regulation Clause Number</p><p>${chklitem.regulationClauseNo}</p></td>
                                  <td class="word-wrap"><p class="visible-xs visible-sm table-row-title">Regulations</p><p>${chklitem.regulationClause}</p></td>
                                  <td class="word-wrap"><p class="visible-xs visible-sm table-row-title">Checklist Item</p><p>${chklitem.checklistItem}</p></td>
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
                                    <p>
                                      <a href="javascript:void(0)" class="btnUp" data-id="${chklitem.itemId}${sectionStatus.index}"
                                          onclick="javascript:swapPosition('${chklitem.itemId}${sectionStatus.index}','UP', 'itemClass')">
                                        <em class=""> </em>Up
                                      </a>
                                      <br/>
                                      <a href="javascript:void(0)" class="btnUp" data-id="${chklitem.itemId}${sectionStatus.index}"
                                          onclick="javascript:swapPosition('${chklitem.itemId}${sectionStatus.index}','DOWN', 'itemClass')">
                                        <em class=""></em>Down
                                      </a>
                                      <br/>
                                      <a href="javascript:void(0)" class="btnUp"  onclick="javascript:removeSectionItem('<iais:mask
                                              name="currentValidateId" value="${chklsec.id}"/>','<iais:mask name="sectionItemid" value="${chklitem.itemId}"/>')">
                                        <em class=""></em>Remove
                                      </a>
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

              <div class="row">
                <div class="col-xs-12 col-sm-6">
                  <a class="back" onclick="doBack()();" href="javascript:void(0)"><em class="fa fa-angle-left"></em> Back</a>
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" href="javascript:void(0)" onclick="javascript:addSectionItem();">Add Section Item</a>
                    <a class="btn btn-primary next" href="javascript:void(0)" onclick="javascript:preViewConfig();">Next</a>
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



<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
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

        for(var i = 0; i < secIndex.length; i++){
            secIndex[i].value = i
        }

        var out = $("#sortSection").children()
        for(var i = 0; i < out.length; i++){
          var hid = $(out[i]).children().find("input[type='hidden']")
          for(var j = 0; j < hid.length; j++){
            hid[j].name += i;
            hid[j].value = j;
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
        var value = '${configIdAttr}'
        SOP.Crud.cfxSubmit("mainForm","backLastPage", value);
    }

    function removeSectionItem(sectionId, itemId){
        $('input[name=currentValidateId]').val(sectionId);
        $('input[name=sectionItemid]').val(itemId);
        SOP.Crud.cfxSubmit("mainForm","removeSectionItem", itemId);
    }

    function removeSection(id){
      $('input[name=currentValidateId]').val(id);
        SOP.Crud.cfxSubmit("mainForm","removeSection", id);
    }
</script>