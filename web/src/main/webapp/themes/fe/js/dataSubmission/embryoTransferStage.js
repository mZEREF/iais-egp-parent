$(document).ready(function () {
    $('.transferNumSelect').change(function () {
        let v = parseInt($(this).val());
        let $section2nd = $('#section2nd');
        let $section3rd = $('#section3rd');
        if (v == 1) {
            $section2nd.hide();
            $section3rd.hide();
            hide2nd();
            hide3rd();
        } else if (v == 2) {
            $section2nd.show();
            $section3rd.hide();
            hide3rd();
        } else if (v == 3) {
            $section2nd.show();
            $section3rd.show();
        }
    });

    function hide2nd() {
        let $secondSelect = $('select[name="secondEmbryoAge"]');

        $secondSelect.val(null);
        $secondSelect.niceSelect("update");

        let $radio = $('input[name="secondEmbryoType"]');
        $radio.attr("checked", false);
        $radio.prop("checked", false);
    }

    function hide3rd() {
        let $secondSelect = $('select[name="thirdEmbryoAge"]');

        $secondSelect.val(null);
        $secondSelect.niceSelect("update");

        let $radio = $('input[name="thirdEmbryoType"]');
        $radio.attr("checked", false);
        $radio.prop("checked", false);
    }
});