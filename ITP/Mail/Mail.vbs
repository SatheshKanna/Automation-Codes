Option Explicit 
Dim ToList, SubjectDetails, BodyContent , objOutlook , objMail
ToList = Wscript.Arguments.Item(0)
SubjectDetails = Wscript.Arguments.Item(1)
BodyContent = Wscript.Arguments.Item(2)

Set objOutlook = CreateObject("Outlook.Application")
Set objMail = objOutlook.CreateItem(0)
objMail.To = ToList
objMail.Subject = SubjectDetails
objMail.Body = BodyContent
objMail.Send
Set objMail = Nothing
Set objOutlook = Nothing