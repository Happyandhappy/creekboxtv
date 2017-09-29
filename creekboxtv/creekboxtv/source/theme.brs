function initTheme()

    app = CreateObject("roAppManager")
    theme = CreateObject("roAssociativeArray")
   
    theme.OverhangSliceSD = "pkg:/locale/default/images/header_sd.png"
    theme.OverhangSliceHD = "pkg:/locale/default/images/header_hd.png"

    'sets background for the language selector tamil,telugu
    'theme.FilterBannerActiveSD="pkg:/images/filter_banner_active_sd.png"
    'theme.FilterBannerActiveHD="pkg:/images/filter_banner_active_hd.png"


    theme.GridScreenOverhangSliceSD="pkg:/locale/default/images/grid_header_sd.png"
    theme.GridScreenOverhangSliceHD="pkg:/locale/default/images/grid_header_hd.png"

   
    'theme.GridScreenOverhangSliceSD="pkg:/images/grid_header_sd.png"
    'theme.GridScreenOverhangSliceHD="pkg:/images/grid_header_hd.png"

    theme.GridScreenOverhangHeightHD="110"
    theme.GridScreenOverhangHeightSD="90"
   
    ' will set the background around the focused item in the grid view
    'theme.GridScreenFocusBorderSD="pkg:/images/gridfocused_sd.png"
    'theme.GridScreenFocusBorderHD="pkg:/images/gridfocused_hd.png"

  

    'theme.listItemHighlight           = "#FFFFFF"
    'ListItemHighlightHD: "pkg:/images/select_bkgnd.png"
    'ListItemHighlightSD: "pkg:/images/select_bkgnd.png"


    theme.BackgroundColor="#000000"
    'sets the grid screens background color
    'theme.GridScreenBackgroundColor = "#e2e2e2"
    'theme.GridScreenListNameColor="#8dc43f"
    ' Adding colors to desc box
    'for the title
    'theme.GridScreenDescriptionTitleColor="#ff0000"
    'for the description
    'theme.GridScreenDescriptionSynopsisColor="#240e4f"
    'theme.GridScreenDescriptionImageHD="pkg:/images/gridscreen_desc_bg_hd.png"
    'theme.GridScreenDescriptionImageSD="pkg:/images/gridscreen_desc_bg_sd.png"

    app.SetTheme(theme)

End function