<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/12/17
  Time: 9:43
  To change this template use File | Settings | File Templates.
--%>
<!-- session timeout handling - START -->
<%
  int timeout = 20;  // in minute
  int warning = 15;  // in minute
%>
<div id="timeoutDlg" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="timeoutDlg">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title" id="gridSystemModalLabel">Timeout</h3>
      </div>
      <div class="modal-body">
        <div class="row">
          <input type="hidden" name="fangDuoJirejectDate" id="fangDuoJirejectDate">
          <div class="col-md-12">
            <div style="line-height:18px;">Your session will be invalid in <label style="margin-bottom: 0px;" id="countdownLbl">0<%=(timeout - warning)%>:00</label> minute(s). You can only extend the session by clicking Extend button.</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <input class="btn btn-secondary" style="float:right" type="button" onclick="javascript:doExtend();" value="Extend"/>
        <span style="float:right">&nbsp;</span>
        <a onclick="javascript:doLogout();" style="float:right" id="intraTimeOutLogout" class="btn btn-secondary">Logout</a>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        initSessionTimeout();
    });

    var warningDlgIntHook;
    var countdownIntHook;
    function initSessionTimeout() {
        window.clearTimeout(warningDlgIntHook);
        var min = parseInt('<%=warning%>');
        var logout = parseInt('<%=timeout%>');
        warningDlgIntHook = window.setTimeout('showTimeoutWarning();', min * 60 * 1000, 'JavaScript');
        window.setTimeout('doLogout();', logout * 60 * 1000, 'JavaScript');
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
        $('#timeoutDlg').modal('show');
        startCountdown(min);
    }

    function doExtend() {
        $('#timeoutDlg').modal('hide');
        window.clearInterval(countdownIntHook);

        // call AJAX to extend the current session
        $.ajax({
            type:"GET",
            url:BASE_CONTEXT_PATH + "/halp-time-out/intranet.extend",
            async: false
        });
        initSessionTimeout();
    }

    function doLogout() {
        window.location.replace("${pageContext.request.contextPath}/eservice/INTRANET/IntraLogout");
        $('#timeoutDlg').modal('hide');
    }
</script>
<!-- session timeout handling - END -->