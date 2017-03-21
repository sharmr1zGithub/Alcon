<%@ page contentType="text/html;charset=windows-1252"%>
<html>
  <head>
<script language="javascript">  
function onPrint(){
 var noofcheckbox=1;  
 var noofcheckbox=document.form2.chkdDoNo.length;
 alert(noofcheckbox);
}
</script>
    
    <title>untitled</title>
  </head>

  <FORM name="form2" method="post" action="PrintShippingMark1.jsp" >
   <INPUT name="chkdDoNo" id="chkdDoNo" Type="Checkbox" style="border:0;background=#dddddd" >
   <INPUT name="REFNO" type = "TEXT"  size="20"  MAXLENGTH=20>
    <input type="button" value="Print" onClick="onPrint();">
    </FORM>
 
  
</html>
