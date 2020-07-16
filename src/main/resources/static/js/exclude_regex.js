// jQuery
function replaceItems (html) {
    // Replace the <fieldset id="items"> with a new one returned by server.
    $('#excludeRegexesFields').replaceWith($(html));
}

$('button[name="addExcludeRegex"]').click(function (event) {
    event.preventDefault();
    var data = $('form').serialize();
    // Add parameter "addItem" to POSTed form data. Button's name and value is
    // POSTed only when clicked. Since "event.preventDefault();" prevents from
    // actual clicking the button, following line will add parameter to form
    // data.
    data += 'addExcludeRegex';
    $.post('/list', data, replaceItems);
});

$('button[name="removeExcludeRegex"]').click(function (event) {
    event.preventDefault();
    var data = $('form').serialize();
    // Add parameter and index of item that is going to be removed.
    data += 'removeItem=' + $(this).val();
    $.post('/list', data, replaceItems);
});