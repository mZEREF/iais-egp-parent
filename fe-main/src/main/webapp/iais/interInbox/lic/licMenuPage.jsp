<div class="row">
  <div class="col-xs-12 col-md-9">
    <div class="navigation">
      <div class="nav nav-tabs nav-menu">
        <li class="active"><a href="#"><span>Dashboard</span></a></li>
        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" href="javascript:;"><span>eServices</span></a>
          <menu:load id="inbox-top-menus">
            <menu:include name="INTER_INBOX"/>
          </menu:load>
          <ul class="dropdown-menu">
            <menu:iterate id="inbox-top-menus" var="item" varStatus="status">
              <c:if test="${item.depth > 0}">
              <li>
                <a href="<c:out value="${item.url}" />">
                  <egov-smc:commonLabel><c:out value="${item.displayLabel}"/></egov-smc:commonLabel>
                </a>
              </li>
              </c:if>
            </menu:iterate>
            <li class="divider" role="separator"></li>
            <li><a href="#">Step-by-step guide to eServices</a></li>
          </ul>
        </li>
        <li><a href="#"><span>Activity Log</span></a></li>
        <li><a id="premList"><span>Premises List</span></a></li>
        <li><a id="psnList"><span>Personnel List</span></a></li>
        <li><a href="#"><span>Licensee Details</span></a></li>
      </div>
    </div>
  </div>
  <div class="col-xs-10 col-xs-offset-1 col-lg-offset-0 col-lg-3">
    <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:;">Tan Mei Ling Joyce</a>
      <ul class="dropdown-menu" aria-labelledby="profileBtn">
        <li class="management-account"><a href="#">Manage Account</a></li>
        <li class="logout"><a href="#">Logout</a></li>
      </ul>
    </div>
  </div>
</div>
</div>
<form class="form-inline" method="post" id="topMenu" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="crud_action_type" value="">
  <%--<input type="hidden" name="crud_action_type_form" value="back123">--%>
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" name="crud_action_type_menu" value="">
</form>

<script>

    function submitMenu(action, menu, form) {
        $("[name='crud_action_type']").val(action);
        $('[name="crud_action_type_menu"]').val(menu);
        $("[name='crud_action_type_form_value']").val(form);

        $('#topMenu').submit();
    }

    $('#premList').click(function () {
        submitMenu('topMenu', 'premisesList', null)
    });

    $('#psnList').click(function () {
        submitMenu('topMenu', 'personnelList', null)
    });


    $('#amendLicence').click(function () {
        submitMenu('doRfc', null, null)
    });



    function doSubmitForm(action, menu, form){
        $("[name='crud_action_type']").val(action);
        $('[name="crud_action_type_menu"]').val(menu);
        $("[name='crud_action_type_form_value']").val(form);
        $('#menuListForm').submit();
    }
    
    function menuListForBack(action,form) {
        $('[name="crud_action_back"]').val('back');
        $("[name='crud_action_type']").val(action);
        $("[name='crud_action_type_form_value']").val(form);
        $('#menuListForm').submit();
    }

</script>