<!--
function validatePwd(form)
{
 // Modified by Arun on 08 Aug2011 for #25046
   // if (form.PASSWORD.value.length < 6)
   //alert('length'+form.PASSWORD.value.replace(/^\s+/g, '').replace(/\s+$/g, '').length); 
   
    if(/[\s]/.test(form.PASSWORD.value)) {
    	alert("Space character is not allowed.");
    	form.PASSWORD.value = "";
	    form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
    	form.PASSWORD.focus();
    	return false;
    }
    if (form.PASSWORD.value.length < 8)
    {
	    //alert("Please Fill in the Old Password with 6 - 10 characters..!");
	    alert("Please fill in the old password with 8 - 16 characters.");
	    form.PASSWORD.value = "";
	    form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.PASSWORD.focus();
	    return false;
    }
    
    if(/[\s]/.test(form.NPASSWORD.value)) {
    	alert("Space character is not allowed.");
    	form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
    	form.NPASSWORD.focus();
    	return false;
    }
    
    if (form.NPASSWORD.value.length < 8)
    {
	    // Modified by Arun on 08 Aug 2011 for #25046
	    //alert("Please Fill in the New Password with 6 - 10 characters..!");
	    alert("Please fill in the new password with 8 - 16 characters.");
	    form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.NPASSWORD.focus();
	    return false;
    }

    if (form.PASSWORD.value == form.NPASSWORD.value)
    {
	    alert("Please choose a password different from old password.")
	    form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.NPASSWORD.focus();
	    return false;
    }

	/* Modified by Arun for #25046
    if (form.USER_ID.value == form.NPASSWORD.value)
    {
	    alert("Please choose a password different from User ID.")
	    return false;
    }*/
    // used  PWD_Uppercase insted of NPASSWORD since USER_ID is in upper case
    
    if (form.USER_ID.value == form.NPASSWORD.value.toUpperCase())
    {
	    alert("Please choose a password different from User ID.")
	    form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.NPASSWORD.focus();
	    return false;
    }

    if (form.NPASSWORD.value != form.CPASSWORD.value)
    {
	    alert("Your new password and confirm password does not match.")
	    form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.NPASSWORD.focus();
	    return false;
    }
    
    //Below if condition is added by jyoti for Password Policy INC000002484690 
    var passwd = form.NPASSWORD.value;
    var policy_passwd = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@#$%^&;*.\_\(\)\-+=}{\":?'\/\\><\[\]|,]).{4,16}/;
    if(!passwd.match(policy_passwd))
    {
    	alert("Password is invalid. It should contain:\n(i)Atleast one number(0-9).\n(ii)Atleast one upper case letter(A-Z).\n(iii)Atleast one lower case letter(a-z).\n(iv)Atleast one special charater (~!@#$%^&\"*_-+=`|\(){}[]:;'<>,.?/).");
    	form.NPASSWORD.value = "";
	    form.CPASSWORD.value = "";
	    form.NPASSWORD.focus();
	    return false;
    }

}
// Method added by Arun for #25046
function validateSystemParams(form){
   

	if (form.PWDAGE.value == "" || form.PWDAGE == null || form.PWDAGE.value < 30 || form.PWDAGE.value > 365 )
    {
	    alert("Please enter Password Age between 30 to 365 days.");
	    form.PWDAGE.value = "";
	    form.PWDAGE.focus();
	    return false;
    }
    if (form.MINRANGE.value == "" || form.MINRANGE == null || form.MINRANGE.value < 5 ||form.MINRANGE.value > 30)
    {
	    alert("Please enter Range To Remind between 5 to 30 days.");
	    form.MINRANGE.value = "";
	    form.MINRANGE.focus();
	    return false;
    }
    if (form.LOGIN_ATTEMPTS.value == "" || form.LOGIN_ATTEMPTS == null || form.LOGIN_ATTEMPTS.value <= 0 ||form.LOGIN_ATTEMPTS.value > 9)
    {
	    alert("Please enter Login Attempts between 1 to 9.")
	    form.LOGIN_ATTEMPTS.value = "";
	    form.LOGIN_ATTEMPTS.focus();
	    return false;
    }
    //Below two conditions is added by Jyoti for TIBCO INC000002484471 ticket
    var SAP_file = form.CONF_PATH.value;
    var lastindexSAP = SAP_file.length -1;
    var lastchSAP = SAP_file.charAt(lastindexSAP);
    if(!(lastchSAP=='\\'))
    {
    	alert("Please enter valid SAP file path. Example Z:\\ABC\\XYZ\\");
    	form.CONF_PATH.value = "";
    	form.CONF_PATH.focus();
    	return false;
    
    }
    
    var upload_file = form.UPLOAD_PATH.value;
    var lastindexUpload = upload_file.length -1;
    var lastchupload = upload_file.charAt(lastindexUpload);
    if(!(lastchupload=='\\'))
    {
    	alert("Please enter valid WMS Upload path. Example Z:\\ABC\\XYZ\\");
    	form.UPLOAD_PATH.value = "";
    	form.UPLOAD_PATH.focus();
    	return false;
    
    }
    
    //Below 3 are added by Ranjana for the generation of Inbound File under ticket WO0000000356180
    
    if(form.DELIVERY_NO.value=="" || form.DELIVERY_NO.value==null)
    {
    alert("Please Enter Delivery No");
    form.DELIVERY_NO.value="";
    form.DELIVERY_NO.focus();
    return false;
    }
    
     if(form.PALLET_ID.value=="" || form.PALLET_ID.value==null)
    {
    alert("Please Enter Pallet id");
    form.PALLET_ID.value="";
    form.PALLET_ID.focus();
    return false;
    }
    
     if(form.MTID.value=="" || form.MTID.value==null)
    {
    alert("Please Enter Mtid");
    form.MTID.value="";
    form.MTID.focus();
    return false;
    }
    
}

 function IsAlphaNumeric(e) {
   if ((e.keyCode >= 33 && e.keyCode <= 47) || (e.keyCode >= 58 && e.keyCode <= 127) )
        {
          return false;
        }
      }


// -->