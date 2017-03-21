//This js is added on 4-july-2014 for user group active/inactive ticket #WO0000000041526
function validateGroup(form)
{
	if(form.GROUP_ACTIVE.value == 'INACTIVE')
    	{
    	if(form.REASON.value == '')
    	{
    	alert("Please Enter the Reason ..!")
    	return false;
    	}
    }
 }
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
 