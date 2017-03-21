<!--
function validate()
{
	if (document.loginForm.name.value.length < 2)
	{
	// Message modifed by Arun for #25046
		//alert("Please fill in the Login Name with 2 - 10 characters!")
		alert("Please fill in the user id with 2-10 characters.")
		document.loginForm.name.value="";
		document.loginForm.pwd.value="";
		document.loginForm.name.focus();
	return false;
	}
	//Modified by Arun on 11 Aug 2011 for #25046
	/*if (document.loginForm.pwd.value.length < 7)
	{
		alert("Please fill in the Password with 8 - 10  characters!")
	return false;
	}*/
	
	if (document.loginForm.pwd.value.length < 8)
	{
		alert("Please fill in the password with 8 - 16  characters")
		document.loginForm.pwd.value="";
		document.loginForm.pwd.focus();
	return false;
	}
}

function ref()
{
	document.loginForm.name.focus();
}




// -->
