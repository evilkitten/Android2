Include"Inc.bb"
Include"TEST.bb"

Global SPECTRUM_MODE

Global POLYGON_DENSITY=4
Global EXIT_RUNTIME=False

Initialise
Runtime

Function Initialise()
	SPECTRUM_MODE=Instr(CommandLine(),"/zx")
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
;~F#B#10#19
;~C#Blitz3D