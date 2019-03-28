Option Explicit 
Dim attchment,ToList, FromList, CcList, SubjectDetails
ToList = Wscript.Arguments.Item(0)
CcList = Wscript.Arguments.Item(1)
path = Wscript.Arguments.Item(2)
SubjectDetails = Wscript.Arguments.Item(3)

 
Dim  path, MyApp, MyItem, aniTextFile, objFSO, aniData

REM attchment = path

aniTextFile = path

Set objFSO = CreateObject("Scripting.FileSystemObject")
aniData = objFSO.OpenTextFile(aniTextFile,1).ReadAll

Set MyApp = CreateObject("Outlook.Application")
	Set MyItem = MyApp.CreateItem(0)
	With MyItem
		 .To = ToList
		 .Cc = CcList
		 .Subject = SubjectDetails
		 .ReadReceiptRequested = False
		 .HTMLBody = aniData
		
       		
	End With
	REM MyItem.Attachments.Add attchment
	MyItem.Send
	