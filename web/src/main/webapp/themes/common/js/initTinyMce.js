$(window).on("load", function(){
    tinymce.init({
        selector: "#htmlEditroArea",  // change this value according to your HTML
        menubar: 'file edit view insert format tools',
        plugins: ['print preview fullpage',
            'advlist autolink lists link image charmap print preview anchor',
            'searchreplace visualblocks code fullscreen',
            'insertdatetime media table paste code help wordcount',
            'noneditable'
        ],
        toolbar: 'undo redo | formatselect | ' +
            ' bold italic backcolor | alignleft aligncenter ' +
            ' alignright alignjustify | bullist numlist outdent indent |' +
            ' removeformat | help',
    });
});