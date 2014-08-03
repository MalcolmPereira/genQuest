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

    $('#categoryEditTable td').click(function(event){
        if(event.target.id.indexOf("categoryName_") > -1){
           return

        }else if(event.target.id.indexOf("categoryDesc_") > -1){
           $(event.target).attr("contentEditable",true);
        }
    });

    $('#categoryEditTable td span').click(function(event){
         if(event.target.id.indexOf("categoryReset_") > -1){
            var value = $("input[name='categoryDescHidden_"+event.target.id.substring(14,event.target.id.length)+"']").val();
            if(confirm("are you sure!!!")){
              $("#categoryDesc_"+event.target.id.substring(14,event.target.id.length)).text(value)
              $("#categoryDesc_"+event.target.id.substring(14,event.target.id.length)).attr("contentEditable",false);
            }
         }
    });

});

