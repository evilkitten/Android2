Const MAP_GROUND=0
Const MAP_WALL=1
Const MAP_TREE=2
Const MAP_MINE=3
Const MAP_BOUNCER=4
Const MAP_HOVERDROID=5
Const MAP_MILLITOID=6
Const MAP_CAPSULE=7

Const MAPSIZEX=60
Const MAPSIZEY=64

Const MAP_EXTENSION$=".a2m"

Const MAP_MAZEOFDEATH$="Maze Of Death"

Global MAP_BANK

Function MapsDir$()
	Return ROOT$+"Maps\"
End Function

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
;~IDEal Editor Parameters:
;~C#Blitz3D