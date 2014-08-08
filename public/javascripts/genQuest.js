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
              '<br/><input type="hidden" id="addAnswerOptionLengthHidden" name="answerOptionLengthHidden" value="1"/></div>'
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
     var counter = 1 + parseInt($("#addAnswerOptionLengthHidden").val());
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
    $("#addAnswerOptionLengthHidden").val(counter);
}

function updateNewOptionGroup(groupId){
    var counter = 1 + parseInt($("#updateAnswerOptionLengthHidden").val());
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
         '<span class="glyphicon glyphicon-plus" onclick="updateNewOptionGroup('+counter+')"></span>&nbsp;'+
         '<span class="glyphicon glyphicon-minus" onclick="removeUpdateOptionGroup('+counter+')"></span></div>'
    );
    newOptionGroupDiv.appendTo("#optionsDIV");
    $("#updateAnswerOptionLengthHidden").val(counter);
}

function removeOptionGroup(counter){
    counter =  parseInt($("#addAnswerOptionLengthHidden").val());
    if(counter == 1){
    	alert("No more options to remove");
    	return false;
    }
    $("#option"+counter).remove()
    $("#addAnswerOptionLengthHidden").val(counter-1);
}

function removeUpdateOptionGroup(counter){
    counter =  parseInt($("#updateAnswerOptionLengthHidden").val());
    if(counter == 1){
    	alert("No more options to remove");
    	return false;
    }
    $("#option"+counter).remove()
    $("#updateAnswerOptionLengthHidden").val(counter-1);
}

function updateQuestion(questionId){
    $("#updateQuestionHeader").text($("#questionNameHidden_"+questionId).val());
    $("#categoryIdUpdateHidden").val($("#categoryIdHidden_"+questionId).val());
    $("#questionIdUpdateHidden").val($("#questionIdHidden_"+questionId).val());
    $("#questionNameUpdateHidden").val($("#questionNameHidden_"+questionId).val());
    $("#answerUpdateId").val($("#questionAnswerHidden_"+questionId).val());

    if (!!$('#answerOptionsHidden_'+questionId).length) {
       var answerOptionLength       = parseInt($("#answerOptionsSizeHidden_"+questionId).val())
       var answerOptionType         = $("#answerOptionsHidden_"+questionId).val()
       var answerOptionDIVString    = '';

       if(!$("#updateFormDiv").length){
            answerOptionDIVString    =  '<div id="updateFormDiv" class="form-group">'+
                                              '<label for="answerOptions">Select Option Type</label>'+
                                              '<select class="form-control" id="answerOptions" name="answerOptions">';
                  if(answerOptionType){
                     answerOptionDIVString = answerOptionDIVString + '<option value="false">Single Correct</option><option value="true" selected>Multiple Correct</option>';
                  }else{
                     answerOptionDIVString = answerOptionDIVString + '<option value="false" selected>Single Correct</option><option value="true">Multiple Correct</option>';
                  }
                  answerOptionDIVString = answerOptionDIVString + '</select><br/><input type="hidden" id="updateAnswerOptionLengthHidden" name="updateAnswerOptionLengthHidden" value=""/></div>';


                   for ( var i = 1,j = 0; i < answerOptionLength + 1; i++,j++ ) {
                          answerOptionDIVString = answerOptionDIVString + '<div id="optionsDIV"><div id="option'+i+'"><div class="form-group">'+
                                            '<input type="text" name="optionName['+i+']" value="'+$("#answerOptionName_"+j+questionId).val()+'"required/>&nbsp;'+
                                            '<select id="optionCorrect[1]" name="optionCorrect['+i+']">';
                          if($("#answerOptionCorrect_"+j+questionId).val() == "true"){
                              answerOptionDIVString = answerOptionDIVString + '<option value="true" selected>Correct</option>'+
                                                      '<option value="false">Wrong</option></select>&nbsp;';
                          }else{
                              answerOptionDIVString = answerOptionDIVString + '<option value="true">Correct</option>'+
                                                    '<option value="false" selected>Wrong</option></select>&nbsp;';
                          }

                          if(i == 1){
                              answerOptionDIVString = answerOptionDIVString + '<span class="glyphicon glyphicon-plus" onclick="updateNewOptionGroup('+i+')"></span></div></div></div>';
                          }else{
                              answerOptionDIVString = answerOptionDIVString + '<span class="glyphicon glyphicon-plus" onclick="updateNewOptionGroup('+i+')"></span>&nbsp;'+
                                                                              '<span class="glyphicon glyphicon-minus" onclick="removeUpdateOptionGroup('+i+')"></span></div></div></div>';
                          }
                          answerOptionDIVString = answerOptionDIVString + '</div>';
                         }
                         var answerOptionDiv = $(document.createElement('div')).attr("id", 'updateAnswerOptionDIV');
                         answerOptionDiv.html(answerOptionDIVString);
                         answerOptionDiv.appendTo("#answerUpdateOptions");
                         $("#updateAnswerOptionLengthHidden").val(answerOptionLength);
       }
       $("#showAnswerUpdateOptionButtonDIV").hide();
       $("#hideAnswerUpdateOptionButtonDIVUpdate").show();
    }else{
      $("#showAnswerUpdateOptionButtonDIV").show();
      $("#hideAnswerUpdateOptionButtonDIVUpdate").hide();
    }

    $("#questionModelUpdate").modal('show');
}

function showAnswerOptionDivUpdate(){
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
                   '<br/><input type="hidden" id="addAnswerOptionLengthHidden" name="answerOptionLengthHidden" value="1"/></div>'
               );
     answerOptionDiv.appendTo("#answerUpdateOptions");
     $("#showAnswerUpdateOptionButtonDIV").hide();
     $("#hideAnswerUpdateOptionButtonDIVUpdate").show();
}

function hideAnswerOptionDivUpdate(){
    $("#updateAnswerOptionDIV").remove();
    $("#showAnswerUpdateOptionButtonDIV").show();
    $("#hideAnswerUpdateOptionButtonDIVUpdate").hide();
}


function deleteQuestion(questionId){
    $("#deleteQuestionId").val(questionId);
    $('#deleteForm').submit();
}