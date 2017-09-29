'******************************************************
'Registry Helper Functions
'******************************************************
Function RegRead(key, section=invalid)
    if section = invalid then section = "Default"
    sec = CreateObject("roRegistrySection", section)
    if sec.Exists(key) then return sec.Read(key)
    return invalid
End Function

Function RegWrite(key, val, section=invalid)
    if section = invalid then section = "Default"
    sec = CreateObject("roRegistrySection", section)
    sec.Write(key, val)
    sec.Flush() 'commit it
End Function

Function RegDelete(key, section=invalid)
    if section = invalid then section = "Default"
    sec = CreateObject("roRegistrySection", section)
    sec.Delete(key)
    sec.Flush()
End Function
Function AnyToString(any As Dynamic) As dynamic
    if any = invalid return "invalid"
    if isstr(any) return any
    if isint(any) return itostr(any)
    if isbool(any)
        if any = true return "true"
        return "false"
    endif
    if isfloat(any) return Str(any)
    if type(any) = "roTimespan" return itostr(any.TotalMilliseconds()) + "ms"
    return invalid
End Function
'******************************************************
'Convert anything to a string
'
'Always returns a string
'******************************************************
Function tostr(any)
    ret = AnyToString(any)
    if ret = invalid ret = type(any)
    if ret = invalid ret = "unknown" 'failsafe
    return ret
End Function

'******************************************************
'isxmlelement
'
'Determine if the given object supports the ifXMLElement interface
'******************************************************
Function isxmlelement(obj as dynamic) As Boolean
    if obj = invalid return false
    if GetInterface(obj, "ifXMLElement") = invalid return false
    return true
End Function

'******************************************************
'islist
'
'Determine if the given object supports the ifList interface
'******************************************************
Function islist(obj as dynamic) As Boolean
    if obj = invalid return false
    if GetInterface(obj, "ifArray") = invalid return false
    return true
End Function


'******************************************************
'isint
'
'Determine if the given object supports the ifInt interface
'******************************************************
Function isint(obj as dynamic) As Boolean
    if obj = invalid return false
    if GetInterface(obj, "ifInt") = invalid return false
    return true
End Function

'******************************************************
' validstr
'
' always return a valid string. if the argument is
' invalid or not a string, return an empty string
'******************************************************
Function validstr(obj As Dynamic) As String
    if isnonemptystr(obj) return obj
    return ""
End Function

'******************************************************
'isstr
'
'Determine if the given object supports the ifString interface
'******************************************************
Function isstr(obj as dynamic) As Boolean
    if obj = invalid return false
    if GetInterface(obj, "ifString") = invalid return false
    return true
End Function

'******************************************************
'isnonemptystr
'
'Determine if the given object supports the ifString interface
'and returns a string of non zero length
'******************************************************
Function isnonemptystr(obj)
    if isnullorempty(obj) return false
    return true
End Function


'******************************************************
'isnullorempty
'
'Determine if the given object is invalid or supports
'the ifString interface and returns a string of non zero length
'******************************************************
Function isnullorempty(obj)
    if obj = invalid return true
    if not isstr(obj) return true
    if Len(obj) = 0 return true
    return false
End Function

'******************************************************
'isbool
'
'Determine if the given object supports the ifBoolean interface
'******************************************************
Function isbool(obj as dynamic) As Boolean
    if obj = invalid return false
    if GetInterface(obj, "ifBoolean") = invalid return false
    return true
End Function


'******************************************************
'isfloat
'
'Determine if the given object supports the ifFloat interface
'******************************************************
Function isfloat(obj as dynamic) As Boolean
    if obj = invalid return false
    if GetInterface(obj, "ifFloat") = invalid return false
    return true
End Function


'******************************************************
'strtobool
'
'Convert string to boolean safely. Don't crash
'Looks for certain string values
'******************************************************
Function strtobool(obj As dynamic) As Boolean
    if obj = invalid return false
    if type(obj) <> "roString" and type(obj) <> "String" return false
    o = strTrim(obj)
    o = Lcase(o)
    if o = "true" return true
    if o = "t" return true
    if o = "y" return true
    if o = "1" return true
    return false
End Function


'******************************************************
'itostr
'
'Convert int to string. This is necessary because
'the builtin Stri(x) prepends whitespace
'******************************************************
Function itostr(i As Integer) As String
    str = Stri(i)
    return strTrim(str)
End Function


'******************************************************
'Get remaining hours from a total seconds
'******************************************************
Function hoursLeft(seconds As Integer) As Integer
    hours% = seconds / 3600
    return hours%
End Function


'******************************************************
'Get remaining minutes from a total seconds
'******************************************************
Function minutesLeft(seconds As Integer) As Integer
    hours% = seconds / 3600
    mins% = seconds - (hours% * 3600)
    mins% = mins% / 60
    return mins%
End Function


'******************************************************
'Pluralize simple strings like "1 minute" or "2 minutes"
'******************************************************
Function Pluralize(val As Integer, str As String) As String
    ret = itostr(val) + " " + str
    if val <> 1 ret = ret + "s"
    return ret
End Function


'******************************************************
'Trim a string
'******************************************************
Function strTrim(str As String) As String
    st=CreateObject("roString")
    st.SetString(str)
    return st.Trim()
End Function


'******************************************************
'Tokenize a string. Return roList of strings
'******************************************************
Function strTokenize(str As String, delim As String) As Object
    st=CreateObject("roString")
    st.SetString(str)
    return st.Tokenize(delim)
End Function


'******************************************************
'Replace substrings in a string. Return new string
'******************************************************
Function strReplace(basestr As String, oldsub As String, newsub As String) As String
    newstr = ""
    i = 1
    while i <= Len(basestr)
        x = Instr(i, basestr, oldsub)
        if x = 0 then
            newstr = newstr + Mid(basestr, i)
            exit while
        endif
        if x > i then
            newstr = newstr + Mid(basestr, i, x-i)
            i = x
        endif
        newstr = newstr + newsub
        i = i + Len(oldsub)
    end while
    return newstr
End Function

REM ******************************************************
REM HttpEncode - just encode a string
REM ******************************************************
Function HttpEncode(str As String) As String
    o = CreateObject("roUrlTransfer")
    return o.Escape(str)
End Function

'******************************************************
'Get our device version
'******************************************************
Function GetDeviceVersion()
    return CreateObject("roDeviceInfo").GetVersion()
End Function

'******************************************************
'Get our serial number
'******************************************************
Function GetDeviceESN()
    return CreateObject("roDeviceInfo").GetDeviceUniqueId()
End Function

'******************************************************
' Set code that links the device
'******************************************************
Function SetAuthData(userToken As String) As Void
    sec = CreateObject("roRegistrySection", "Authentication")
    sec.Write("UserRegistrationToken", userToken)
    sec.Flush()
End Function

'******************************************************
' Get code that links the device
'******************************************************
Function GetAuthData() As Dynamic
    sec = CreateObject("roRegistrySection", "Authentication")
    if sec.Exists("UserRegistrationToken") 
        token=sec.Read("UserRegistrationToken")
        if token<>invalid then
            return token
        else
            return "false"
        endif
        'return sec.Read("UserRegistrationToken")
    endif
    return invalid
End Function

'******************************************************
' Save Extra data in registry
'******************************************************
Function SetXtraData(key As String,value as String) As Void
    sec = CreateObject("roRegistrySection", "Authentication")
    sec.Write(key, value)
    sec.Flush()
End Function


'******************************************************
' Get Extra data in the registry
'******************************************************
Function GetXtraData(key as String) As Dynamic
    sec = CreateObject("roRegistrySection", "Authentication")
    if sec.Exists(key) 
        return sec.Read(key)
    endif
    return invalid
End Function

'******************************************************
' Get api url
'******************************************************
Function GetAPIurl()    
    return "http://tvstartup.biz/mng-channel/vpanel/api/"
End Function

'*********************************************************
'  Get API user Pass
'*********************************************************
Function GetAPIUserPass()
    'return "username:password"
    return "user=creekboxtv&pass=a29e49a6ed079c64c7ebe7fcce65152d"
End Function

'******************************************************
' Make API Call
'******************************************************
Function APICall(url as String) as dynamic
    statusRequest=CreateObject("roUrlTransfer")
    print "APICall"+url
    'ba = CreateObject("roByteArray") 
    'ba.FromAsciiString(GetAPIUserPass())
    'statusRequest.AddHeader("Authorization", "Basic " + ba.ToBase64String())
    statusRequest.SetURL(url)
    return ParseJson(statusRequest.GetToString())
End Function

Function GetImageUrl()
    return "http://tvstartup.biz/mng-channel/vpanel/uploads/"
End Function

Function getVideoUrl(filename as String)
    return "http://barakyah.videocdn.scaleengine.net/barakyah-vod/play/sestore1/barakyah/newsystem/"+filename+"/playlist.m3u8"
End Function