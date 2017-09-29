function showHomeScreen()
    initTheme()
    port = CreateObject("roMessagePort")
    grid = CreateObject("roGridScreen")
    grid.SetMessagePort(port) 

    grid.setGridStyle("two-row-flat-landscape-custom")
    grid.SetBreadcrumbEnabled(false)

    gridResponse=APICall(GetAPIurl()+"grid_av.php?"+GetAPIUserPass())

    rowTitles = CreateObject("roArray", 10, true)

    for each category in gridResponse.data 
        rowTitles.Push(category.title)
    end for

    grid.SetupLists(rowTitles.Count())
    grid.SetListNames(rowTitles) 

    vod_category_count = 0
    c=0
    for each category in gridResponse.data
        list=CreateObject("roArray",10,true)      


        if category.type="vod_category" then
            vod_category_count = vod_category_count + 1
            for each video in gridResponse.data[c].videos
                o=CreateObject("roAssociativeArray")
                o.ContentType = "episode"
                o.Title = video.title           
                o.SDPosterURL = video.ffmpeg_30 
                o.HDPosterURL = video.ffmpeg_20
                o.ShortDescriptionLine1 = video.title
                o.ShortDescriptionLine2 = ""
                o.Description = video.description
                o.Rating = invalid
                o.StarRating = invalid
                o.Length = invalid
                list.Push(o)            
            endfor

        else if category.type="livestream_category" then

            for each video in gridResponse.data[c].videos
                o=CreateObject("roAssociativeArray")
                o.ContentType = "episode"
                o.Title = video.title           
                o.SDPosterURL = video.ffmpeg_30 
                o.HDPosterURL = video.ffmpeg_20
                o.ShortDescriptionLine1 = video.title
                o.ShortDescriptionLine2 = ""
                o.Description = video.description
                o.Rating = invalid
                o.StarRating = invalid
                o.Length = invalid
                list.Push(o)            
            endfor
        endif
        
        grid.SetContentList(c, list) 
        c=c+1
    endfor

    

    grid.SetUpBehaviorAtTopRow("stop")
    grid.SetDescriptionVisible(true)   
    grid.Show() 


    total_vod_category_count  = vod_category_count+1

    while true
        msg = wait(0, port)
        if type(msg) = "roGridScreenEvent" then
            if msg.isScreenClosed() then
                return -1
            elseif msg.isListItemFocused()
                print "Focused msg: ";msg.GetMessage();"row: ";msg.GetIndex();
                print " col: ";msg.GetData()
            elseif msg.isListItemSelected()
                print "Selected msg: ";msg.GetMessage();"row: ";msg.GetIndex();
                print " col: ";msg.GetData()

                if msg.GetIndex()=0

                    print "Play the livestream"
                    streamer(gridResponse.data[msg.GetIndex()].videos[msg.GetData()].url,gridResponse.data[msg.GetIndex()].videos[msg.GetData()].title)

                else

                    ShowDetails(gridResponse.data[msg.GetIndex()].videos[msg.GetData()])

                endif
            endif
        endif
    end while
end function
