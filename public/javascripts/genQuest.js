function clearLoginError() {
	var div = document.getElementById('errorDiv');
    if (div.style.display !== 'none') {
        div.style.display = 'none';
    }else {
        div.style.display = 'block';
    }
}

$(document).ready(function() {
    $('#selecctall').click(function(event) {  //on click 
        $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"               
        });
    });
    $('#unselecctall').click(function(event) {  //on click 
        $('.checkbox1').each(function() { //loop through each checkbox
                this.checked = false;  //select all checkboxes with class "checkbox1"               
        });
    });
	
});