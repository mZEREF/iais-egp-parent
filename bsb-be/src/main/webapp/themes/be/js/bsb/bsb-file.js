function downloadFile(cond, id) {
    var url;
    if (cond === 'saved') {
        url = "/bsb-be/ajax/doc/download/facility/repo/" + id;
    }
    window.open(url);
}