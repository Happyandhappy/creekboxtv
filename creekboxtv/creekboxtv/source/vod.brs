function ShowVod()

    port = CreateObject("roMessagePort")
    poster = CreateObject("roPosterScreen")
    poster.SetBreadcrumbEnabled(false)   
    poster.SetMessagePort(port)     
    
    poster.SetListStyle("flat-category")

    allvods = CreateObject("roArray", 10, true)
    response=APICall(GetAPIurl()+"videos.php?"+GetAPIUserPass())
    if response <> invalid
        for each video in response.videos
            vod = CreateObject("roAssociativeArray")
            vod.id=video.id
            vod.ContentType = "episode"
            vod.Title = video.title
            vod.ShortDescriptionLine1 = video.title
            vod.ShortDescriptionLine2 = video.description
            vod.Description = video.description
            vod.SDPosterURL = video.image_1
            vod.HDPosterURL = video.image_1
            vod.StreamUrls=video.hls_stream
            vod.length=video.length
            allvods.push(vod)
        end For
    end if
    
    poster.SetContentList(allvods)
    poster.SetFocusedListItem(1)
    poster.Show()     
    
    while true
        msg = wait(0, port)
        if msg.isScreenClosed() Then
           return 1
        else if msg.isListItemSelected()
            ShowDetails(response.videos[msg.GetIndex()])
        end if  
    end while
end function
