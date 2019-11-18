<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="wrapper">
  <div class="form-inner-content editableMode">
    <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
    <form  name="formRender" method="post" target="_parent">
      <div class="canvasContent">
        <!-- Place holder for tabbed interface. -->
        <input type="hidden" id="testCode" value="HST"/>
        <div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
          <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
            <div id="control--runtime--0" class="page control control-area  container-p-1">
              <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
              <table class="control-grid"><tbody><tr height="100%"><td class="first last" style="width: 100%;"><div id="control--runtime--1" class="control control-caption-horizontal">
                <div class="control-label-span control-set-alignment">
                  <label id="control--runtime--1--label" class="control-label control-set-font control-font-label"></label>
                  <span class="upload_controls"></span>
                </div>
                <div class="control-input-span control-set-alignment">
                  <div class="normal-indicator">
                    <table>
                      <tbody>
                      <c:forEach var="levelOneList" items="${HcsaSvcSubtypeOrSubsumedDto}" varStatus="levelOne">
                        <!--one -->
                        <tr>
                          <td>
                            <div class="control-item-container parent-form-check" data-parent="${levelOneList.code}" >
                              <input type="checkbox" id="control--${levelOne.index}--${levelOne.index}"
                                     name="control--runtime--1" class="control-input"
                                     value="<c:out value="${levelOneList.id};${levelOneList.type};${levelOneList.name }" />">
                              <label  for="control--${levelOne.index}--${levelOne.index}" data-code="<c:out value="${levelOneList.code}" />" class="control-label control-set-font control-font-normal">
                                <span class="check-square"></span>${levelOneList.name}
                              </label>
                            </div>
                          </td>
                        </tr>
                        <c:if test="${not empty levelOneList.list}">
                          <c:forEach var="levelTwoList" items="${levelOneList.list}" varStatus="levelTwo">
                            <!--two -->
                            <tr>
                              <td>
                                <div class="control-item-container sub-form-check parent-form-check disabled" data-parent="${levelTwoList.code}" data-child="${levelOneList.code}" >
                                  <input type="checkbox" id="control--${levelTwo.begin}--${levelTwo.index}"
                                         name="control--runtime--1" class="control-input"
                                         value="<c:out value="${levelTwoList.id};${levelTwoList.type};${levelTwoList.name }" />">
                                  <label  for="control--${levelTwo.index}--${levelTwo.index}" class="control-label control-set-font control-font-normal">
                                    <span class="check-square"></span>${levelTwoList.name}
                                  </label>
                                </div>
                              </td>
                            </tr>
                            <c:if test="${not empty levelTwoList.list}">
                              <!--three -->
                              <c:forEach var="levelThreeList" items="${levelTwoList.list}" varStatus="levelThree">
                                <tr>
                                  <td>
                                    <div class="control-item-container sub-form-check double parent-form-check disabled" data-parent="${levelThreeList.code}" data-child="${levelTwoList.code}" >
                                      <input type="checkbox" id="control--${levelThree.index}--${levelThree.index}"
                                             name="control--runtime--1" class="control-input"
                                             value="<c:out value="${levelThreeList.id};${levelThreeList.type};${levelThreeList.name }" />">
                                      <label  for="control--${levelThree.index}--${levelThree.index}" class="control-label control-set-font control-font-normal">
                                        <span class="check-square"></span>${levelThreeList.name}
                                      </label>
                                    </div>
                                  </td>
                                </tr>
                              </c:forEach>
                            </c:if>
                          </c:forEach>
                        </c:if>
                      </c:forEach>


                      </tbody></table>
                  </div>
                  <div id="control--runtime--1--errorMsg_right" style="display: none;" class="error_placements"></div>
                </div>
              </div></td></tr></tbody></table>
              <div id="control--runtime--0--errorMsg_page_bottom" class="error_placements"></div>
            </div></div><div id="demo" style="display:none;" class="ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide"></div>
        </div>
      </div>
      <input type="hidden" name="OWASP_CSRFTOKEN" value="8OG4-EKVV-5RSW-YM8P-C5DX-N6DE-WQSU-0K8N"></form>
  </div>

</div>

<script>
    $(document).ready(function () {
        var testCode = $('#testCode').val();
        //console.log($(".control-font-normal").data('data-code'));
        /*if("HST"==testCode){
          $(".control-font-normal").data('data-code').each(function (k,v) {
              console.log(k);
              console.log(v);
          });
        }*/


    });


</script>