Include"Inc.bb"

Global TEST

Include"TEST.bb"

Global SPECTRUM_MODE
Global WIRE

Global POLYGON_DENSITY=4
Global EXIT_RUNTIME=False

Initialise
Runtime

Function Runtime()
	Repeat
		Loop
	Until EXIT_RUNTIME=True
	CloseDown
End Function

Function Loop()
	EXIT_RUNTIME=KeyHit(1)+KeyDown(1)
End Function

Function CloseDown()
	ClearWorld True,True,True
	
	EndGraphics
	
	Delete Each BOUNCER
	Delete Each GHOST
	Delete Each HOVERDROID
	Delete Each MILLITOID
	Delete Each MILLITOIDSEGMENT
	Delete Each MINE
	Delete Each TREE
	
	If Not(TEST) Then RemoveUnPackedAssets
	
	RuntimeError"Remember to pack assets and change path in ROOT/INIT.BB : ANIMFILE() and MATFILE()"
End Function
;~IDEal Editor Parameters:
;~F#F#1A
;~C#Blitz3D