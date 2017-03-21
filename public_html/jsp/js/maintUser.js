<!--
function validateUser(form)
{
   
    if (form.PASSWORD.value.length < 6)
    {
    alert("Please Fill in the Password with 6 - 10 characters..!");
    return false;
    }
    
    if (form.USER_ID.value == form.PASSWORD.value)
    {
    alert("Please choose a password different from User ID..!")
    return false;
    }

    if (form.PASSWORD.value != form.CPASSWORD.value)
    {
    alert("Your Password and Confirm Password does not match..!")
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
    if(/[\s]/.test(form.PASSWORD.value)) {
    	alert("Space character is not allowed.");
    	form.PASSWORD.value = "";
	    form.CPASSWORD.value = "";
    	form.PASSWORD.focus();
    	return false;
    }

    if (form.USER_NAME.value.length < 1)
    {
    alert("Please Fill in the User Name ..!")
    form.USER_NAME.focus();
    return false;
    }

    if (form.EFFECTIVE_DATE.value.length > 0)
    {
       if (form.EFFECTIVE_DATE.value.length < 8)
        {
        alert("Invalid Date ..!")
        form.EFFECTIVE_DATE.focus();
        return false;
        }
    }

    if (form.USER_LEVEL.value.length < 1)
    {
    alert("Please Choose the User Level ..!")
    form.USER_LEVEL.focus();
    return false;
    }
    //Below if condition is added on 6-june-2014 for user active/inactive ticket #WO0000000041526
    if (form.USER_ACTIVE.value == 'INACTIVE')
    {
    	if(form.REASON.value == '')
    	{
    	alert("Please Enter the Reason ..!")
    	return false;
    	}
    }
   

}

function clearText(form)
{
    form.PASSWORD.value="";
    form.CPASSWORD.value="";
    form.ENCRYPT_FLAG.value=1;
} 

//Below functions is added on 3-june-2014 for user active/inactive ticket #WO0000000041526
function hide(name)
{
	var trid = document.getElementById("hideTR");
		
	if(name=='ACTIVE')
	trid.style.display='none';
}

function show(name)
{
	var trid = document.getElementById("hideTR");
    
    if(name=='INACTIVE'){
        trid.style.display = 'block';
        }
        else {
        trid.style.display = 'none';
        }
}


// -->