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

    $("#questionForm").submit(function(){
      $("#categoryId").removeAttr('disabled');
    });

});

function getAddCategoryModal(){
    $("#categoryLabel").text("Add New Category");
    var addModelDIVHtml = '<form method="POST" action="/addcategory">'+
                          '<div class="form-group">' +
                          '<input type="text" id="newCategoryNameId" name="categoryName" placeholder="Category Name" class="form-control" autofocus required/>'+
    	                  '</div>'+
    	                  '<div class="form-group">'+
                          '<textarea class="form-control" id="newCategoryDescId" name="categoryDesc" placeholder="Category Description" required></textarea>'+
                          '</div>'+
                          '<input  type="hidden" id="categoryId" name="categoryId" value=""/>'+
                          '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
                          '<button type="submit" id="saveNewCategoryButton" class="btn btn-primary">Save changes</button>'+
                          '</form>';
    $("#modelBodyDiv").html(addModelDIVHtml);
    $("#categoryModel").modal('show');
}

function getUpdateCategoryModal(categoryId){
   $("#categoryLabel").text("Update Category");
   var updateModelDIVHtml = '<form method="POST" action="/updatecategory">'+
                            '<div class="form-group">'+
                            '<h4 id="updateCategoryHeader">'+$("#categoryNameHidden_"+categoryId).val()+'</h4>'+
                            '</div>'+
                            '<div class="form-group">'+
                            '<textarea class="form-control" id="updateCategoryDescId" name="categoryDesc" placeholder="Category Description" autofocus required></textarea>'+
                            '</div>'+
                            '<input  type="hidden" id="updatecategoryId"  name="categoryId" value="'+$("#categoryIdHidden_"+categoryId).val()+'"/>'+
                            '<input  type="hidden" id="updateCategoryNameId" name="categoryName" value="'+$("#categoryNameHidden_"+categoryId).val()+'"/>'+
                            '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
                            '<button type="submit" id="saveUpdateCategoryButton" class="btn btn-primary">Save changes</button>'+
                            '</form>';
   $("#modelBodyDiv").html(updateModelDIVHtml);
   $("#updateCategoryDescId").val($("#categoryDescHidden_"+categoryId).val());
   $("#categoryModel").modal('show');
}

function deleteCategory(categoryId){
    $("#deleteCategoryId").val(categoryId);
    $('#deleteForm').submit();
}

function getAddQuestionModal(){
    $("#questionModelLabel").text("Add New Question");
    $("#addCategoryDiv").show();
    $("#categoryId").attr("required","true");
    $("#questionNameDiv").html('<textarea id="questionNameId" name="question" placeholder="Question" '+
                               'class="form-control" autofocus="true" required="true"></textarea>');
    $("#questionAnswerDiv").html('<textarea id="answerId" name="answer" placeholder="Answer" '+
                                 'class="form-control" required="true"></textarea>');
    $("#answerOptionDIV").remove();
    $("#showAnswerOptionButtonDIV").show();
    $("#hideAnswerOptionButtonDIV").hide();
    $("#questionForm").attr("action","/addquestion");
    $("#questionModel").modal('show');
}

function showAnswerOptionDiv(){
     var answerOptionDiv = '<div id="answerOptionDIV">' +
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
                           '<br/><input type="hidden" id="addAnswerOptionLengthHidden" name="answerOptionLengthHidden" value="1"/></div>';
     $("#answerOptions").html(answerOptionDiv);
     $("#showAnswerOptionButtonDIV").hide();
     $("#hideAnswerOptionButtonDIV").show();
}

function hideAnswerOptionDiv(){
    $("#answerOptionDIV").remove();
    $("#showAnswerOptionButtonDIV").show();
    $("#hideAnswerOptionButtonDIV").hide();
}

function addNewOptionGroup(groupId){
     var counter = 1 + parseInt($("#addAnswerOptionLengthHidden").val());
     if(counter > 5){
       alert("Only 5 answer options allowed");
       return false;
    }
    var newOptionGroupDiv = $(document.createElement('div')).attr("id", 'option' + counter);
    newOptionGroupDiv.html('<div class="form-group">'+
                           '<input type="text" name="optionName['+counter+']" required/>&nbsp;'+
                           '<select id="optionCorrect['+counter+']" name="optionCorrect['+counter+']"">'+
                           '<option value="true">Correct</option><option value="false">Wrong</option></select>&nbsp;'+
                           '<span class="glyphicon glyphicon-plus" onclick="addNewOptionGroup('+counter+')"></span>&nbsp;'+
                           '<span class="glyphicon glyphicon-minus" onclick="removeOptionGroup('+counter+')"></span></div>'
                          );
    newOptionGroupDiv.appendTo("#optionsDIV");
    $("#addAnswerOptionLengthHidden").val(counter);
}

function removeOptionGroup(counter){
    counter =  parseInt($("#addAnswerOptionLengthHidden").val());
    if(counter == 1){
    	alert("No more options to remove");
    	return false;
    }
    $("#option"+counter).remove();
    $("#addAnswerOptionLengthHidden").val(counter-1);
}

function getUpdateQuestionModal(questionId){
    $("#questionModelLabel").text("Update Question");
    $("#categoryId").val($("#categoryIdHidden_"+questionId).val());
    $("#categoryId").attr("required","true");
    $("#categoryId").attr("disabled","false");
    $("#questionNameDiv").html('<textarea id="questionNameId" name="question" placeholder="Question"'+
                                'class="form-control" autofocus="true" required="true"></textarea>');
    $("#questionNameId").val($("#questionNameHidden_"+questionId).val());
    $("#questionAnswerDiv").html('<textarea id="answerId" name="answer" placeholder="Answer" '+
                                     'class="form-control" required="true"></textarea>');
    $("#answerId").val($("#questionAnswerHidden_"+questionId).val());
    $("#questionId").val($("#questionIdHidden_"+questionId).val());
    $("#answerOptionDIV").remove();


    if (!!$('#answerOptionsHidden_'+questionId).length) {
            var answerOptionLength       = parseInt($("#answerOptionsSizeHidden_"+questionId).val());
            var answerOptionType         = $("#answerOptionsHidden_"+questionId).val();

            var answerOptionDIVString    = '<div id="answerOptionDIV">'+
                                           '<div id="updateFormDiv" class="form-group">'+
                                           '<label for="answerOptions">Select Option Type</label>'+
                                           '<select class="form-control" id="answerOptions" name="answerOptions">';
            if(answerOptionType){
                answerOptionDIVString = answerOptionDIVString + '<option value="false">Single Correct</option><option value="true" selected>Multiple Correct</option>';
            }else{
                answerOptionDIVString = answerOptionDIVString + '<option value="false" selected>Single Correct</option><option value="true">Multiple Correct</option>';
            }
            answerOptionDIVString = answerOptionDIVString + '</select><br/><input type="hidden" id="addAnswerOptionLengthHidden" name="addAnswerOptionLengthHidden" value=""/></div>';

            for ( var i = 1,j = 0; i < answerOptionLength + 1; i++,j++ ) {
                 answerOptionDIVString = answerOptionDIVString + '<div id="option'+i+'"><div class="form-group">'+
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
                    answerOptionDIVString = answerOptionDIVString + '<span class="glyphicon glyphicon-plus" onclick="addNewOptionGroup('+i+')"></span></div></div>';
                 }else{
                    answerOptionDIVString = answerOptionDIVString + '<span class="glyphicon glyphicon-plus"  onclick="addNewOptionGroup('+i+')"></span>&nbsp;'+
                                                                    '<span class="glyphicon glyphicon-minus" onclick="removeOptionGroup('+i+')"></span></div></div>';
                 }
            }
            answerOptionDIVString    = answerOptionDIVString = answerOptionDIVString + '</div>';
            $("#answerOptions").html(answerOptionDIVString);
            $("#showAnswerOptionButtonDIV").hide();
            $("#hideAnswerOptionButtonDIV").show();
            $("#addAnswerOptionLengthHidden").val(answerOptionLength);
    }else{
      $("#showAnswerOptionButtonDIV").show();
      $("#hideAnswerOptionButtonDIV").hide();
      $("#addAnswerOptionLengthHidden").val(0);
    }
    $("#questionForm").attr("action","/updatequestion");
    $("#questionModel").modal('show');
}

function deleteQuestion(questionId){
    $("#deleteQuestionId").val(questionId);
    $('#deleteForm').submit();
}
