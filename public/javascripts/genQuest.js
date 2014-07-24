function clearLoginError() {
	var div = document.getElementById('errorDiv');
    if (div.style.display !== 'none') {
        div.style.display = 'none';
    }else {
        div.style.display = 'block';
    }
}