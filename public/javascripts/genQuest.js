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

    $('.modal').on('shown.bs.modal', function() {
      $(this).find('[autofocus]').focus();
    });

});

function deleteCategory(categoryId){
    $("#deleteCategoryId").val(categoryId);
    $('#deleteForm').submit();
}

function updateCategory(categoryId){
    $("#updateCategoryHeader").text($("#categoryNameHidden_"+categoryId).val());
    $("#updatecategoryId").val($("#categoryIdHidden_"+categoryId).val());
    $("#updateCategoryNameId").val($("#categoryNameHidden_"+categoryId).val());
    $("#updateCategoryDescId").val($("#categoryDescHidden_"+categoryId).val());
    $("#categoryModelUpdate").modal('show');
}