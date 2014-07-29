function clearLoginError() {
	var div = document.getElementById('errorDiv');
    if (div.style.display !== 'none') {
        div.style.display = 'none';
    }else {
        div.style.display = 'block';
    }
}

$(document).ready(function() {
    $('#selectall').click(function(event) {   
       if(this.checked) { 
        	$('.checkbox1').each(function() { 
            	    this.checked = true;                 
        	});
       }else{
       		$('.checkbox1').each(function() { 
                this.checked = false;                 
        	});
       } 	
    });
});