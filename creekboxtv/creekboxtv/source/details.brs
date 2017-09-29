function ShowDetails(vod as dynamic)
    print vod
    port = CreateObject("roMessagePort")
    springBoard = CreateObject("roSpringboardScreen")
    springBoard.SetMessagePort(port) 
    position="0"
    newpos=GetXtraData(vod.id)
    if newpos=invalid
        newpos="0"
    endif
    o = CreateObject("roAssociativeArray")
    o.ContentType = "episode"
    o.Title = vod.title
    o.ShortDescriptionLine1 = vod.title
    o.ShortDescriptionLine2 = ""
    o.Description = vod.description
    o.SDPosterURL=vod.ffmpeg_30 
    o.HDPosterURL=vod.ffmpeg_20
    o.Rating = invalid
    o.StarRating = invalid
    o.Length = vod.length
    o.StreamUrls=getVideoUrl(vod.video)
    
    
    if newpos.toint()>120 then   
        springBoard.AddButton(1, "Play from Beginning")
        springBoard.AddButton(2, "Resume Playing")  
    else
        springBoard.AddButton(1, "Play from Beginning")
    end if
    springBoard.SetStaticRatingEnabled(false)
    springBoard.SetContent(o)
    springBoard.AllowNavLeft(false)
    springBoard.AllowNavRight(false)
    springBoard.AllowNavRewind(false)
    springBoard.AllowNavFastForward(false)
    springBoard.Show() 
    while true
        msg = wait(0, port)
        if type(msg) = "roSpringboardScreenEvent"
            if msg.isScreenClosed() Then
                return -1
            else if msg.isButtonPressed()
                    if msg.GetIndex()=1
                        status="incomplete"
                        status=player(vod.id,getVideoUrl(vod.video),vod.title,"","0")
                        if status="completed" then 
                            springBoard.AllowUpdates(false)
                            springBoard.clearButtons()
                            springBoard.AddButton(1, "Play from Beginning")                            
                            springBoard.AllowUpdates(true)     
                        else
                            springBoard.AllowUpdates(false)
                            springBoard.clearButtons()
                            springBoard.AddButton(1, "Play from Beginning")                            
                            springBoard.AddButton(2, "Resume Playing")
                            springBoard.AllowUpdates(true)  
                        endif
                    else if msg.GetIndex()=2
                        newpos=GetXtraData(vod.id)
                        print isstr(tostr(newpos))
                        status="incomplete"                        
                        status=player(vod.id,getVideoUrl(vod.video),vod.title,"",tostr(newpos))
                        if status="completed" then 
                            springBoard.AllowUpdates(false)
                            springBoard.clearButtons()
                            springBoard.AddButton(1, "Play from Beginning")                            
                            springBoard.AllowUpdates(true)  
                        else
                            springBoard.AllowUpdates(false)
                            springBoard.clearButtons()
                            springBoard.AddButton(1, "Play from Beginning")  
                            springBoard.AddButton(2, "Resume Playing")                          
                            springBoard.AllowUpdates(true)  
                        endif
                    else
                        print "Do something else"
                    endif
            endif
        end if
    end while
end function
