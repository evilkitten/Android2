Include"Inc.bb"
Include"TEST.bb"

Global SPECTRUM_MODE;=True
Global POLYGON_DENSITY=4
Global EXIT_RUNTIME=False

Initialise
Runtime

Function Initialise()
	RUNTIME_Example
End Function

Function Runtime()
	Repeat
		Loop
	Until EXIT_RUNTIME=True
	ClearWorld
	EndGraphics
	End
End Function

Function Loop()
EXIT_RUNTIME=KeyHit(1)+KeyDown(1)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D