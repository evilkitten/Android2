;SHARED FILESYSTEM

Const VISUAL_DIR_NAME$="Visual"
Global ROOT$

;CHANGE ACCORDINGLY FOR DEBUG / RELEASE VERSIONING
Function InitialiseSharedFilesystem()
	ROOT$=CurrentDir();SystemProperty( "AppDir" );
End Function

Function MapsDir$()
	Return ROOT$+"Maps\"
End Function

Function VisualDir$()
	Return ROOT+VISUAL_DIR_NAME$+"\"
End Function

;SHARED GENERAL MAP

Global MAP_BANK

Const MAP_GROUND=0
Const MAP_WALL=1
Const MAP_TREE=2
Const MAP_MINE=3
Const MAP_BOUNCER=4
Const MAP_HOVERDROID=5
Const MAP_MILLITOID=6
Const MAP_CAPSULE=7

Const MAP_GROUND_NAME$="Empty"
Const MAP_WALL_NAME$="Wall"
Const MAP_TREE_NAME$="Tree"
Const MAP_MINE_NAME$="Mine"
Const MAP_BOUNCER_NAME$="Bouncer"				;With Permission from C. Panayi
Const MAP_HOVERDROID_NAME$="Hoverdroid"		;With Permission from C. Panayi
Const MAP_MILLITOID_NAME$="Millitoid"				;With Permission from C. Panayi
Const MAP_CAPSULE_NAME$="Capsule"

Const MAPSIZEX=60
Const MAPSIZEY=64

Const MAP_EXTENSION$=".a2m"
Const DOCS_EXTENSION$=".pdf"
Const IMG_EXTENSION$=".png"

;SHARED MILLITOID DETAILS

Const MILLITOID_MAX=5
Const MILLITOID_SEGMENTS_MAX=4		;Actual Segments - 1 (Does not include HEAD)

;SHARED CAPSULE

Global CAPSULEX
Global CAPSULEY

;MEMORY HANDLERS

Function PeekArrayValue(X,Y)
	Local Offset=GetArrayOffset(X,Y)
	Return (PeekByte(MAP_BANK,Offset)) And 7
End Function

Function PokeArrayValue(X,Y,Value)
	Local Offset=GetArrayOffset(X,Y)
	PokeByte MAP_BANK,Offset,Value And 7
End Function

Function GetArrayOffset(X,Y)
	Return X + (Y * MAPSIZEX)
End Function

;MAP BLOCKED

Function IsBlockedUp(X,Y)
	Local YY=Y-1
	If (YY<0) Then YY=MAPSIZEY+YY
	Return (GetBlocked(PeekArrayValue(X,YY))) 
End Function

Function IsBlockedDown(X,Y)
	Local YY=((Y+1) Mod MAPSIZEY)
	Return (GetBlocked(PeekArrayValue(X,YY)))
End Function

Function IsBlockedLeft(X,Y)
	Local XX=X-1
	If (XX<0) Then XX=MAPSIZEX+XX
	Return (GetBlocked(PeekArrayValue(XX,Y)))
End Function

Function IsBlockedRight(X,Y)
	Local XX=((X+1) Mod MAPSIZEX)
	Return (GetBlocked(PeekArrayValue(XX,Y)))
End Function

Function GetBlocked(MapType)
	Select MapType
		Case MAP_WALL,MAP_TREE,MAP_BOUNCER,MAP_HOVERDROID,MAP_MILLITOID,MAP_CAPSULE:
			Return True
		Default:
			Return False
	End Select
End Function
;~IDEal Editor Parameters:
;~F#6#A#E#3B#40#45#4B#51#56#5C#61
;~C#Blitz3D