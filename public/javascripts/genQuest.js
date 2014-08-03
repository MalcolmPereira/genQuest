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

    $('#categoryEditTable td').blur(function(event){
        if(event.target.id.indexOf("categoryName_") > -1){
            return
        }else if(event.target.id.indexOf("categoryDesc_") > -1){
             if(
                $.trim($("input[name='categoryDescHidden_"+event.target.id.substring(13,event.target.id.length)+"']").val())
                !=
                $.trim($("#categoryDesc_"+event.target.id.substring(13,event.target.id.length)).text())
             ){
                $("#categoryReset_"+event.target.id.substring(13,event.target.id.length)).show();
             }
        }
    });

    $('#categoryEditTable td span').click(function(event){
         if(event.target.id.indexOf("categoryReset_") > -1){
            var value = $("input[name='categoryDescHidden_"+event.target.id.substring(14,event.target.id.length)+"']").val();
            if(confirm("Are you sure you want to revert new updates ? ")){
              $("#categoryDesc_"+event.target.id.substring(14,event.target.id.length)).text(value)
              $("#categoryDesc_"+event.target.id.substring(14,event.target.id.length)).attr("contentEditable",false);
              $("#categoryReset_"+event.target.id.substring(14,event.target.id.length)).hide();
            }
         }
    });

    $('#saveNewCategoryButton').click(function(event){
         alert($("#newCategoryNameId").val())
         alert($("#newCategoryDescId").val())

    });
});

