<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/12/17
  Time: 9:43
  To change this template use File | Settings | File Templates.
--%>
<!-- session timeout handling - START -->
<%
  int timeout = 30;  // in minute
  int warning = 25;  // in minute
%>
<div id="timeoutDlg" class="modal fade in dialog" tabindex="-1" role="dialog" aria-labelledby="rejectDate"
     style="left: 50%; top: 50%; transform: translate(-50%, -50%); min-width: 80%; overflow: visible; bottom: inherit; right: inherit; display: block; padding-right: 17px;">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title" id="gridSystemModalLabel">Timeout</h3>
      </div>
      <div class="modal-body">
        <div class="row">
          <input type="hidden" name="fangDuoJirejectDate" id="fangDuoJirejectDate">
          <div class="col-md-8 col-md-offset-2">
            <div style="line-height:18px;">Your session will be invalid in <label style="margin-bottom: 0px;" id="countdownLbl">0<%=(timeout - warning)%>:00</label> minute(s). You can only extend the session by clicking Extend button.</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <a onclick="javascript:doLogout();" style="float:right" name="filterBtn" class="btn btn-secondary">Logout</a>
        <span style="float:right">&nbsp;</span>
        <input class="btn btn-secondary" id="interTimeOutLogout" style="float:right" type="button" onclick="javascript:doExtend();" value="Extend"/>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $('#timeoutDlg').dialog({
            autoOpen: false,
            modal: true,
            resizable: false,
        });

        initSessionTimeout();
    });

    var warningDlgIntHook;
    var countdownIntHook;
    function initSessionTimeout() {
        window.clearTimeout(warningDlgIntHook);

        var min = parseInt('<%=warning%>');
        warningDlgIntHook = window.setTimeout('showTimeoutWarning();', min * 60 * 1000, 'JavaScript');
    }

    function startCountdown(min) {
        min = min * 60;
        countdownIntHook = window.setInterval(function() {
            --min;
            var mm = Math.floor(min / 60);
            if (mm < 10) {
                mm = '0' + mm;
            }
            var ss = Math.floor(min % 60);
            if (ss < 10) {
                ss = '0' + ss;
            }
            $('#countdownLbl').html(mm + ':' + ss);
            if (min === 0) {
                window.clearInterval(countdownIntHook);

                // logout automatically if no action taken.
                doLogout();
            }
        }, 1000, 'JavaScript');
    }

    function showTimeoutWarning() {
        var min = parseInt('<%=(timeout - warning)%>');
        $('#timeoutDlg').dialog('open');
        startCountdown(min);
    }

    function doExtend() {
        $('#timeoutDlg').dialog('close');
        window.clearInterval(countdownIntHook);

        // call AJAX to extend the current session
        $.ajax({
            type: "POST",
            url: '/web-common/time-out/internet.extend',
            async: false
        });
        $.ajax({
            type:"GET",
            url:BASE_CONTEXT_PATH + "/time-out/internet.extend",
            async: false
        });
        initSessionTimeout();
    }

    function doLogout() {
        window.location.replace("${pageContext.request.contextPath}/eservice/INTERNET/InterLogout");
        $('#timeoutDlg').dialog('close');
    }
</script>
<!-- session timeout handling - END -->