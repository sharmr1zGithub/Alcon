<!--
function validateUser(form)
{
    if (form.USER_ID.value.length < 2)
    {
	    alert("Please Fill in the User ID with 2 - 10 characters..!");
	    form.PASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.USER_ID.focus();
	    return false;
    }
    if(/[\s]/.test(form.PASSWORD.value)) {
    	alert("Space character is not allowed.");
    	form.PASSWORD.value = "";
	    form.CPASSWORD.value = "";
    	form.PASSWORD.focus();
    	return false;
    }
    // Modified by Arun on 21 AuSeptg 2011 for #25046
    //if (form.PASSWORD.value.length < 6)
    if (form.PASSWORD.value.length < 8)
    {
	    //alert("Please Fill in the Password with 6 - 10 characters..!");
	    alert("Please fill in the new password with 8 - 16 characters.");
	    form.PASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.PASSWORD.focus();
	    return false;
    }
    //Below if condition is added by jyoti for Password Policy INC000002484690 
    var passwd = form.PASSWORD.value;
    var policy_passwd = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#$%^&;*.\_\(\)\-+=}{\":?'\/\\><\[\]|,]).{4,16}/;
    if(!passwd.match(policy_passwd))
    {
    	alert("Password is invalid. It should contain:\n(i)Atleast one number(0-9).\n(ii)Atleast one upper case letter(A-Z).\n(iii)Atleast one lower case letter(a-z).\n(iv)Atleast one special charater (~!@#$%^&\"*_-+=`|\(){}[]:;'<>,.?/).");
    	form.PASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.PASSWORD.focus();
	    return false;
    }
    

    if (form.USER_ID.value == form.PASSWORD.value)
    {
	    alert("Please choose a password different from User ID..!");
	    form.PASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.PASSWORD.focus();
	    return false;
    }

    if (form.PASSWORD.value != form.CPASSWORD.value)
    {
	    alert("Your Password and Confirm Password does not match..!");
	    form.PASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.PASSWORD.focus();
	    return false;
    }

    if (form.USER_NAME.value.length < 1)
    {
	    alert("Please Fill in the User Name ..!");
	    form.USER_NAME.focus();
	    return false;
    }

    if (form.EFFECTIVE_DATE.value.length > 0)
    {
       if (form.EFFECTIVE_DATE.value.length < 8)
        {
        alert("Invalid Date ..!");
        form.EFFECTIVE_DATE.focus();
        return false;
        }
    }

    if (form.USER_LEVEL.value.length < 1)
    {
    alert("Please Choose the User Level ..!");
    form.USER_LEVEL.focus();
    return false;
    }

}


// -->