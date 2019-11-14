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
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type" value="">
  <input type="hidden" name="crud_action_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <input type="hidden" name="currentValidateId" value="">

  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="instruction-content center-content">
            <h2>Add Section</h2>

            <c:forEach var = "chklsec" items = "${configSessionAttr.sectionDtos}" varStatus="status">
              <div class="gray-content-box" id="div1">
                <div class="panel panel-default">
                  <div class="panel-heading" id="headingOne" role="tab">
                    <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">${chklsec.section}</a>
                      <p class="text-right"><a href="#"><i class=""></i>UP</a></p>
                      <p class="text-right"><a href="#"><i class=""></i>DOWN</a></p>
                    </h4>

                  </div>
                  <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                    <div class="panel-body">
                      <p class="text-right"><a  onclick="javascript:routeToItemProcess('${chklsec.id}');"><i class="fa fa-pencil-square-o"></i>Config Checklist Item </a></p>


                  <div class="panel-main-content">

                    <div class="table-gp">
                      <table class="table">
                        <thead>
                        <tr>
                          <th>Regulation Clause Number</th>
                          <th>Regulations</th>
                          <th>Checklist Item</th>
                          <th>Service</th>
                          <th>Risk Level</th>
                          <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                  <c:forEach var = "chklitem" items = "${chklsec.checklistItemDtos}" varStatus="status">
                            <tr>
                              <td>
                                <p>${chklitem.regulationClauseNo}</p>
                              </td>
                              <td>
                                <p>${chklitem.regulationClause}</p>
                              </td>
                              <td>
                                <p>${chklitem.checklistItem}</p>
                              </td>
                              <td>
                                <p>${chklitem.riskLevel}</p>
                              </td>
                              <td>
                                <p>${chklitem.status}</p>
                              </td>
                            </tr>
                  </c:forEach>
                        </tbody>
                      </table>
                    </div>
                  </div>

                    </div>
                  </div>
                </div>
              </div>
            </c:forEach>
            <div class="application-tab-footer">
              <div class="row">
                <div class="col-xs-12 col-sm-6">
                  <p><a class="back" href="#" onclick="javascript:doBack()"><i class="fa fa-angle-left"></i> Back</a></p>
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: addSectionItem();">Add Section Item</a>
                    <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: preViewConfig();">Next</a>

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


<script type="text/javascript">
    function addSectionItem() {
        SOP.Crud.cfxSubmit("mainForm","addSectionItem");
    }

    function preViewConfig(){
        SOP.Crud.cfxSubmit("mainForm","preViewConfig");
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