Function ShowMessage(title as String,msg as String)

    dialog=CreateObject("roMessageDialog")
    port = CreateObject("roMessagePort")    
    dialog.SetMessagePort(port)

    dialog.SetTitle(title)
    dialog.SetText(msg)
    dialog.AddButton(1,"OK")
    dialog.show()
    while true

        dlgMsg = wait(0, dialog.GetMessagePort())
        if type(dlgMsg) = "roMessageDialogEvent"
            if dlgmsg.isScreenClosed()
                exit while 
            else if dlgmsg.isButtonPressed()
                exit while
            end if
        end if
    end while
End Function

Function ShowPleaseWait(title As dynamic, text As dynamic) As Object
    port = CreateObject("roMessagePort")
    dialog = CreateObject("roOneLineDialog")
    dialog.SetMessagePort(port)
    dialog.SetTitle(title)
    dialog.ShowBusyAnimation()
    dialog.Show()
    return dialog
End Function