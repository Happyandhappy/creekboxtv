function search()

    displayHistory = true 
    history = CreateObject("roArray", 1, true)
    port = CreateObject("roMessagePort")
    screen = CreateObject("roSearchScreen")
    screen.SetMessagePort(port)

    if displayHistory
        screen.SetSearchTermHeaderText("Search Movie")
        screen.SetSearchButtonText("Search")
        screen.SetClearButtonText("clear history")
        screen.SetClearButtonEnabled(true) 'defaults to true
        screen.SetSearchTerms(history)
    else
        screen.SetSearchTermHeaderText("Suggestions:")
        screen.SetSearchButtonText("Search")
        screen.SetClearButtonEnabled(false)
    endif
    screen.Show()
    done = false
    while done = false
        msg = wait(0, screen.GetMessagePort())
        if type(msg) = "roSearchScreenEvent"
            if msg.isScreenClosed()                
                return -1
            else if msg.isCleared()
                history.Clear()
            else if msg.isPartialResult()
                ShowMessage("Info","We are still working on this.")
                results = GenerateMovieSearchSuggestions(msg.GetMessage())              
                screen.ClearSearchTerms()
                for each r in results                   
                    screen.addSearchTerm(r)
                end for
            else if msg.isFullResult()
                
            else

                Print "Process the result now"
                print "Unknown event: Type: "; msg.GetType(); " Index: ";msg.GetIndex();" Data : ";msg.GetData();" msg: ";msg.GetMessage()

            endif

        endif
    endwhile
    print "Exiting..."

End Function

Function GenerateMovieSearchSuggestions(partSearchText As String) As Object

    suggestions = CreateObject("roArray", 4, true)    
    response=APICall(GetAPIurl()+"search.php?q="+HttpEncode(partSearchText))    
    
    return suggestions

End Function