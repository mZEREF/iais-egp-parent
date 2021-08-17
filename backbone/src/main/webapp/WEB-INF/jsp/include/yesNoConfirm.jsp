<%--
  Created by IntelliJ IDEA.
  User: wangyu
  Date: 29/4/2020
  Time: 上午 9:58
  To change this template use File | Settings | File Templates.
--%>
<script type="text/javascript">
    function showPopupConfirmation(options) {
        if (!options) {
            return
        }
        var title = options.title || '';
        var id = options.id || 'MOH_Confirmation';
        var confirmBtn = options.confirmBtn || 'Confirm';
        var cancelBtn = options.cancelBtn || 'Cancel';
        var confirmCallback = options.confirmCallback;
        var data = options.data;
        var modalHtml = '';
        modalHtml += '<div class="modal fade" id="' + id + '">';
        modalHtml += '<div class="modal-dialog modal-dialog-centered">';
        modalHtml += '<div class="modal-content">';
        modalHtml += '<div class="modal-header">';
        modalHtml += '<h5 class="modal-title">' + title + '</h5>';
        modalHtml += '<button type="button" class="close" data-toggle="tooltip" data-dismiss="modal" title="Close"><span>&times;</span></button>';
        modalHtml += '</div>';
        modalHtml += '<div class="modal-body">' + options.msg + '</div>';
        modalHtml += '<div class="modal-footer">';
        modalHtml += '<button type="button" class="btn btn-default" onclick="$(\'#' + id + '\').modal(\'hide\');">' + cancelBtn + '</button>';
        modalHtml += '<button type="button" class="btn ' + 'button-primary' + '" id="' + id + '_Confirm">' + confirmBtn + '</button>';
        modalHtml += '</div>';
        modalHtml += '</div>';
        modalHtml += '</div>';
        modalHtml += '</div>';
        $('#mainForm').append(modalHtml);
        $('#' + id).on('hidden.bs.modal', function () {
            initOtherModal();
            $(document).off('click', '#' + id + '_Confirm');
            $('#' + id).remove();
            $(document.body).removeAttr('style')
        });
        $(document).on('click', '#' + id + '_Confirm', function () {
            if (options.waitHide) {
                $('#' + id).modal('hide', eval(confirmCallback))
            } else {
                callFunc(confirmCallback, data);
                $('#' + id).modal('hide')
            }
        });
        $('#' + id).modal({backdrop: 'static', keyboard: !1, show: !0})
    }

    function initOtherModal() {
        if ($('.modal.in').length > 0) {
            $(document.body).addClass('modal-open');
        }
    }

    function callFunc(func) {
        this[func].apply(this, Array.prototype.slice.call(arguments, 1));
    }
</script>