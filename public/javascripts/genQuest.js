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

function showAnswerOptionDiv(){
     var answerOptionDiv = $(document.createElement('div')).attr("id", 'answerOptionDIV');
     answerOptionDiv.html(
              '<div class="form-group">'+
              '<label for="answerOptions">Select Option Type</label>'+
              '<select class="form-control" id="answerOptions" name="answerOptions">'+
              '<option value="false">Single Correct</option><option value="true">Multiple Correct</option>'+
              '</select></div>'+
              '<div id="optionsDIV"><div id="option1"><div class="form-group">'+
              '<input type="text" name="optionName[1]" required/>&nbsp;'+
              '<select id="optionCorrect[1]" name="optionCorrect[1]">'+
              '<option value="true">Correct</option>'+
              '<option value="false">Wrong</option></select>&nbsp;'+
              '<span class="glyphicon glyphicon-plus" onclick="addNewOptionGroup(1)"></span></div></div></div>'+
              '<br/></div>'
          );
     answerOptionDiv.appendTo("#answerOptions");
     $("#showAnswerOptionButtonDIV").hide()
     $("#hideAnswerOptionButtonDIV").show()
}

function hideAnswerOptionDiv(){
    $("#answerOptionDIV").remove()
    $("#showAnswerOptionButtonDIV").show()
    $("#hideAnswerOptionButtonDIV").hide()
}

function addNewOptionGroup(groupId){
    var counter = 1 + groupId
    if(counter > 5){
       alert("Only 5 answer options allowed");
       return false;
    }
    var newOptionGroupDiv = $(document.createElement('div')).attr("id", 'option' + counter);
    newOptionGroupDiv.html(
         '<div class="form-group">'+
         '<input type="text" name="optionName['+counter+']" required/>&nbsp;'+
         '<select id="optionCorrect['+counter+']" name="optionCorrect['+counter+']"">'+
         '<option value="true">Correct</option><option value="false">Wrong</option></select>&nbsp;'+
         '<span class="glyphicon glyphicon-plus" onclick="addNewOptionGroup('+counter+')"></span>&nbsp;'+
         '<span class="glyphicon glyphicon-minus" onclick="removeOptionGroup('+counter+')"></span></div>'
    );
    newOptionGroupDiv.appendTo("#optionsDIV");
}

function removeOptionGroup(counter){
    if(counter==1){
    	alert("No more options to remove");
    	return false;
    }
    $("#option"+counter).remove()
}

function updateQuestion(questionId){
    $("#updateQuestionHeader").text($("#questionNameHidden_"+questionId).val());
    $("#categoryIdUpdateHidden").val($("#categoryIdHidden_"+questionId).val());
    $("#questionIdUpdateHidden").val($("#questionIdHidden_"+questionId).val());
    $("#questionModelUpdate").modal('show');
}

function deleteQuestion(questionId){
    $("#deleteQuestionId").val(questionId);
    $('#deleteForm').submit();
}