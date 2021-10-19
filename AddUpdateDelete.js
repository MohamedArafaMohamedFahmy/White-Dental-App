function doPost(e) {
  //fill your id
  var id = SpreadsheetApp.openById('11THMN-YOn1zugqDodePxi8vUR97TtxsJ94eq18IkzkY'); 
  
  var timestamp = Utilities.formatDate(new Date(), "GMT+7", "MM/dd/yyyy HH:mm:ss");
  var idPatient = e.parameter.idPatient;
  var patientName = e.parameter.patientName; 
  var age = e.parameter.age; 
  var phoneNumber = e.parameter.phoneNumber;
  var address = e.parameter.address;
  var job = e.parameter.job;

    var flag = 1;
     if (flag == 1) {
        id.appendRow([timestamp,idPatient,patientName,age,phoneNumber,address,job]);
        var result = "Insertion successful";   
     }

   return ContentService
        .createTextOutput(result)
        .setMimeType(ContentService.MimeType.JAVASCRIPT);


}

function doGet(e) {
  var op = e.parameter.action;

  var ss = SpreadsheetApp.openById('11THMN-YOn1zugqDodePxi8vUR97TtxsJ94eq18IkzkY');
  var sheet = ss.getSheetByName("Sheet1"); 
    
  var patientName = e.parameter.patientName; 

  if (op == "update"){
        return update_value(e, sheet);
  }
   if (op == "delete"){
        return delete_value(e, sheet);
   }

}

function delete_value(request, sheet) {

    var output = ContentService.createTextOutput();
    var idPatient = request.parameter.idPatient;
    var country = request.parameter.name;
    var flag = 0;



    var lr = sheet.getLastRow();
    for (var i = 1; i <= lr; i++) {
        var rid = sheet.getRange(i, 2).getValue();
        if (rid == idPatient) {
            sheet.deleteRow(i);
            var result = "value deleted successfully";
            flag = 1;
        }

    }

    if (flag == 0)
        var result = "id not found";

    result = JSON.stringify({
        "result": result
    });

    return ContentService
        .createTextOutput(result)
        .setMimeType(ContentService.MimeType.JAVASCRIPT);

}


function update_value(request, sheet) {

    var output = ContentService.createTextOutput();
     var timestamp = Utilities.formatDate(new Date(), "GMT+7", "MM/dd/yyyy HH:mm:ss");
  var idPatient = request.parameter.idPatient;
  var patientName = request.parameter.patientName; 
  var age = request.parameter.age; 
  var phoneNumber = request.parameter.phoneNumber;
  var address = request.parameter.address;
  var job = request.parameter.job;
    var flag = 0;
  
    var lr = sheet.getLastRow();
    for (var i = 1; i <= lr; i++) {
        var rid = sheet.getRange(i, 2).getValue();
        if (rid == idPatient) {
             sheet.getRange(i, 3).setValue(patientName);
            sheet.getRange(i, 4).setValue(age);
            sheet.getRange(i, 5).setValue(phoneNumber);
            sheet.getRange(i, 6).setValue(address);
            sheet.getRange(i, 7).setValue(job);
            var result = "value updated successfully";
            flag = 1;
        }
    }
    if (flag == 0){
       var result = "id not found";
     
    }
       
    return ContentService
        .createTextOutput(result)
        .setMimeType(ContentService.MimeType.JAVASCRIPT);
}
