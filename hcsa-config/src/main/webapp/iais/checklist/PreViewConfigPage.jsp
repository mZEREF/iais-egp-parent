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
            <h2>PreView Config Page</h2>
            <div class="form-horizontal">
              <div class="form-group">
                <div class="col-xs-12">
                  <td>
                    <%--<label>
                      Common  &nbsp; <input class="form-check-input" id="commmon" type="radio" name="common" aria-invalid="false" value="General Regulation"> ${configSessionAttr.common}
                    </label>--%>
                  </td>
                </div>
              </div>
            <div class="application-tab-footer">
              <div class="row">
                <div class="col-xs-12 col-sm-6">
                  <p><a class="back" href="#"><i class="fa fa-angle-left"></i> Back</a></p>
                </div>
                <div class="col-xs-12 col-sm-6">
                  <div class="text-right text-center-mobile">
                    <a class="btn btn-primary next" href="javascript:void(0);" onclick="javascript: submitConfig();">Submit</a>

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
    function submitConfig() {
        SOP.Crud.cfxSubmit("mainForm","submitConfig");
    }

    function doBack(){
        SOP.Crud.cfxSubmit("mainForm","backLastPage");
    }
</script>