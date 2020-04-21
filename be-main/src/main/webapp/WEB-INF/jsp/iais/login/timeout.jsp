<!-- session timeout handling - START -->
<%
  int timeout = 30;  // in minute
  int warning = 25;  // in minute
%>
<div id="timeoutDlg" class="dialog">
  <div class="form" style="margin-top:4px;">
    <div style="line-height:18px;">Your session will be invalid in <label id="countdownLbl">0<%=(timeout - warning)%>:00</label> minute(s). You can only extend the session by clicking Extend button.</div>
  </div>
  <div class="spacer" style="margin:10px 0;"></div>
  <div class="action">
    <input type="button" onclick="javascript:doExtend();" value="Extend"/>
    <input type="button" onclick="javascript:doLogout();" value="Logout"/>
  </div>
</div>

<script type="text/javascript">
$(document).ready(function() {
    $('#timeoutDlg').dialog({
        autoOpen: false,
        modal: true,
        resizable: false,
        title: 'Timeout'
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
        url: '<c:url value="/icms/ajax.jsp"/>?handler=sg.gov.subcourts.icms.common.SessionTimeoutAjax.extendSession',
        async: false
    });

    initSessionTimeout();
}

function doLogout() {
    $('#timeoutDlg').dialog('close');

    // call AJAX to logout the current session
    $.ajax({
        url: '<c:url value="/icms/ajax.jsp"/>?handler=sg.gov.subcourts.icms.common.SessionTimeoutAjax.logoutSession',
        async: false
    });

    // redirect to login url
    submitLink('<c:url value="/system/logout.jsp"/>');
}
</script>
<!-- session timeout handling - END -->