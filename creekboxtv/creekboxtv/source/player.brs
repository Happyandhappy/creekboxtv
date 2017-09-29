function player(video_id as String,stream as String,streamTitle as String,subtitle_url as String,position as String)

    print "play: "+stream
    p = CreateObject("roMessagePort")
    video = CreateObject("roVideoScreen")
    video.setMessagePort(p)

    videoclip = CreateObject("roAssociativeArray")
    videoclip.StreamBitrates = [0]
    videoclip.StreamUrls = [stream]

    di = CreateObject("roDeviceInfo")
    if di.GetDisplayType() = "HDTV" then 
        videoclip.streamqualities=["HD"]
    else
        videoclip.streamqualities=["SD"]
    end if

    videoclip.PlayStart=position.toint()

    videoclip.StreamFormat = "hls"
    videoclip.switchingstrategy="full-adaptation"
    videoclip.Title = streamTitle
    if subtitle_url<>invalid
        videoclip.SubtitleUrl = subtitle_url
    end if
    
    lastSavedPos   = 0
    statusInterval = 10
    nowpos=0
    
    video.SetPositionNotificationPeriod(10)
    video.SetContent(videoclip)
    video.show()
    

    while true
        msg = wait(0, video.GetMessagePort())
        if type(msg) = "roVideoScreenEvent"
            if msg.isScreenClosed() then 'ScreenClosed event
                return "incomplete"
                exit while                
            else if msg.isFullResult()
                SetXtraData(video_id,"0")
                return "completed"
            else if msg.isResumed()
                SetXtraData(video_id,nowpos.tostr())
            else if msg.isPlaybackPosition() then
                nowpos = msg.GetIndex()
                if nowpos > 120
                    if abs(nowpos - lastSavedPos) > statusInterval
                        lastSavedPos = nowpos
                        SetXtraData(video_id,nowpos.tostr())               
                    end if
                end if
            else if msg.isRequestFailed()
                showMessage("Information","Something went wrong while we tried to play the video")
            else
                print "Unknown event: "; msg.GetType(); " msg: "; msg.GetMessage()
            endif
        end if
    end while
end function


function streamer(stream as String,streamTitle as String)
    print "stream: "+stream
    p = CreateObject("roMessagePort")
    video = CreateObject("roVideoScreen")
    video.setMessagePort(p)

    videoclip = CreateObject("roAssociativeArray")
    videoclip.StreamBitrates = [0]

    videoclip.StreamUrls = [stream]

    di = CreateObject("roDeviceInfo")
    if di.GetDisplayType() = "HDTV" then 
        videoclip.streamqualities=["HD"]
    else
        videoclip.streamqualities=["SD"]
    end if
    
    videoclip.StreamFormat = "hls"
    videoclip.Title = streamTitle
    videoclip.SubtitleUrl = ""
    videoclip.switchingstrategy="full-adaptation"
        
    video.SetContent(videoclip)
    video.show()

    while true
        msg = wait(0, video.GetMessagePort())
        if type(msg) = "roVideoScreenEvent"
            if msg.isScreenClosed() then 'ScreenClosed event           
                return "incomplete"
                exit while    
            else if msg.isRequestFailed()                   
                showMessage("Information","Something went wrong while we tried to play the video")
            else
                print "Unknown event: "; msg.GetType(); " msg: "; msg.GetMessage()
            endif
        end if
    end while
end function